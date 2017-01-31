package scraper.mbank;

import com.meterware.httpunit.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.SAXException;
import scraper.BankAccount;
import scraper.BankScraper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Scraper implements BankScraper {
    private static String MBANK_URL = "https://online.mbank.pl/";
    private String login;
    private String password;
    private String requestVerificationToken;
    private WebConversation webConversation;

    public Scraper(String login, String password) {
        this.login = login;
        this.password = password;
        webConversation = new WebConversation();
        HttpUnitOptions.setDefaultCharacterSet("UTF-8");
        HttpUnitOptions.setScriptingEnabled(true);
    }

    @Override
    public List<BankAccount> getAccounts() throws SAXException, IOException {
        sendCredentials();
        discoverRequestVerificationToken();
        String accountsJson = fetchAccountsJson();
        return getAccountsFromJson(accountsJson);
    }

    private void sendCredentials() throws IOException, SAXException {
        WebRequest request = new PostMethodWebRequest(MBANK_URL +"pl/LoginMain/Account/JsonLogin",
                new ByteArrayInputStream(prepareLoginData().getBytes()),
                "application/json;charset=UTF-8");
        request.setHeaderField("Content-Type", "application/json;charset=UTF-8");
        webConversation.sendRequest(request);
    }

    private  String prepareLoginData() {
        String jsonPrefix = "{_UserName_:_";
        String jsonInfix = "_,_Password_:_";
        String jsonSuffix = "_,_Seed_:_IuPy73fmyUmjeMYhanpFEw==_,_Scenario_:_Default_,_UWAdditionalParams_:{_InOut_:" +
                "null,_ReturnAddress_:null,_Source_:null},_Lang_:__}";
        return (jsonPrefix + login + jsonInfix + password + jsonSuffix).replace('_', '"');
    }

    private void discoverRequestVerificationToken() throws SAXException, IOException {
        WebRequest requestForToken = new GetMethodWebRequest(MBANK_URL + "pl");
        WebResponse response = webConversation.sendRequest(requestForToken);
        requestVerificationToken = response.getElementsWithName("__AjaxRequestVerificationToken")[0].getAttribute("content");
    }

    private String fetchAccountsJson() throws IOException, SAXException {
        WebRequest request = new PostMethodWebRequest(MBANK_URL + "pl/MyDesktop/Desktop/GetAccountsList");
        request.setHeaderField("X-Request-Verification-Token", requestVerificationToken);
        request.setHeaderField("X-Requested-With", "XMLHttpRequest");
        request.setHeaderField("X-Tab-Id", webConversation.getCookieValue("mBank_tabId"));
        WebResponse response = webConversation.sendRequest(request);
        return response.getText();
    }

    private List<BankAccount> getAccountsFromJson(String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray array = jsonObject.getJSONArray("accountDetailsList");
        List<JSONObject> l = new ArrayList<>();
        for (int i = 0; i < array.length(); ++i)
            l.add(array.getJSONObject(i));
        return l.stream().map(item -> new Account(item)).collect(Collectors.toList());
    }
}
