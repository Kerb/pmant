package eu.pmant.app.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
            
            Если данных нет — пиши:
            `(Информация отсутствует в тексте)`
            
            ---
            
            ### 🔹 Формат вывода
            
            **Используй Markdown, нейтральный деловой стиль.**
            Игнорируй приветствия, повторы, не добавляй интерпретаций.
            
            ## Minutes of Meeting
            
            **Дата:** (если есть) \s
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

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
            .addUserMessage(prompt.replace("[minutes_of_meeting]", meetingText))
            .model(ChatModel.CHATGPT_4O_LATEST)
            .build();

        ChatCompletion chatCompletion = client.chat().completions().create(params);
        return chatCompletion.choices().get(0).message().content();
    }
}
