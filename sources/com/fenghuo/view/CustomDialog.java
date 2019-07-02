package com.fenghuo.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import com.fiberhome.wifiserver.C0171R;

public class CustomDialog extends AlertDialog implements OnClickListener {
    public String activityType = "";
    private Context context_ = null;
    public long id_;
    public boolean isBottom = false;
    public boolean isShowMsg = false;
    private String message = "";
    public TextView tv = null;
    private AlertType type = AlertType.DIALOG_ALERT;

    public enum AlertType {
        DIALOG_CONFIRM,
        DIALOG_WAITING,
        DIALOG_ALERT,
        DIALOG_ATTACHALERT
    }

    public CustomDialog(Context context, AlertType alertType) {
        super(context);
        this.context_ = context;
        this.type = alertType;
    }

    public CustomDialog(Context context, int i) {
        super(context, C0171R.style.progressDialog);
        this.context_ = context;
        this.type = AlertType.DIALOG_WAITING;
    }

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int i, AlertType alertType) {
        super(context, i);
        this.type = alertType;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        switch (this.type) {
            case DIALOG_WAITING:
                setContentView(C0171R.layout.dialog_waiting);
                this.tv = (TextView) findViewById(C0171R.id.msg);
                if (this.isShowMsg) {
                    this.tv.setVisibility(0);
                } else {
                    this.tv.setVisibility(8);
                }
                this.tv.setText(this.message);
                return;
            default:
                return;
        }
    }

    public void onClick(View view) {
        dismiss();
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public void setTV(String str, boolean z) {
        if (this.tv != null) {
            this.tv.setText(str);
            if (z && this.tv.getVisibility() != 0) {
                this.tv.setVisibility(0);
            } else if (!z && this.tv.getVisibility() != 8) {
                this.tv.setVisibility(8);
            }
        }
    }

    public void setProperty(int i) {
        Window window = getWindow();
        LayoutParams attributes = window.getAttributes();
        if (i == 0) {
            attributes.x = 0;
            attributes.y = 75;
        }
        attributes.gravity = 80;
        window.setAttributes(attributes);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onKeyDown(int r5, android.view.KeyEvent r6) {
        /*
        r4 = this;
        switch(r5) {
            case 4: goto L_0x0008;
            default: goto L_0x0003;
        };
    L_0x0003:
        r0 = super.onKeyDown(r5, r6);
        return r0;
    L_0x0008:
        r0 = "LoginInActivity";
        r1 = r4.activityType;	 Catch:{ Exception -> 0x0027 }
        r0 = r0.equals(r1);	 Catch:{ Exception -> 0x0027 }
        if (r0 == 0) goto L_0x0016;
    L_0x0012:
        r0 = r4.context_;	 Catch:{ Exception -> 0x0027 }
        if (r0 == 0) goto L_0x0016;
    L_0x0016:
        r0 = r4.id_;
        r2 = 0;
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 <= 0) goto L_0x0003;
    L_0x001e:
        r0 = r4.id_;
        com.fenghuo.http.HttpManager.cancelConnection(r0);
        r4.dismiss();
        goto L_0x0003;
    L_0x0027:
        r0 = move-exception;
        goto L_0x0016;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fenghuo.view.CustomDialog.onKeyDown(int, android.view.KeyEvent):boolean");
    }
}
