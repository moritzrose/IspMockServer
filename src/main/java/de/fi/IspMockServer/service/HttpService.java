package de.fi.IspMockServer.service;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
public class HttpService {

    private static HttpService httpService;

    private HttpService() {
    }

    public static HttpService getInstance() {
        if (httpService == null) {
            httpService = new HttpService();
        }
        return httpService;
    }

    public HttpStatusCode initiateCall() {
        return HttpStatusCode.valueOf(200);
    }

    public HttpStatusCode answerCall() {
        return HttpStatusCode.valueOf(200);
    }

    public HttpStatusCode holdCall() {
        return HttpStatusCode.valueOf(200);
    }

    public HttpStatusCode unholdCall() {
        return HttpStatusCode.valueOf(200);
    }

    public HttpStatusCode releaseCall() {
        return HttpStatusCode.valueOf(200);
    }

    public HttpStatusCode setAgentNotReady() {
        return HttpStatusCode.valueOf(200);
    }

    public HttpStatusCode setAgentReady() {
        return HttpStatusCode.valueOf(200);
    }

    public HttpStatusCode errorTester() {
        return HttpStatusCode.valueOf(404);
    }
}
