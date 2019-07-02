package com.fenghuo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

public class MD5Util {
    static char[] hexdigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String getMD5(File file) {
        Exception e;
        Throwable th;
        String str = null;
        FileInputStream fileInputStream;
        try {
            MessageDigest instance = MessageDigest.getInstance(Sign.MD5);
            fileInputStream = new FileInputStream(file);
            try {
                byte[] bArr = new byte[2048];
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    instance.update(bArr, 0, read);
                }
                str = byteToHexString(instance.digest());
                try {
                    fileInputStream.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            } catch (Exception e3) {
                e = e3;
                try {
                    e.printStackTrace();
                    try {
                        fileInputStream.close();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                    return str;
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        fileInputStream.close();
                    } catch (IOException e222) {
                        e222.printStackTrace();
                    }
                    throw th;
                }
            }
        } catch (Exception e4) {
            e = e4;
            Object obj = str;
            e.printStackTrace();
            fileInputStream.close();
            return str;
        } catch (Throwable th3) {
            fileInputStream = str;
            th = th3;
            fileInputStream.close();
            throw th;
        }
        return str;
    }

    private static String byteToHexString(byte[] bArr) {
        int i = 0;
        char[] cArr = new char[32];
        int i2 = 0;
        while (i < 16) {
            byte b = bArr[i];
            int i3 = i2 + 1;
            cArr[i2] = hexdigits[(b >>> 4) & 15];
            i2 = i3 + 1;
            cArr[i3] = hexdigits[b & 15];
            i++;
        }
        return new String(cArr);
    }
}
