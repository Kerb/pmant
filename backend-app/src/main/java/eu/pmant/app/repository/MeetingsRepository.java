package eu.pmant.app.repository;

import eu.pmant.app.dto.MeetingStatus;
import eu.pmant.app.generated.jooq.Tables;
import eu.pmant.app.generated.jooq.tables.pojos.UserMeetings;
import eu.pmant.app.generated.jooq.tables.records.UserMeetingsRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Repository
@Slf4j
@Profile("prod")
public class MeetingsRepository {

    private final DSLContext dslContext;

    @Transactional
    public UserMeetings create(UserMeetings userMeetings) {
        UserMeetingsRecord record = dslContext.insertInto(Tables.USER_MEETINGS)
            .set(Tables.USER_MEETINGS.USER_ID, userMeetings.getUserId())
            .set(Tables.USER_MEETINGS.FILE_NAME, userMeetings.getFileName())
            .set(Tables.USER_MEETINGS.FILE_PATH, userMeetings.getFilePath())
            .set(Tables.USER_MEETINGS.DURATION, userMeetings.getDuration())
            .set(Tables.USER_MEETINGS.TITLE, userMeetings.getTitle())
            .set(Tables.USER_MEETINGS.STATUS, userMeetings.getStatus())
            .set(Tables.USER_MEETINGS.UPLOAD_DATE, userMeetings.getUploadDate())
            .returning(Tables.USER_MEETINGS.RECORDING_ID)
            .fetchOne();

        if (record != null) {
            userMeetings.setRecordingId(record.getValue(Tables.USER_MEETINGS.RECORDING_ID));
        } else {
            throw new RuntimeException("Failed to create user meeting");
        }
        return userMeetings;
    }

    @Transactional
    public void updateStatusAndSpeech(UserMeetings userMeetings) {
        log.info("Обновление speech, status, recognizeTaskId для записи: {}", userMeetings.getRecordingId());
        dslContext.update(Tables.USER_MEETINGS)
            .set(Tables.USER_MEETINGS.SPEECH, userMeetings.getSpeech())
            .set(Tables.USER_MEETINGS.STATUS, userMeetings.getStatus())
            .set(Tables.USER_MEETINGS.RECOGNIZE_TASK_ID, userMeetings.getRecognizeTaskId())
            .where(Tables.USER_MEETINGS.RECORDING_ID.eq(userMeetings.getRecordingId()))
            .execute();
    }

    @Transactional
    public void updateStatusAndMoM(UserMeetings userMeetings) {
        log.info("Обновление mom, status, recognizeTaskId для записи: {}", userMeetings.getRecordingId());
        dslContext.update(Tables.USER_MEETINGS)
            .set(Tables.USER_MEETINGS.STATUS, userMeetings.getStatus())
            .set(Tables.USER_MEETINGS.MINUTES_OF_MEETING, userMeetings.getMinutesOfMeeting())
            .where(Tables.USER_MEETINGS.RECORDING_ID.eq(userMeetings.getRecordingId()))
            .execute();
    }

    public List<UserMeetings> findMeetingsByUserId(Long userId) {
        log.info("Поиск UserMeetings по userId: {}", userId);
        return dslContext.selectFrom(Tables.USER_MEETINGS)
            .where(Tables.USER_MEETINGS.USER_ID.eq(userId))
            .orderBy(Tables.USER_MEETINGS.UPLOAD_DATE.desc())
            .fetchInto(UserMeetings.class);
    }

    public UserMeetings findMeetingByIdAndUserId(Long id, Long userId) {
        log.info("Поиск UserMeetings по id: {} и userId: {}", id, userId);
        return dslContext.selectFrom(Tables.USER_MEETINGS)
            .where(Tables.USER_MEETINGS.RECORDING_ID.eq(id))
            .and(Tables.USER_MEETINGS.USER_ID.eq(userId))
            .fetchOneInto(UserMeetings.class);
    }

    public List<UserMeetings> findMeetingsToRecognize(int maxLimit) {
        log.info("Поиск UserMeetings для обработки");
        List<UserMeetings> userMeetingsProcessing = dslContext.selectFrom(Tables.USER_MEETINGS)
            .where(Tables.USER_MEETINGS.STATUS.eq(MeetingStatus.TRANSCRIBATION_IN_PROGRESS.name()))
            .limit(maxLimit)
            .fetchInto(UserMeetings.class);
        log.info("Найдено {} задач в статусе TRANSCRIBATION_IN_PROGRESS", userMeetingsProcessing.size());

        List<UserMeetings> userMeetingsToProcess = dslContext.selectFrom(Tables.USER_MEETINGS)
            .where(Tables.USER_MEETINGS.STATUS.eq(MeetingStatus.WAIT_TRANSCRIBATION.name()))
            .limit(maxLimit - userMeetingsProcessing.size())
            .fetchInto(UserMeetings.class);
        log.info("Найдено {} задач в статусе WAIT_TRANSCRIBATION", userMeetingsToProcess.size());
        return Stream.concat(userMeetingsProcessing.stream(), userMeetingsToProcess.stream()).toList();
    }

    public List<UserMeetings> findMeetingsToExtractMinutesOfMeeting(int maxLimit) {
        log.info("Поиск UserMeetings для выделения MoM");
        List<UserMeetings> userMeetingsProcessing = dslContext.selectFrom(Tables.USER_MEETINGS)
            .where(Tables.USER_MEETINGS.STATUS.eq(MeetingStatus.TRANSCRIBATION_SUCCESS.name()))
            .limit(maxLimit)
            .fetchInto(UserMeetings.class);

        log.info("Найдено {} задач в статусе TRANSCRIBATION_SUCCESS, для которых можно запустить распознавание MoM",
            userMeetingsProcessing.size());
        return userMeetingsProcessing;

    }
}
