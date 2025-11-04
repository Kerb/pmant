package eu.pmant.app.async;

import eu.pmant.app.generated.jooq.tables.pojos.UserMeetings;
import eu.pmant.app.repository.MeetingsRepository;
import eu.pmant.app.service.LemonfoxSpeechRecognizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import static eu.pmant.app.dto.MeetingStatus.*;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.speech-to-text.type", havingValue = "lemonfox-api")
@Service
public class AsyncRecognizeSpeechUsingLemonfoxApiService {

    private final MeetingsRepository meetingsRepository;
    private final LemonfoxSpeechRecognizeService speechRecognizeService;

    @Scheduled(fixedDelay = 10_000L)
    public void startRecognizeVoiceBatch() {
        log.info("Очередная итерация обработки пачки задач");
        List<UserMeetings> meetingsToProcess = meetingsRepository.findMeetingsToRecognize(10);
        for (UserMeetings meeting : meetingsToProcess) {
            try {
                if (meeting.getStatus().equals(WAIT_TRANSCRIBATION.name())) {
                    meeting.setStatus(TRANSCRIBATION_IN_PROGRESS.name());
                    meetingsRepository.updateStatusAndSpeech(meeting);

                    String text = speechRecognizeService.recognizeSpeech(meeting.getFilePath());
                    meeting.setSpeech(text);
                    meeting.setStatus(TRANSCRIBATION_SUCCESS.name());
                    meetingsRepository.updateStatusAndSpeech(meeting);
                }
            } catch (Exception e) {
                log.error("{}", e.getMessage(), e);
                meeting.setStatus(TRANSCRIBATION_FAIL.name());
                meetingsRepository.updateStatusAndSpeech(meeting);
            }
        }
    }
}
