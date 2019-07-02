package com.fenghuo.calllog;

import java.util.ArrayList;

public class CallLogFhItem {
    public int count = 0;
    public boolean details = false;
    public String id;
    public long lastTime;
    public ArrayList<CallLogItem> mChildItems = new ArrayList();
    public String mDepartment;
    public long mDuration;
    public String mName;
    public String mNumber;
    public String mTitle;
    public int type;

    public int getCount() {
        return this.count;
    }

    public CallLogItem getChild(int i) {
        return (CallLogItem) this.mChildItems.get(i);
    }

    public ArrayList<CallLogItem> getAllChilds() {
        return this.mChildItems;
    }

    public void addChild(CallLogItem callLogItem) {
        this.mChildItems.add(callLogItem);
    }
}
