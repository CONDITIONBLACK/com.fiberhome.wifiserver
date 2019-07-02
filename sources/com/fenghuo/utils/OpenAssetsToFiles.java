package com.fenghuo.utils;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OpenAssetsToFiles {
    public static void unZipAssetsAndChomd(Context context) {
        String str = context.getFilesDir().toString() + "/";
        installPreload(context, str);
        ShellCommands.chmod775(str + "wifiscan");
        ShellCommands.chmod775(str + "wifiscan_pie");
        ShellCommands.chmod775(str + "bk_samples.bin");
        ShellCommands.chmod775(str + "terrorism_apps.csv");
        ShellCommands.chmod775(str + "id.conf");
        ShellCommands.chmod775(str + "getVirAccount");
        ShellCommands.chmod775(str + "gen_wifi_cj_flag");
        ShellCommands.chmod775(str + "gen_wifi_cj_flag_pie");
    }

    public static void installPreload(Context context, String str) {
        try {
            for (String str2 : context.getAssets().list("xbin")) {
                String str3 = str + str2;
                if (!new File(str3).exists()) {
                    copyAllDirectory(context, "xbin/" + str2, str3);
                }
            }
        } catch (IOException e) {
        }
    }

    public static void copyAllDirectory(Context context, String str, String str2) {
        String[] list;
        int i = 0;
        try {
            list = context.getAssets().list(str);
        } catch (Exception e) {
            list = null;
        }
        if (list == null || list.length == 0) {
            try {
                File file = new File(str2);
                if (!file.exists()) {
                    createFile(str2);
                }
                InputStream open = context.getAssets().open(str, 3);
                OutputStream fileOutputStream = new FileOutputStream(file);
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = open.read(bArr);
                    if (read > 0) {
                        fileOutputStream.write(bArr, 0, read);
                    } else {
                        open.close();
                        fileOutputStream.close();
                        return;
                    }
                }
            } catch (Exception e2) {
                return;
            }
        }
        File file2 = new File(str2 + "/");
        if (!file2.exists()) {
            file2.mkdirs();
            file2.setExecutable(true, false);
            file2.setReadable(true, false);
            file2.setWritable(true, false);
        }
        int length = list.length;
        while (i < length) {
            String str3 = list[i];
            copyAllDirectory(context, str + "/" + str3, str2 + "/" + str3);
            i++;
        }
    }

    public static File createFile(String str) {
        File file = new File(str);
        if (file != null && file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            return file;
        } catch (Exception e) {
            try {
                File file2 = new File(file.getParent());
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                file.createNewFile();
                if (!file.getName().equals("wifiscan") && !file.getName().equals("wifiscan_pie")) {
                    return file;
                }
                file.setExecutable(true, false);
                file.setReadable(true, false);
                file.setWritable(true, false);
                return file;
            } catch (Exception e2) {
                return null;
            }
        }
    }
}
