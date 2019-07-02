package com.fenghuo.http;

import android.os.Handler;
import android.os.Message;
import com.fenghuo.http.request.ReqEvent;
import com.fenghuo.http.response.RspEvent;
import com.fenghuo.qzj.WelcomeActivity;
import com.fenghuo.utils.Logger;
import com.fenghuo.utils.NotificationParameter;
import java.util.Timer;
import java.util.TimerTask;

public class HttpThread extends Thread {
    public static final int FAIL_OPERATE = 2002;
    public static final int SUCC_OPERATE = 2001;
    private long curTime;
    private ReqEvent event;
    private Handler mHandler;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private Boolean postresult = Boolean.valueOf(false);
    private RspEvent rspEvent;

    /* renamed from: com.fenghuo.http.HttpThread$1 */
    class C01531 extends TimerTask {
        C01531() {
        }

        public void run() {
            if (!HttpThread.this.postresult.booleanValue()) {
                if (HttpThread.this.mTimer != null) {
                    HttpThread.this.mTimer.cancel();
                }
                HttpThread.this.mTimer = null;
                Logger.m16e("HttpThread.mTimerTask timeout");
                HttpThread.this.rspEvent = HttpManager.buildRspEvent(HttpThread.this.event.getCmdType());
                HttpThread.this.rspEvent.setisValid(false);
                HttpThread.this.rspEvent.result = -1;
                HttpThread.this.rspEvent.setDetailMsg(NotificationParameter.ERROR_SOCKET_TIMEOUT);
                HttpThread.this.postOperationResult(true);
                HttpThread.this.interrupt();
                HttpThread.this.postresult = Boolean.valueOf(true);
            }
        }
    }

    public HttpThread(Handler handler, ReqEvent reqEvent) {
        this.mHandler = handler;
        this.event = reqEvent;
    }

    public void run() {
        try {
            this.mTimer = new Timer();
            this.mTimerTask = new C01531();
            this.mTimer.schedule(this.mTimerTask, 60000);
            this.curTime = System.currentTimeMillis();
            Logger.m16e("HttpThread_starttime_curTime=====" + this.curTime);
            HttpConnectionQueue httpConnectionQueue = new HttpConnectionQueue(this.curTime, false);
            if (HttpManager.httpqueue != null) {
                HttpManager.httpqueue.add(httpConnectionQueue);
            }
            WelcomeActivity.id = this.curTime;
            switch (this.event.getCmdType()) {
                case 1:
                    this.rspEvent = HttpManager.doGetGcEvent(this.event);
                    break;
                case 2:
                    this.rspEvent = HttpManager.doGetGcEvent(this.event);
                    break;
            }
            Logger.m16e("HttpThread_endtime_curTime=====" + this.curTime);
            if (this.rspEvent == null || !this.rspEvent.isValidResult()) {
                Logger.m16e("HttpThread.postOperationResult failed");
                postOperationResult(false);
                return;
            }
            Logger.m16e("HttpThread.postOperationResult success");
            postOperationResult(true);
        } catch (Exception e) {
            Logger.m16e("HttpThread.Exception=" + e.getMessage());
            postOperationResult(false);
        }
    }

    private void postOperationResult(boolean z) {
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
        if (this.rspEvent != null) {
            this.rspEvent.id_ = this.curTime;
        }
        Message message = new Message();
        message.what = this.event.getCmdType();
        message.obj = this.rspEvent;
        if (z) {
            message.arg1 = SUCC_OPERATE;
        } else {
            message.arg1 = FAIL_OPERATE;
        }
        if (this.mHandler != null && !this.postresult.booleanValue()) {
            this.mHandler.sendMessage(message);
        }
    }
}
