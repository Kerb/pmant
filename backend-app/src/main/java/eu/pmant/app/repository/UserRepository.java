package eu.pmant.app.repository;

import eu.pmant.app.generated.jooq.tables.pojos.UserAccount;
import java.util.Optional;

public interface UserRepository {

    UserAccount create(UserAccount userAccount);

    Optional<UserAccount> findByLogin(String login);

}
