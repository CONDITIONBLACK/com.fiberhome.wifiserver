package com.fenghuo.http.response;

import org.json.JSONObject;

public class RspZIPEvent extends RspEvent {
    public String responseStatusCode = "";

    public RspZIPEvent() {
        super(101);
    }

    public boolean parserResponse(String str) {
        try {
            this.responseStatusCode = new JSONObject(str).getString("status");
            this.isValid = true;
        } catch (Exception e) {
            this.isValid = false;
        }
        return true;
    }
}
