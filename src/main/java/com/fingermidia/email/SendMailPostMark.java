package com.fingermidia.email;

import com.fingermidia.integration.Generic;
import com.fingermidia.integration.Request;
import com.fingermidia.integration.Response;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendMailPostMark extends Thread {
    private static final String URL = "https://api.postmarkapp.com/email";
    protected String subject;
    protected String message;
    protected String sendTo;
    protected String apiToken;
    protected String contact;
    public STREAM stream = STREAM.OUTBOUND;

    public enum STREAM {
        OUTBOUND,
        BROADCAST
    }

    public SendMailPostMark(String subject, String message, String sendTo, String apiToken, String contact) {
        this.subject = subject;
        this.message = message;
        this.sendTo = sendTo;
        this.apiToken = apiToken;
        this.contact = contact;
    }

    public SendMailPostMark(String subject, String message, String sendTo, STREAM stream, String apiToken, String contact) {
        this.subject = subject;
        this.message = message;
        this.sendTo = sendTo;
        this.stream = stream;
        this.apiToken = apiToken;
        this.contact = contact;
    }

    @Override
    public void run() {
        try {
            send(subject, message, sendTo, stream, apiToken, contact);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static JSONObject send(String subject, String message, String sendTo, STREAM stream, String apiToken, String contact) throws Exception {
        Request req = new Request();
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("X-Postmark-Server-Token", apiToken);

        JSONObject j = new JSONObject();
        j.put("From", contact);
        j.put("To", sendTo);
        j.put("Subject", subject);
        j.put("HtmlBody", message);
        if (stream == STREAM.OUTBOUND) {
            j.put("MessageStream", "outbound");
        } else {
            j.put("MessageStream", "broadcast");
        }

        req.setHeaders(headers);
        req.setUrl(URL);
        req.setBody(j.toString());
        Response res = Generic.requestPost(req);
        return res.getJSON();
    }
}
