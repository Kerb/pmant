package eu.pmant.app.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseOutputItem;
import com.openai.models.responses.ResponsePrompt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MinutesOfMeetingExtractServiceImpl implements MinutesOfMeetingExtractService {
    @Override
    public Optional<String> extractMinutesOfMeeting(String meetingText) {
        OpenAIClient client = OpenAIOkHttpClient.fromEnv();
        Response response = client.responses().create(
            ResponseCreateParams.builder()
                .prompt(
                    ResponsePrompt.builder().id("pmpt_690daff760088193b2276959934efca00abc42b2e6c34d50").build()
                )
                .build()
        );
        List<ResponseOutputItem> outputItems = response.output();
        for (ResponseOutputItem outputItem : outputItems) {
            log.info("outputItem: {}", outputItem);
        }
        return Optional.empty();
    }
}