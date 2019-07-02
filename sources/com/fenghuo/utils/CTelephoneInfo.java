package com.fenghuo.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

public class CTelephoneInfo {
    private static CTelephoneInfo CTelephoneInfo;
    private static Context mContext;
    private String imeiSIM1 = "";
    private String imeiSIM2 = "";
    private String imsiSIM1 = "";
    private String imsiSIM2 = "";

    private static class GeminiMethodNotFoundException extends Exception {
        private static final long serialVersionUID = -3241033488141442594L;

        public GeminiMethodNotFoundException(String str) {
            super(str);
        }
    }

    private CTelephoneInfo() {
    }

    public static synchronized CTelephoneInfo getInstance(Context context) {
        CTelephoneInfo cTelephoneInfo;
        synchronized (CTelephoneInfo.class) {
            if (CTelephoneInfo == null) {
                CTelephoneInfo = new CTelephoneInfo();
            }
            mContext = context;
            cTelephoneInfo = CTelephoneInfo;
        }
        return cTelephoneInfo;
    }

    public String getImeiSIM1() {
        return this.imeiSIM1;
    }

    public String getImeiSIM2() {
        return this.imeiSIM2;
    }

    public String getImsiSIM1() {
        return this.imsiSIM1;
    }

    public String getImsiSIM2() {
        return this.imsiSIM2;
    }

    public void setCTelephoneInfo() {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService("phone");
        CTelephoneInfo.imeiSIM1 = telephonyManager.getDeviceId();
        CTelephoneInfo.imeiSIM2 = "";
        CTelephoneInfo.imsiSIM1 = telephonyManager.getSubscriberId();
        CTelephoneInfo.imsiSIM2 = "";
        try {
            CTelephoneInfo.imeiSIM1 = getOperatorBySlot(mContext, "getDeviceIdGemini", 0);
            CTelephoneInfo.imeiSIM2 = getOperatorBySlot(mContext, "getDeviceIdGemini", 1);
            CTelephoneInfo.imsiSIM1 = getOperatorBySlot(mContext, "getSubscriberIdGemini", 0);
            CTelephoneInfo.imsiSIM2 = getOperatorBySlot(mContext, "getSubscriberIdGemini", 1);
        } catch (GeminiMethodNotFoundException e) {
            try {
                CTelephoneInfo.imeiSIM1 = getOperatorBySlot(mContext, "getDeviceId", 0);
                CTelephoneInfo.imeiSIM2 = getOperatorBySlot(mContext, "getDeviceId", 1);
                CTelephoneInfo.imsiSIM1 = getOperatorBySlot(mContext, "getSubscriberId", 0);
                CTelephoneInfo.imsiSIM2 = getOperatorBySlot(mContext, "getSubscriberId", 1);
            } catch (GeminiMethodNotFoundException e2) {
            }
        }
    }

    private static String getOperatorBySlot(Context context, String str, int i) throws GeminiMethodNotFoundException {
        String str2 = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        try {
            Object invoke = Class.forName(telephonyManager.getClass().getName()).getMethod(str, new Class[]{Integer.TYPE}).invoke(telephonyManager, new Object[]{Integer.valueOf(i)});
            if (invoke != null) {
                return invoke.toString();
            }
            return str2;
        } catch (Exception e) {
            throw new GeminiMethodNotFoundException(str);
        }
    }
}
