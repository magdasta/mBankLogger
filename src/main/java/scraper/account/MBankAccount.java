package scraper.account;

import org.json.JSONObject;

public class MBankAccount extends BankAccount {
    private String name;

    public MBankAccount(JSONObject jsonAccountData) {
        name = jsonAccountData.getString("ProductName");
        number = jsonAccountData.getString("AccountNumber");
        balance = jsonAccountData.getString("Balance");
    }

    @Override
    public String toString() {
        return name + " " + number + ":\t" + balance;
    }
}