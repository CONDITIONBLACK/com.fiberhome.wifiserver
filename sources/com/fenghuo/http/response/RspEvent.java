package com.fenghuo.http.response;

public class RspEvent {
    public static final int RSP_CONTACT_EVENT = 102;
    public static final int RSP_ZIP_EVENT = 101;
    protected int cmdType;
    public String detail;
    public long id_;
    protected boolean isValid = true;
    public int result = -1;

    public RspEvent(int i) {
        this.cmdType = i;
    }

    public int getCmdType() {
        return this.cmdType;
    }

    public boolean parserResponse(String str) {
        this.isValid = false;
        return false;
    }

    public void setDetailMsg(String str) {
        if (str != null) {
            this.detail = str;
        }
    }

    public boolean isValidResult() {
        return this.isValid;
    }

    public void setisValid(boolean z) {
        this.isValid = z;
    }
}
