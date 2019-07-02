package com.fenghuo.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.protocol.HTTP;

public class ShellCommands {
    public static String TAG = "ShellCommand";

    public static class StreamGobbler extends Thread {
        private InputStream err;
        private String errlog;
        private InputStream in;
        private String log;

        public StreamGobbler(InputStream inputStream, String str, InputStream inputStream2, String str2) {
            this.in = inputStream2;
            this.err = inputStream;
            this.errlog = str;
            this.errlog = str2;
        }

        public void run() {
            try {
                Util.writeFile(this.errlog, ShellCommands.loadStream(this.err));
                Util.writeFile(this.log, ShellCommands.loadStream(this.in));
                this.in.close();
                this.err.close();
            } catch (Exception e) {
            }
        }
    }

    public static void doSuCmds(String str, String str2) {
        try {
            Process exec = Runtime.getRuntime().exec(str);
            new StreamGobbler(exec.getErrorStream(), Global.absolutefilesPath_ + "/11", exec.getInputStream(), Global.absolutePath_ + "22").start();
            DataOutputStream dataOutputStream = new DataOutputStream(exec.getOutputStream());
            dataOutputStream.writeBytes(str2 + "\n");
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            dataOutputStream.close();
            exec.waitFor();
        } catch (Exception e) {
            System.out.println("Exception--->" + e.toString());
            e.printStackTrace();
        }
    }

    public static String loadStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[4096];
        while (true) {
            int read = inputStream.read(bArr, 0, 4096);
            if (read != -1) {
                byteArrayOutputStream.write(bArr, 0, read);
            } else {
                String str = new String(byteArrayOutputStream.toByteArray(), HTTP.UTF_8);
                inputStream.close();
                byteArrayOutputStream.close();
                return str;
            }
        }
    }

    public static boolean doExceCmds(String str) {
        try {
            Process exec = Runtime.getRuntime().exec(str);
            new StreamGobbler(exec.getErrorStream(), Global.absolutePath_ + "error", exec.getInputStream(), Global.absolutePath_ + "log").start();
            exec.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        return false;
    }

    public static boolean chmod775(String str) {
        try {
            Runtime.getRuntime().exec("chmod 777 " + str).waitFor();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
