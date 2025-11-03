package eu.pmant.app.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import eu.pmant.app.service.SpeechRecognizeService;
import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public SpeechRecognizeService speechRecognizeService() {
        return mock(SpeechRecognizeService.class);
    }
}