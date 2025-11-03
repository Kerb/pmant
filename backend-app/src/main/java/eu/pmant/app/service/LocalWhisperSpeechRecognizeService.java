package eu.pmant.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@Slf4j
public class LocalWhisperSpeechRecognizeService implements SpeechRecognizeService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public LocalWhisperSpeechRecognizeService() {
        this.webClient = WebClient.builder()
            .baseUrl("http://transcriber-app:8000")
            .build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String recognizeSpeech(String audioFilePath) {
        return createTranscriptionTask(audioFilePath);
    }

    @Override
    public TranscriptionResult getTranscriptionResult(String taskId) {
        String responseBody = webClient.get()
            .uri("/getTranscribeTask?task_id=" + taskId)
            .retrieve()
            .bodyToMono(String.class)
            .block();

        try {
            JsonNode root = objectMapper.readTree(responseBody);

            String status = root.path("status").asText("");
            String text = root.path("text").asText("");

            return new TranscriptionResult(status, text);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse transcription status", e);
        }
    }

    private String createTranscriptionTask(String audioFilePath) {
        log.info("Creating transcription task for audio file: {}", audioFilePath);
        String responseBody = webClient.post()
            .uri("/createTranscribeTask")
            .body(BodyInserters.fromMultipartData("file", new FileSystemResource(audioFilePath)))
            .retrieve()
            .bodyToMono(String.class)
            .block();

        try {
            JsonNode root = objectMapper.readTree(responseBody);
            String taskId = root.get("task_id").asText();
            log.info("Created taskId: {}", taskId);
            return taskId;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse task ID from response", e);
        }
    }
}
