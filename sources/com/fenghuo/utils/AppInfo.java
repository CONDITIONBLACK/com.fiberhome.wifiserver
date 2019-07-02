package com.fenghuo.utils;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private String appCodeSize;
    private String appName = "";
    private String appPath;
    private String cacheSize;
    private String codeSize;
    private String dataSize;
    private Drawable icon;
    private boolean isSystemApp = false;
    private String md5;
    private String packageName = "";
    private String publish;
    private String sha1;
    private String sha256;
    private long time;
    private int versionCode;
    private String versionName = "";

    public String getPublish() {
        return this.publish;
    }

    public void setPublish(String str) {
        this.publish = str;
    }

    public String getSha1() {
        return this.sha1;
    }

    public void setSha1(String str) {
        this.sha1 = str;
    }

    public String getSha256() {
        return this.sha256;
    }

    public void setSha256(String str) {
        this.sha256 = str;
    }

    public int getVersionCode() {
        return this.versionCode;
    }

    public void setVersionCode(int i) {
        this.versionCode = i;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String str) {
        this.md5 = str;
    }

    public String toString() {
        return this.appName + "\t" + this.packageName + "\tinstalled\t" + this.versionName + "\t" + this.codeSize + "\t" + this.time + "\t" + this.appPath + "\t" + getVersionCode() + "\t" + getSha1() + "\t" + getMd5() + "\t" + getPublish();
    }

    public String getAppPath() {
        return this.appPath;
    }

    public void setAppPath(String str) {
        this.appPath = str;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long j) {
        this.time = j / 1000;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public void setVersionName(String str) {
        this.versionName = str;
    }

    public String getDataSize() {
        return this.dataSize;
    }

    public void setDataSize(String str) {
        this.dataSize = str;
    }

    public String getCodeSize() {
        return this.codeSize;
    }

    public void setCodeSize(String str) {
        this.codeSize = str;
    }

    public String getCacheSize() {
        return this.cacheSize;
    }

    public void setCacheSize(String str) {
        this.cacheSize = str;
    }

    public String getAppCodeSize() {
        return this.appCodeSize;
    }

    public void setAppCodeSize() {
        this.appCodeSize += this.cacheSize + this.codeSize + this.dataSize;
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public void setIcon(Drawable drawable) {
        this.icon = drawable;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String str) {
        this.appName = str;
    }

    public boolean getIsSystemApp() {
        return this.isSystemApp;
    }

    public void setIsSystemApp(boolean z) {
        this.isSystemApp = z;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String str) {
        this.packageName = str;
    }
}
