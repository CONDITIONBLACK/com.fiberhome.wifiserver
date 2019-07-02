package com.fenghuo.utils;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class XZip {
    public static List<File> GetFileList(String str, boolean z, boolean z2) throws Exception {
        Log.v("XZip", "GetFileList(String)");
        List<File> arrayList = new ArrayList();
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(str));
        String str2 = "";
        while (true) {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry != null) {
                String name = nextEntry.getName();
                if (nextEntry.isDirectory()) {
                    File file = new File(name.substring(0, name.length() - 1));
                    if (z) {
                        arrayList.add(file);
                    }
                } else {
                    File file2 = new File(name);
                    if (z2) {
                        arrayList.add(file2);
                    }
                }
            } else {
                zipInputStream.close();
                return arrayList;
            }
        }
    }

    public static InputStream UpZip(String str, String str2) throws Exception {
        Log.v("XZip", "UpZip(String, String)");
        ZipFile zipFile = new ZipFile(str);
        return zipFile.getInputStream(zipFile.getEntry(str2));
    }

    public static void UnZipFolder(String str, String str2) throws Exception {
        File file = new File(str2);
        if (!file.exists()) {
            file.mkdirs();
        }
        Log.v("XZip", "UnZipFolder(String, String)");
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(str));
        String str3 = "";
        while (true) {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry != null) {
                String name = nextEntry.getName();
                if (nextEntry.isDirectory()) {
                    new File(str2 + File.separator + name.substring(0, name.length() - 1)).mkdirs();
                } else {
                    int lastIndexOf;
                    File file2 = new File(str2 + File.separator + name);
                    try {
                        lastIndexOf = name.lastIndexOf("/");
                        if (lastIndexOf >= 0) {
                            new File(str2 + File.separator + name.substring(0, lastIndexOf)).mkdirs();
                        }
                    } catch (Exception e) {
                    }
                    file2.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    byte[] bArr = new byte[1024];
                    while (true) {
                        lastIndexOf = zipInputStream.read(bArr);
                        if (lastIndexOf == -1) {
                            break;
                        }
                        fileOutputStream.write(bArr, 0, lastIndexOf);
                        fileOutputStream.flush();
                    }
                    fileOutputStream.close();
                }
            } else {
                zipInputStream.close();
                return;
            }
        }
    }

    public static void ZipFolder(String str, String str2) throws Exception {
        Log.v("XZip", "ZipFolder(String, String)");
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(str2));
        File file = new File(str);
        ZipFiles(file.getParent() + File.separator, file.getName(), zipOutputStream);
        zipOutputStream.finish();
        zipOutputStream.close();
    }

    private static void ZipFiles(String str, String str2, ZipOutputStream zipOutputStream) throws Exception {
        int i = 0;
        Log.v("XZip", "ZipFiles(String, String, ZipOutputStream)");
        if (zipOutputStream != null) {
            File file = new File(str + str2);
            if (file.isFile()) {
                int indexOf = str2.indexOf("/");
                if (indexOf >= 0) {
                    str2 = str2.substring(indexOf + 1);
                }
                ZipEntry zipEntry = new ZipEntry(str2);
                FileInputStream fileInputStream = new FileInputStream(file);
                zipOutputStream.putNextEntry(zipEntry);
                byte[] bArr = new byte[4096];
                while (true) {
                    indexOf = fileInputStream.read(bArr);
                    if (indexOf != -1) {
                        zipOutputStream.write(bArr, 0, indexOf);
                    } else {
                        zipOutputStream.closeEntry();
                        return;
                    }
                }
            }
            String[] list = file.list();
            if (list.length <= 0) {
                zipOutputStream.putNextEntry(new ZipEntry(str2 + File.separator));
                zipOutputStream.closeEntry();
            }
            while (i < list.length) {
                ZipFiles(str, str2 + File.separator + list[i], zipOutputStream);
                i++;
            }
        }
    }

    public void finalize() throws Throwable {
    }
}
