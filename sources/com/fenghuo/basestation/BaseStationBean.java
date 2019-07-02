package com.fenghuo.basestation;

import android.text.TextUtils;

public class BaseStationBean {
    private String BID;
    private String CellID;
    private String ECGI;
    private String LAC;
    private String MCC;
    private String MNC;
    private String NID;
    private String SID;

    public String getMCC() {
        return this.MCC;
    }

    public void setMCC(String str) {
        this.MCC = str;
    }

    public String getMNC() {
        return this.MNC;
    }

    public void setMNC(String str) {
        this.MNC = str;
    }

    public String getECGI() {
        return this.ECGI;
    }

    public void setECGI(String str) {
        this.ECGI = str;
    }

    public String getLAC() {
        return this.LAC;
    }

    public void setLAC(String str) {
        this.LAC = str;
    }

    public String getCellID() {
        return this.CellID;
    }

    public void setCellID(String str) {
        this.CellID = str;
    }

    public String getSID() {
        return this.SID;
    }

    public void setSID(String str) {
        this.SID = str;
    }

    public String getNID() {
        return this.NID;
    }

    public void setNID(String str) {
        this.NID = str;
    }

    public String getBID() {
        return this.BID;
    }

    public void setBID(String str) {
        this.BID = str;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (TextUtils.isEmpty(this.MCC)) {
            stringBuffer.append("\t");
        } else {
            stringBuffer.append(parse(this.MCC)).append("\t");
        }
        if (TextUtils.isEmpty(this.MNC)) {
            stringBuffer.append("\t");
        } else {
            stringBuffer.append(parse(this.MNC)).append("\t");
        }
        if (TextUtils.isEmpty(this.ECGI)) {
            stringBuffer.append("\t");
        } else {
            stringBuffer.append(parse(this.ECGI)).append("\t");
        }
        if (TextUtils.isEmpty(this.LAC)) {
            stringBuffer.append("\t");
        } else {
            stringBuffer.append(parse(this.LAC)).append("\t");
        }
        if (TextUtils.isEmpty(this.CellID)) {
            stringBuffer.append("\t");
        } else {
            stringBuffer.append(parse(this.CellID)).append("\t");
        }
        if (TextUtils.isEmpty(this.SID)) {
            stringBuffer.append("\t");
        } else {
            stringBuffer.append(parse(this.SID)).append("\t");
        }
        if (TextUtils.isEmpty(this.NID)) {
            stringBuffer.append("\t");
        } else {
            stringBuffer.append(parse(this.NID)).append("\t");
        }
        if (TextUtils.isEmpty(this.BID)) {
            stringBuffer.append("\t");
        } else {
            stringBuffer.append(parse(this.BID)).append("\t");
        }
        return stringBuffer.toString();
    }

    private String parse(String str) {
        try {
            if (Integer.parseInt(str) <= 0) {
                return "";
            }
            return str;
        } catch (Exception e) {
            return str;
        }
    }
}
