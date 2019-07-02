package com.fenghuo.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class Sign {
    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA1";
    public static final String SHA256 = "SHA256";

    public static String getSingInfo(Context context, String str, String str2) {
        String str3;
        Exception e;
        String str4 = "";
        try {
            Signature[] signatures = getSignatures(context, str);
            int length = signatures.length;
            str3 = str4;
            int i = 0;
            while (i < length) {
                try {
                    Signature signature = signatures[i];
                    if (MD5.equals(str2)) {
                        str3 = getSignatureString(signature, MD5);
                    } else if (SHA1.equals(str2)) {
                        str3 = getSignatureString(signature, SHA1);
                    } else if (SHA256.equals(str2)) {
                        str3 = getSignatureString(signature, SHA256);
                    }
                    i++;
                } catch (Exception e2) {
                    e = e2;
                }
            }
        } catch (Exception e3) {
            Exception exception = e3;
            str3 = str4;
            e = exception;
            e.printStackTrace();
            return str3;
        }
        return str3;
    }

    public static Signature[] getSignatures(Context context, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, 64).signatures;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getSignatureString(Signature signature, String str) {
        byte[] toByteArray = signature.toByteArray();
        String str2 = "";
        try {
            MessageDigest instance = MessageDigest.getInstance(str);
            if (instance != null) {
                byte[] digest = instance.digest(toByteArray);
                StringBuilder stringBuilder = new StringBuilder();
                for (byte b : digest) {
                    stringBuilder.append(Integer.toHexString((b & 255) | 256).substring(1, 3));
                }
                str2 = stringBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str2;
    }

    public static String getFileMD5(Context context, String str) {
        String str2 = "";
        try {
            str2 = MD5Util.getMD5(new File(str));
        } catch (Exception e) {
        }
        return str2;
    }

    public static String getAPKSigInfo(String str) {
        String str2 = "";
        try {
            String principal;
            JarFile jarFile = new JarFile(str);
            ZipEntry jarEntry = jarFile.getJarEntry("AndroidManifest.xml");
            if (jarEntry != null) {
                byte[] bArr = new byte[8192];
                do {
                } while (new BufferedInputStream(jarFile.getInputStream(jarEntry)).read(bArr, 0, bArr.length) != -1);
                Certificate[] certificates = jarEntry.getCertificates();
                if (certificates != null && certificates.length > 0) {
                    principal = ((X509Certificate) certificates[0]).getSubjectDN().toString();
                    return principal;
                }
            }
            principal = str2;
            return principal;
        } catch (IOException e) {
            e.printStackTrace();
            return str2;
        } catch (Exception e2) {
            e2.printStackTrace();
            return str2;
        }
    }
}
