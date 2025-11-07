package eu.pmant.app.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
                .toolChoice(
                    ResponseCreateParams.ToolChoice
                        .ofAllowed(
                            ToolChoiceAllowed.builder()
                                .mode(ToolChoiceAllowed.Mode.AUTO)
                                .build()
                        )
                )
                .build()
        );
        log.info("response: {}", response);

        // Разбор текста
        String textOutput = response.output().stream()
            .flatMap(outputItem -> outputItem.message().stream())
            .flatMap(item -> item.content().stream())
            .flatMap(content -> content.outputText().stream())
            .flatMap(outputItem -> Stream.of(outputItem.text()))
            .collect(Collectors.joining());

        // Поиск и логирование tool calls, если есть (доступно — зависит от модели и вашей интеграции)
        response.output().stream()
            .flatMap(outputItem -> outputItem.functionCall().stream())
            .forEach(functionToolCall -> log.info("functionToolCall: {}", functionToolCall));
        return Optional.ofNullable(textOutput);
    }
}