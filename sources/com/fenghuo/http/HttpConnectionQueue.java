package com.fenghuo.http;

public class HttpConnectionQueue {
    public boolean canceled_;
    public long id_;

    public HttpConnectionQueue(long j, boolean z) {
        this.id_ = j;
        this.canceled_ = z;
    }
}
