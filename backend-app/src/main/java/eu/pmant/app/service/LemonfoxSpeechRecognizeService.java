package eu.pmant.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class LemonfoxSpeechRecognizeService implements SpeechRecognizeService {

    private final WebClient webClient = WebClient.builder()
        .baseUrl("https://api.lemonfox.ai")
        .build();

    private final String apiKey;

    public LemonfoxSpeechRecognizeService(@Value("${LEMONFOX_API_KEY}") String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String recognizeSpeech(String audioFilePath) {
        log.info("Передаем в API распознавания текст и синхронно ожидаем ответ....");
        String recognizedText = webClient.post()
            .uri("/v1/audio/transcriptions")
            .header("Authorization", "Bearer " + apiKey)
            .body(
                BodyInserters.fromMultipartData("file", new FileSystemResource(audioFilePath))
                    .with("response_format", "vtt")
            )
            .retrieve()
            .bodyToMono(String.class)
            .block()
            .replaceAll("\\n","\n");
        log.info("Получили текст.");
        return recognizedText;
    }

    @Override
    public TranscriptionResult getTranscriptionResult(String taskId) {
        throw new RuntimeException("Не применимо");
    }
}
