package eu.pmant.app.async;

import eu.pmant.app.dto.MeetingStatus;
import eu.pmant.app.generated.jooq.tables.pojos.UserMeetings;
import eu.pmant.app.repository.MeetingsRepository;
import eu.pmant.app.service.MinutesOfMeetingExtractService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Service
public class AsyncMinutesOfMemoryExtractService {

    private final MeetingsRepository meetingsRepository;
    private final MinutesOfMeetingExtractService minutesOfMeetingExtractService;

    @Scheduled(fixedDelay = 10_000L)
    public void startMomExtractionBatch() {
        log.info("Берем пачку задач для получения по ним memory of meeting");
        List<UserMeetings> meetingsToExtractMinutesOfMeeting = meetingsRepository.findMeetingsToExtractMinutesOfMeeting(10);
        for (UserMeetings userMeetingsToExtractMoM : meetingsToExtractMinutesOfMeeting) {
            log.info("Анализируем запись: {}", userMeetingsToExtractMoM.getRecordingId());
            String speech = userMeetingsToExtractMoM.getSpeech();
            String newStatus;
            try {
                String minutesOfMeeting = minutesOfMeetingExtractService.extractMinutesOfMeeting(speech).orElse("");
                newStatus = MeetingStatus.of(userMeetingsToExtractMoM.getStatus())
                    .map(resolveNewStatus(speech))
                    .map(Enum::name)
                    .orElseThrow();
                userMeetingsToExtractMoM.setMinutesOfMeeting(minutesOfMeeting);
            } catch (Exception e) {
                log.error("{}", e.getMessage(), e);
                newStatus = MeetingStatus.of(userMeetingsToExtractMoM.getStatus())
                    .map(s -> MeetingStatus.MINUTES_OF_MEETING_FAIL)
                    .map(Enum::name)
                    .orElseThrow();
            }
            userMeetingsToExtractMoM.setStatus(newStatus);
            meetingsRepository.updateStatusAndMoM(userMeetingsToExtractMoM);
        }
    }

    @Nonnull
    private Function<MeetingStatus, MeetingStatus> resolveNewStatus(String speech) {
        return s -> StringUtils.isEmpty(speech)
            ? s.transitTo(MeetingStatus.MINUTES_OF_MEETING_FAIL)
            : s.transitTo(MeetingStatus.MINUTES_OF_MEETING_SUCCESS);
    }
}
