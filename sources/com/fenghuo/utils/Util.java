package com.fenghuo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.storage.StorageManager;
import android.support.v4.view.MotionEventCompat;
import android.text.format.DateFormat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EncodingUtils;

public class Util {
    public static final String tag = "Util";

    public static char Hex2Chr(byte b) {
        return new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'}[b & 15];
    }

    public static String escapexml_fan(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            switch (charAt) {
                case MotionEventCompat.AXIS_GENERIC_3 /*34*/:
                    stringBuffer.append("&quot;");
                    break;
                case MotionEventCompat.AXIS_GENERIC_7 /*38*/:
                    stringBuffer.append("&amp;");
                    break;
                case MotionEventCompat.AXIS_GENERIC_8 /*39*/:
                    stringBuffer.append("&apos;");
                    break;
                case '<':
                    stringBuffer.append("&lt;");
                    break;
                case '>':
                    stringBuffer.append("&gt;");
                    break;
                default:
                    stringBuffer.append(charAt);
                    break;
            }
        }
        return stringBuffer.toString();
    }

    public static final Long parseToLong(String str, long j) {
        Long valueOf = Long.valueOf(j);
        try {
            valueOf = Long.valueOf(Long.parseLong(str.trim()));
        } catch (Exception e) {
            Logger.debugMessage("Util.parseToLong(): " + e.getMessage());
        }
        return valueOf;
    }

    public static final int parseToInt(String str, int i) {
        try {
            i = Integer.parseInt(str.trim());
        } catch (Exception e) {
            Logger.debugMessage("Util.parseToInt(): " + e.getMessage());
        }
        return i;
    }

    public static String getCookie(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        String str2 = "";
        String[] split = str.split(";");
        if (split != null) {
            for (int i = 0; i < split.length; i++) {
                if (split[i].contains("JSESSIONID")) {
                    return split[i];
                }
            }
        }
        return str2;
    }

    public static String getCurrentDateAndTime() {
        String str = "";
        try {
            long currentTimeMillis = System.currentTimeMillis();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(currentTimeMillis);
            str = simpleDateFormat.format(instance.getTime());
        } catch (Exception e) {
        }
        return str;
    }

    public static String getCurrentDateAndTime2() {
        String str = "";
        try {
            long currentTimeMillis = System.currentTimeMillis();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(currentTimeMillis);
            str = simpleDateFormat.format(instance.getTime());
        } catch (Exception e) {
        }
        return str;
    }

    public static String formatTimeStampString(long j) {
        String str = "";
        try {
            str = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(j));
        } catch (Exception e) {
        }
        return str;
    }

    public static String formatDateStampString(long j) {
        String str = "";
        try {
            str = new SimpleDateFormat("yyyy/MM/dd").format(new Date(j));
        } catch (Exception e) {
        }
        return str;
    }

    public static String formatDateMonthStampString(long j) {
        String str = "";
        try {
            str = new SimpleDateFormat("yyyyMM").format(new Date(j));
        } catch (Exception e) {
        }
        return str;
    }

    public static String formatCallLogType(int i) {
        String str = "";
        switch (i) {
            case 1:
                return "InComing";
            case 2:
                return "OutGoing";
            case 3:
                return "Missed";
            default:
                return "InComing";
        }
    }

    public static File createFile(String str) {
        File file = new File(str);
        if (file != null && file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            return file;
        } catch (Exception e) {
            try {
                File file2 = new File(file.getParent());
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                file.createNewFile();
                return file;
            } catch (Exception e2) {
                return null;
            }
        }
    }

    public static String getStoragePath(Context context, boolean z) {
        StorageManager storageManager = (StorageManager) context.getSystemService("storage");
        try {
            Class cls = Class.forName("android.os.storage.StorageVolume");
            Method method = storageManager.getClass().getMethod("getVolumeList", new Class[0]);
            Method method2 = cls.getMethod("getPath", new Class[0]);
            Method method3 = cls.getMethod("isRemovable", new Class[0]);
            Object invoke = method.invoke(storageManager, new Object[0]);
            int length = Array.getLength(invoke);
            for (int i = 0; i < length; i++) {
                Object obj = Array.get(invoke, i);
                String str = (String) method2.invoke(obj, new Object[0]);
                if (z == ((Boolean) method3.invoke(obj, new Object[0])).booleanValue()) {
                    return str;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
        }
        return "";
    }

    public static boolean writeFile(String str, String str2) {
        return writeFile(str, str2.getBytes());
    }

    public static boolean writeFile(String str, byte[] bArr) {
        if (str == null || str.length() <= 0) {
            return false;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(createFile(str));
            fileOutputStream.write(bArr);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean appendFile(String str, String str2) {
        return appendFile(str, str2.getBytes());
    }

    public static boolean appendFile(String str, byte[] bArr) {
        if (str == null || str.length() <= 0) {
            return false;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(createFile(str), true);
            fileOutputStream.write(bArr);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean appendAppListFile(String str, String str2) {
        if (str == null || str.length() <= 0) {
            return false;
        }
        try {
            File file = new File(str);
            if (!file.exists()) {
                file = createFile(str);
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write(str2.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean DeleteFile(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        File file = new File(str);
        if (!file.exists()) {
            return false;
        }
        if (file.exists()) {
            if (file.isDirectory()) {
                boolean z;
                File[] listFiles = file.listFiles();
                if (listFiles != null && listFiles.length > 0) {
                    z = true;
                    for (File path : listFiles) {
                        if (!DeleteFile(path.getPath())) {
                            z = false;
                        }
                    }
                } else if (file.delete()) {
                    z = true;
                } else {
                    z = false;
                }
                return z;
            } else if (!file.delete()) {
                return false;
            }
        }
        return true;
    }

    public static String escapexml(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            switch (charAt) {
                case MotionEventCompat.AXIS_GENERIC_3 /*34*/:
                    stringBuffer.append("&quot;");
                    break;
                case MotionEventCompat.AXIS_GENERIC_7 /*38*/:
                    stringBuffer.append("&amp;");
                    break;
                case MotionEventCompat.AXIS_GENERIC_8 /*39*/:
                    stringBuffer.append("&apos;");
                    break;
                case '<':
                    stringBuffer.append("&lt;");
                    break;
                case '>':
                    stringBuffer.append("&gt;");
                    break;
                default:
                    stringBuffer.append(charAt);
                    break;
            }
        }
        return stringBuffer.toString();
    }

    public static String getLocalIpAddress() {
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration inetAddresses = ((NetworkInterface) networkInterfaces.nextElement()).getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String str = inetAddress.getHostAddress().toString();
                        if (isValidIp(str) && str.startsWith("192")) {
                            return str;
                        }
                    }
                }
            }
        } catch (SocketException e) {
        }
        return "192.168.43.*";
    }

    public static boolean isValidIp(String str) {
        if (str == null || str.length() <= 7) {
            return false;
        }
        String str2 = "";
        if (str.charAt(0) == '.' || str.charAt(str.length() - 1) == '.') {
            return false;
        }
        int i = 0;
        int i2 = 0;
        while (i < str.length()) {
            if (str.charAt(i) == '.') {
                i2++;
                if (Integer.parseInt(str2) > 255) {
                    return false;
                }
                str2 = "";
            } else if (str.charAt(i) < '0' || str.charAt(i) > '9') {
                return false;
            } else {
                str2 = str2 + String.valueOf(str.charAt(i));
            }
            i++;
        }
        if (i2 == 3) {
            return true;
        }
        return false;
    }

    public static HashMap<String, String> readFileByLinesScandirApps(File file) {
        BufferedReader bufferedReader;
        Throwable th;
        BufferedReader bufferedReader2 = null;
        HashMap<String, String> hashMap = new HashMap();
        if (file == null || !file.exists() || 0 == file.length() || file.isDirectory()) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer("");
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            try {
                String str = "";
                while (true) {
                    str = bufferedReader.readLine();
                    if (str == null) {
                        break;
                    } else if (str != null && str.contains(",")) {
                        String[] split = str.split(",");
                        if (!(split.length < 2 || split[1] == null || split[1].equals(""))) {
                            hashMap.put(split[1], split[0]);
                        }
                    }
                }
                if (bufferedReader == null) {
                    return hashMap;
                }
                try {
                    bufferedReader.close();
                    return hashMap;
                } catch (Exception e) {
                    return hashMap;
                }
            } catch (Exception e2) {
                bufferedReader2 = bufferedReader;
                if (bufferedReader2 != null) {
                    return hashMap;
                }
                try {
                    bufferedReader2.close();
                    return hashMap;
                } catch (Exception e3) {
                    return hashMap;
                }
            } catch (Throwable th2) {
                th = th2;
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (Exception e4) {
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
            if (bufferedReader2 != null) {
                return hashMap;
            }
            bufferedReader2.close();
            return hashMap;
        } catch (Throwable th3) {
            th = th3;
            bufferedReader = null;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            throw th;
        }
    }

    public static String readFileByLines(File file) {
        BufferedReader bufferedReader;
        String str;
        BufferedReader bufferedReader2;
        Throwable th;
        if (file == null || !file.exists() || 0 == file.length() || file.isDirectory()) {
            return "";
        }
        String str2 = "";
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            try {
                str = "";
                while (true) {
                    str = bufferedReader.readLine();
                    if (str == null) {
                        break;
                    } else if (!(str == null || str.equals("") || str.equals("\n"))) {
                        String[] split = str.split("\t");
                        if (split == null || split.length < 2 || !isNumeric(split[0])) {
                            str = str2 + str;
                        }
                        str2 = str;
                    }
                }
                if (bufferedReader == null) {
                    return str2;
                }
                try {
                    bufferedReader.close();
                    return str2;
                } catch (Exception e) {
                    return str2;
                }
            } catch (Exception e2) {
                str = str2;
                bufferedReader2 = bufferedReader;
                if (bufferedReader2 != null) {
                    return str;
                }
                try {
                    bufferedReader2.close();
                    return str;
                } catch (Exception e3) {
                    return str;
                }
            } catch (Throwable th2) {
                th = th2;
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (Exception e4) {
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
            str = str2;
            bufferedReader2 = null;
            if (bufferedReader2 != null) {
                return str;
            }
            bufferedReader2.close();
            return str;
        } catch (Throwable th3) {
            bufferedReader = null;
            th = th3;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            throw th;
        }
    }

    public static String readFile(String str) {
        String str2 = "";
        if (!new File(str).exists()) {
            return "";
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            byte[] bArr = new byte[fileInputStream.available()];
            fileInputStream.read(bArr);
            str2 = EncodingUtils.getString(bArr, HTTP.UTF_8);
            fileInputStream.close();
            return str2;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("e=" + e.toString());
            return "";
        } catch (IOException e2) {
            e2.printStackTrace();
            System.out.println("e=" + e2.toString());
            return "";
        }
    }

    public static boolean isNumeric(String str) {
        if (Pattern.compile("[0-9]*").matcher(str).matches()) {
            return true;
        }
        return false;
    }

    public static int readFileCountByLines(File file) {
        Throwable th;
        int i = 0;
        if (!(file == null || !file.exists() || 0 == file.length() || file.isDirectory())) {
            String str = "";
            BufferedReader bufferedReader = null;
            BufferedReader bufferedReader2;
            try {
                bufferedReader2 = new BufferedReader(new FileReader(file));
                try {
                    String str2 = "";
                    while (bufferedReader2.readLine() != null) {
                        i++;
                    }
                    if (bufferedReader2 != null) {
                        try {
                            bufferedReader2.close();
                        } catch (Exception e) {
                        }
                    }
                } catch (Exception e2) {
                    if (bufferedReader2 != null) {
                        try {
                            bufferedReader2.close();
                        } catch (Exception e3) {
                        }
                    }
                    return i;
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader = bufferedReader2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (Exception e4) {
                        }
                    }
                    throw th;
                }
            } catch (Exception e5) {
                bufferedReader2 = null;
                if (bufferedReader2 != null) {
                    bufferedReader2.close();
                }
                return i;
            } catch (Throwable th3) {
                th = th3;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
        }
        return i;
    }

    public static String getContentTypeByFilepath(String str) {
        return MimeTypes.getContentTypeByFilepath(str);
    }

    public static void copyBitmapFile(String str) {
        InputStream fileInputStream;
        IOException e;
        FileNotFoundException e2;
        InputStream inputStream;
        Throwable th;
        OutputStream outputStream = null;
        String str2 = Global.esnPath_ + "CaptureImages/";
        File file = new File(str2);
        if (!file.exists()) {
            file.mkdir();
        }
        StringBuilder stringBuilder = new StringBuilder();
        DateFormat dateFormat = new DateFormat();
        str2 = str2 + "Snapshots_" + stringBuilder.append(DateFormat.format("yyyyMMddhhmmss", Calendar.getInstance(Locale.CHINA))).append(".jpg").toString();
        if (new File(str).exists()) {
            OutputStream fileOutputStream;
            try {
                fileInputStream = new FileInputStream(str);
                try {
                    fileOutputStream = new FileOutputStream(str2);
                    try {
                        byte[] bArr = new byte[1024];
                        while (true) {
                            int read = fileInputStream.read(bArr);
                            if (read <= 0) {
                                break;
                            }
                            fileOutputStream.write(bArr, 0, read);
                        }
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e32) {
                                e32.printStackTrace();
                            }
                        }
                    } catch (FileNotFoundException e4) {
                        e2 = e4;
                        inputStream = fileInputStream;
                        try {
                            e2.printStackTrace();
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException e322) {
                                    e322.printStackTrace();
                                }
                            }
                            if (fileOutputStream == null) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException e3222) {
                                    e3222.printStackTrace();
                                }
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            fileInputStream = inputStream;
                            outputStream = fileOutputStream;
                            if (fileInputStream != null) {
                                try {
                                    fileInputStream.close();
                                } catch (IOException e5) {
                                    e5.printStackTrace();
                                }
                            }
                            if (outputStream != null) {
                                try {
                                    outputStream.close();
                                } catch (IOException e52) {
                                    e52.printStackTrace();
                                }
                            }
                            throw th;
                        }
                    } catch (IOException e6) {
                        e3222 = e6;
                        outputStream = fileOutputStream;
                        try {
                            e3222.printStackTrace();
                            if (fileInputStream != null) {
                                try {
                                    fileInputStream.close();
                                } catch (IOException e32222) {
                                    e32222.printStackTrace();
                                }
                            }
                            if (outputStream == null) {
                                try {
                                    outputStream.close();
                                } catch (IOException e322222) {
                                    e322222.printStackTrace();
                                }
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            if (fileInputStream != null) {
                                fileInputStream.close();
                            }
                            if (outputStream != null) {
                                outputStream.close();
                            }
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        outputStream = fileOutputStream;
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        throw th;
                    }
                } catch (FileNotFoundException e7) {
                    e2 = e7;
                    fileOutputStream = null;
                    inputStream = fileInputStream;
                    e2.printStackTrace();
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (fileOutputStream == null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e8) {
                    e322222 = e8;
                    e322222.printStackTrace();
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    if (outputStream == null) {
                        outputStream.close();
                    }
                }
            } catch (FileNotFoundException e9) {
                e2 = e9;
                fileOutputStream = null;
                e2.printStackTrace();
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileOutputStream == null) {
                    fileOutputStream.close();
                }
            } catch (IOException e10) {
                e322222 = e10;
                fileInputStream = null;
                e322222.printStackTrace();
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (outputStream == null) {
                    outputStream.close();
                }
            } catch (Throwable th5) {
                th = th5;
                fileInputStream = null;
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                throw th;
            }
        }
    }

    public static final String isContainsInvalidCharacter(String str) {
        String str2 = "[`~!#$%^&*()+=|{}':;',\\[\\]<>?~！#￥%……&*（）——+|{}【】［］＊%@（）‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(str2).matcher(str);
        while (matcher.find()) {
            str = str.replaceAll(str2, "");
        }
        Object replace = str.replace(" ", "_");
        if (containEmoji(replace)) {
            replace = newStringModel(replace);
        }
        return Pattern.matches(new String("^[一-龥A-Za-z0-9_-]+"), replace) ? replace : "Android";
    }

    private static String newStringModel(String str) {
        int length = str.length();
        String str2 = "";
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        while (i < length) {
            char charAt = str.charAt(i);
            String str3 = "";
            if (isEmojiCharacter(charAt)) {
                i++;
            } else {
                str3 = String.valueOf(charAt);
            }
            stringBuilder.append(str3);
            i++;
        }
        return stringBuilder.toString();
    }

    private static boolean containEmoji(String str) {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (isEmojiCharacter(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isEmojiCharacter(char c) {
        return (c == '\u0000' || c == '\t' || c == '\n' || c == '\r' || ((c >= ' ' && c <= '퟿') || ((c >= '' && c <= '�') || (c >= '\u0000' && c <= '￿')))) ? false : true;
    }

    public static boolean IDCardValidate(String str) {
        String[] strArr = new String[]{"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] strArr2 = new String[]{"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        String str2 = "";
        if (str.length() != 18) {
            return false;
        }
        if (str.length() == 18) {
            str2 = str.substring(0, 17);
        }
        if (!isNumeric(str2)) {
            return false;
        }
        String substring = str2.substring(6, 10);
        String substring2 = str2.substring(10, 12);
        String substring3 = str2.substring(12, 14);
        if (!isDate(substring + "-" + substring2 + "-" + substring3)) {
            return false;
        }
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (gregorianCalendar.get(1) - Integer.parseInt(substring) > 150 || gregorianCalendar.getTime().getTime() - simpleDateFormat.parse(substring + "-" + substring2 + "-" + substring3).getTime() < 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (ParseException e2) {
            e2.printStackTrace();
        }
        if (Integer.parseInt(substring2) > 12 || Integer.parseInt(substring2) == 0) {
            return false;
        }
        if (Integer.parseInt(substring3) > 31 || Integer.parseInt(substring3) == 0) {
            return false;
        }
        int i = 0;
        for (int i2 = 0; i2 < 17; i2++) {
            i += Integer.parseInt(String.valueOf(str2.charAt(i2))) * Integer.parseInt(strArr2[i2]);
        }
        str2 = str2 + strArr[i % 11];
        if (str.length() != 18) {
            return true;
        }
        if (str2.equals(str)) {
            return true;
        }
        return false;
    }

    public static boolean isDate(String str) {
        if (Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$").matcher(str).matches()) {
            return true;
        }
        return false;
    }

    public static String getIpAddress(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == 0) {
                try {
                    Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
                    while (networkInterfaces.hasMoreElements()) {
                        Enumeration inetAddresses = ((NetworkInterface) networkInterfaces.nextElement()).getInetAddresses();
                        while (inetAddresses.hasMoreElements()) {
                            InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                            if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            } else if (activeNetworkInfo.getType() == 1) {
                return intIP2StringIP(((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getIpAddress());
            } else {
                if (activeNetworkInfo.getType() == 9) {
                    return getLocalIp();
                }
            }
        }
        return null;
    }

    private static String intIP2StringIP(int i) {
        return (i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
    }

    private static String getLocalIp() {
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration inetAddresses = ((NetworkInterface) networkInterfaces.nextElement()).getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
        }
        return "0.0.0.0";
    }
}
