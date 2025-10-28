package eu.pmant.app.repository;

import eu.pmant.app.generated.jooq.Tables;
import eu.pmant.app.generated.jooq.tables.pojos.UserAccount;
import eu.pmant.app.generated.jooq.tables.records.UserAccountRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepository {

    private final DSLContext dslContext;

    @Transactional
    public UserAccount create(UserAccount userAccount) {
        UserAccountRecord record = dslContext.insertInto(Tables.USER_ACCOUNT)
            .set(Tables.USER_ACCOUNT.LOGIN, userAccount.getLogin())
            .set(Tables.USER_ACCOUNT.PASSWORD_HASH, userAccount.getPasswordHash())
            .returning(Tables.USER_ACCOUNT.ID)
            .fetchOne();

        if (record != null) {
            userAccount.setId(record.getValue(Tables.USER_ACCOUNT.ID));
        } else {
            throw new RuntimeException("Failed to create userAccount");
        }
        return userAccount;
    }

    public Optional<UserAccount> findByLogin(String login) {
        return dslContext.selectFrom(Tables.USER_ACCOUNT)
                .where(Tables.USER_ACCOUNT.LOGIN.eq(login))
                .fetchOptionalInto(UserAccount.class);
    }
    public void deleteAll() {
        dslContext.deleteFrom(Tables.USER_ACCOUNT).execute();
    }
}