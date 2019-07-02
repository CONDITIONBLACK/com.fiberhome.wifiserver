package com.fenghuo.http.response;

import org.json.JSONObject;

public class ModelResponse extends RspEvent {
    public String responseStatusCode = "";

    public ModelResponse() {
        super(2);
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
