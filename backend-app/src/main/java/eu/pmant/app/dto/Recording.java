package eu.pmant.app.dto;

import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Recording {
    @Nonnull
    private Long id;
    @Nonnull
    private String title;
    @Nonnull
    private String date;
    @Nonnull
    private String time;
    @Nonnull
    private String duration;
    @Nonnull
    private Integer participants;
    @Nonnull
    private String status;
}
