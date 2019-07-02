package com.fenghuo.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FromEndRF {
    public static String read(String str) {
        String readLine;
        IOException e;
        FileNotFoundException e2;
        Throwable th;
        String str2;
        String str3 = "";
        RandomAccessFile randomAccessFile;
        try {
            randomAccessFile = new RandomAccessFile(str, "r");
            try {
                long length = randomAccessFile.length();
                long filePointer = randomAccessFile.getFilePointer();
                length = (length + filePointer) - 3;
                randomAccessFile.seek(length);
                while (length > filePointer) {
                    int read = randomAccessFile.read();
                    if (read == 10 || read == 13) {
                        readLine = randomAccessFile.readLine();
                        break;
                    }
                    length--;
                    randomAccessFile.seek(length);
                }
                readLine = str3;
                try {
                    randomAccessFile.close();
                    RandomAccessFile randomAccessFile2 = null;
                    if (null != null) {
                        try {
                            randomAccessFile2.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                } catch (FileNotFoundException e4) {
                    e2 = e4;
                    try {
                        e2.printStackTrace();
                        if (randomAccessFile != null) {
                            try {
                                randomAccessFile.close();
                            } catch (IOException e32) {
                                e32.printStackTrace();
                            }
                        }
                        return readLine;
                    } catch (Throwable th2) {
                        th = th2;
                        if (randomAccessFile != null) {
                            try {
                                randomAccessFile.close();
                            } catch (IOException e322) {
                                e322.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e5) {
                    e322 = e5;
                    e322.printStackTrace();
                    if (randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                        } catch (IOException e3222) {
                            e3222.printStackTrace();
                        }
                    }
                    return readLine;
                }
            } catch (FileNotFoundException e6) {
                FileNotFoundException fileNotFoundException = e6;
                readLine = str3;
                e2 = fileNotFoundException;
            } catch (IOException e7) {
                IOException iOException = e7;
                readLine = str3;
                e3222 = iOException;
            }
        } catch (FileNotFoundException e62) {
            randomAccessFile = null;
            str2 = str3;
            e2 = e62;
            readLine = str2;
            e2.printStackTrace();
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
            return readLine;
        } catch (IOException e72) {
            randomAccessFile = null;
            str2 = str3;
            e3222 = e72;
            readLine = str2;
            e3222.printStackTrace();
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
            return readLine;
        } catch (Throwable th3) {
            th = th3;
            randomAccessFile = null;
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
            throw th;
        }
        return readLine;
    }
}
