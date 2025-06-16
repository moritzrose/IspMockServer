package de.fi.IspMockServer.service;

import de.fi.IspMockServer.entitys.UserSession;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class HttpService {

    private static final String URL = "https://178.15.147.134:8043/agentgateway/resource/onlineagent/";
    private static final String AGENT_ID = System.getenv("poc.agentid");
    private static final String PASSWORD = System.getenv("poc.password");
    private static final String PHONE_NO = System.getenv("poc.tel");


    /**
     * <pre>
     * "caller": "40038",
     * "called": "40040",
     * "skillid": 25,
     * "callappdata": "",
     * "mediaability": 1,
     * "userVideoDirection": 4
     * </pre>
     */
    public String initiateCall(UserSession userSession) {

        final String sessionId = userSession.getSessionId();

        final List<NameValuePair> entity = new ArrayList<>();
        entity.add(new BasicNameValuePair("caller", "40038"));
        entity.add(new BasicNameValuePair("called", "40040"));
        entity.add(new BasicNameValuePair("skillid", "25"));
        entity.add(new BasicNameValuePair("callappdata", ""));
        entity.add(new BasicNameValuePair("mediaability", "1"));
        entity.add(new BasicNameValuePair("userVideoDirection", "4"));


        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            final HttpPut httpPut = new HttpPut(String.format("https://IP address:Port number/agentgateway/resource/voicecall/%s/callout", sessionId));
            httpPut.setEntity(new UrlEncodedFormEntity(entity));

            try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
                return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            return "\"message\": \"\",\n" +
                    "    \t\"retcode\": \"0\",\n" +
                    "    \t\"result\": \"1455885056-1095\"\n";
        }
    }

    public String answerCall() {
        return "\"message\": \"\",\n" +
                "    \t\"retcode\": \"0\",\n" +
                "    \t\"result\": \"1455885056-1095\"\n";
    }

    public Optional<String> getGuid(UserSession userSession) {
        final String sessionId = userSession.getSessionId();

        final List<NameValuePair> entity = new ArrayList<>();
        entity.add(new BasicNameValuePair("password", ""));
        entity.add(new BasicNameValuePair("phonenum", "40038"));
        entity.add(new BasicNameValuePair("status", "4"));
        entity.add(new BasicNameValuePair("releasephone", "true"));
        entity.add(new BasicNameValuePair("agenttype", "4"));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            final HttpPut httpPut = new HttpPut(String.format("https://ip:port/agentgateway/resource/onlineagent/%s", sessionId));

            httpPut.setEntity(new UrlEncodedFormEntity(entity));

            try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
                return Optional.of(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            return Optional.of("\"message\": \"\",\n" +
                    "    \t\"retcode\": \"0\",\n" +
                    "    \t\"result\": \"1455885056-1095\"\n");
        }
    }

    public String holdCall() {
        return "\"message\": \"\",\n" +
                "    \t\"retcode\": \"0\",\n" +
                "    \t\"result\": \"1455885056-1095\"\n";
    }

    public String unholdCall() {
        return "\"message\": \"\",\n" +
                "    \t\"retcode\": \"0\",\n" +
                "    \t\"result\": \"1455885056-1095\"\n";
    }

    public String releaseCall() {
        return "\"message\": \"\",\n" +
                "    \t\"retcode\": \"0\",\n" +
                "    \t\"result\": \"1455885056-1095\"\n";
    }

    public String setAgentNotReady() {
        return "\"message\": \"\",\n" +
                "    \t\"retcode\": \"0\",\n" +
                "    \t\"result\": \"1455885056-1095\"\n";
    }

    public String setAgentReady() {
        return "\"message\": \"\",\n" +
                "    \t\"retcode\": \"0\",\n" +
                "    \t\"result\": \"1455885056-1095\"\n";
    }

    public Optional<String> login(UserSession userSession) {

        final String sessionId = userSession.getSessionId();


        try {

            String requestBody = String.format(
                    "{" +
                            "\"password\":\"%s\"," +
                            "\"phonenum\":\"%s\"," +
                            "\"status\":\"%s\"," +
                            "\"releasephone\":\"%s\"," +
                            "\"agenttype\":\"%s\"}", PASSWORD, PHONE_NO, 4, true, 4);

            URL url = new URL(URL + AGENT_ID);
            return makeHttpRequest(url, "PUT", requestBody);

        } catch (Exception e) {
        return Optional.of(Arrays.toString(e.getStackTrace()));
        }
    }

    private Optional<String> makeHttpRequest(URL url, String method, String requestBody) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");

        connection.setDoOutput(true);
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return Optional.of(response.toString());
        }
    }

    public Optional<String> logOut(String sessionId) {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            final HttpDelete httpDelete = new HttpDelete(String.format("https://IP address:Port number/agentgateway/resource/onlineagent/%s/logout", sessionId));

            try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {
                return Optional.of(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            return Optional.of("\"message\": \"\",\n" +
                    "    \t\"retcode\": \"0\",\n" +
                    "    \t\"result\": \"1455885056-1095\"\n");
        }
    }
}
