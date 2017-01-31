package account.mbank;

import account.BankAccount;
import org.json.JSONObject;

public class Account extends BankAccount {
    private String name;

    public Account(JSONObject jsonAccountData) {
        name = jsonAccountData.getString("ProductName");
        number = jsonAccountData.getString("AccountNumber");
        balance = jsonAccountData.getString("Balance");
    }

    @Override
    public String toString() {
        return name + " " + number + ":\t" + balance;
    }
}
