import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

class HttpRequester {
    private HttpURLConnection connection;
    private String host;

    HttpRequester(String host) {
        this.host = host;
    }

    // returns a list of values of given http header
    List<String> getHeaderFieldsByName(String headerName) {
        return connection.getHeaderFields().get(headerName);
    }

    void setHttpRequestHeader(String headerName, String headerValue) {
        connection.setRequestProperty(headerName, headerValue);
    }

    void setRequestCookie(String cookie) {
        connection.setRequestProperty("Cookie", cookie);
    }

    void setupBasicHttpConnection(String requestMethod, String urlPath) throws IOException {
        URL url = new URL(host + urlPath);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);
    }


    void sendRequestBody(String requestBody) throws IOException {
        connection.setRequestProperty("Content-Length", Integer.toString(requestBody.length()));
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(requestBody);
        outputStream.flush();
        outputStream.close();
    }

    String receiveHttpResponse() throws IOException {
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
