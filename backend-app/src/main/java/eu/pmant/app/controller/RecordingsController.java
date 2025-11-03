package eu.pmant.app.controller;

import eu.pmant.app.dto.*;
import eu.pmant.app.generated.jooq.tables.pojos.UserMeetings;
import eu.pmant.app.repository.MeetingsRepository;
import eu.pmant.app.session.SessionDataProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class RecordingsController {

    private final MeetingsRepository meetingsRepository;
    private final SessionDataProvider sessionDataProvider;

    @PostMapping("/createRecording")
    public ResponseEntity<RecordingCreateResponse> createRecording(@RequestParam("file") MultipartFile file) {
        try {
            Long userId = sessionDataProvider.getSessionData().getUserId();

            // todo вынести в параметры
            long maxSize = 50 * 1024 * 1024;
            if (file.getSize() > maxSize) {
                throw new RuntimeException("File too large");
            }
            // todo проверить тип файла

            // Generate temporary file name
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String tempFileName = String.format("%d/%s_%s.%s", userId, LocalDateTime.now(), UUID.randomUUID(), extension);

            // Save file locally in /tmp directory
            Path filePath = Paths.get("/var/recordings", tempFileName);
            Files.createDirectories(filePath.getParent());
            file.transferTo(filePath);
            log.info("File saved to {}", filePath.toFile().getAbsolutePath());

            UserMeetings userMeetings = new UserMeetings();
            userMeetings.setUserId(userId);
            userMeetings.setFileName(originalFilename);
            userMeetings.setFilePath(filePath.toString());
            userMeetings.setDuration(0L); //todo реализовать
            userMeetings.setTitle(file.getOriginalFilename());
            userMeetings.setStatus(MeetingStatus.WAIT_TRANSCRIBATION.name());
            userMeetings.setUploadDate(LocalDateTime.now());
            userMeetings.setSpeech("");

            UserMeetings savedUserMeeting = meetingsRepository.create(userMeetings);

            return ResponseEntity.ok(
                RecordingCreateResponse.builder()
                    .success(true)
                    .recordingId(savedUserMeeting.getRecordingId())
                    .build()
            );
        } catch (IOException e) {
            log.error("{}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(
                    RecordingCreateResponse.builder()
                        .success(false)
                        .build()
                );
        }
    }

    @PostMapping("/recordings")
    public ResponseEntity<RecordingsResponse> recordingsList() {
        List<UserMeetings> meetingsByUser = meetingsRepository
            .findMeetingsByUserId(sessionDataProvider.getSessionData().getUserId());

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        List<Recording> recordings = meetingsByUser.stream()
            .map(meeting -> Recording.builder()
                .id(meeting.getRecordingId())
                .title(meeting.getTitle())
                .date(dateFormatter.format(meeting.getUploadDate()))
                .time(timeFormatter.format(meeting.getUploadDate()))
                .duration("xx min")
                .status(meeting.getStatus())
                .build()
            ).toList();

        RecordingsResponse response = RecordingsResponse.builder()
            .success(true)
            .recordings(recordings).build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recording-details/{id}")
    public ResponseEntity<RecordingDetailsResponse> recordingDetails(@PathVariable("id") Long id) {
        Long userId = sessionDataProvider.getSessionData().getUserId();
        UserMeetings foundMeeting = meetingsRepository.findMeetingByIdAndUserId(id, userId);

        if (foundMeeting == null) {
            log.info("Не нашли запись с id: {}", id);
            return ResponseEntity
                .ok(
                    RecordingDetailsResponse.builder()
                        .success(false)
                        .build()
                );
        }
        log.info("Нашли запись с id: {}", id);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        return ResponseEntity.ok(
            RecordingDetailsResponse.builder()
                .success(true)
                .meetingDetails(
                    Recording.builder()
                        .id(foundMeeting.getRecordingId())
                        .title(foundMeeting.getTitle())
                        .date(dateFormatter.format(foundMeeting.getUploadDate()))
                        .time(timeFormatter.format(foundMeeting.getUploadDate()))
                        .duration("xx min") // todo реализовать
                        .status(foundMeeting.getStatus())
                        .speech(foundMeeting.getSpeech())
                        .build()
                )
                .build()
        );
    }

}
