package com.fingermidia.email;

import com.fingermidia.integration.Generic;
import com.fingermidia.integration.Request;
import com.fingermidia.integration.Response;
import com.wildbit.java.postmark.Postmark;
import com.wildbit.java.postmark.client.ApiClient;
import com.wildbit.java.postmark.client.data.model.message.Message;
import com.wildbit.java.postmark.client.data.model.message.MessageResponse;
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
    public String pathFile;

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
        this.pathFile = null;
    }

    public SendMailPostMark(String subject, String message, String sendTo, STREAM stream, String apiToken, String contact) {
        this.subject = subject;
        this.message = message;
        this.sendTo = sendTo;
        this.stream = stream;
        this.apiToken = apiToken;
        this.contact = contact;
        this.pathFile = null;
    }

    public SendMailPostMark(String subject, String message, String sendTo, STREAM stream, String apiToken, String contact, String pathFile) {
        this.subject = subject;
        this.message = message;
        this.sendTo = sendTo;
        this.stream = stream;
        this.apiToken = apiToken;
        this.contact = contact;
        this.pathFile = pathFile;
    }

    @Override
    public void run() {
        try {
            if (pathFile == null) {
                send(subject, message, sendTo, stream, apiToken, contact);
            } else {
                sendPostmark(subject, message, sendTo, stream, apiToken, contact, pathFile);
            }
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

    public static MessageResponse sendPostmark(String subject, String body, String sendTo, STREAM stream, String apiToken, String contact, String pathFile) throws Exception {
        ApiClient client = Postmark.getApiClient(apiToken);
        Message message = new Message(contact, sendTo, subject, body);
        if (stream == STREAM.OUTBOUND) {
            message.setMessageStream("outbound");
        } else {
            message.setMessageStream("broadcast");
        }
        message.addAttachment(pathFile);
        return client.deliverMessage(message);
    }
}
