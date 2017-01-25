import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

import static java.util.Collections.*;

class MBankLogger {
    private MBankHttpRequester mBankHttpRequester;

    void tryIt() throws IOException {
        setupMBankHttpRequester();
        prepareAndPrintAccountsData();
    }

    private void setupMBankHttpRequester() throws IOException {
        mBankHttpRequester = new MBankHttpRequester();
        mBankHttpRequester.setCookieManager();
        mBankHttpRequester.setRequestVerificationToken();
    }

    private void prepareAndPrintAccountsData() throws IOException {
        mBankHttpRequester.sendRequestForGettingAccountsData();
        String responseJson = mBankHttpRequester.receiveHttpResponse();
        printSortedAccounts(getAccountsFromJson(responseJson));
    }

    private void printSortedAccounts(ArrayList<Account> accounts) {
        sort(accounts);
        for (Account account : accounts)
            System.out.println(account.toString());
    }

    private ArrayList<Account> getAccountsFromJson(String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray array = jsonObject.getJSONArray("accountDetailsList");
        ArrayList<Account> accounts = new ArrayList<>();
        for (int i = 0; i < array.length(); ++i)
            accounts.add(new Account(array.getJSONObject(i)));
        return accounts;
    }

}
