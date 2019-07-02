package com.fenghuo.http.request;

import com.fenghuo.contact.ContactsHelper;
import com.fenghuo.utils.Global;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.entity.FileEntity;

public class ReqEvent {
    protected static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
    public static final int ZIP_EVENT = 1;
    protected int cmdType;
    protected Map<String, String> paramsMap = new HashMap();
    protected String reqIPPORT = "";
    protected String reqUrl = "";
    protected String xmlContent;

    public ReqEvent(int i) {
        this.cmdType = i;
        this.xmlContent = "";
        this.reqIPPORT = Global.getGlobal().getAppIPPORT();
    }

    public int getCmdType() {
        return this.cmdType;
    }

    public String getEventXML() {
        return this.xmlContent;
    }

    public FileEntity getFileEntity() {
        return null;
    }

    public String getReqUrl() {
        return this.reqUrl;
    }

    public void addParamMapItem(String str, String str2) {
        this.paramsMap.put(str, str2);
    }

    public String getParamsMap() {
        String str = "";
        for (Entry entry : this.paramsMap.entrySet()) {
            Object key = entry.getKey();
            str = str + ("&" + key + "=" + entry.getValue());
        }
        if (!str.equals("")) {
            str = str.replaceFirst("&", "");
        }
        return str.replaceAll(" ", "%20").replaceAll(ContactsHelper.NoteDataSplitor, "%40");
    }
}
