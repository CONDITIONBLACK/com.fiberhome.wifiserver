package com.fenghuo.calllog;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog.Calls;
import com.fenghuo.utils.Global;
import com.fenghuo.utils.Util;
import java.util.ArrayList;

public class CallLogHelper {
    public static final String[] CALL_INFO = new String[]{"_id", "number", "date", "type", "new", "name", "duration"};
    public static final Uri URI = Calls.CONTENT_URI;
    private static CallLogHelper sInstance;
    public ArrayList<CallLogFhItem> callgroup = new ArrayList();
    private int count = 0;
    public Handler loginHandler_ = null;
    private Context mContext;
    private int position = 0;
    private ArrayList<String> tempGroupNumber = new ArrayList();

    private void sendMssage(int i) {
        Message message = new Message();
        message.what = i;
        Bundle bundle = new Bundle();
        bundle.putInt("count", this.count);
        bundle.putInt("poistion", this.position);
        message.setData(bundle);
        this.loginHandler_.sendMessage(message);
    }

    public static synchronized CallLogHelper getInstance(Context context) {
        CallLogHelper callLogHelper;
        synchronized (CallLogHelper.class) {
            if (sInstance == null) {
                sInstance = new CallLogHelper(context);
            }
            callLogHelper = sInstance;
        }
        return callLogHelper;
    }

    public CallLogHelper(Context context) {
        this.mContext = context;
    }

    private void addGroupItem(Context context, CallLogFhItem callLogFhItem) {
        if (!this.tempGroupNumber.contains(callLogFhItem.mNumber)) {
            this.callgroup.add(callLogFhItem);
            this.tempGroupNumber.add(callLogFhItem.mNumber);
        }
    }

    public void queryAllLog(Handler handler) {
        this.loginHandler_ = handler;
        if (this.mContext != null) {
            this.callgroup.clear();
            this.tempGroupNumber.clear();
            Cursor query = this.mContext.getContentResolver().query(URI, CALL_INFO, null, null, "date DESC");
            if (query != null && query.getCount() > 0) {
                this.count = query.getCount();
                query.moveToFirst();
                int columnIndex = query.getColumnIndex("name");
                int columnIndex2 = query.getColumnIndex("type");
                int columnIndex3 = query.getColumnIndex("date");
                int columnIndex4 = query.getColumnIndex("_id");
                int columnIndex5 = query.getColumnIndex("number");
                int columnIndex6 = query.getColumnIndex("duration");
                for (int i = 0; i < this.count; i++) {
                    query.moveToPosition(i);
                    this.position = i;
                    String string = query.getString(columnIndex);
                    String string2 = query.getString(columnIndex5);
                    CallLogFhItem callLogFhItem = new CallLogFhItem();
                    if (string == null) {
                        string = "";
                    }
                    callLogFhItem.mName = string;
                    if (callLogFhItem.mName != null) {
                        callLogFhItem.mName = callLogFhItem.mName.replace("\u0000", "");
                        callLogFhItem.mName = callLogFhItem.mName.replace(Character.toString('￿'), "");
                        callLogFhItem.mName = callLogFhItem.mName.replace(Character.toString('\u0000'), "");
                        callLogFhItem.mName = callLogFhItem.mName.replace(Character.toString('＀'), "");
                        callLogFhItem.mName = callLogFhItem.mName.replace(Character.toString('쳌'), "");
                        callLogFhItem.mName = callLogFhItem.mName.replace(Character.toString('\u000e'), "");
                        callLogFhItem.mName = callLogFhItem.mName.replace(Character.toString('\u0014'), "");
                        callLogFhItem.mName = callLogFhItem.mName.replace(Character.toString('਑'), "");
                        callLogFhItem.mName = callLogFhItem.mName.replace(Character.toString('\u0003'), "");
                        callLogFhItem.mName = callLogFhItem.mName.replace(Character.toString('\u001b'), "");
                    }
                    callLogFhItem.mNumber = string2;
                    callLogFhItem.lastTime = query.getLong(columnIndex3);
                    callLogFhItem.type = query.getInt(columnIndex2);
                    callLogFhItem.id = query.getString(columnIndex4);
                    callLogFhItem.mDuration = query.getLong(columnIndex6);
                    sendMssage(104);
                    this.callgroup.add(callLogFhItem);
                    if (!(string2 == null || string2.equals(""))) {
                        Util.appendAppListFile(Global.esnPath_ + "country_code", string2 + "\n");
                    }
                }
                if (query != null) {
                    query.close();
                }
            }
        }
    }

    public String getHelperData() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<Dialings>");
        stringBuffer.append("<Count>").append(this.callgroup.size()).append("</Count>");
        int i = 0;
        int i2 = 0;
        while (i < this.callgroup.size()) {
            int i3 = i2 + 1;
            CallLogFhItem callLogFhItem = (CallLogFhItem) this.callgroup.get(i);
            stringBuffer.append("<Dialing>");
            stringBuffer.append("<ID>").append(i3).append("</ID>");
            stringBuffer.append("<Type>").append(Util.formatCallLogType(callLogFhItem.type)).append("</Type>");
            stringBuffer.append("<Name>").append(Util.escapexml(callLogFhItem.mName)).append("</Name>");
            stringBuffer.append("<Number>").append(callLogFhItem.mNumber).append("</Number>");
            stringBuffer.append("<StartTime>").append(Util.formatTimeStampString(callLogFhItem.lastTime)).append("</StartTime>");
            stringBuffer.append("<Duration>").append(callLogFhItem.mDuration).append("</Duration>");
            stringBuffer.append("<INTERCEPT_STATE>").append(0).append("</INTERCEPT_STATE>");
            stringBuffer.append("</Dialing>");
            ArrayList arrayList = callLogFhItem.mChildItems;
            int i4 = 0;
            while (i4 < arrayList.size()) {
                int i5 = i3 + 1;
                CallLogItem callLogItem = (CallLogItem) arrayList.get(i4);
                stringBuffer.append("<Dialing>");
                stringBuffer.append("<ID>").append(i5).append("</ID>");
                stringBuffer.append("<Type>").append(Util.formatCallLogType(callLogItem.type)).append("</Type>");
                stringBuffer.append("<Name>").append(Util.escapexml(callLogFhItem.mName)).append("</Name>");
                stringBuffer.append("<Number>").append(callLogFhItem.mNumber).append("</Number>");
                stringBuffer.append("<StartTime>").append(Util.formatTimeStampString(callLogItem.mDate.longValue())).append("</StartTime>");
                stringBuffer.append("<Duration>").append(callLogItem.mDuration).append("</Duration>");
                stringBuffer.append("<INTERCEPT_STATE>").append(0).append("</INTERCEPT_STATE>");
                stringBuffer.append("</Dialing>");
                i4++;
                i3 = i5;
            }
            i++;
            i2 = i3;
        }
        stringBuffer.append("</Dialings>");
        return stringBuffer.toString();
    }

    public String getCallHtml() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<h2 id=\"Dialing\"><a href=\"#PhoneInfo\" style=\"text-decoration:none;\">Dialing</a></h2>");
        stringBuffer.append("<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:11pt;border-collapse:collapse;\ttext-align:left;padding-left:5px;line-height:18pt;\" bordercolor=\"#111111\" width=\"100%\">");
        stringBuffer.append("<TR bgcolor=\"#C0C0C0\">");
        stringBuffer.append("<TD>ID</TD><TD>CallType</TD><TD>Name</TD><TD>Number</TD><TD>StartTime</TD><TD>Duration</TD></TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TD></TD></TR>");
        int i = 0;
        int i2 = 0;
        while (i < this.callgroup.size()) {
            int i3 = i2 + 1;
            CallLogFhItem callLogFhItem = (CallLogFhItem) this.callgroup.get(i);
            stringBuffer.append("<TR>");
            stringBuffer.append("<TD>").append(i3).append("</TD>");
            stringBuffer.append("<TD>").append(Util.formatCallLogType(callLogFhItem.type)).append("</TD>");
            stringBuffer.append("<TD>").append(Util.escapexml(callLogFhItem.mName)).append("</TD>");
            stringBuffer.append("<TD>").append(callLogFhItem.mNumber).append("</TD>");
            stringBuffer.append("<TD>").append(Util.formatTimeStampString(callLogFhItem.lastTime)).append("</TD>");
            stringBuffer.append("<TD>").append(callLogFhItem.mDuration).append("</TD>");
            stringBuffer.append("</TR>");
            ArrayList arrayList = callLogFhItem.mChildItems;
            int i4 = 0;
            while (i4 < arrayList.size()) {
                int i5 = i3 + 1;
                CallLogItem callLogItem = (CallLogItem) arrayList.get(i4);
                stringBuffer.append("<TR>");
                stringBuffer.append("<TD>").append(i5).append("</TD>");
                stringBuffer.append("<TD>").append(Util.formatCallLogType(callLogItem.type)).append("</TD>");
                stringBuffer.append("<TD>").append(Util.escapexml(callLogFhItem.mName)).append("</TD>");
                stringBuffer.append("<TD>").append(callLogFhItem.mNumber).append("</TD>");
                stringBuffer.append("<TD>").append(Util.formatTimeStampString(callLogItem.mDate.longValue())).append("</TD>");
                stringBuffer.append("<TD>").append(callLogItem.mDuration).append("</TD>");
                stringBuffer.append("</TR>");
                i4++;
                i3 = i5;
            }
            i++;
            i2 = i3;
        }
        stringBuffer.append("</table>");
        return stringBuffer.toString();
    }
}
