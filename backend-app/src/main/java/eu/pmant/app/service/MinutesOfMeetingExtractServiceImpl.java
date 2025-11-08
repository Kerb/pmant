package eu.pmant.app.service;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.*;
import lombok.Data;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class MinutesOfMeetingExtractServiceImpl implements MinutesOfMeetingExtractService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public Optional<String> extractMinutesOfMeeting(String meetingText) {
        OpenAIClient client = OpenAIOkHttpClient.fromEnv();

        List<ResponseInputItem> inputs = new ArrayList();
        inputs.add(
            ResponseInputItem.ofMessage(ResponseInputItem.Message.builder()
                .addInputTextContent(meetingText)
                .role(ResponseInputItem.Message.Role.USER)
                .build())
        );

        ResponseCreateParams.Builder createParamsBuilder = ResponseCreateParams.builder()
            .prompt(ResponsePrompt.builder().id("pmpt_690daff760088193b2276959934efca00abc42b2e6c34d50").build())
            .addTool(CreateTrelloActionItem.class)
            .toolChoice(ToolChoiceOptions.AUTO) // 🔥 ключевой момент
            .input(ResponseCreateParams.Input.ofResponse(inputs));

        ResponseCreateParams firstLlmRequest = createParamsBuilder.build();
        log.info("firstLlmRequest: {}", firstLlmRequest);
        Response response = client.responses().create(firstLlmRequest);
        log.info("response: {}", response);

        // Поиск и логирование tool calls, если есть (доступно — зависит от модели и вашей интеграции)
        List<ResponseOutputItem> functionShouldBeCalledByModelDecision = response.output().stream()
            .filter(ResponseOutputItem::isFunctionCall)
            .toList();
        functionShouldBeCalledByModelDecision.forEach(callFunctionConsumer(inputs));

        if (CollectionUtils.isNotEmpty(functionShouldBeCalledByModelDecision)) {
            try {
                log.info("Модель решила вызвать tools: {}", OBJECT_MAPPER.writeValueAsString(functionShouldBeCalledByModelDecision));

                ResponseCreateParams secondLlmRequest = createParamsBuilder.input(ResponseCreateParams.Input.ofResponse(inputs)).build();
                log.info("secondLlmRequest: {}", secondLlmRequest);
                response = client.responses().create(secondLlmRequest);
                log.info("response: {}", response);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            log.info("Модель не приняла решения вызвать tools");
        }

        // Разбор текста
        String textOutput = response.output().stream()
            .filter(ResponseOutputItem::isMessage)
            .flatMap(outputItem -> outputItem.message().stream())
            .flatMap(item -> item.content().stream())
            .flatMap(content -> content.outputText().stream())
            .flatMap(outputItem -> Stream.of(outputItem.text()))
            .collect(Collectors.joining());

        return Optional.ofNullable(textOutput);
    }

    @NotNull
    private Consumer<ResponseOutputItem> callFunctionConsumer(List<ResponseInputItem> inputs) {
        return functionCall -> {
            ResponseFunctionToolCall functionToolCall = functionCall.asFunctionCall();
            inputs.add(ResponseInputItem.ofFunctionCall(functionToolCall));
            inputs.add(ResponseInputItem.ofFunctionCallOutput(ResponseInputItem.FunctionCallOutput.builder()
                .callId(functionToolCall.callId())
                .outputAsJson(callFunction(functionToolCall))
                .build()));
        };
    }

    private static Object callFunction(ResponseFunctionToolCall functionCall) {
        switch (functionCall.name()) {
            case "create_trello_card":
                return functionCall.arguments(CreateTrelloActionItem.class).execute();
            case "CreateTrelloActionItem":
                return functionCall.arguments(CreateTrelloActionItem.class).execute();
            default:
                try {
                    log.info("Модель решила сделать вызов функции: {}", OBJECT_MAPPER.writeValueAsString(functionCall));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                return false;
        }
    }

    @Slf4j
    @Data
    @JsonClassDescription("Функция create_trello_card")
    public static class CreateTrelloActionItem {

        @JsonPropertyDescription("Имя или роль ответственного (если указано)")
        private String name;

        @JsonPropertyDescription("Описание Action Item")
        private String description;

        @JsonPropertyDescription("Срок выполнения (если указан)")
        private String dueDate;

        public CreateTrelloActionItemResult execute() {
            log.info("Вызов функции CreateTrelloActionItem ('{}', '{}', '{}')", name, description, dueDate);
            return CreateTrelloActionItemResult.ofSuccess();
        }
    }

    @JsonClassDescription("Результат вызова create_trello_card")
    public record CreateTrelloActionItemResult(

        @JsonPropertyDescription("true: вызов успешный; false: ошибки вызова") boolean success) {

        public static CreateTrelloActionItemResult ofSuccess() {
            return new CreateTrelloActionItemResult(true);
        }
    }
}












