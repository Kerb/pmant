package eu.pmant.app.util;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    public void testHash() {
        PasswordUtil passwordUtil = new PasswordUtil();
        String hashPassword = passwordUtil.hashPassword("123");
        Assert.notNull(hashPassword, "Hash is null");
    }
}