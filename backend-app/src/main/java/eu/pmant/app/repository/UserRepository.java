package eu.pmant.app.repository;

import eu.pmant.app.generated.jooq.Tables;
import eu.pmant.app.model.User;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    private final DSLContext dslContext;

    public UserRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public User create(User user) {
        Long createdUserId = dslContext.insertInto(Tables.USERS)
                .set(Tables.USERS.LOGIN, user.getLogin())
                .set(Tables.USERS.PASSWORD_HASH, user.getPasswordHash())
                .returning(Tables.USERS.ID)
                .fetchOne()
                .getValue(Tables.USERS.ID);
        return user.toBuilder().id(createdUserId).build();
    }

    public Optional<User> findByLogin(String login) {
        return dslContext.selectFrom(Tables.USERS)
                .where(Tables.USERS.LOGIN.eq(login))
                .fetchOptionalInto(User.class);
    }

    public Optional<User> findById(Long id) {
        return dslContext.selectFrom(Tables.USERS)
                .where(Tables.USERS.ID.eq(id))
                .fetchOptionalInto(User.class);
    }

    public void deleteAll() {
        dslContext.deleteFrom(Tables.USERS).execute();
    }
}