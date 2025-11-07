package eu.pmant.app.service;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        List<ResponseInputItem> inputs = List.of(
            ResponseInputItem.ofMessage(ResponseInputItem.Message.builder()
                .addInputTextContent(meetingText)
                .role(ResponseInputItem.Message.Role.USER)
                .build())
        );

        ResponseCreateParams.Builder builder = ResponseCreateParams.builder()
            .prompt(ResponsePrompt.builder().id("pmpt_690daff760088193b2276959934efca00abc42b2e6c34d50").build())
            .addTool(CreateTrelloActionItem.class)
            .input(ResponseCreateParams.Input.ofResponse(inputs));

        Response response = client.responses().create(builder.build());
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
            .filter(ResponseOutputItem::isFunctionCall)
            .flatMap(outputItem -> outputItem.functionCall().stream())
            .forEach(functionToolCall -> log.info("functionToolCall: {}", functionToolCall));

        return Optional.ofNullable(textOutput);
    }

    @Slf4j
    @Data
    @JsonClassDescription("Создаёт карточку с Action Item в Trello по данным из текста.")
    public static class CreateTrelloActionItem {

        @JsonPropertyDescription("Имя или роль ответственного (если указано)")
        private String name;

        @JsonPropertyDescription("Описание Action Item")
        private String description;

        @JsonPropertyDescription("Срок выполнения (если указан)")
        private String dueDate;

        public void execute() {
            log.info("Вызвали tool CreateTrelloActionItem ({}, {}, {})", name, description, dueDate);
        }
    }
}












