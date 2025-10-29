package eu.pmant.app.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OpenAISpeechRecognizeService implements SpeechRecognizeService {

    public String recognizeSpeech(String audioFilePath) {
        WebClient webClient = WebClient.builder()
            .baseUrl("https://api.openai.com/v1/audio/transcriptions")
            .defaultHeader("Authorization", "Bearer " + System.getenv("OPENAI_API_KEY"))
            .build();

        Mono<String> responseMono = webClient.post()
            .body(BodyInserters.fromMultipartData("file", new FileSystemResource(audioFilePath))
                .with("model", "whisper-1"))
            .retrieve()
            .bodyToMono(String.class);

        return responseMono.block();
    }
}
