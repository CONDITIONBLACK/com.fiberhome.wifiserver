package com.fenghuo.calendar;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.fenghuo.utils.Util;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CalendarHepler {
    private static CalendarHepler sInstance;
    private ArrayList<Calendar> calendarlist = new ArrayList();
    private int count = 0;
    public Handler loginHandler_ = null;
    private Context mContext;
    private int position = 0;

    public static synchronized CalendarHepler getInstance(Context context) {
        CalendarHepler calendarHepler;
        synchronized (CalendarHepler.class) {
            if (sInstance == null) {
                sInstance = new CalendarHepler(context);
            }
            calendarHepler = sInstance;
        }
        return calendarHepler;
    }

    public CalendarHepler(Context context) {
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

    public void GetCalendar(Handler handler) {
        String str;
        Cursor query;
        Exception e;
        Throwable th;
        this.calendarlist.clear();
        this.loginHandler_ = handler;
        String str2 = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (Integer.parseInt(VERSION.SDK) >= 8) {
            str = "content://com.android.calendar/events";
        } else {
            str = "content://calendar/events";
        }
        try {
            query = this.mContext.getContentResolver().query(Uri.parse(str), null, null, null, null);
            if (query != null) {
                this.count = query.getCount();
                this.position = 0;
                for (boolean moveToFirst = query.moveToFirst(); moveToFirst; moveToFirst = query.moveToNext()) {
                    this.position++;
                    Calendar calendar = new Calendar();
                    calendar.Title = query.getString(query.getColumnIndex("title"));
                    calendar.Event = query.getString(query.getColumnIndex("eventLocation"));
                    if (calendar.Event == null) {
                        calendar.Event = "";
                    }
                    calendar.Description = query.getString(query.getColumnIndex("description"));
                    if (calendar.Description == null) {
                        calendar.Description = "";
                    }
                    String string = query.getString(query.getColumnIndex("dtstart"));
                    if (string == null || string.equals("") || string.equals("null")) {
                        calendar.StartTime = "null";
                    } else {
                        calendar.StartTime = simpleDateFormat.format(new Date(Long.parseLong(string)));
                    }
                    string = query.getString(query.getColumnIndex("dtend"));
                    if (string == null || string.equals("") || string.equals("null")) {
                        try {
                            calendar.EndTime = "null";
                        } catch (Exception e2) {
                            e = e2;
                        }
                    } else {
                        calendar.EndTime = simpleDateFormat.format(new Date(Long.parseLong(string)));
                    }
                    sendMssage(106);
                    this.calendarlist.add(calendar);
                }
            }
            if (query != null) {
                query.close();
            }
        } catch (Exception e3) {
            e = e3;
            query = null;
            try {
                Log.e("calendar", e.getMessage());
                if (query != null) {
                    query.close();
                }
            } catch (Throwable th2) {
                th = th2;
                if (query != null) {
                    query.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
    }

    public String getHelperData() {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<Calendars>");
        stringBuffer.append("<Count>").append(this.calendarlist.size()).append("</Count>");
        int i2 = 0;
        while (i2 < this.calendarlist.size()) {
            int i3 = i + 1;
            Calendar calendar = (Calendar) this.calendarlist.get(i2);
            stringBuffer.append("<Calendar>");
            stringBuffer.append("<ID>").append(i3).append("</ID>");
            stringBuffer.append("<Subject>").append(calendar.Title).append("</Subject>");
            stringBuffer.append("<Location>").append(Util.escapexml(calendar.Event)).append("</Location>");
            stringBuffer.append("<Description>").append(Util.escapexml(calendar.Description)).append("</Description>");
            stringBuffer.append("<BeginTime>").append(calendar.StartTime).append("</BeginTime>");
            stringBuffer.append("<EndTime>").append(calendar.EndTime).append("</EndTime>");
            stringBuffer.append("</Calendar>");
            i2++;
            i = i3;
        }
        stringBuffer.append("</Calendars>");
        return stringBuffer.toString();
    }

    public String getCalendarHtml() {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<h2 id=\"Calendar\"><a href=\"#PhoneInfo\" style=\"text-decoration:none;\">Calendar</a></h2>");
        stringBuffer.append("<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:11pt;border-collapse:collapse;\ttext-align:left;padding-left:5px;line-height:18pt;\" bordercolor=\"#111111\" width=\"100%\">");
        stringBuffer.append("<TR bgcolor=\"#C0C0C0\">");
        stringBuffer.append("<TD>ID</TD><TD>Title</TD><TD>Description</TD><TD>Number</TD><TD>Event</TD><TD>StartTime</TD><TD>EndTime</TD></TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TD></TD></TR>");
        int i2 = 0;
        while (i2 < this.calendarlist.size()) {
            int i3 = i + 1;
            Calendar calendar = (Calendar) this.calendarlist.get(i2);
            stringBuffer.append("<TR>");
            stringBuffer.append("<TD>").append(i3).append("</TD>");
            stringBuffer.append("<TD>").append(Util.escapexml(calendar.Title)).append("</TD>");
            stringBuffer.append("<TD>").append(Util.escapexml(calendar.Description)).append("</TD>");
            stringBuffer.append("<TD>").append(Util.escapexml(calendar.Event)).append("</TD>");
            stringBuffer.append("<TD>").append(calendar.StartTime).append("</TD>");
            stringBuffer.append("<TD>").append(calendar.EndTime).append("</TD>");
            stringBuffer.append("</TR>");
            i2++;
            i = i3;
        }
        stringBuffer.append("</table>");
        return stringBuffer.toString();
    }
}
