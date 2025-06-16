package de.fi.IspMockServer.service;

import de.fi.IspMockServer.entitys.ResponseDto;
import de.fi.IspMockServer.entitys.UserSession;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.web.servlet.function.RequestPredicates.GET;

@Service
public class HttpService {


    private static final TrustManager MOCK_TRUST_MANAGER = new X509ExtendedTrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {

        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {

        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
            // empty method
        }
        // ... Other void methods
    };
    private static final String URL = "https://178.15.147.134:8043/agentgateway/resource/onlineagent/";
    private static final String AGENT_ID = System.getenv("poc.agentid");
    private static final String PASSWORD = System.getenv("poc.password");
    private static final String PHONE_NO = System.getenv("poc.tel");
    private static final String REQUEST_BODY =
            "{\"password\":\"%s\"," +
                    "\"phonenum\":\"%s\"," +
                    "\"status\":\"%s\"," +
                    "\"releasephone\":\"%s\"," +
                    "\"agenttype\":\"%s\"}";


    public Optional<String> initiateCall(UserSession userSession) {

        final String sessionId = userSession.getSessionId();

        try {
            String requestBody = String.format(REQUEST_BODY, PASSWORD, PHONE_NO, 4, true, 4);

            String url = URL + AGENT_ID;
            return Optional.of(makeHttpRequest(url, "PUT", requestBody).get().getBody());

        } catch (Exception e) {
            return Optional.of(Arrays.toString(e.getStackTrace()));
        }
    }

    public Optional<String> answerCall() {
        try {
            String requestBody = String.format(REQUEST_BODY, PASSWORD, PHONE_NO, 4, true, 4);

            String url = URL + AGENT_ID;
            return Optional.of(makeHttpRequest(url, "PUT", requestBody).get().getBody());

        } catch (Exception e) {
            return Optional.of(Arrays.toString(e.getStackTrace()));
        }
    }

    public Optional<String> holdCall() {
        try {
            String requestBody = String.format(REQUEST_BODY, PASSWORD, PHONE_NO, 4, true, 4);

            String url = URL + AGENT_ID;
            return Optional.of(makeHttpRequest(url, "PUT", requestBody).get().getBody());

        } catch (Exception e) {
            return Optional.of(Arrays.toString(e.getStackTrace()));
        }
    }

    public Optional<String> unholdCall() {
        try {
            String requestBody = String.format(REQUEST_BODY, PASSWORD, PHONE_NO, 4, true, 4);

            String url = URL + AGENT_ID;
            return Optional.of(makeHttpRequest(url, "PUT", requestBody).get().getBody());

        } catch (Exception e) {
            return Optional.of(Arrays.toString(e.getStackTrace()));
        }
    }

    public Optional<String> releaseCall() {
        try {
            String requestBody = String.format(REQUEST_BODY, PASSWORD, PHONE_NO, 4, true, 4);

            String url = URL + AGENT_ID;
            return Optional.of(makeHttpRequest(url, "PUT", requestBody).get().getBody());

        } catch (Exception e) {
            return Optional.of(Arrays.toString(e.getStackTrace()));
        }
    }

    public Optional<String> setAgentNotReady() {
        try {
            String requestBody = String.format(REQUEST_BODY, PASSWORD, PHONE_NO, 4, true, 4);

            String url = URL + AGENT_ID;
            return Optional.of(makeHttpRequest(url, "PUT", requestBody).get().getBody());

        } catch (Exception e) {
            return Optional.of(Arrays.toString(e.getStackTrace()));
        }
    }

    public Optional<String> setAgentReady(UserSession userSession) {
        String guid = userSession.getGuid();
        try {
            String requestBody = String.format(REQUEST_BODY, PASSWORD, PHONE_NO, 4, true, 4);

            String url = URL + AGENT_ID;
            return Optional.of(makeHttpRequest(url, "PUT", requestBody).get().getBody());

        } catch (Exception e) {
            return Optional.of(Arrays.toString(e.getStackTrace()));
        }
    }

    public Optional<String> maintainHeartBeat() {
        try {
            String requestBody = String.format(REQUEST_BODY, PASSWORD, PHONE_NO, 4, true, 4);

            String url = URL + AGENT_ID + "/handshake";
            return Optional.of(makeHttpRequest(url, "PUT", requestBody).get().getBody());

        } catch (Exception e) {
            return Optional.of(Arrays.toString(e.getStackTrace()));
        }
    }

    public Optional<String> login(UserSession userSession) {
        try {
            String requestBody = String.format(REQUEST_BODY, PASSWORD, PHONE_NO, 4, true, 4);

            String url =URL + AGENT_ID;
            return Optional.of(makeHttpRequest(url, "PUT", requestBody).get().getBody());

        } catch (Exception e) {
            return Optional.of(Arrays.toString(e.getStackTrace()));
        }
    }

    private Optional<ResponseDto> makeHttpRequest(String url, String method, String requestBody) throws IOException, URISyntaxException {

        try {
            SSLContext sslContext = SSLContext.getInstance("SSL"); // OR TLS
            sslContext.init(null, new TrustManager[]{MOCK_TRUST_MANAGER}, new SecureRandom());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .method(method, HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpClient client = HttpClient.newBuilder().sslContext(sslContext).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ResponseDto responseDto = new ResponseDto();
            responseDto.setHeader(response.headers().map());
            responseDto.setBody(response.body());
            return Optional.of(responseDto);
        } catch (URISyntaxException | InterruptedException e) {
            return Optional.empty();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<String> logOut(String sessionId) {

        try {
            String requestBody = String.format(REQUEST_BODY, PASSWORD, PHONE_NO, 4, true, 4);

            String url = URL + AGENT_ID;
            return Optional.of(makeHttpRequest(url, "PUT", requestBody).get().getBody());

        } catch (Exception e) {
            return Optional.of(Arrays.toString(e.getStackTrace()));
        }
    }
}
