package eu.pmant.app.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
public class SessionData implements Serializable {

    final Long userId;
    final String login;

}
