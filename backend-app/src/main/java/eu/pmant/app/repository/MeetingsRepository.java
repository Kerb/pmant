package eu.pmant.app.repository;

import eu.pmant.app.generated.jooq.Tables;
import eu.pmant.app.generated.jooq.tables.pojos.UserMeetings;
import eu.pmant.app.generated.jooq.tables.records.UserMeetingsRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
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

    public List<UserMeetings> findMeetingsByUserId(Long userId) {
        return dslContext.selectFrom(Tables.USER_MEETINGS)
            .where(Tables.USER_MEETINGS.USER_ID.eq(userId))
            .fetchInto(UserMeetings.class);
    }

    public UserMeetings findMeetingByIdAndUserId(Long id, Long userId) {
        return dslContext.selectFrom(Tables.USER_MEETINGS)
            .where(Tables.USER_MEETINGS.RECORDING_ID.eq(id))
            .and(Tables.USER_MEETINGS.USER_ID.eq(userId))
            .fetchOneInto(UserMeetings.class);
    }
}
