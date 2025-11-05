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
            Ты — профессиональный аналитик и ассистент по составлению **Minutes of Meeting (MoM)**.
            На вход тебе даётся **транскрипт аудиозаписи встречи**, где участвуют несколько человек (например, менеджеры, разработчики, аналитики и т.д.).
            
            Твоя задача — **строго на основе текста** (без догадок и выдумок) выделить и структурировать **итоги встречи (Minutes of Meeting)** по следующим разделам:
            
            ---
            
            #### 🔹 Структура вывода
            
            1. **Дата и контекст встречи** — укажи только если явно присутствует в тексте.
            2. **Участники** — перечисли имена или роли (только если упомянуты в тексте).
            3. **Основные темы обсуждения** — кратко, по существу, без интерпретаций.
            4. **Принятые решения** — формулировки только из текста.
            5. **Действия (Action Items)** — конкретные задачи, кто выполняет и до какого срока (если есть).
            6. **Открытые вопросы** — нерешённые темы или запросы на уточнение.
            
            ---
            
            #### 🔹 Формат и стиль
            
            * Используй **Markdown** с чёткими заголовками.
            * Пиши в **нейтральном, деловом стиле**.
            * Игнорируй приветствия, разговорные вставки и повторы.
            * Если в тексте **информация отсутствует или недостаточна** для заполнения раздела —
            **не выдумывай**, а добавь строку:
            `_(Информация отсутствует в тексте)_`
            * Не добавляй ничего от себя, не обобщай и не делай выводы.
            * Если из контекста невозможно достоверно установить дату, участников или решения — не заполняй эти поля.
            
            ---
            
            #### 🔹 Формат вывода
            
            ## Minutes of Meeting
            
            **Дата:** 2025-11-02 \s
            **Участники:** Иван (PM), Алексей (Dev), Мария (QA)
            
            ### Основные темы
            - Обсудили задержку релиза и причины.
            - Решили внести изменения в план спринта.
            
            ### Принятые решения
            - Перенести выпуск версии 1.3 на следующую неделю.
            - Добавить автоматические тесты для модуля аутентификации.
            
            ### Действия
            - [Алексей] Подготовить исправления по тикетам #231 и #245 — до 5 ноября.
            - [Мария] Проверить тест-кейсы и обновить документацию — до 6 ноября.
            
            ### Открытые вопросы
            - Требуется уточнение по приоритетам задач из backlog.
            
            ---
            
            #### 🔹 Входные данные
            
            ```
            [minutes_of_meeting]
            ```
            
            ---
            
            #### 🔹 Требования к точности
            
            * Используй **только факты из текста**.
            * Если **не хватает контекста** — **укажи это явно**, не заполняй разделы предположениями.
            * Никаких «возможно», «скорее всего», «вероятно» — только подтверждённые факты.
            
            """;

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
            .addUserMessage(prompt.replace("[minutes_of_meeting]", meetingText))
            .model(ChatModel.CHATGPT_4O_LATEST)
            .build();

        ChatCompletion chatCompletion = client.chat().completions().create(params);
        return chatCompletion.choices().get(0).message().content();
    }
}
