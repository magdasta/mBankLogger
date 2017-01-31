package scraper.mbank;

import scraper.BankAccount;
import org.json.JSONObject;

import java.math.BigDecimal;

public class Account extends BankAccount {
    private String name;

    public Account(JSONObject jsonAccountData) {
        name = jsonAccountData.getString("ProductName");
        number = jsonAccountData.getString("AccountNumber");
        balance = new BigDecimal(jsonAccountData.getString("Balance").replace(',', '.'));
        availableBalance = new BigDecimal(jsonAccountData.getString("AvailableBalance").replace(',', '.'));
        currency = jsonAccountData.getString("Currency");
    }

    @Override
    public String toString() {
        return name + " " + number + ":\t" + balance + "\t" + availableBalance + "\t" + currency;
    }
}
