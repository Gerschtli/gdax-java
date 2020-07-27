package com.coinbase.exchange.api.accounts;

import com.coinbase.exchange.api.BaseIntegrationTest;
import com.coinbase.exchange.api.payments.CoinbaseAccount;
import com.coinbase.exchange.api.payments.PaymentService;
import com.coinbase.exchange.api.payments.PaymentType;
import com.coinbase.exchange.api.withdrawals.WithdrawalsService;
import com.coinbase.exchange.model.PaymentResponse;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * See class doc for BaseIntegrationTest
 */
@Ignore
public class WithdrawalIntegrationTest extends BaseIntegrationTest {

    private final static Logger log  = LoggerFactory.getLogger(WithdrawalIntegrationTest.class);

    PaymentService paymentService; // TODO Mock
    WithdrawalsService withdrawalsService; // TODO Mock
    AccountService accountService; // TODO Mock

    @Test
    public void withdrawToCoinbaseAccount(){
        List<Account> gdaxAccounts = accountService.getAccounts();
        List<PaymentType> paymentTypes = paymentService.getPaymentTypes();
        List<CoinbaseAccount> coinbaseAccounts = paymentService.getCoinbaseAccounts();
        assertTrue(paymentTypes.size() > 0);

        PaymentType mainType = getUsdPaymentType(paymentTypes);
        Account gdaxAccount = getUsdAccount(gdaxAccounts);
        CoinbaseAccount account = getUsdCoinbaseAccount(coinbaseAccounts);

        log.info("Testing withdrawToPayment with " + mainType.getId());

        BigDecimal gdaxUSDValue = gdaxAccount.getBalance();
        BigDecimal withdrawAmount = new BigDecimal(100);
        PaymentResponse response = withdrawalsService.makeWithdrawalToCoinbase(withdrawAmount, mainType.getCurrency(), account.getId());
        assertTrue(response.getId().length() > 0 && response.getAmount().compareTo(withdrawAmount) == 0);
        log.info("Returned: " + response.getId());
        gdaxAccount = accountService.getAccount(gdaxAccount.getId());
        assertEquals(0, gdaxUSDValue.subtract(withdrawAmount).compareTo(gdaxAccount.getBalance()));
    }

    private CoinbaseAccount getUsdCoinbaseAccount(List<CoinbaseAccount> coinbaseAccounts) {
        CoinbaseAccount account = null;
        for(CoinbaseAccount coinbaseAccount : coinbaseAccounts){
            if(coinbaseAccount.getCurrency().equalsIgnoreCase("USD")){
                account = coinbaseAccount;
            }
        }
        assertNotNull(account);
        return account;
    }

    private Account getUsdAccount(List<Account> gdaxAccounts) {
        Account gdaxAccount = null;
        for(Account account : gdaxAccounts){
            if(account.getCurrency().equalsIgnoreCase("USD")){
                gdaxAccount = account;
                break;
            }
        }
        assertNotNull(gdaxAccount);
        return gdaxAccount;
    }

    private PaymentType getUsdPaymentType(List<PaymentType> paymentTypes) {
        PaymentType mainType = null;
        for(PaymentType paymentType : paymentTypes){
           if(paymentType.getCurrency().equalsIgnoreCase("USD")){
               mainType = paymentType;
               break;
           }
        }
        assertNotNull(mainType);
        return mainType;
    }
}
