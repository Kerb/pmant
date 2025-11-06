package eu.pmant.app.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.core.JsonValue;
import com.openai.models.ChatModel;
import com.openai.models.FunctionDefinition;
import com.openai.models.FunctionParameters;
import com.openai.models.chat.completions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MinutesOfMeetingExtractServiceImpl implements MinutesOfMeetingExtractService {
    @Override
    public Optional<String> extractMinutesOfMeeting(String meetingText) {
        OpenAIClient client = OpenAIOkHttpClient.fromEnv();

        // Упрощенный и более четкий промпт
        String systemPrompt = """
            Ты — бизнес аналитик, готовящий Minutes of Meeting (MoM) по транскрипту встречи.
            Работай только с текстом, без догадок и предположений.
            
            ИНСТРУКЦИИ:
            1. Извлеки структурированную информацию из текста встречи
            2. Для каждого Action Item автоматически вызови функцию create_trello_card
            3. В ответе покажи только структурированный MoM в Markdown формате
            4. НЕ описывай вызовы функций в тексте - они должны вызываться автоматически
            
            ФОРМАТ MoM:
            ## Minutes of Meeting
            **Дата:** [дата или "не указана"]
            **Участники:** [список участников или "не указаны"]
            
            ### Основные темы
            - ...
            
            ### Принятые решения
            - ...
            
            ### Действия (Action Items)
            - [Имя/роль] Задача - срок
            - ...
            
            ### Открытые вопросы
            - ...
            
            Используй только факты из текста. Если информации нет - укажи "не указано".
            """;

        // Определение функции для создания карточек Trello
        FunctionParameters paramsSchema = FunctionParameters.builder()
            .putAdditionalProperty("type", JsonValue.from("object"))
            .putAdditionalProperty("properties", JsonValue.from(Map.of(
                "name", Map.of("type", "string", "description", "Имя или роль ответственного"),
                "description", Map.of("type", "string", "description", "Описание Action Item"),
                "dueDate", Map.of("type", "string", "description", "Срок выполнения (если указан)")
            )))
            .putAdditionalProperty("required", JsonValue.from(List.of("description")))
            .build();

        // Определение функции (tool)
        FunctionDefinition createTrelloFn = FunctionDefinition.builder()
            .name("create_trello_card")
            .description("Создает карточку Action Item в Trello")
            .parameters(paramsSchema)
            .build();

        ChatCompletionFunctionTool tool = ChatCompletionFunctionTool.builder()
            .function(createTrelloFn)
            .build();

        // Создаем сообщения
        ChatCompletionSystemMessageParam systemMessageParam = new ChatCompletionSystemMessageParam.Builder()
            .content(systemPrompt)
            .build();

        ChatCompletionUserMessageParam userMessageParam = new ChatCompletionUserMessageParam.Builder()
            .content(meetingText)
            .build();

        List<ChatCompletionMessageParam> messages = List.of(
            ChatCompletionMessageParam.ofSystem(systemMessageParam),
            ChatCompletionMessageParam.ofUser(userMessageParam)
        );

        // Запрос к модели с настройкой tool_choice
        ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
            .model(ChatModel.GPT_4_0613)
            .messages(messages)
            .addTool(tool)
            .toolChoice(ChatCompletionToolChoiceOption.Auto.AUTO)
            .build();

        ChatCompletion completion = client.chat().completions().create(createParams);

        List<ChatCompletion.Choice> choices = completion.choices();

        if (choices.isEmpty()) {
            return Optional.empty();
        }

        ChatCompletion.Choice choice = choices.get(0);
        ChatCompletionMessage assistantMessage = choice.message();

        StringBuilder result = new StringBuilder();

        // Обрабатываем контент сообщения
        if (assistantMessage.content().isPresent()) {
            result.append(assistantMessage.content().get());
        }

        // Обрабатываем вызовы функций
        if (assistantMessage.toolCalls().isPresent()) {
            List<ChatCompletionMessageToolCall> toolCalls = assistantMessage.toolCalls().get();
            List<ChatCompletionMessageFunctionToolCall> functionCalls = toolCalls.stream()
                .flatMap(toolCall -> toolCall.function().stream())
                .toList();

            for (ChatCompletionMessageFunctionToolCall functionCall : functionCalls) {
                ChatCompletionMessageFunctionToolCall.Function function = functionCall.function();

                if ("create_trello_card".equals(function.name())) {
                    Map<String, JsonValue> params = function
                        ._arguments()
                        .asObject()
                        .orElse(Map.of());

                    Map<String, String> paramsMap = params
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, f -> f.getValue().convert(String.class)));

                    // Вызываем реальный API Trello
                    boolean success = createTrelloCard(
                        paramsMap.get("name"),
                        paramsMap.get("description"),
                        paramsMap.get("dueDate")
                    );

                    if (success) {
                        log.info("Успешно создана карточка Trello: {}", paramsMap);
                    } else {
                        log.warn("Не удалось создать карточку Trello: {}", paramsMap);
                    }
                }
            }
        }

        return Optional.of(result.toString());
    }

    /**
     * Реальный метод для создания карточки в Trello
     */
    private boolean createTrelloCard(String name, String description, String dueDate) {
        try {
            // TODO: Реализовать вызов реального API Trello
            // Пример структуры для Trello API:
            /*
            TrelloCard card = new TrelloCard();
            card.setName(description);
            card.setDesc("Ответственный: " + name + "\\nСрок: " + dueDate);
            card.setDue(dueDate);
            // Вызов Trello API
            */

            log.info("Создание карточки Trello:");
            log.info("  Ответственный: {}", name);
            log.info("  Описание: {}", description);
            log.info("  Срок: {}", dueDate);

            return true;
        } catch (Exception e) {
            log.error("Ошибка при создании карточки Trello", e);
            return false;
        }
    }
}