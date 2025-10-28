package eu.pmant.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeResponse implements ApiResponse {
    boolean success;

    MeData data;

}
