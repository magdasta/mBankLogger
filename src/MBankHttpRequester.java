import java.io.IOException;

class MBankHttpRequester extends HttpRequester{
    private String requestVerificationToken;
    private MBankCookieManager mBankCookieManager;

    MBankHttpRequester() {
        super(ConstantsHolder.MBANK_URL);
    }

    void setCookieManager() throws IOException {
        mBankCookieManager = new MBankCookieManager();
    }

    void sendHttpRequestForDiscoveringCookies() throws IOException {
        setupBasicHttpConnection("POST", ConstantsHolder.LOGIN_PATH);
        setHttpRequestHeader("Content-Type", "application/json;charset=UTF-8");
        sendRequestBody(prepareLoginData());
    }

    private String prepareLoginData() {
        return "{\"UserName\":\"username_goes_here\"," +
                "\"Password\":\"password_goes_here\"," +
                "\"Seed\":\"IuPy73fmyUmjeMYhanpFEw==\"," +
                "\"Scenario\":\"Default\"," +
                "\"UWAdditionalParams\":{\"InOut\":null," +
                "\"ReturnAddress\":null," +
                "\"Source\":null}," +
                "\"Lang\":\"\"}";
    }
    void setRequestVerificationToken() throws IOException {
        //HttpRequester httpRequester = new HttpRequester(ConstantsHolder.MBANK_URL);
        setupBasicHttpConnection("GET", ConstantsHolder.REQUEST_VERIFICATION_PATH);
        setRequestCookie(mBankCookieManager.prepareCookieHttpHeader());
        String html = receiveHttpResponse();
        requestVerificationToken = findRequestVerificationTokenInHtml(html);
    }

    private String findRequestVerificationTokenInHtml(String html) {
        int endOfToken = html.indexOf("\" name=\"__AjaxRequestVerificationToken\">");
        String htmlWithoutSuffix = html.substring(0, endOfToken);
        int lastIndexOfQuotationMark = htmlWithoutSuffix.lastIndexOf('"');
        return htmlWithoutSuffix.substring(lastIndexOfQuotationMark + 1);
    }

    void sendRequestForGettingAccountsData() throws IOException {
        String requestBody = "{}";
        prepareRequesterForGettingAccountsData();
        sendRequestBody(requestBody);
    }

    private void prepareRequesterForGettingAccountsData() throws IOException {
        setupBasicHttpConnection("POST", ConstantsHolder.GET_ACCOUNTS_PATH);
        setRequestCookie(mBankCookieManager.prepareCookieHttpHeader());
        setHttpRequestHeader("X-Request-Verification-Token", requestVerificationToken);
        setHttpRequestHeader("X-Requested-With", "XMLHttpRequest");
        setHttpRequestHeader("X-Tab-Id", mBankCookieManager.getRawTabIdValue());
    }

}
