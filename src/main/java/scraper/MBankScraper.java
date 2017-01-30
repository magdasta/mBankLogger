package scraper;

import com.meterware.httpunit.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.SAXException;
import account.BankAccount;
import account.MBankAccount;
import scraper.generic.BankScraper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MBankScraper implements BankScraper {
    private static String MBANK_URL = "https://online.mbank.pl/";
    private String login;
    private String password;
    private String requestVerificationToken;
    private WebConversation webConversation;

    public MBankScraper(String login, String password) {
        this.login = login;
        this.password = password;
        setupWebConversation();
    }

    @Override
    public List<BankAccount> getAccounts() throws IOException, SAXException {
        sendCredentials();
        discoverRequestVerificationToken();
        String accountsJson = getAccountsJson();
        return getAccountsFromJson(accountsJson);
    }

    private void setupWebConversation() {
        webConversation = new WebConversation();
        HttpUnitOptions.setDefaultCharacterSet("UTF-8");
        HttpUnitOptions.setScriptingEnabled(true);
    }

    private void sendCredentials() throws IOException, SAXException {
        WebRequest request = new PostMethodWebRequest(MBANK_URL +"pl/LoginMain/Account/JsonLogin",
                new ByteArrayInputStream(prepareLoginData().getBytes()),
                "application/json;charset=UTF-8");
        request.setHeaderField("Content-Type", "application/json;charset=UTF-8");
        webConversation.sendRequest(request);
    }

    private void discoverRequestVerificationToken() throws SAXException, IOException {
        WebRequest requestForToken = new GetMethodWebRequest(MBANK_URL + "pl");
        WebResponse response = webConversation.sendRequest(requestForToken);
        requestVerificationToken = response.getElementsWithName("__AjaxRequestVerificationToken")[0].getAttribute("content");
    }

    private String getAccountsJson() throws IOException, SAXException {
        WebRequest request = new PostMethodWebRequest(MBANK_URL + "pl/MyDesktop/Desktop/GetAccountsList");
        request.setHeaderField("X-Request-Verification-Token", requestVerificationToken);
        request.setHeaderField("X-Requested-With", "XMLHttpRequest");
        request.setHeaderField("X-Tab-Id", webConversation.getCookieValue("mBank_tabId"));
        WebResponse response = webConversation.sendRequest(request);
        return response.getText();
    }

    private  String prepareLoginData() {
        String jsonPrefix = "{_UserName_:_";
        String jsonInfix = "_,_Password_:_";
        String jsonSuffix = "_,_Seed_:_IuPy73fmyUmjeMYhanpFEw==_,_Scenario_:_Default_,_UWAdditionalParams_:{_InOut_:" +
                "null,_ReturnAddress_:null,_Source_:null},_Lang_:__}";
        return (jsonPrefix + login + jsonInfix + password + jsonSuffix).replace('_', '"');
    }

    private List<BankAccount> getAccountsFromJson(String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray array = jsonObject.getJSONArray("accountDetailsList");
        List<BankAccount> accounts = new ArrayList<>();
        for (int i = 0; i < array.length(); ++i)
            accounts.add(new MBankAccount(array.getJSONObject(i)));
        return accounts;
    }
}
