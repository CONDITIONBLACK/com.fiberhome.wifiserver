package com.fenghuo.utils;

import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.http.protocol.HTTP;

public class Logger {
    public static final String LOGFILE = "qzj.log";
    public static final String LOGTAG = "qzj";
    public static boolean printLog = true;

    public static void setPrintLog(boolean z) {
        printLog = z;
    }

    /* renamed from: v */
    public static void m19v(String str) {
        if (printLog) {
            Log.v(LOGTAG, str);
        }
    }

    /* renamed from: e */
    public static void m16e(String str) {
        if (printLog) {
            Log.e(LOGTAG, str);
        }
    }

    /* renamed from: e */
    public static void m17e(String str, Exception exception) {
        if (printLog) {
            Log.e(LOGTAG, str, exception);
        }
    }

    /* renamed from: i */
    public static void m18i(String str) {
        if (printLog) {
            Log.i(LOGTAG, str);
        }
    }

    /* renamed from: w */
    public static void m20w(String str) {
        if (printLog) {
            Log.w(LOGTAG, str);
        }
    }

    /* renamed from: d */
    public static void m15d(String str) {
        if (printLog) {
            Log.d(LOGTAG, str);
        }
    }

    public static synchronized void debugMessage(String str) {
        synchronized (Logger.class) {
            if (printLog) {
                m18i(str);
                String property = System.getProperty("file.separator");
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + property + LOGTAG + property, LOGFILE);
                if (file != null) {
                    try {
                        if (file.length() > 5242880) {
                            file.delete();
                        }
                        if (!file.exists()) {
                            File parentFile = file.getParentFile();
                            if (!parentFile.exists()) {
                                parentFile.mkdirs();
                            }
                            file.createNewFile();
                        }
                        FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append("   " + getCurrentDateAndTime(0));
                        stringBuffer.append("   " + str).append("    ");
                        stringBuffer.append("\r\n");
                        try {
                            fileOutputStream.write(stringBuffer.toString().getBytes(HTTP.UTF_8));
                        } catch (UnsupportedEncodingException e) {
                            fileOutputStream.write(stringBuffer.toString().getBytes());
                        }
                        fileOutputStream.close();
                    } catch (Exception e2) {
                        Log.e("Logger", "debugMessage(): " + e2.getMessage());
                    }
                }
            }
        }
    }

    private static String getCurrentDateAndTime(long j) {
        String str = "";
        if (j == 0) {
            try {
                j = System.currentTimeMillis();
            } catch (Exception e) {
                return "";
            }
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        return simpleDateFormat.format(instance.getTime());
    }
}
