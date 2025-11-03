package eu.pmant.app.config;

import eu.pmant.app.async.AsyncRecognizeSpeechService;
import eu.pmant.app.repository.MeetingsRepository;
import eu.pmant.app.service.LocalWhisperSpeechRecognizeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsyncConfig {

    @Bean
    public AsyncRecognizeSpeechService asyncRecognizeSpeechService(MeetingsRepository meetingsRepository,
                                                                   LocalWhisperSpeechRecognizeService speechRecognizeService) {
        return new AsyncRecognizeSpeechService(meetingsRepository, speechRecognizeService);
    }
}