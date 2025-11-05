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
            Ты — профессиональный аналитик и ассистент по составлению протоколов встреч.
            На входе тебе даётся *транскрипт аудиозаписи встречи*, где участвуют несколько человек (например, менеджеры, разработчики, аналитики и т.д.).
            
            Твоя задача — на основе текста выделить и структурировать **Minutes of Meeting (MoM)**, включив следующие разделы:
            
            1. **Дата и контекст встречи** — если есть в тексте (иначе не указывай).
            2. **Участники** — перечисли имена или роли (если упоминаются).
            3. **Основные темы обсуждения** — краткое содержание ключевых пунктов.
            4. **Решения, принятые на встрече** — чёткие формулировки.
            5. **Действия (Action Items)** — конкретные задачи с ответственными и, если есть, сроками.
            6. **Открытые вопросы или нерешённые темы** — то, что требует дальнейшего обсуждения.
            
            Формат вывода:
            
            * Используй чёткую структуру Markdown с заголовками.
            * Сохрани нейтральный, деловой стиль.
            * Игнорируй "водные" фразы, разговорные элементы, повторы и приветствия.
            * Если информация отсутствует — просто пропусти раздел.
            
            **Входные данные:**
            
            ```
            [minutes_of_meeting]
            ```
            
            **Выходные данные (пример формата):**
            
            ```
            ## Minutes of Meeting
            **Дата:** 2025-11-02
            **Участники:** Иван (PM), Алексей (Dev), Мария (QA)
            
            ### Основные темы
            - Обсудили задержку релиза и причины.
            - Решили внести изменения в план спринта.
            
            ### Принятые решения
            - Перенести выпуск версии 1.3 на следующую неделю. \s
            - Добавить автоматические тесты для модуля аутентификации. \s
            
            ### Действия
            - [Алексей] Подготовить исправления по тикетам #231 и #245 — до 5 ноября. \s
            - [Мария] Проверить тест-кейсы и обновить документацию — до 6 ноября.
            
            ### Открытые вопросы
            - Требуется уточнение по приоритетам задач из backlog.
            ```
            
            Если транскрипция длинная, можешь объединять повторы и логически группировать информацию.
            """;

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
            .addUserMessage(prompt.replace("[minutes_of_meeting]", meetingText))
            .model(ChatModel.GPT_3_5_TURBO)
            .build();

        ChatCompletion chatCompletion = client.chat().completions().create(params);
        return chatCompletion.choices().get(0).message().content();
    }
}
