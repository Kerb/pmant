package eu.pmant.app.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class User {

    private final Long id;
    private final String login;
    private final String passwordHash;

}