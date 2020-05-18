package com.coinbase.exchange.api.accounts;

import com.coinbase.exchange.api.BaseIntegrationTest;
import com.coinbase.exchange.api.useraccount.UserAccountData;
import com.coinbase.exchange.api.useraccount.UserAccountService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * See class doc for BaseIntegrationTest
 */
public class UserAccountServiceIntegrationTest extends BaseIntegrationTest {

    UserAccountService userAccountService;

    /**
     * Trailing volume could be empty so all we have to do is make sure it's not returning null
     */
    @Test
    public void getTrailingVolume(){
        List<UserAccountData> data = userAccountService.getTrailingVolume();
        assertNotNull(data);
    }
}
