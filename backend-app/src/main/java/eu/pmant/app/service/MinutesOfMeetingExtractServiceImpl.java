package eu.pmant.app.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                .input(meetingText)
                .build()
        );
        log.info("response: {}", response);
        return Optional.ofNullable(response.output().stream()
            .flatMap(outputItem -> outputItem.message().stream())
            .flatMap(item ->item.content().stream())
            .flatMap(content ->content.outputText().stream())
            .flatMap(outputItem -> Stream.of(outputItem.text()))
            .collect(Collectors.joining()));
    }
}