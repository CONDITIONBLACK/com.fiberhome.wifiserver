package com.fenghuo.contact;

import com.fenghuo.utils.Util;
import java.util.ArrayList;

public class ContactsTool {
    public static String getVcardValueByType(int i, int i2, ArrayList<String> arrayList) {
        String str = "";
        if (arrayList != null && arrayList.size() > 0) {
            int i3 = 0;
            while (i3 < arrayList.size()) {
                String str2 = (String) arrayList.get(i3);
                if (str2 == null || str2.length() < 2 || i != Util.parseToInt(str2.substring(0, 2), i2)) {
                    str2 = str;
                } else if (str.equals("")) {
                    str2 = str2.substring(2);
                } else {
                    str2 = str + "," + str2.substring(2);
                }
                i3++;
                str = str2;
            }
        }
        return str;
    }

    public static String getVcardAddressValueByType(ArrayList<String> arrayList) {
        StringBuffer stringBuffer = new StringBuffer("");
        if (arrayList != null && arrayList.size() > 0) {
            String str = (String) arrayList.get(0);
            if (str.length() > 2) {
                stringBuffer.append(str.substring(2));
            }
            for (int i = 1; i < arrayList.size(); i++) {
                str = (String) arrayList.get(i);
                if (str.length() > 2) {
                    stringBuffer.append("/" + str.substring(2));
                }
            }
        }
        return stringBuffer.toString();
    }

    public static String getVcardValueByType(ArrayList<String> arrayList) {
        StringBuffer stringBuffer = new StringBuffer("");
        if (arrayList != null && arrayList.size() > 0) {
            String str = (String) arrayList.get(0);
            if (str.length() > 2) {
                stringBuffer.append(str.substring(2));
            }
            for (int i = 1; i < arrayList.size(); i++) {
                str = (String) arrayList.get(i);
                if (str.length() > 2) {
                    stringBuffer.append("/" + str.substring(2));
                }
            }
        }
        return stringBuffer.toString();
    }

    public static String getVcardValueByType(String str, ArrayList<String> arrayList) {
        String str2 = "";
        String str3 = "";
        int i = 0;
        while (i < arrayList.size()) {
            str3 = (String) arrayList.get(i);
            if (str3 == null || str3.length() < 3 || !str3.subSequence(0, 2).toString().equals(str)) {
                str3 = str2;
            } else if (str2.equals("")) {
                str3 = str3.substring(2, str3.length());
            } else {
                str3 = str2 + "," + str3.substring(2, str3.length());
            }
            i++;
            str2 = str3;
        }
        return str2;
    }
}
