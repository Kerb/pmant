package eu.pmant.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogoutResponse implements ApiResponse {
    private boolean success;

    private String message;
}
