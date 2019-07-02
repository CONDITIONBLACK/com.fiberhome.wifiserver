package com.fenghuo.utils;

import java.util.HashMap;
import java.util.Map;

public class MimeTypes {
    private static Map<String, String> mMimeTypes = new HashMap();

    static {
        mMimeTypes.put("ez", "application/andrew-inset");
        mMimeTypes.put("hqx", "application/mac-binhex40");
        mMimeTypes.put("cpt", "application/mac-compactpro");
        mMimeTypes.put("doc", "application/msword");
        mMimeTypes.put("bin", "application/octet-stream");
        mMimeTypes.put("dms", "application/octet-stream");
        mMimeTypes.put("lha", "application/octet-stream");
        mMimeTypes.put("lzh", "application/octet-stream");
        mMimeTypes.put("exe", "application/octet-stream");
        mMimeTypes.put("class", "application/octet-stream");
        mMimeTypes.put("so", "application/octet-stream");
        mMimeTypes.put("dll", "application/octet-stream");
        mMimeTypes.put("oda", "application/oda");
        mMimeTypes.put("pdf", "application/pdf");
        mMimeTypes.put("ai", "application/postscript");
        mMimeTypes.put("eps", "application/postscript");
        mMimeTypes.put("ps", "application/postscript");
        mMimeTypes.put("smi", "application/smil");
        mMimeTypes.put("smil", "application/smil");
        mMimeTypes.put("mif", "application/vnd.mif");
        mMimeTypes.put("xls", "application/vnd.ms-excel");
        mMimeTypes.put("ppt", "application/vnd.ms-powerpoint");
        mMimeTypes.put("wbxml", "application/vnd.wap.wbxml");
        mMimeTypes.put("wmlc", "application/vnd.wap.wmlc");
        mMimeTypes.put("wmlsc", "application/vnd.wap.wmlscriptc");
        mMimeTypes.put("bcpio", "application/x-bcpio");
        mMimeTypes.put("vcd", "application/x-cdlink");
        mMimeTypes.put("pgn", "application/x-chess-pgn");
        mMimeTypes.put("cpio", "application/x-cpio");
        mMimeTypes.put("csh", "application/x-csh");
        mMimeTypes.put("dcr", "application/x-director");
        mMimeTypes.put("dir", "application/x-director");
        mMimeTypes.put("dxr", "application/x-director");
        mMimeTypes.put("dvi", "application/x-dvi");
        mMimeTypes.put("spl", "application/x-futuresplash");
        mMimeTypes.put("gtar", "application/x-gtar");
        mMimeTypes.put("hdf", "application/x-hdf");
        mMimeTypes.put("js", "application/x-javascript");
        mMimeTypes.put("skp", "application/x-koan");
        mMimeTypes.put("skd", "application/x-koan");
        mMimeTypes.put("skt", "application/x-koan");
        mMimeTypes.put("skm", "application/x-koan");
        mMimeTypes.put("latex", "application/x-latex");
        mMimeTypes.put("nc", "application/x-netcdf");
        mMimeTypes.put("cdf", "application/x-netcdf");
        mMimeTypes.put("sh", "application/x-sh");
        mMimeTypes.put("shar", "application/x-shar");
        mMimeTypes.put("swf", "application/x-shockwave-flash");
        mMimeTypes.put("sit", "application/x-stuffit");
        mMimeTypes.put("sv4cpio", "application/x-sv4cpio");
        mMimeTypes.put("sv4crc", "application/x-sv4crc");
        mMimeTypes.put("tar", "application/x-tar");
        mMimeTypes.put("tcl", "application/x-tcl");
        mMimeTypes.put("tex", "application/x-tex");
        mMimeTypes.put("texinfo", "application/x-texinfo");
        mMimeTypes.put("texi", "application/x-texinfo");
        mMimeTypes.put("t", "application/x-troff");
        mMimeTypes.put("tr", "application/x-troff");
        mMimeTypes.put("roff", "application/x-troff");
        mMimeTypes.put("man", "application/x-troff-man");
        mMimeTypes.put("me", "application/x-troff-me");
        mMimeTypes.put("ms", "application/x-troff-ms");
        mMimeTypes.put("ustar", "application/x-ustar");
        mMimeTypes.put("src", "application/x-wais-source");
        mMimeTypes.put("xhtml", "application/xhtml+xml");
        mMimeTypes.put("xht", "application/xhtml+xml");
        mMimeTypes.put("zip", "application/zip");
        mMimeTypes.put("au", "audio/basic");
        mMimeTypes.put("snd", "audio/basic");
        mMimeTypes.put("mid", "audio/midi");
        mMimeTypes.put("midi", "audio/midi");
        mMimeTypes.put("kar", "audio/midi");
        mMimeTypes.put("mpga", "audio/mpeg");
        mMimeTypes.put("mp2", "audio/mpeg");
        mMimeTypes.put("mp3", "audio/mpeg");
        mMimeTypes.put("aif", "audio/x-aiff");
        mMimeTypes.put("aiff", "audio/x-aiff");
        mMimeTypes.put("aifc", "audio/x-aiff");
        mMimeTypes.put("m3u", "audio/x-mpegurl");
        mMimeTypes.put("ram", "audio/x-pn-realaudio");
        mMimeTypes.put("rm", "audio/x-pn-realaudio");
        mMimeTypes.put("rpm", "audio/x-pn-realaudio-plugin");
        mMimeTypes.put("ra", "audio/x-realaudio");
        mMimeTypes.put("wav", "audio/x-wav");
        mMimeTypes.put("pdb", "chemical/x-pdb");
        mMimeTypes.put("xyz", "chemical/x-xyz");
        mMimeTypes.put("bmp", "image/bmp");
        mMimeTypes.put("gif", "image/gif");
        mMimeTypes.put("ief", "image/ief");
        mMimeTypes.put("jpeg", "image/jpeg");
        mMimeTypes.put("jpg", "image/jpeg");
        mMimeTypes.put("jpe", "image/jpeg");
        mMimeTypes.put("png", "image/png");
        mMimeTypes.put("tiff", "image/tiff");
        mMimeTypes.put("tif", "image/tiff");
        mMimeTypes.put("djvu", "image/vnd.djvu");
        mMimeTypes.put("djv", "image/vnd.djvu");
        mMimeTypes.put("wbmp", "image/vnd.wap.wbmp");
        mMimeTypes.put("ras", "image/x-cmu-raster");
        mMimeTypes.put("pnm", "image/x-portable-anymap");
        mMimeTypes.put("pbm", "image/x-portable-bitmap");
        mMimeTypes.put("pgm", "image/x-portable-graymap");
        mMimeTypes.put("ppm", "image/x-portable-pixmap");
        mMimeTypes.put("rgb", "image/x-rgb");
        mMimeTypes.put("xbm", "image/x-xbitmap");
        mMimeTypes.put("xpm", "image/x-xpixmap");
        mMimeTypes.put("xwd", "image/x-xwindowdump");
        mMimeTypes.put("igs", "model/iges");
        mMimeTypes.put("iges", "model/iges");
        mMimeTypes.put("msh", "model/mesh");
        mMimeTypes.put("mesh", "model/mesh");
        mMimeTypes.put("silo", "model/mesh");
        mMimeTypes.put("wrl", "model/vrml");
        mMimeTypes.put("vrml", "model/vrml");
        mMimeTypes.put("css", "text/css");
        mMimeTypes.put("html", "text/html");
        mMimeTypes.put("htm", "text/html");
        mMimeTypes.put("asc", "text/plain");
        mMimeTypes.put("txt", "text/plain");
        mMimeTypes.put("rtx", "text/richtext");
        mMimeTypes.put("rtf", "text/rtf");
        mMimeTypes.put("sgml", "text/sgml");
        mMimeTypes.put("sgm", "text/sgml");
        mMimeTypes.put("tsv", "text/tab-separated-values");
        mMimeTypes.put("wml", "text/vnd.wap.wml");
        mMimeTypes.put("wmls", "text/vnd.wap.wmlscript");
        mMimeTypes.put("etx", "text/x-setext");
        mMimeTypes.put("xsl", "text/xml");
        mMimeTypes.put("xml", "text/xml");
        mMimeTypes.put("mpeg", "video/mpeg");
        mMimeTypes.put("mpg", "video/mpeg");
        mMimeTypes.put("mpe", "video/mpeg");
        mMimeTypes.put("qt", "video/quicktime");
        mMimeTypes.put("mov", "video/quicktime");
        mMimeTypes.put("mxu", "video/vnd.mpegurl");
        mMimeTypes.put("avi", "video/x-msvideo");
        mMimeTypes.put("movie", "video/x-sgi-movie");
        mMimeTypes.put("ice", "x-conference/x-cooltalk");
    }

    public void put(String str, String str2) {
        mMimeTypes.put(str, str2.toLowerCase());
    }

    public String getMimeType(String str) {
        return (String) mMimeTypes.get(str.toLowerCase());
    }

    public static String getContentTypeByFileExt(String str) {
        String str2 = (String) mMimeTypes.get(str.toLowerCase());
        if (str2 == null) {
            return "application/octet-stream";
        }
        return str2;
    }

    public static boolean isImageType(String str) {
        if (str == null || !str.toLowerCase().startsWith("image/")) {
            return false;
        }
        return true;
    }

    public static boolean isUixmlType(String str) {
        return str.startsWith("text/uixml");
    }

    public static boolean isReflashType(String str) {
        return str.startsWith("reflash");
    }

    public static String getContentTypeByFilepath(String str) {
        int lastIndexOf = str.lastIndexOf(".");
        if (lastIndexOf < 0) {
            return "application/octet-stream";
        }
        String str2 = (String) mMimeTypes.get(str.substring(lastIndexOf + 1).toLowerCase());
        if (str2 == null) {
            return "application/octet-stream";
        }
        return str2;
    }
}
