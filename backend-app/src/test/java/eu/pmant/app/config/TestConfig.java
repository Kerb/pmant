package eu.pmant.app.config;

import eu.pmant.app.generated.jooq.tables.pojos.UserAccount;
import eu.pmant.app.repository.MeetingsRepository;
import eu.pmant.app.repository.UserRepository;
import eu.pmant.app.service.SpeechRecognizeService;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Optional;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public SpeechRecognizeService speechRecognizeService() {
        return mock(SpeechRecognizeService.class);
    }

    @Bean
    public UserRepository userRepositoryMockService() {
        UserRepository userRepository = mock(UserRepository.class);

        UserAccount createdUser = new UserAccount();
        createdUser.setId(1L);
        createdUser.setLogin("MockUser");
        createdUser.setPasswordHash("MockPass");

        Mockito.doReturn(createdUser)
            .when(userRepository)
            .create(ArgumentMatchers.any());


        Mockito.doReturn(Optional.empty())
            .when(userRepository)
            .findByLogin(ArgumentMatchers.anyString());

        Mockito.doReturn(Optional.of(new UserAccount(2L, "existinguser", "existinguserpasshash")))
            .when(userRepository)
            .findByLogin("existinguser");
        return userRepository;
    }

    @Bean
    @Primary
    public MeetingsRepository meetingsRepositoryMockService() {
        return Mockito.mock(MeetingsRepository.class);
    }

    @Bean
    public DataSource dataSource() {
        return Mockito.mock(DataSource.class);
    }
}
