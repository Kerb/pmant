package eu.pmant.app.async;

import eu.pmant.app.dto.MeetingStatus;
import eu.pmant.app.generated.jooq.tables.pojos.UserMeetings;
import eu.pmant.app.repository.MeetingsRepository;
import eu.pmant.app.service.LocalWhisperSpeechRecognizeService;
import eu.pmant.app.service.SpeechRecognizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static eu.pmant.app.dto.MeetingStatus.TRANSCRIBATION_IN_PROGRESS;
import static eu.pmant.app.dto.MeetingStatus.WAIT_TRANSCRIBATION;
import static eu.pmant.app.service.SpeechRecognizeService.WhisperStatus.DONE;
import static eu.pmant.app.service.SpeechRecognizeService.WhisperStatus.FAILED;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.speech-to-text.type", havingValue = "local-faster-whisper")
@Service
public class AsyncRecognizeSpeechService {

    private final MeetingsRepository meetingsRepository;
    private final LocalWhisperSpeechRecognizeService speechRecognizeService;

    @Scheduled(fixedDelay = 10_000L)
    public void startRecognizeVoiceBatch() {
        log.info("Очередная итерация обработки пачки задач");
        List<UserMeetings> meetingsToProcess = meetingsRepository.findMeetingsToRecognize(10);
        for (UserMeetings meeting : meetingsToProcess) {
            log.info("Анализируем задачу из БД: {} recognizeTaskId: {}", meeting.getRecordingId(), meeting.getRecognizeTaskId());

            if (meeting.getStatus().equals(WAIT_TRANSCRIBATION.name())) {
                String taskId = speechRecognizeService.recognizeSpeech(meeting.getFilePath());
                meeting.setStatus(TRANSCRIBATION_IN_PROGRESS.name());
                meeting.setRecognizeTaskId(taskId);
                meetingsRepository.updateStatusAndSpeech(meeting);
                log.info("Создана задача whisper: '{}' для распознавания звука", taskId);
            } else if (meeting.getStatus().equals(TRANSCRIBATION_IN_PROGRESS.name())) {
                SpeechRecognizeService.TranscriptionResult transcriptionResult = speechRecognizeService.getTranscriptionResult(meeting.getRecognizeTaskId());
                log.info("Для taskId: {} получили: {}", meeting.getRecognizeTaskId(), transcriptionResult);
                if (Objects.equals(transcriptionResult.getStatus(), DONE.getStatus())) {
                    log.info("Задача whisper: '{}' завершена успешно", meeting.getRecognizeTaskId());
                    meeting.setStatus(MeetingStatus.TRANSCRIBATION_SUCCESS.name());
                    meeting.setSpeech(transcriptionResult.getText());
                    meetingsRepository.updateStatusAndSpeech(meeting);
                } else if (Objects.equals(transcriptionResult.getStatus(), FAILED.getStatus())) {
                    log.info("Задача whisper: '{}' завершена с ошибкой", meeting.getRecognizeTaskId());
                    meeting.setStatus(MeetingStatus.TRANSCRIBATION_FAIL.name());
                    meeting.setSpeech("");
                    meetingsRepository.updateStatusAndSpeech(meeting);
                } else {
                    log.info("Задача whisper: '{}' статус все еще inprogress", meeting.getRecognizeTaskId());
                }
            }
        }
    }
}
