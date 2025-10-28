package eu.pmant.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeData {
    String username;
    Long userId;
}
