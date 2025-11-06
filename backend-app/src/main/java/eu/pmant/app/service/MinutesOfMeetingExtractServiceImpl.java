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
        String prompt = """
            Ты — аналитик, готовящий **Minutes of Meeting (MoM)** по **транскрипту встречи** (менеджеры, разработчики, аналитики и т.д.).
            Работай **только с текстом**, без догадок.
            
            ---
            
            ### 🔹 Задача
            
            Извлеки из транскрипта и структурируй итоги по разделам:
            
            1. **Дата и контекст** — только если явно указаны.
            2. **Участники** — имена или роли (только из текста).
            3. **Темы** — кратко, по сути.
            4. **Решения** — только зафиксированные в тексте.
            5. **Действия (Action items)** — кто, что, до какого срока (если указано).
            6. **Открытые вопросы** — нерешённые темы или запросы на уточнение.
            
            Если данных нет — пиши: `(Информация отсутствует в тексте)`
            
            Если найдутся Действия(action items) — используй функцию create_trello_card для каждого Action Item с параметрами:
            - name (имя/роль ответственного, если указано, иначе передавай: "")
            - description (обязательное поле: краткое описание задачи)
            - dueDate (строкой, если указан срок, иначе передавай: "")
            
            Например, Если есть Action Item:
            - [Dev] Сделать код-ревью — до 2025-11-10
            
            то вызови функцию так:
            
            create_trello_card({
              "name": "Dev",
              "description": "Сделать код-ревью",
              "dueDate": "2025-11-10"ƒ
            })
            
            ---
            
            ### 🔹 Формат вывода
            
            **Используй Markdown, нейтральный деловой стиль.**
            Игнорируй приветствия, повторы, не добавляй интерпретаций.
            
            ## Minutes of Meeting
            
            **Дата:** (если есть)
            **Участники:** (если есть)
            
            ### Основные темы
            - ...
            
            ### Принятые решения
            - ...
            
            ### Действия
            - [Имя/роль] Задача — срок (если указан)
            
            ### Открытые вопросы
            - ...
            
            ---
            
            ### 🔹 Входные данные
            
            [minutes_of_meeting]
            
            ---
            
            ### 🔹 Требования
            
            * Используй **только факты из текста**.
            * Если чего-то **недостаточно**, укажи это явно.
            * Никаких предположений или слов вроде *«возможно»*, *«скорее всего»*.
            """;


        // Определение параметров функции в виде JSON Schema (FunctionParameters)
        FunctionParameters paramsSchema = FunctionParameters.builder()
            .putAdditionalProperty("type", JsonValue.from("object"))
            .putAdditionalProperty("properties", JsonValue.from(Map.of(
                "name", Map.of("type", "string", "description", "Имя или роль ответственного"),
                "description", Map.of("type", "string", "description", "Описание задачи"),
                "dueDate", Map.of("type", "string", "description", "Срок выполнения (если указан)")
            )))
            .putAdditionalProperty("required", JsonValue.from(List.of("description")))
            .build();

        // Определение функции (tool)
        FunctionDefinition createTrelloFn = FunctionDefinition.builder()
            .name("create_trello_card")
            .description("Создать карточку в Trello по Action Item из MoM. Возвращает URL карточки.")
            .parameters(paramsSchema)
            .build();

        ChatCompletionFunctionTool tool = ChatCompletionFunctionTool.builder()
            .function(createTrelloFn)
            .build();

        // Построение запроса
        ChatCompletionCreateParams.Builder createParamsBuilder = ChatCompletionCreateParams.builder()
            .model(ChatModel.GPT_4_0613) // либо другой поддерживаемый моделью
            .addTool(tool)
            .addUserMessage(prompt.replace("[minutes_of_meeting]", meetingText))
            .toolChoice(ChatCompletionToolChoiceOption.Auto.AUTO);

        ChatCompletionCreateParams createParams = createParamsBuilder.build();

        // Отправляем запрос и обрабатываем ответ(ы)
        ChatCompletion completion = client.chat().completions().create(createParams);

        List<ChatCompletion.Choice> choices = completion.choices();

        if (choices.isEmpty()) {
            return Optional.empty();
        }

        ChatCompletion.Choice choice = choices.get(0);
        ChatCompletionMessage assistantMessage = choice.message();

        if (assistantMessage.toolCalls().isPresent()) {
            log.info("Модель вернула инфу, что хочет вызывать тулзы");
            List<ChatCompletionMessageToolCall> chatCompletionMessageToolCalls = assistantMessage.toolCalls().get();
            List<ChatCompletionMessageFunctionToolCall> toolsCall = chatCompletionMessageToolCalls.stream()
                .flatMap(f -> f.function().stream())
                .toList();

            for (ChatCompletionMessageFunctionToolCall fnToolCall : toolsCall) {
                ChatCompletionMessageFunctionToolCall.Function fn = fnToolCall.function();
                String fnName = fn.name();
                log.info("Модель хотела бы вызвать функцию: {}", fnName);
                if ("create_trello_card".equals(fnName)) {
                    Map<String, JsonValue> params = fn._arguments()
                        .asObject()
                        .orElse(Map.of());

                    log.info("Call to create_trello_card(" + params + ")");
                }
            }
        }
        return assistantMessage.content().or(() -> Optional.of("(Нет текста в ответе модели)"));
    }
}
