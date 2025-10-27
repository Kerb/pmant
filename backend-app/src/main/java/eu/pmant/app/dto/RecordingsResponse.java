package eu.pmant.app.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class RecordingsResponse implements ApiResponse {
    /**
     * Статус ответа
     */
    private boolean success;

    /**
     * Список записей
     */
    List<Recording> recordings;
}
