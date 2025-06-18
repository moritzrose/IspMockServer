package de.fi.IspMockServer.service;

import de.fi.IspMockServer.entitys.huawei.RequestDto;
import de.fi.IspMockServer.entitys.huawei.ResponseDto;
import de.fi.IspMockServer.entitys.UserSession;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private static final String URL = "https://10.0.2.79:8043/agentgateway/resource/onlineagent/";
    private static final String AGENT_ID = System.getenv("poc.agentid");
    private static final String PASSWORD = System.getenv("poc.password");
    private static final String PHONE_NO = System.getenv("poc.tel");

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    public static final String CALL_BACK_URI = "http://10.200.80.5:8080/softphone/event/";

    public Optional<String> initiateCall(UserSession userSession) {
        return Optional.empty();

    }

    public Optional<String> answerCall(UserSession userSession) {
        try {
            RequestDto requestDto = new RequestDto();

            Map<String, String> header = new HashMap<>();
            header.put("Content-Type", "application/json");
            header.put("Guid", userSession.getGuid());

            String url = URL + AGENT_ID + "/answer?=" + userSession.getCallData().getCallid();
            Optional<ResponseDto> responseDto = makeHttpRequest(url, "PUT", requestDto, header);
            return responseDto.map(ResponseDto::getBody);
        } catch (Exception e) {
            return Optional.of(Arrays.toString(e.getStackTrace()));
        }
    }

    public Optional<String> holdCall() {
        return Optional.empty();

    }

    public Optional<String> unholdCall() {
        return Optional.empty();

    }

    public Optional<String> releaseCall() {
        return Optional.empty();

    }

    public Optional<String> setAgentNotReady(UserSession userSession) {
        try {
            RequestDto requestDto = new RequestDto();

            Map<String, String> header = new HashMap<>();
            header.put("Content-Type", "application/json");
            header.put("Guid", userSession.getGuid());

            String url = URL + AGENT_ID + "/saybusy";
            Optional<ResponseDto> responseDto = makeHttpRequest(url, "POST", requestDto, header);
            return responseDto.map(ResponseDto::getBody);
        } catch (Exception e) {
            return Optional.of(Arrays.toString(e.getStackTrace()));
        }
    }

    public Optional<String> setAgentReady(UserSession userSession) {
        try {
            RequestDto requestDto = new RequestDto();

            Map<String, String> header = new HashMap<>();
            header.put("Content-Type", "application/json");
            header.put("Guid", userSession.getGuid());

            String url = URL + AGENT_ID + "/sayfree";
            Optional<ResponseDto> responseDto = makeHttpRequest(url, "POST", requestDto, header);
            return responseDto.map(ResponseDto::getBody);
        } catch (Exception e) {
            return Optional.of(Arrays.toString(e.getStackTrace()));
        }
    }

    public Optional<String> maintainHeartBeat() {
        return Optional.empty();
    }

    public Optional<String> login(UserSession userSession) {
        try {
            RequestDto requestDto = new RequestDto();
            requestDto.setPassword(PASSWORD);
            requestDto.setPhonenum(PHONE_NO);
            requestDto.setStatus("4");
            requestDto.setCallBackUri(CALL_BACK_URI + AGENT_ID);
            requestDto.setAutoanswer(false);
            requestDto.setServiceToken(userSession.getSessionId());
            Map<String, String> header = new HashMap<>();
            header.put("Content-Type", "application/json");

            String url = URL + AGENT_ID;
            Optional<ResponseDto> responseDto = makeHttpRequest(url, "PUT", requestDto, header);
            if (responseDto.isEmpty()) {
                return Optional.empty();
            }
            if (responseDto.get().getHeader().get("Guid") != null) {
                userSession.setGuid(responseDto.get().getHeader().get("Guid").get(0));
            }
            return Optional.of(responseDto.get().getBody());
        } catch (Exception e) {
            return Optional.of(Arrays.toString(e.getStackTrace()));
        }
    }

    private Optional<ResponseDto> makeHttpRequest(String url, String method, RequestDto requestDto, Map<String, String> header) throws IOException, URISyntaxException {

        try {
            SSLContext sslContext = SSLContext.getInstance("SSL"); // OR TLS
            sslContext.init(null, new TrustManager[]{MOCK_TRUST_MANAGER}, new SecureRandom());

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .method(method, HttpRequest.BodyPublishers.ofString(requestDto.toJson()));

            header.forEach(requestBuilder::header);


            HttpClient client = HttpClient.newBuilder().sslContext(sslContext).build();
            HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());

            System.out.println("OUTGOING: " + requestDto.toJson());

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

    public Optional<String> logOut(UserSession userSession) {
        try {
            RequestDto requestDto = new RequestDto();

            Map<String, String> header = new HashMap<>();
            header.put("Content-Type", "application/json");
            header.put("Guid", userSession.getGuid());

            String url = URL + AGENT_ID + "/logout";
            Optional<ResponseDto> responseDto = makeHttpRequest(url, "DELETE", requestDto, header);
            return responseDto.map(ResponseDto::getBody);
        } catch (Exception e) {
            return Optional.of(Arrays.toString(e.getStackTrace()));
        }
    }

    public Optional<String> fetchMetadata(UserSession userSession) {
        try {
            RequestDto requestDto = new RequestDto();

            Map<String, String> header = new HashMap<>();
            header.put("Content-Type", "application/json");
            header.put("Guid", userSession.getGuid());

            String url = URL + AGENT_ID + "/appdata/?callId={" + userSession.getCallInfo().getCallid() + "}";
            Optional<ResponseDto> responseDto = makeHttpRequest(url, "GET", requestDto, header);
            return responseDto.map(ResponseDto::getBody);
        } catch (Exception e) {
            return Optional.of(Arrays.toString(e.getStackTrace()));
        }
    }
}
