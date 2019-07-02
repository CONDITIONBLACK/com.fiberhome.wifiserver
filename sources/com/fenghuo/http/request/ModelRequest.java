package com.fenghuo.http.request;

import com.fenghuo.utils.Global;
import com.fenghuo.utils.NotificationParameter;

public class ModelRequest extends ReqEvent {
    public static final int MODELREQUEST = 2;

    public ModelRequest() {
        super(2);
        this.reqUrl = String.format(NotificationParameter.SERVER_URL, new Object[]{this.reqIPPORT});
        initParams();
    }

    private void initParams() {
        addParamMapItem("model", Global.getGlobal().loadModel());
        addParamMapItem("ip", Global.getGlobal().getLocalIP());
    }

    public String getReqUrl() {
        return this.reqUrl + "getmodel?" + getParamsMap();
    }
}
