package eu.pmant.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecordingCreateResponse implements ApiResponse {
    boolean success;
    Long recordingId;
}
