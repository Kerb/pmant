package eu.pmant.app.dto;

import eu.pmant.app.generated.jooq.tables.pojos.UserMeetings;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecordingDetailsResponse implements ApiResponse {

    boolean success;

    UserMeetings meetingDetails;

}
