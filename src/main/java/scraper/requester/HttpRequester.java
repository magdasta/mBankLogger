package scraper.requester;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HttpRequester {

    private HttpURLConnection connection;
    private String host;

    public HttpRequester(String host) {
        this.host = host;
    }

    // returns a list of values of given http header
    public List<String> getHeaderFieldsByName(String headerName) {
        return connection.getHeaderFields().get(headerName);
    }

    public void setHttpRequestHeader(String headerName, String headerValue) {
        connection.setRequestProperty(headerName, headerValue);
    }

    public void setRequestCookie(String cookie) {
        connection.setRequestProperty("Cookie", cookie);
    }

    public void setupBasicHttpConnection(String requestMethod, String urlPath) throws IOException {
        URL url = new URL(host + urlPath);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);
    }


    public void sendRequestBody(String requestBody) throws IOException {
        connection.setRequestProperty("Content-Length", Integer.toString(requestBody.length()));
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(requestBody);
        outputStream.flush();
        outputStream.close();
    }

    public String receiveHttpResponse() throws IOException {
        BufferedReader in =
                new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
