package scraper;

import org.json.JSONArray;
import org.json.JSONObject;
import scraper.account.BankAccount;
import scraper.account.MBankAccount;
import scraper.requester.HttpRequester;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MBankScraper implements BankScraper {
    private static String MBANK_URL = "https://online.mbank.pl";
    private String login;
    private String password;
    private String mBank2Cookie;
    private String mBank_tabIdCookie;
    private String MBANK2_COOKIE_NAME = "mBank2";
    private String MBANK_TABID_COOKIE_NAME = "mBank_tabId";
    private String requestVerificationToken;


    @Override
    public List<BankAccount> getAccounts() throws IOException {
        askForCredentials();
        prepareCookies();
        prepareRequestVerificationToken();
        return prepareAccountsList();
    }

    private void askForCredentials() {
        Console console = System.console();
        try {
            console.printf("Login: ");
            login = console.readLine();
            password = new String(console.readPassword("Password: "));
        }
        catch(NullPointerException nullPointerException ) {
            System.out.println("Couldn't get Console instance - unable to ask for credentials. " +
                    "Please try running this application from shell or edit MBankScraper's " +
                    "'askForCredentials' method to provide login and password other way.");
            System.exit(0);
        }
        // if you have to, hardcode login data here and delete the 'exit' statement above
        //login = "login_goes_here";
        //password = "password_goes_here";
    }

    private void prepareCookies() throws IOException {
        setCookies(prepareHttpRequestForDiscoveringCookies());
    }

    private HttpRequester prepareHttpRequestForDiscoveringCookies() throws IOException {
        HttpRequester httpRequester = new HttpRequester(MBANK_URL);
        httpRequester.setupBasicHttpConnection("POST", "/pl/LoginMain/Account/JsonLogin");
        httpRequester.setHttpRequestHeader("Content-Type", "application/json;charset=UTF-8");
        httpRequester.sendRequestBody(prepareLoginData());
        return httpRequester;
    }

    private String prepareLoginData() {
        String jsonPrefix = "{\"UserName\":\"";
        String jsonInfix = "\",\"Password\":\"";
        String jsonSuffix = "\",\"Seed\":\"IuPy73fmyUmjeMYhanpFEw==\"," +
                "\"Scenario\":\"Default\"," +
                "\"UWAdditionalParams\":{\"InOut\":null," +
                "\"ReturnAddress\":null," +
                "\"Source\":null}," +
                "\"Lang\":\"\"}";
        return jsonPrefix + login + jsonInfix + password + jsonSuffix;
    }

    private void setCookies(HttpRequester httpRequester) {
        List<String> headersSettingCookies = httpRequester.getHeaderFieldsByName("Set-Cookie");

        mBank2Cookie = findCookieInHeaders(headersSettingCookies, MBANK2_COOKIE_NAME);
        mBank_tabIdCookie = findCookieInHeaders(headersSettingCookies, MBANK_TABID_COOKIE_NAME);
    }

    private String findCookieInHeaders(List<String> httpHeaders, String cookieName) {
        for (String header : httpHeaders)
            if (header.contains(cookieName))
                return extractCookie(header, cookieName);
        return "";
    }
    // cookies start with their names and are separated with semicolon
    private String extractCookie(String httpHeader, String cookieName) {
        int beginIndex = httpHeader.indexOf(cookieName);
        String headerWithoutPrefix = httpHeader.substring(beginIndex);
        return headerWithoutPrefix.split(";")[0];
    }
    private List<BankAccount> prepareAccountsList() throws IOException {
        HttpRequester httpRequester = prepareRequesterForGettingAccountsData();
        String json = httpRequester.receiveHttpResponse();
        return getAccountsFromJson(json);
    }

    private HttpRequester prepareRequesterForGettingAccountsData() throws IOException {
        HttpRequester httpRequester = prepareHttpRequesterForGettingAccountsData();
        String requestBody = "{}";
        httpRequester.sendRequestBody(requestBody);
        return httpRequester;

    }

    private List<BankAccount> getAccountsFromJson(String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray array = jsonObject.getJSONArray("accountDetailsList");
        List<BankAccount> accounts = new ArrayList<>();
        for (int i = 0; i < array.length(); ++i)
            accounts.add(new MBankAccount(array.getJSONObject(i)));
        return accounts;
    }

    private HttpRequester prepareHttpRequesterForGettingAccountsData() throws IOException {
        HttpRequester httpRequester = new HttpRequester(MBANK_URL);
        httpRequester.setupBasicHttpConnection("POST", "/pl/MyDesktop/Desktop/GetAccountsList");
        httpRequester.setRequestCookie(prepareCookieHttpHeader());
        httpRequester.setHttpRequestHeader("X-Request-Verification-Token", requestVerificationToken);
        httpRequester.setHttpRequestHeader("X-Requested-With", "XMLHttpRequest");
        httpRequester.setHttpRequestHeader("X-Tab-Id", getRawTabIdValue());
        return httpRequester;
    }


    // returns String containing both cookies formatted for sending as a "Cookie" http header
    private String prepareCookieHttpHeader() {
        String cookie = "";
        if (! "".equals(mBank2Cookie))
            cookie = cookie + "; " + mBank2Cookie;
        if (! "".equals(mBank_tabIdCookie))
            cookie = cookie + "; " + mBank_tabIdCookie;
        return cookie;
    }

    private String getRawTabIdValue() {
        return mBank_tabIdCookie.substring(MBANK_TABID_COOKIE_NAME.length() + 1);
    }

    private void prepareRequestVerificationToken() throws IOException {
        HttpRequester httpRequester = new HttpRequester(MBANK_URL);
        httpRequester.setupBasicHttpConnection("GET", "/pl");
        httpRequester.setRequestCookie(prepareCookieHttpHeader());
        String html = httpRequester.receiveHttpResponse();
        requestVerificationToken = findRequestVerificationTokenInHtml(html);
    }

    private String findRequestVerificationTokenInHtml(String html) {
        int endOfToken = html.indexOf("\" name=\"__AjaxRequestVerificationToken\">");
        String htmlWithoutSuffix = html.substring(0, endOfToken);
        int lastIndexOfQuotationMark = htmlWithoutSuffix.lastIndexOf('"');
        return htmlWithoutSuffix.substring(lastIndexOfQuotationMark + 1);
    }
}
