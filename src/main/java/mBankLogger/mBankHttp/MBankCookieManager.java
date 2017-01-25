package mBankLogger.mBankHttp;


import java.io.IOException;
import java.util.List;

class MBankCookieManager {
    private static final String MBANK2_COOKIE_NAME = "mBank2";
    private static final String MBANK_TABID_COOKIE_NAME = "mBank_tabId";

    // cookies in format cookieName:cookieValue (like in http headers)
    private String mBank2Cookie;
    private String mBank_tabIdCookie;

    MBankCookieManager() throws IOException {
        MBankHttpRequester mBankHttpRequester = new MBankHttpRequester();
        mBankHttpRequester.sendHttpRequestForDiscoveringCookies();
        setCookies(mBankHttpRequester);
    }

    private void setCookies(MBankHttpRequester mBankHttpRequester) {
        List<String> headersSettingCookies = mBankHttpRequester.getHeaderFieldsByName("Set-Cookie");
        mBank_tabIdCookie = findCookieInHeaders(headersSettingCookies, MBANK_TABID_COOKIE_NAME);
        mBank2Cookie = findCookieInHeaders(headersSettingCookies, MBANK2_COOKIE_NAME);
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

    // returns value of the cookie without its name and colon
    String getRawTabIdValue() {
        return mBank_tabIdCookie.substring(MBANK_TABID_COOKIE_NAME.length() + 1);
    }

    // returns String containing both cookies formatted for sending as a "Cookie" http header
    String prepareCookieHttpHeader() {
        String cookie = "";
        if (! "".equals(mBank2Cookie))
            cookie = cookie + "; " + mBank2Cookie;
        if (! "".equals(mBank_tabIdCookie))
            cookie = cookie + "; " + mBank_tabIdCookie;
        return cookie;
    }
}
