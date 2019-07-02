package com.fenghuo.utils;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Process;
import android.os.StatFs;
import android.os.UserManager;
import android.provider.Settings.Secure;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.os.EnvironmentCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import com.fenghuo.basestation.BaseStation;
import com.fiberhome.wifiserver.C0171R;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.TimeZone;

public class Global {
    public static String ALL_MEM = "";
    private static String IP_DEVICE = "";
    public static int ROOT_FLAG = 0;
    public static int SIMSTATE = 9;
    public static String TIMEZONE = "";
    public static String USER_DIVISION;
    public static String absolutePath_ = "";
    public static String absolutefilesPath_ = "";
    public static String esnPath_ = "";
    private static Global gInstance_;
    public static String mSdCardPath_ = "";
    public static String modelDevice = "";
    public static String uploadPath_ = "";
    public static String zipEsnPath_ = "";
    public static String zipTestPath = "";
    public String SN = "";
    public String SimSN = "";
    private String appIP_ = "";
    private String appPORT_ = "";
    public String imei_ = "";
    public String imsi_ = "";
    private String localIP_ = "";
    public String macAddress = "";
    public String macBlueBooth = "";
    public String manufacturerDevice = "";
    public String modeRealDevice = "";
    public String versionDevice = "";

    private Global() {
        mSdCardPath_ = Environment.getExternalStorageDirectory().toString();
    }

    public static Global getGlobal() {
        if (gInstance_ == null) {
            gInstance_ = new Global();
        }
        return gInstance_;
    }

    public void init(final Context context) {
        if (context != null) {
            String file = context.getFilesDir().toString();
            if (file == null || file.length() <= 5) {
                absolutefilesPath_ = "/data/data/com.fiberhome.wifiserver/files";
                file = "/data/data/com.fiberhome.wifiserver/";
            } else {
                absolutefilesPath_ = file;
                file = file.substring(0, file.length() - 5);
            }
            absolutePath_ = file + "qzj/";
            uploadPath_ = absolutePath_ + "uploadfile";
            File file2 = new File(absolutePath_);
            if (!(file2 == null || file2.exists())) {
                file2.mkdirs();
            }
            this.localIP_ = Util.getLocalIpAddress();
            Logger.m16e("address=" + this.localIP_);
            if (this.localIP_ == null || this.localIP_.length() == 0) {
                this.localIP_ = "192.168.43.*";
            }
            this.appIP_ = convertIP(this.localIP_);
            this.appPORT_ = context.getString(C0171R.string.app_port);
            CTelephoneInfo instance = CTelephoneInfo.getInstance(context);
            instance.setCTelephoneInfo();
            this.imei_ = instance.getImeiSIM1();
            if (this.imei_ == null || this.imei_.equals("")) {
                this.imei_ = instance.getImeiSIM2();
            } else {
                String imeiSIM2 = instance.getImeiSIM2();
                if (!(imeiSIM2 == null || imeiSIM2.equals("") || imeiSIM2.equals(this.imei_))) {
                    this.imei_ += "," + imeiSIM2;
                }
            }
            if (this.imei_ == null || this.imei_.equals("")) {
                loadESN(context);
            }
            this.imsi_ = instance.getImsiSIM1();
            if (this.imsi_ == null || this.imsi_.equals("")) {
                this.imsi_ = instance.getImsiSIM2();
            } else {
                file = instance.getImsiSIM2();
                if (!(file == null || file.equals("") || file.equals(this.imsi_))) {
                    this.imsi_ += "," + file;
                }
            }
            if (this.imsi_ == null || this.imsi_.equals("")) {
                loadIMSI(context);
            }
            loadSN(context);
            getSN();
            getDivisionUser(context);
            new Thread() {
                public void run() {
                    super.run();
                    Global.this.loadWifiMac(context);
                    Global.this.LoadBlueBoothMAC(context);
                }
            }.start();
            file = loadM().replaceAll(" ", "_");
            if (file == null || file.equals("")) {
                file = "Android";
            }
            modelDevice = file;
            file = loadRealModel().replace(" ", "_");
            if (file == null || file.equals("")) {
                file = "Android";
            }
            this.modeRealDevice = file + "_" + modelDevice;
            this.manufacturerDevice = loadManufacturer().replace(" ", "_");
            this.versionDevice = loadVersion().replace(" ", "_");
            if (this.manufacturerDevice == null && this.manufacturerDevice.equals("")) {
                this.manufacturerDevice = "Android_Device";
            }
            if (this.versionDevice == null) {
                this.versionDevice = "";
            }
            file = absolutePath_ + this.imei_;
            esnPath_ = file + "/";
            zipEsnPath_ = file + ".zip";
            zipTestPath = absolutePath_ + "/temp.zip";
            file2 = new File(esnPath_);
            if (!(file2 == null || file2.exists())) {
                file2.mkdirs();
            }
            if (!(this.imei_ == null || this.imei_.equals(""))) {
                if (this.imei_.contains(",")) {
                    String[] split = this.imei_.split(",");
                    for (String str : split) {
                        Util.appendAppListFile(esnPath_ + "hardware", str + "\n");
                    }
                } else {
                    Util.appendAppListFile(esnPath_ + "hardware", this.imei_ + "\n");
                }
            }
            checkRootFlag();
            getTimeZone();
            getMemInfoType(context);
            BaseStation.saveStation(context);
            getSimState(context);
            IP_DEVICE = Util.getIpAddress(context);
            if (IP_DEVICE == null) {
                IP_DEVICE = "";
            }
            Util.writeFile(esnPath_ + "model", modelDevice);
        }
    }

    private static String getLocalMacAddressFromIp() {
        try {
            InetAddress localInetAddress = getLocalInetAddress();
            if (localInetAddress == null) {
                return "";
            }
            byte[] hardwareAddress = NetworkInterface.getByInetAddress(localInetAddress).getHardwareAddress();
            if (hardwareAddress == null) {
                return "";
            }
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < hardwareAddress.length; i++) {
                if (i != 0) {
                    stringBuffer.append(':');
                }
                String toHexString = Integer.toHexString(hardwareAddress[i] & 255);
                if (toHexString.length() == 1) {
                    toHexString = 0 + toHexString;
                }
                stringBuffer.append(toHexString);
            }
            return stringBuffer.toString().toUpperCase();
        } catch (Exception e) {
            return null;
        }
    }

    private static InetAddress getLocalInetAddress() {
        InetAddress inetAddress;
        SocketException e;
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress inetAddress2 = null;
            while (networkInterfaces != null) {
                try {
                    if (!networkInterfaces.hasMoreElements()) {
                        break;
                    }
                    Enumeration inetAddresses = ((NetworkInterface) networkInterfaces.nextElement()).getInetAddresses();
                    while (inetAddresses != null && inetAddresses.hasMoreElements()) {
                        inetAddress = (InetAddress) inetAddresses.nextElement();
                        try {
                            if (!inetAddress.isLoopbackAddress() && inetAddress.getHostAddress().indexOf(":") == -1) {
                                break;
                            }
                            inetAddress2 = null;
                        } catch (SocketException e2) {
                            e = e2;
                        }
                    }
                    inetAddress = inetAddress2;
                    if (inetAddress != null) {
                        return inetAddress;
                    }
                    inetAddress2 = inetAddress;
                } catch (SocketException e3) {
                    SocketException socketException = e3;
                    inetAddress = inetAddress2;
                    e = socketException;
                }
            }
            return inetAddress2;
        } catch (SocketException e32) {
            e = e32;
            inetAddress = null;
            e.printStackTrace();
            return inetAddress;
        }
    }

    private static String getNewMac() {
        try {
            List<NetworkInterface> list = Collections.list(NetworkInterface.getNetworkInterfaces());
            if (list == null) {
                return null;
            }
            for (NetworkInterface networkInterface : list) {
                if (networkInterface.getName().equalsIgnoreCase("wlan0")) {
                    byte[] hardwareAddress = networkInterface.getHardwareAddress();
                    if (hardwareAddress == null) {
                        return null;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    int length = hardwareAddress.length;
                    for (int i = 0; i < length; i++) {
                        stringBuilder.append(String.format("%02X:", new Object[]{Byte.valueOf(hardwareAddress[i])}));
                    }
                    if (stringBuilder.length() > 0) {
                        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    }
                    return stringBuilder.toString();
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getwifimac(Context context) {
        return loadWifiMacAddress(context);
    }

    public String loadWifiMacAddress(Context context) {
        String str = "";
        str = "";
        File file = new File("/sys/class/net/wlan0/address");
        BufferedReader bufferedReader;
        String readLine;
        if (file.exists()) {
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                while (true) {
                    readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    str = readLine;
                }
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            file = new File("/sys/class/net/eth0/address");
            if (file.exists()) {
                try {
                    bufferedReader = new BufferedReader(new FileReader(file));
                    while (true) {
                        readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        }
                        str = readLine;
                    }
                    bufferedReader.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return str;
    }

    public String GetLocalMainMacAddress(Context context) {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!defaultAdapter.isEnabled() && defaultAdapter.enable()) {
            while (true) {
                if (defaultAdapter.getState() != 11 && defaultAdapter.getState() == 12) {
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        String address = defaultAdapter.getAddress();
        if (address == null || address.equals("null") || address.equals("<UNKNOWN>") || address.equals("") || address.equals("02:00:00:00:00:00")) {
            address = Secure.getString(context.getContentResolver(), "bluetooth_address");
        }
        if (address == null) {
            return "";
        }
        return address;
    }

    public void getSN() {
        try {
            Class cls = Class.forName("android.os.SystemProperties");
            this.SN = (String) cls.getMethod("get", new Class[]{String.class, String.class}).invoke(cls, new Object[]{"ril.serialnumber", EnvironmentCompat.MEDIA_UNKNOWN});
            if (this.SN.equals(EnvironmentCompat.MEDIA_UNKNOWN)) {
                cls = Class.forName("android.os.SystemProperties");
                this.SN = (String) cls.getMethod("get", new Class[]{String.class, String.class}).invoke(cls, new Object[]{"ro.serialno", EnvironmentCompat.MEDIA_UNKNOWN});
            }
        } catch (Exception e) {
        }
    }

    public void loadWifiMac(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        WifiInfo connectionInfo = wifiManager == null ? null : wifiManager.getConnectionInfo();
        if (connectionInfo != null) {
            this.macAddress = connectionInfo.getMacAddress();
        }
        if (this.macAddress == null || this.macAddress.equals("null") || this.macAddress.equals("") || this.macAddress.equals("02:00:00:00:00:00")) {
            this.macAddress = getLocalMacAddressFromIp();
        }
        if (this.macAddress == null || this.macAddress.equals("null") || this.macAddress.equals("") || this.macAddress.equals("02:00:00:00:00:00")) {
            this.macAddress = getNewMac();
        }
        if (this.macAddress == null || this.macAddress.equals("null") || this.macAddress.equals("") || this.macAddress.equals("02:00:00:00:00:00")) {
            this.macAddress = getwifimac(context);
        }
        if (this.macAddress != null && !this.macAddress.equals("null") && !this.macAddress.equals("") && !this.macAddress.equals("02:00:00:00:00:00")) {
            Util.appendAppListFile(esnPath_ + "hardware", this.macAddress.toLowerCase() + "\n");
        }
    }

    @SuppressLint({"NewApi"})
    public void LoadBlueBoothMAC(Context context) {
        if (VERSION.SDK_INT >= 23) {
            this.macBlueBooth = Secure.getString(context.getContentResolver(), "bluetooth_address");
        } else {
            try {
                BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
                if (defaultAdapter != null) {
                    this.macBlueBooth = defaultAdapter.getAddress();
                    if (this.macBlueBooth == null || this.macBlueBooth.equals("null") || this.macBlueBooth.equals("<UNKNOWN>") || this.macBlueBooth.equals("") || this.macBlueBooth.equals("02:00:00:00:00:00")) {
                        this.macBlueBooth = GetLocalMainMacAddress(context);
                    }
                } else {
                    this.macBlueBooth = GetLocalMainMacAddress(context);
                }
            } catch (Exception e) {
            }
        }
        if (this.macBlueBooth != null && !this.macBlueBooth.equals("null") && !this.macBlueBooth.equals("<UNKNOWN>") && !this.macBlueBooth.equals("") && !this.macBlueBooth.equals("02:00:00:00:00:00")) {
            Util.appendAppListFile(esnPath_ + "hardware", this.macBlueBooth + "\n");
        }
    }

    public void getDivisionUser(Context context) {
        String str = "";
        if (VERSION.SDK_INT >= 17) {
            str = String.valueOf(((UserManager) context.getSystemService("user")).getSerialNumberForUser(Process.myUserHandle()));
        }
        String str2 = "";
        if (mSdCardPath_.contains("/")) {
            str2 = mSdCardPath_.substring(mSdCardPath_.lastIndexOf(47) + 1);
        }
        if (!str2.equals("")) {
            if (str == null || str.equals("") || !str2.equals(str)) {
                str = str2;
            }
            USER_DIVISION = str + ":;";
        } else if (!TextUtils.isEmpty(str)) {
            USER_DIVISION = str + ":;";
        }
    }

    public void setLocalIP(String str) {
        this.localIP_ = str;
    }

    public String getLocalIP() {
        return this.localIP_;
    }

    public void setAppIP(String str) {
        this.appIP_ = str;
    }

    public String getAppIP() {
        return this.appIP_;
    }

    public void setAppPORT(String str) {
        this.appPORT_ = str;
    }

    public String getAppPORT() {
        return this.appPORT_;
    }

    public String getAppIPPORT() {
        String str = "";
        if (this.appIP_ == null || this.appPORT_ == null) {
            return str;
        }
        return String.format("%s:%s", new Object[]{this.appIP_, this.appPORT_});
    }

    public String loadESN(Context context) {
        this.imei_ = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        if (this.imei_ == null) {
            this.imei_ = "";
        }
        return this.imei_;
    }

    public String loadSN(Context context) {
        this.SimSN = ((TelephonyManager) context.getSystemService("phone")).getSimSerialNumber();
        if (this.SimSN == null) {
            this.SimSN = "";
        }
        return this.SimSN;
    }

    public String loadIMSI(Context context) {
        this.imsi_ = ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
        if (this.imsi_ == null) {
            this.imsi_ = "";
        }
        return this.imsi_;
    }

    public String loadModel() {
        String isContainsInvalidCharacter = Util.isContainsInvalidCharacter(Build.MODEL);
        if (isContainsInvalidCharacter == null || "".equals(isContainsInvalidCharacter)) {
            return isContainsInvalidCharacter;
        }
        return isContainsInvalidCharacter.replaceAll(" ", "_");
    }

    public String loadM() {
        return Build.MODEL;
    }

    public String loadRealModel() {
        return Build.BRAND;
    }

    public String loadManufacturer() {
        return Build.MANUFACTURER;
    }

    public String loadVersion() {
        return VERSION.RELEASE;
    }

    public String getPhoneData(long j, long j2) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<General>");
        stringBuffer.append("<Manufacturer>").append(this.manufacturerDevice).append("</Manufacturer>");
        stringBuffer.append("<Model>").append(modelDevice).append("</Model>");
        stringBuffer.append("<RealModel>").append(this.modeRealDevice).append("</RealModel>");
        stringBuffer.append("<IMEI>").append(this.imei_).append("</IMEI>");
        stringBuffer.append("<IMSI>").append(this.imsi_).append("</IMSI>");
        stringBuffer.append("<ICCID>").append("").append("</ICCID>");
        stringBuffer.append("<PhoneSN>").append(this.SN).append("</PhoneSN>");
        stringBuffer.append("<OSVersion>").append(this.versionDevice).append("</OSVersion>");
        stringBuffer.append("<WifiMac>").append(this.macAddress).append("</WifiMac>");
        stringBuffer.append("<BlueBoothMac>").append(this.macBlueBooth).append("</BlueBoothMac>");
        stringBuffer.append("<TZI>").append("Pacific Standard Time").append("</TZI>");
        stringBuffer.append("<Dump>").append("LogicalDump").append("</Dump>");
        stringBuffer.append("<ConnectType>").append("WIFI").append("</ConnectType>");
        stringBuffer.append("<Cable>").append("").append("</Cable>");
        stringBuffer.append("<Date>").append(Util.formatDateStampString(j)).append("</Date>");
        stringBuffer.append("<StartTime>").append(Util.formatTimeStampString(j)).append("</StartTime>");
        stringBuffer.append("<EndTime>").append(Util.formatTimeStampString(j2)).append("</EndTime>");
        stringBuffer.append("<DeviceName>").append("MobileHunter").append("</DeviceName>");
        stringBuffer.append("<PlatformType>").append("Android").append("</PlatformType>");
        stringBuffer.append("<USER_DIVISION>").append(USER_DIVISION).append("</USER_DIVISION>");
        stringBuffer.append("<TIMEZONE>").append(TIMEZONE).append("</TIMEZONE>");
        stringBuffer.append("<PHONE_STORAGE>").append(ALL_MEM).append("</PHONE_STORAGE>");
        stringBuffer.append("<CRACK_STATE>").append(ROOT_FLAG).append("</CRACK_STATE>");
        stringBuffer.append("<SIM_STATE>").append(SIMSTATE).append("</SIM_STATE>");
        stringBuffer.append("<IP_ADDRESS>").append(IP_DEVICE).append("</IP_ADDRESS>");
        return stringBuffer.toString();
    }

    public static String convertIP(String str) {
        String str2 = "";
        if (str == null || str.length() == 0) {
            return str2;
        }
        int lastIndexOf = str.lastIndexOf(".");
        if (lastIndexOf >= 0 && lastIndexOf < str.length()) {
            str.substring(0, lastIndexOf + 1) + "1";
        }
        return "192.168.43.1";
    }

    public String getHtmlHead() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<HEAD>");
        stringBuffer.append("<TITLE>CellHunter Reporter</TITLE>");
        stringBuffer.append("<META http-equiv=\"content-type\" content=\"text/html\" charset=utf-8>");
        stringBuffer.append("<STYLE TYPE=\"text/css\"></STYLE>");
        stringBuffer.append("</HEAD>");
        return stringBuffer.toString();
    }

    public String getPhoneInfo(long j, long j2) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<h2 id=\"PhoneInfo\">PhoneInfo</h2>");
        stringBuffer.append("<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:11pt;border-collapse:collapse;\ttext-align:left;padding-left:5px;line-height:18pt;\" bordercolor=\"#111111\" width=\"100%\">");
        stringBuffer.append("<tr><td bgcolor=\"#C0C0C0\" height=\"23\" colspan=\"2\"></td></tr><TR>");
        stringBuffer.append("<TD width=\"35%\">Manufacturer</td><td>Android</TD></TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TD width=\"35%\">Model</td><td>").append(Build.MODEL).append("(WIFI)").append("</TD></TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TD width=\"35%\">RealModel</td><td>").append(Build.MODEL).append("(WIFI)").append("</TD></TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TD width=\"35%\">IMEI</td><td>").append(this.imei_).append("</TD></TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TD width=\"35%\">IMSI</td><td>").append(this.imsi_).append("</TD></TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TD width=\"35%\">ConnectType</td><td>WIFI</TD></TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TD width=\"35%\">StartTime</td><td>").append(Util.formatTimeStampString(j)).append("</TD></TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TD width=\"35%\">EndTime</td><td>").append(Util.formatTimeStampString(j2)).append("</TD></TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TD width=\"35%\">Date</td><td>").append(Util.formatDateStampString(j)).append("</TD></TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TD width=\"35%\">TZI</td><td>Pacific Standard Time</TD></TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TD width=\"35%\">Version</td><td>1.0.0</TD></TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TD width=\"35%\">SN</td><td>").append(Util.formatDateMonthStampString(j)).append("</TD></TR>");
        stringBuffer.append("</table>");
        return stringBuffer.toString();
    }

    public String getSelection() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<h2>Selection</h2><table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:11pt;border-collapse:collapse;\ttext-align:left;padding-left:5px;line-height:18pt;\" bordercolor=\"#111111\" width=\"100%\">");
        stringBuffer.append("<tr><td bgcolor=\"#C0C0C0\" height=\"23\" colspan=\"2\"></td></tr><TR>");
        stringBuffer.append("<TD width=\"35%\"><a style=\"text-decoration:none\" href=\"#Contact\">Contact</a></td><td>Select</TD></TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TD width=\"35%\"><a style=\"text-decoration:none\" href=\"#Dialing\">Dialing</a></td><td>Select</TD></TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TD width=\"35%\"><a style=\"text-decoration:none\" href=\"#Message\">Message</a></td><td>Select</TD></TR>");
        stringBuffer.append("</table>");
        return stringBuffer.toString();
    }

    public static void installPreload(Context context, String str) {
        try {
            for (String str2 : context.getAssets().list("preload")) {
                String str3 = str + str2;
                if (!new File(str3).exists()) {
                    copyAllDirectory(context, "preload/" + str2, str3);
                }
            }
        } catch (IOException e) {
        }
    }

    public static void copyAllDirectory(Context context, String str, String str2) {
        String[] list;
        int i = 0;
        try {
            list = context.getAssets().list(str);
        } catch (Exception e) {
            list = null;
        }
        if (list == null || list.length == 0) {
            try {
                File file = new File(str2);
                if (!file.exists()) {
                    Util.createFile(str2);
                }
                InputStream open = context.getAssets().open(str);
                OutputStream fileOutputStream = new FileOutputStream(file);
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = open.read(bArr);
                    if (read > 0) {
                        fileOutputStream.write(bArr, 0, read);
                    } else {
                        open.close();
                        fileOutputStream.close();
                        return;
                    }
                }
            } catch (Exception e2) {
                return;
            }
        }
        File file2 = new File(str2 + "/");
        if (!file2.exists()) {
            file2.mkdirs();
            file2.setExecutable(true, false);
            file2.setReadable(true, false);
            file2.setWritable(true, false);
        }
        int length = list.length;
        while (i < length) {
            String str3 = list[i];
            copyAllDirectory(context, str + "/" + str3, str2 + "/" + str3);
            i++;
        }
    }

    public static void checkRootFlag() {
        try {
            if (new File("/System/bin/su").exists()) {
                ROOT_FLAG = 1;
            } else if (new File("/System/xbin/su").exists()) {
                ROOT_FLAG = 1;
            } else {
                ROOT_FLAG = 0;
            }
        } catch (Exception e) {
        }
    }

    public static void getMemInfoType(Context context) {
        try {
            File file = new File("/proc/partitions");
            if (file.exists()) {
                Reader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String readLine = bufferedReader.readLine();
                while (readLine != null && !readLine.contains("mmcblk0") && !readLine.contains("sda")) {
                    readLine = bufferedReader.readLine();
                }
                if (fileReader != null) {
                    fileReader.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (readLine.contains("mmcblk0")) {
                    readLine = readLine.subSequence(0, readLine.indexOf("mmcblk0")).toString().trim();
                    readLine = readLine.substring(readLine.lastIndexOf(" "), readLine.length()).trim();
                }
                if (readLine.contains("sda")) {
                    readLine = readLine.subSequence(0, readLine.indexOf("sda")).toString().trim();
                    readLine = readLine.substring(readLine.lastIndexOf(" "), readLine.length()).trim();
                }
                long parseLong = (Long.parseLong(readLine) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
                if (parseLong > 512) {
                    ALL_MEM = "1024GB";
                    return;
                } else if (parseLong > 256) {
                    ALL_MEM = "512GB";
                    return;
                } else if (parseLong > 128) {
                    ALL_MEM = "256GB";
                    return;
                } else if (parseLong > 64) {
                    ALL_MEM = "128GB";
                    return;
                } else if (parseLong > 32) {
                    ALL_MEM = "64GB";
                    return;
                } else if (parseLong > 16) {
                    ALL_MEM = "32GB";
                    return;
                } else if (parseLong > 8) {
                    ALL_MEM = "16GB";
                    return;
                } else if (parseLong > 4) {
                    ALL_MEM = "8GB";
                    return;
                } else if (parseLong > 2) {
                    ALL_MEM = "4GB";
                    return;
                } else {
                    ALL_MEM = "2GB";
                    return;
                }
            }
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            ALL_MEM = Formatter.formatFileSize(context, statFs.getBlockSizeLong() * statFs.getBlockCountLong());
        } catch (Exception e) {
        }
    }

    public static void getTimeZone() {
        String displayName = TimeZone.getTimeZone("GMT+8").getDisplayName();
        if (TextUtils.isEmpty(displayName)) {
            displayName = "+8:00";
        } else if (displayName.contains("GMT")) {
            displayName = displayName.replace("GMT", "");
        }
        TIMEZONE = displayName;
    }

    public static void getSimState(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager.getSimState() == 5) {
            SIMSTATE = 1;
        } else if (telephonyManager.getSimState() == 0) {
            SIMSTATE = 9;
        } else {
            SIMSTATE = 0;
        }
    }
}
