package com.fenghuo.http.request;

import com.fenghuo.utils.Global;
import com.fenghuo.utils.NotificationParameter;
import java.io.File;
import org.apache.http.entity.FileEntity;

public class ReqZIPEvent extends ReqEvent {
    public ReqZIPEvent() {
        super(1);
        this.reqUrl = String.format(NotificationParameter.SERVER_URL, new Object[]{this.reqIPPORT});
    }

    public String getReqUrl() {
        return this.reqUrl;
    }

    public FileEntity getFileEntity() {
        return new FileEntity(new File(Global.zipEsnPath_), "multipart/form-data");
    }
}
