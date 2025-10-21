package eu.pmant.app.repository;

import eu.pmant.app.model.User;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static eu.pmant.app.jooq.tables.Users.USERS;

@Repository
public class UserRepository {

    private final DSLContext dslContext;

    public UserRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public User create(User user) {
        dslContext.insertInto(USERS)
                .set(USERS.ID, user.getId())
                .set(USERS.LOGIN, user.getLogin())
                .set(USERS.PASSWORD_HASH, user.getPasswordHash())
                .execute();
        return user;
    }

    public Optional<User> findByLogin(String login) {
        return dslContext.selectFrom(USERS)
                .where(USERS.LOGIN.eq(login))
                .fetchOptionalInto(User.class);
    }

    public Optional<User> findById(UUID id) {
        return dslContext.selectFrom(USERS)
                .where(USERS.ID.eq(id))
                .fetchOptionalInto(User.class);
    }
}