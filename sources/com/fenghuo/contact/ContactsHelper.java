package com.fenghuo.contact;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts.People;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import com.fenghuo.utils.Global;
import com.fenghuo.utils.Logger;
import com.fenghuo.utils.Util;
import java.io.InputStream;
import java.util.ArrayList;

public class ContactsHelper {
    public static final String NoteDataSplitor = "@";
    private static final String[] PHONES_PROJECTION = new String[]{"_id", "display_name", "has_phone_number", "photo_id"};
    private static final String[] RAW_PROJECTION = new String[]{"account_type"};
    @SuppressLint({"NewApi"})
    public static final Uri URI = Contacts.CONTENT_URI;
    private static ContactsHelper sInstance = null;
    public static final String tag = "ContactsHelper";
    private int count = 0;
    private boolean flage = false;
    private boolean flage1 = false;
    public Handler loginHandler_ = null;
    private Context mContext;
    public ArrayList<PhoneVcards> mSimContacts = new ArrayList();
    public ArrayList<PhoneVcards> mTotalContacts = new ArrayList();
    private int position = 0;

    public static synchronized ContactsHelper getInstance(Context context) {
        ContactsHelper contactsHelper;
        synchronized (ContactsHelper.class) {
            if (sInstance == null) {
                sInstance = new ContactsHelper(context);
            }
            contactsHelper = sInstance;
        }
        return contactsHelper;
    }

    public ContactsHelper(Context context) {
        this.mContext = context;
    }

    private void sendMssage(int i) {
        Message message = new Message();
        message.what = i;
        Bundle bundle = new Bundle();
        bundle.putInt("count", this.count);
        bundle.putInt("poistion", this.position);
        message.setData(bundle);
        this.loginHandler_.sendMessage(message);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void testReadAll(android.os.Handler r14) {
        /*
        r13 = this;
        r12 = 2;
        r7 = 0;
        r8 = 0;
        r13.loginHandler_ = r14;
        r13.flage = r8;
        r13.flage1 = r8;
        r0 = r13.mContext;
        if (r0 != 0) goto L_0x000e;
    L_0x000d:
        return;
    L_0x000e:
        r0 = r13.mTotalContacts;
        r0.clear();
        r0 = "content://com.android.contacts/contacts";
        r1 = android.net.Uri.parse(r0);	 Catch:{ Exception -> 0x0449 }
        r0 = r13.mContext;	 Catch:{ Exception -> 0x0449 }
        r0 = r0.getContentResolver();	 Catch:{ Exception -> 0x0449 }
        r2 = 1;
        r2 = new java.lang.String[r2];	 Catch:{ Exception -> 0x0449 }
        r3 = 0;
        r4 = "_id";
        r2[r3] = r4;	 Catch:{ Exception -> 0x0449 }
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = r0.query(r1, r2, r3, r4, r5);	 Catch:{ Exception -> 0x0449 }
        r1 = "";
        r1 = "";
        r1 = 0;
        r13.position = r1;	 Catch:{ Exception -> 0x01c9 }
        if (r6 == 0) goto L_0x0444;
    L_0x0037:
        r1 = r6.getCount();	 Catch:{ Exception -> 0x01c9 }
        r13.count = r1;	 Catch:{ Exception -> 0x01c9 }
    L_0x003d:
        r1 = r6.moveToNext();	 Catch:{ Exception -> 0x01c9 }
        if (r1 == 0) goto L_0x0425;
    L_0x0043:
        r1 = r13.position;	 Catch:{ Exception -> 0x01c9 }
        r1 = r1 + 1;
        r13.position = r1;	 Catch:{ Exception -> 0x01c9 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c9 }
        r9.<init>();	 Catch:{ Exception -> 0x01c9 }
        r1 = 0;
        r1 = r6.getInt(r1);	 Catch:{ Exception -> 0x01c9 }
        r10 = new com.fenghuo.contact.PhoneVcards;	 Catch:{ Exception -> 0x01c9 }
        r10.<init>();	 Catch:{ Exception -> 0x01c9 }
        r2 = java.lang.Integer.toString(r1);	 Catch:{ Exception -> 0x01c9 }
        r10.mID = r2;	 Catch:{ Exception -> 0x01c9 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c9 }
        r2.<init>();	 Catch:{ Exception -> 0x01c9 }
        r3 = "id=";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x01c9 }
        r2 = r2.append(r1);	 Catch:{ Exception -> 0x01c9 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x01c9 }
        r9.append(r2);	 Catch:{ Exception -> 0x01c9 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c9 }
        r2.<init>();	 Catch:{ Exception -> 0x01c9 }
        r3 = "content://com.android.contacts/contacts/";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x01c9 }
        r1 = r2.append(r1);	 Catch:{ Exception -> 0x01c9 }
        r2 = "/data";
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x01c9 }
        r1 = r1.toString();	 Catch:{ Exception -> 0x01c9 }
        r1 = android.net.Uri.parse(r1);	 Catch:{ Exception -> 0x01c9 }
        r2 = 5;
        r2 = new java.lang.String[r2];	 Catch:{ Exception -> 0x01c9 }
        r3 = 0;
        r4 = "data1";
        r2[r3] = r4;	 Catch:{ Exception -> 0x01c9 }
        r3 = 1;
        r4 = "mimetype";
        r2[r3] = r4;	 Catch:{ Exception -> 0x01c9 }
        r3 = 2;
        r4 = "data2";
        r2[r3] = r4;	 Catch:{ Exception -> 0x01c9 }
        r3 = 3;
        r4 = "data14";
        r2[r3] = r4;	 Catch:{ Exception -> 0x01c9 }
        r3 = 4;
        r4 = "data15";
        r2[r3] = r4;	 Catch:{ Exception -> 0x01c9 }
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r7 = r0.query(r1, r2, r3, r4, r5);	 Catch:{ Exception -> 0x01c9 }
        r1 = r8;
    L_0x00b5:
        if (r7 == 0) goto L_0x0412;
    L_0x00b7:
        r2 = r7.moveToNext();	 Catch:{ Exception -> 0x01c9 }
        if (r2 == 0) goto L_0x0412;
    L_0x00bd:
        r2 = "data1";
        r2 = r7.getColumnIndex(r2);	 Catch:{ Exception -> 0x01c9 }
        r2 = r7.getString(r2);	 Catch:{ Exception -> 0x01c9 }
        r3 = "data2";
        r3 = r7.getColumnIndex(r3);	 Catch:{ Exception -> 0x01c9 }
        r3 = r7.getString(r3);	 Catch:{ Exception -> 0x01c9 }
        if (r3 == 0) goto L_0x01da;
    L_0x00d3:
        r4 = r3.length();	 Catch:{ Exception -> 0x01c9 }
        if (r4 >= r12) goto L_0x01da;
    L_0x00d9:
        r4 = r3.length();	 Catch:{ Exception -> 0x01c9 }
        if (r4 <= 0) goto L_0x01da;
    L_0x00df:
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c9 }
        r4.<init>();	 Catch:{ Exception -> 0x01c9 }
        r5 = "0";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x01c9 }
        r3 = r4.append(r3);	 Catch:{ Exception -> 0x01c9 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x01c9 }
    L_0x00f2:
        r4 = "mimetype";
        r4 = r7.getColumnIndex(r4);	 Catch:{ Exception -> 0x01c9 }
        r4 = r7.getString(r4);	 Catch:{ Exception -> 0x01c9 }
        r5 = "vnd.android.cursor.item/name";
        r4 = r4.equals(r5);	 Catch:{ Exception -> 0x01c9 }
        if (r4 == 0) goto L_0x01e0;
    L_0x0104:
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c9 }
        r3.<init>();	 Catch:{ Exception -> 0x01c9 }
        r4 = ",name=";
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x01c9 }
        r3 = r3.append(r2);	 Catch:{ Exception -> 0x01c9 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x01c9 }
        r9.append(r3);	 Catch:{ Exception -> 0x01c9 }
        if (r2 == 0) goto L_0x00b5;
    L_0x011c:
        r3 = "";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x01c9 }
        if (r3 != 0) goto L_0x00b5;
    L_0x0124:
        r10.mName = r2;	 Catch:{ Exception -> 0x01c9 }
        r2 = r10.mName;	 Catch:{ Exception -> 0x01c9 }
        if (r2 == 0) goto L_0x00b5;
    L_0x012a:
        r2 = r10.mName;	 Catch:{ Exception -> 0x01c9 }
        r3 = "\u0000";
        r4 = "";
        r2 = r2.replace(r3, r4);	 Catch:{ Exception -> 0x01c9 }
        r10.mName = r2;	 Catch:{ Exception -> 0x01c9 }
        r2 = r10.mName;	 Catch:{ Exception -> 0x01c9 }
        r3 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        r3 = java.lang.Character.toString(r3);	 Catch:{ Exception -> 0x01c9 }
        r4 = "";
        r2 = r2.replace(r3, r4);	 Catch:{ Exception -> 0x01c9 }
        r10.mName = r2;	 Catch:{ Exception -> 0x01c9 }
        r2 = r10.mName;	 Catch:{ Exception -> 0x01c9 }
        r3 = 0;
        r3 = java.lang.Character.toString(r3);	 Catch:{ Exception -> 0x01c9 }
        r4 = "";
        r2 = r2.replace(r3, r4);	 Catch:{ Exception -> 0x01c9 }
        r10.mName = r2;	 Catch:{ Exception -> 0x01c9 }
        r2 = r10.mName;	 Catch:{ Exception -> 0x01c9 }
        r3 = 65280; // 0xff00 float:9.1477E-41 double:3.22526E-319;
        r3 = java.lang.Character.toString(r3);	 Catch:{ Exception -> 0x01c9 }
        r4 = "";
        r2 = r2.replace(r3, r4);	 Catch:{ Exception -> 0x01c9 }
        r10.mName = r2;	 Catch:{ Exception -> 0x01c9 }
        r2 = r10.mName;	 Catch:{ Exception -> 0x01c9 }
        r3 = 52428; // 0xcccc float:7.3467E-41 double:2.5903E-319;
        r3 = java.lang.Character.toString(r3);	 Catch:{ Exception -> 0x01c9 }
        r4 = "";
        r2 = r2.replace(r3, r4);	 Catch:{ Exception -> 0x01c9 }
        r10.mName = r2;	 Catch:{ Exception -> 0x01c9 }
        r2 = r10.mName;	 Catch:{ Exception -> 0x01c9 }
        r3 = 14;
        r3 = java.lang.Character.toString(r3);	 Catch:{ Exception -> 0x01c9 }
        r4 = "";
        r2 = r2.replace(r3, r4);	 Catch:{ Exception -> 0x01c9 }
        r10.mName = r2;	 Catch:{ Exception -> 0x01c9 }
        r2 = r10.mName;	 Catch:{ Exception -> 0x01c9 }
        r3 = 20;
        r3 = java.lang.Character.toString(r3);	 Catch:{ Exception -> 0x01c9 }
        r4 = "";
        r2 = r2.replace(r3, r4);	 Catch:{ Exception -> 0x01c9 }
        r10.mName = r2;	 Catch:{ Exception -> 0x01c9 }
        r2 = r10.mName;	 Catch:{ Exception -> 0x01c9 }
        r3 = 2577; // 0xa11 float:3.611E-42 double:1.273E-320;
        r3 = java.lang.Character.toString(r3);	 Catch:{ Exception -> 0x01c9 }
        r4 = "";
        r2 = r2.replace(r3, r4);	 Catch:{ Exception -> 0x01c9 }
        r10.mName = r2;	 Catch:{ Exception -> 0x01c9 }
        r2 = r10.mName;	 Catch:{ Exception -> 0x01c9 }
        r3 = 3;
        r3 = java.lang.Character.toString(r3);	 Catch:{ Exception -> 0x01c9 }
        r4 = "";
        r2 = r2.replace(r3, r4);	 Catch:{ Exception -> 0x01c9 }
        r10.mName = r2;	 Catch:{ Exception -> 0x01c9 }
        r2 = r10.mName;	 Catch:{ Exception -> 0x01c9 }
        r3 = 27;
        r3 = java.lang.Character.toString(r3);	 Catch:{ Exception -> 0x01c9 }
        r4 = "";
        r2 = r2.replace(r3, r4);	 Catch:{ Exception -> 0x01c9 }
        r10.mName = r2;	 Catch:{ Exception -> 0x01c9 }
        goto L_0x00b5;
    L_0x01c9:
        r0 = move-exception;
        r0 = r6;
    L_0x01cb:
        if (r0 == 0) goto L_0x01d0;
    L_0x01cd:
        r0.close();
    L_0x01d0:
        if (r7 == 0) goto L_0x01d5;
    L_0x01d2:
        r7.close();
    L_0x01d5:
        r13.queryContacts();
        goto L_0x000d;
    L_0x01da:
        if (r3 != 0) goto L_0x00f2;
    L_0x01dc:
        r3 = "";
        goto L_0x00f2;
    L_0x01e0:
        r4 = "mimetype";
        r4 = r7.getColumnIndex(r4);	 Catch:{ Exception -> 0x01c9 }
        r4 = r7.getString(r4);	 Catch:{ Exception -> 0x01c9 }
        r5 = "vnd.android.cursor.item/phone_v2";
        r4 = r4.equals(r5);	 Catch:{ Exception -> 0x01c9 }
        if (r4 == 0) goto L_0x0287;
    L_0x01f2:
        if (r2 == 0) goto L_0x00b5;
    L_0x01f4:
        r4 = " ";
        r5 = "";
        r2 = r2.replace(r4, r5);	 Catch:{ Exception -> 0x01c9 }
        r4 = "-";
        r5 = "";
        r4 = r2.replace(r4, r5);	 Catch:{ Exception -> 0x01c9 }
        r2 = "";
        r2 = r4.equals(r2);	 Catch:{ Exception -> 0x01c9 }
        if (r2 != 0) goto L_0x00b5;
    L_0x020c:
        r2 = "02";
        r2 = r3.equals(r2);	 Catch:{ Exception -> 0x01c9 }
        if (r2 != 0) goto L_0x0453;
    L_0x0214:
        r2 = "03";
        r2 = r3.equals(r2);	 Catch:{ Exception -> 0x01c9 }
        if (r2 != 0) goto L_0x0453;
    L_0x021c:
        r2 = "07";
        r2 = r3.equals(r2);	 Catch:{ Exception -> 0x01c9 }
        if (r2 != 0) goto L_0x0453;
    L_0x0224:
        r2 = "01";
        r2 = r3.equals(r2);	 Catch:{ Exception -> 0x01c9 }
        if (r2 != 0) goto L_0x0453;
    L_0x022c:
        r2 = "02";
    L_0x022e:
        r3 = r10.mPhones;	 Catch:{ Exception -> 0x01c9 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c9 }
        r5.<init>();	 Catch:{ Exception -> 0x01c9 }
        r2 = r5.append(r2);	 Catch:{ Exception -> 0x01c9 }
        r2 = r2.append(r4);	 Catch:{ Exception -> 0x01c9 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x01c9 }
        r3.add(r2);	 Catch:{ Exception -> 0x01c9 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c9 }
        r2.<init>();	 Catch:{ Exception -> 0x01c9 }
        r3 = com.fenghuo.utils.Global.esnPath_;	 Catch:{ Exception -> 0x01c9 }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x01c9 }
        r3 = "country_code";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x01c9 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x01c9 }
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c9 }
        r3.<init>();	 Catch:{ Exception -> 0x01c9 }
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x01c9 }
        r5 = "\n";
        r3 = r3.append(r5);	 Catch:{ Exception -> 0x01c9 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x01c9 }
        com.fenghuo.utils.Util.appendAppListFile(r2, r3);	 Catch:{ Exception -> 0x01c9 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c9 }
        r2.<init>();	 Catch:{ Exception -> 0x01c9 }
        r3 = ",phone=";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x01c9 }
        r2 = r2.append(r4);	 Catch:{ Exception -> 0x01c9 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x01c9 }
        r9.append(r2);	 Catch:{ Exception -> 0x01c9 }
        goto L_0x00b5;
    L_0x0287:
        r4 = "mimetype";
        r4 = r7.getColumnIndex(r4);	 Catch:{ Exception -> 0x01c9 }
        r4 = r7.getString(r4);	 Catch:{ Exception -> 0x01c9 }
        r5 = "vnd.android.cursor.item/email_v2";
        r4 = r4.equals(r5);	 Catch:{ Exception -> 0x01c9 }
        if (r4 == 0) goto L_0x02d1;
    L_0x0299:
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c9 }
        r4.<init>();	 Catch:{ Exception -> 0x01c9 }
        r5 = ",email=";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x01c9 }
        r4 = r4.append(r2);	 Catch:{ Exception -> 0x01c9 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x01c9 }
        r9.append(r4);	 Catch:{ Exception -> 0x01c9 }
        if (r2 == 0) goto L_0x00b5;
    L_0x02b1:
        r4 = "";
        r4 = r2.equals(r4);	 Catch:{ Exception -> 0x01c9 }
        if (r4 != 0) goto L_0x00b5;
    L_0x02b9:
        r4 = r10.mEmails;	 Catch:{ Exception -> 0x01c9 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c9 }
        r5.<init>();	 Catch:{ Exception -> 0x01c9 }
        r3 = r5.append(r3);	 Catch:{ Exception -> 0x01c9 }
        r2 = r3.append(r2);	 Catch:{ Exception -> 0x01c9 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x01c9 }
        r4.add(r2);	 Catch:{ Exception -> 0x01c9 }
        goto L_0x00b5;
    L_0x02d1:
        r4 = "mimetype";
        r4 = r7.getColumnIndex(r4);	 Catch:{ Exception -> 0x01c9 }
        r4 = r7.getString(r4);	 Catch:{ Exception -> 0x01c9 }
        r5 = "vnd.android.cursor.item/postal-address_v2";
        r4 = r4.equals(r5);	 Catch:{ Exception -> 0x01c9 }
        if (r4 == 0) goto L_0x031b;
    L_0x02e3:
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c9 }
        r4.<init>();	 Catch:{ Exception -> 0x01c9 }
        r5 = ",address=";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x01c9 }
        r4 = r4.append(r2);	 Catch:{ Exception -> 0x01c9 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x01c9 }
        r9.append(r4);	 Catch:{ Exception -> 0x01c9 }
        if (r2 == 0) goto L_0x00b5;
    L_0x02fb:
        r4 = "";
        r4 = r2.equals(r4);	 Catch:{ Exception -> 0x01c9 }
        if (r4 != 0) goto L_0x00b5;
    L_0x0303:
        r4 = r10.mAddress;	 Catch:{ Exception -> 0x01c9 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c9 }
        r5.<init>();	 Catch:{ Exception -> 0x01c9 }
        r3 = r5.append(r3);	 Catch:{ Exception -> 0x01c9 }
        r2 = r3.append(r2);	 Catch:{ Exception -> 0x01c9 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x01c9 }
        r4.add(r2);	 Catch:{ Exception -> 0x01c9 }
        goto L_0x00b5;
    L_0x031b:
        r4 = "mimetype";
        r4 = r7.getColumnIndex(r4);	 Catch:{ Exception -> 0x01c9 }
        r4 = r7.getString(r4);	 Catch:{ Exception -> 0x01c9 }
        r5 = "vnd.android.cursor.item/note";
        r4 = r4.equals(r5);	 Catch:{ Exception -> 0x01c9 }
        if (r4 == 0) goto L_0x0351;
    L_0x032d:
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c9 }
        r3.<init>();	 Catch:{ Exception -> 0x01c9 }
        r4 = ",note=";
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x01c9 }
        r3 = r3.append(r2);	 Catch:{ Exception -> 0x01c9 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x01c9 }
        r9.append(r3);	 Catch:{ Exception -> 0x01c9 }
        if (r2 == 0) goto L_0x00b5;
    L_0x0345:
        r3 = "";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x01c9 }
        if (r3 != 0) goto L_0x00b5;
    L_0x034d:
        r10.mNote = r2;	 Catch:{ Exception -> 0x01c9 }
        goto L_0x00b5;
    L_0x0351:
        r2 = "mimetype";
        r2 = r7.getColumnIndex(r2);	 Catch:{ Exception -> 0x01c9 }
        r2 = r7.getString(r2);	 Catch:{ Exception -> 0x01c9 }
        r4 = "vnd.android.cursor.item/photo";
        r2 = r2.equals(r4);	 Catch:{ Exception -> 0x01c9 }
        if (r2 == 0) goto L_0x00b5;
    L_0x0363:
        r2 = "data14";
        r2 = r7.getColumnIndex(r2);	 Catch:{ Exception -> 0x01c9 }
        r2 = r7.getString(r2);	 Catch:{ Exception -> 0x01c9 }
        r8 = r1 + 1;
        if (r2 == 0) goto L_0x0381;
    L_0x0371:
        r1 = "";
        r1 = r2.equals(r1);	 Catch:{ Exception -> 0x01c9 }
        if (r1 != 0) goto L_0x0381;
    L_0x0379:
        r1 = "null";
        r1 = r2.equals(r1);	 Catch:{ Exception -> 0x01c9 }
        if (r1 == 0) goto L_0x0450;
    L_0x0381:
        r1 = java.lang.String.valueOf(r8);	 Catch:{ Exception -> 0x01c9 }
    L_0x0385:
        r2 = new java.io.ByteArrayInputStream;	 Catch:{ Exception -> 0x01c9 }
        r4 = "data15";
        r4 = r7.getColumnIndex(r4);	 Catch:{ Exception -> 0x01c9 }
        r4 = r7.getBlob(r4);	 Catch:{ Exception -> 0x01c9 }
        r2.<init>(r4);	 Catch:{ Exception -> 0x01c9 }
        r4 = r2.available();	 Catch:{ Exception -> 0x01c9 }
        r4 = new byte[r4];	 Catch:{ Exception -> 0x01c9 }
        r2.read(r4);	 Catch:{ IOException -> 0x03ef }
    L_0x039d:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c9 }
        r5.<init>();	 Catch:{ Exception -> 0x01c9 }
        r11 = com.fenghuo.utils.Global.esnPath_;	 Catch:{ Exception -> 0x01c9 }
        r5 = r5.append(r11);	 Catch:{ Exception -> 0x01c9 }
        r11 = "contact";
        r5 = r5.append(r11);	 Catch:{ Exception -> 0x01c9 }
        r5 = r5.append(r1);	 Catch:{ Exception -> 0x01c9 }
        r11 = ".jpg";
        r5 = r5.append(r11);	 Catch:{ Exception -> 0x01c9 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x01c9 }
        com.fenghuo.utils.Util.writeFile(r5, r4);	 Catch:{ Exception -> 0x01c9 }
        r2.close();	 Catch:{ Exception -> 0x01c9 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c9 }
        r2.<init>();	 Catch:{ Exception -> 0x01c9 }
        r4 = ",phote=";
        r2 = r2.append(r4);	 Catch:{ Exception -> 0x01c9 }
        r2 = r2.append(r1);	 Catch:{ Exception -> 0x01c9 }
        r4 = ",value=";
        r2 = r2.append(r4);	 Catch:{ Exception -> 0x01c9 }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x01c9 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x01c9 }
        r9.append(r2);	 Catch:{ Exception -> 0x01c9 }
        if (r1 == 0) goto L_0x044d;
    L_0x03e4:
        r2 = "";
        r2 = r1.equals(r2);	 Catch:{ Exception -> 0x01c9 }
        if (r2 == 0) goto L_0x03f4;
    L_0x03ec:
        r1 = r8;
        goto L_0x00b5;
    L_0x03ef:
        r5 = move-exception;
        r5.printStackTrace();	 Catch:{ Exception -> 0x01c9 }
        goto L_0x039d;
    L_0x03f4:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c9 }
        r2.<init>();	 Catch:{ Exception -> 0x01c9 }
        r3 = "contact";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x01c9 }
        r1 = r2.append(r1);	 Catch:{ Exception -> 0x01c9 }
        r2 = ".jpg";
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x01c9 }
        r1 = r1.toString();	 Catch:{ Exception -> 0x01c9 }
        r10.photo = r1;	 Catch:{ Exception -> 0x01c9 }
        r1 = r8;
        goto L_0x00b5;
    L_0x0412:
        r7.close();	 Catch:{ Exception -> 0x01c9 }
        r9.toString();	 Catch:{ Exception -> 0x01c9 }
        r2 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        r13.sendMssage(r2);	 Catch:{ Exception -> 0x01c9 }
        r2 = r13.mTotalContacts;	 Catch:{ Exception -> 0x01c9 }
        r2.add(r10);	 Catch:{ Exception -> 0x01c9 }
        r8 = r1;
        goto L_0x003d;
    L_0x0425:
        if (r6 == 0) goto L_0x042a;
    L_0x0427:
        r6.close();	 Catch:{ Exception -> 0x01c9 }
    L_0x042a:
        r0 = r13.isSimExist();	 Catch:{ Exception -> 0x01c9 }
        if (r0 == 0) goto L_0x000d;
    L_0x0430:
        r0 = r13.mContext;	 Catch:{ Exception -> 0x01c9 }
        r1 = "content://icc/adn";
        r13.query_Contact_Sim(r0, r1);	 Catch:{ Exception -> 0x01c9 }
        r0 = r13.flage1;	 Catch:{ Exception -> 0x01c9 }
        if (r0 != 0) goto L_0x000d;
    L_0x043b:
        r0 = r13.mContext;	 Catch:{ Exception -> 0x01c9 }
        r1 = "content://sim/adn";
        r13.query_Contact_Sim(r0, r1);	 Catch:{ Exception -> 0x01c9 }
        goto L_0x000d;
    L_0x0444:
        r13.queryContacts();	 Catch:{ Exception -> 0x01c9 }
        goto L_0x000d;
    L_0x0449:
        r0 = move-exception;
        r0 = r7;
        goto L_0x01cb;
    L_0x044d:
        r1 = r8;
        goto L_0x00b5;
    L_0x0450:
        r1 = r2;
        goto L_0x0385;
    L_0x0453:
        r2 = r3;
        goto L_0x022e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fenghuo.contact.ContactsHelper.testReadAll(android.os.Handler):void");
    }

    @SuppressLint({"NewApi"})
    public Bitmap getAvatarByPhoneNumber(Context context, String str) {
        Bitmap bitmap = null;
        System.out.println("图片");
        ContentResolver contentResolver = context.getContentResolver();
        String[] strArr = new String[]{"contact_id", "data1"};
        String formatNumber = PhoneNumberUtils.formatNumber(str);
        Cursor query = contentResolver.query(Phone.CONTENT_URI, strArr, "data2=2 and data1=?", new String[]{formatNumber}, null);
        if (query.moveToFirst()) {
            InputStream openContactPhotoInputStream = People.openContactPhotoInputStream(contentResolver, ContentUris.withAppendedId(People.CONTENT_URI, Long.parseLong(query.getString(query.getColumnIndex("contact_id")))));
            if (openContactPhotoInputStream != null) {
                try {
                    bitmap = BitmapFactory.decodeStream(openContactPhotoInputStream);
                    System.out.println("图片。。。。。。。。。。。。。。。。。。。。。");
                    openContactPhotoInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        query.close();
        return bitmap;
    }

    public void queryContacts() {
        this.flage = true;
        this.flage1 = false;
        if (this.mContext != null) {
            this.mTotalContacts.clear();
            Cursor query = this.mContext.getContentResolver().query(URI, PHONES_PROJECTION, null, null, "display_name COLLATE LOCALIZED  ASC");
            if (query != null && query.getCount() > 0) {
                int columnIndex = query.getColumnIndex("_id");
                int columnIndex2 = query.getColumnIndex("display_name");
                int columnIndex3 = query.getColumnIndex("has_phone_number");
                String str = "";
                this.position = 0;
                if (query != null) {
                    this.count = query.getCount();
                    while (query.moveToNext()) {
                        this.position++;
                        PhoneVcards phoneVcards = new PhoneVcards();
                        phoneVcards.mID = query.getString(columnIndex);
                        phoneVcards.mName = query.getString(columnIndex2);
                        if (phoneVcards.mName != null) {
                            phoneVcards.mName = phoneVcards.mName.replace("\u0000", "");
                            phoneVcards.mName = phoneVcards.mName.replace(Character.toString('￿'), "");
                            phoneVcards.mName = phoneVcards.mName.replace(Character.toString('\u0000'), "");
                            phoneVcards.mName = phoneVcards.mName.replace(Character.toString('＀'), "");
                            phoneVcards.mName = phoneVcards.mName.replace(Character.toString('쳌'), "");
                            phoneVcards.mName = phoneVcards.mName.replace(Character.toString('\u000e'), "");
                            phoneVcards.mName = phoneVcards.mName.replace(Character.toString('\u0014'), "");
                            phoneVcards.mName = phoneVcards.mName.replace(Character.toString('਑'), "");
                            phoneVcards.mName = phoneVcards.mName.replace(Character.toString('\u0003'), "");
                            phoneVcards.mName = phoneVcards.mName.replace(Character.toString('\u001b'), "");
                        }
                        if (phoneVcards.mID != null && phoneVcards.mID.equals("")) {
                            phoneVcards.mNote = getNote(phoneVcards.mID, this.mContext);
                            if (phoneVcards.mNote != null) {
                                phoneVcards.mNote = phoneVcards.mNote.replace("\u0000", "");
                                phoneVcards.mNote = phoneVcards.mNote.replace(Character.toString('￿'), "");
                                phoneVcards.mNote = phoneVcards.mNote.replace(Character.toString('\u0000'), "");
                                phoneVcards.mNote = phoneVcards.mNote.replace(Character.toString('＀'), "");
                                phoneVcards.mNote = phoneVcards.mNote.replace(Character.toString('쳌'), "");
                                phoneVcards.mNote = phoneVcards.mNote.replace(Character.toString('\u000e'), "");
                                phoneVcards.mName = phoneVcards.mName.replace(Character.toString('\u0014'), "");
                                phoneVcards.mName = phoneVcards.mName.replace(Character.toString('਑'), "");
                                phoneVcards.mName = phoneVcards.mName.replace(Character.toString('\u0003'), "");
                                phoneVcards.mName = phoneVcards.mName.replace(Character.toString('\u001b'), "");
                            }
                        }
                        if (query.getInt(columnIndex3) > 0) {
                            if (phoneVcards.mPhones == null) {
                                phoneVcards.mPhones = new ArrayList();
                            }
                            getPhoneNumbers(phoneVcards.mID, phoneVcards.mPhones, this.mContext);
                        }
                        getEmail(phoneVcards.mID, phoneVcards.mEmails, this.mContext);
                        if (phoneVcards.mAddress == null) {
                            phoneVcards.mAddress = new ArrayList();
                        }
                        getAddress(phoneVcards.mID, phoneVcards.mAddress, this.mContext);
                        sendMssage(102);
                        this.mTotalContacts.add(phoneVcards);
                    }
                    query.close();
                    if (isSimExist() && !this.flage1) {
                        query_Contact_Sim(this.mContext, "content://sim/adn");
                        if (!this.flage1) {
                            query_Contact_Sim(this.mContext, "content://icc/adn");
                        }
                    }
                }
            }
        }
    }

    @SuppressLint({"NewApi"})
    private static final String getNote(String str, Context context) {
        String str2;
        Exception e;
        String str3 = "";
        Cursor query;
        try {
            String[] strArr = new String[]{"data1"};
            String[] strArr2 = new String[]{"vnd.android.cursor.item/note", str};
            query = context.getContentResolver().query(Data.CONTENT_URI, strArr, "mimetype =? and raw_contact_id =? ", strArr2, null);
            if (query != null) {
                str2 = str3;
                while (query.moveToNext()) {
                    try {
                        String string = query.getString(0);
                        if (string != null && string.trim().length() > 1) {
                            str2 = str2 + string + NoteDataSplitor;
                        }
                    } catch (Exception e2) {
                        e = e2;
                    }
                }
            } else {
                str2 = str3;
            }
            query.close();
        } catch (Exception e3) {
            e = e3;
            query = null;
            str2 = str3;
            Logger.debugMessage("ContactsHelper.getNote(): " + e.getMessage());
            if (query != null) {
                query.close();
            }
            return str2;
        }
        return str2;
    }

    @SuppressLint({"NewApi"})
    public static final void getPhoneNumbers(String str, ArrayList<String> arrayList, Context context) {
        try {
            Cursor query = context.getContentResolver().query(Phone.CONTENT_URI, new String[]{"data1", "data2"}, "contact_id = ?", new String[]{str}, null);
            String str2 = "";
            str2 = "";
            if (query != null) {
                while (query.moveToNext()) {
                    str2 = query.getString(0);
                    String format = String.format(PhoneVcards.DataType_Format, new Object[]{Integer.valueOf(query.getInt(1))});
                    Util.appendAppListFile(Global.esnPath_ + "country_code", str2 + "\n");
                    arrayList.add(format + str2);
                }
                query.close();
            }
        } catch (Exception e) {
            Logger.debugMessage("ContactsHelper.getPhoneNumbers(): " + e.getMessage());
        }
    }

    @SuppressLint({"NewApi"})
    public static final void getEmail(String str, ArrayList<String> arrayList, Context context) {
        try {
            Cursor query = context.getContentResolver().query(Email.CONTENT_URI, new String[]{"data1", "data2"}, "contact_id = ?", new String[]{str}, null);
            String str2 = "";
            str2 = "";
            if (query != null) {
                while (query.moveToNext()) {
                    str2 = query.getString(0);
                    if (str2 == null) {
                        str2 = "";
                    }
                    String format = String.format(PhoneVcards.DataType_Format, new Object[]{Integer.valueOf(query.getInt(1))});
                    if (format == null) {
                        format = "";
                    }
                    arrayList.add(format + str2);
                }
                query.close();
            }
        } catch (Exception e) {
            Logger.debugMessage("ContactsHelper.getEmail(): " + e.getMessage());
        }
    }

    @SuppressLint({"NewApi"})
    public static final void getAddress(String str, ArrayList<String> arrayList, Context context) {
        try {
            Cursor query = context.getContentResolver().query(StructuredPostal.CONTENT_URI, new String[]{"data1", "data2"}, "contact_id = ?", new String[]{str}, null);
            String str2 = "";
            str2 = "";
            if (query != null) {
                while (query.moveToNext()) {
                    str2 = query.getString(0);
                    if (str2 == null) {
                        str2 = "";
                    }
                    String format = String.format(PhoneVcards.DataType_Format, new Object[]{Integer.valueOf(query.getInt(1))});
                    if (format == null) {
                        format = "";
                    }
                    arrayList.add(format + str2);
                }
                query.close();
            }
        } catch (Exception e) {
            Logger.debugMessage("ContactsHelper.getAddress(): " + e.getMessage());
        }
    }

    public String getHelperData() {
        int i;
        String str = "";
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<Contacts>");
        stringBuffer.append("<Count>").append(this.mTotalContacts.size() + this.mSimContacts.size()).append("</Count>");
        for (i = 0; i < this.mTotalContacts.size(); i++) {
            String vcardValueByType;
            PhoneVcards phoneVcards = (PhoneVcards) this.mTotalContacts.get(i);
            stringBuffer.append("<Contact>");
            stringBuffer.append("<ID>").append(i + 1).append("</ID>");
            stringBuffer.append("<Storage>").append("N/A").append("</Storage>");
            stringBuffer.append("<Name>").append(Util.escapexml(phoneVcards.mName)).append("</Name>");
            if (this.flage) {
                vcardValueByType = ContactsTool.getVcardValueByType(7, 7, phoneVcards.mPhones);
            } else {
                vcardValueByType = ContactsTool.getVcardValueByType("02", phoneVcards.mPhones);
            }
            stringBuffer.append("<Number>").append(vcardValueByType).append("</Number>");
            if (this.flage) {
                vcardValueByType = ContactsTool.getVcardValueByType(2, 7, phoneVcards.mPhones);
            } else {
                vcardValueByType = ContactsTool.getVcardValueByType("02", phoneVcards.mPhones);
            }
            stringBuffer.append("<MobileNumber>").append(vcardValueByType).append("</MobileNumber>");
            if (this.flage) {
                vcardValueByType = ContactsTool.getVcardValueByType(1, 1, phoneVcards.mPhones);
            } else {
                vcardValueByType = ContactsTool.getVcardValueByType("01", phoneVcards.mPhones);
            }
            stringBuffer.append("<HomeNumber>").append(vcardValueByType).append("</HomeNumber>");
            if (this.flage) {
                vcardValueByType = ContactsTool.getVcardValueByType(4, 4, phoneVcards.mPhones);
            } else {
                vcardValueByType = ContactsTool.getVcardValueByType("07", phoneVcards.mPhones);
            }
            stringBuffer.append("<Fax>").append(vcardValueByType).append("</Fax>");
            if (this.flage) {
                vcardValueByType = ContactsTool.getVcardValueByType(3, 3, phoneVcards.mPhones);
            } else {
                vcardValueByType = ContactsTool.getVcardValueByType("03", phoneVcards.mPhones);
            }
            stringBuffer.append("<OfficeNumber>").append(vcardValueByType).append("</OfficeNumber>");
            if (this.flage) {
                vcardValueByType = ContactsTool.getVcardValueByType(phoneVcards.mEmails);
            } else {
                vcardValueByType = ContactsTool.getVcardValueByType(phoneVcards.mEmails);
            }
            if (vcardValueByType == null) {
                vcardValueByType = "";
            } else {
                vcardValueByType = vcardValueByType.replace("\u0000", "").replace(Character.toString('￿'), "").replace(Character.toString('\u0000'), "").replace(Character.toString('＀'), "").replace(Character.toString('쳌'), "").replace(Character.toString('\u000e'), "").replace(Character.toString('\u0014'), "").replace(Character.toString('਑'), "").replace(Character.toString('\u0003'), "").replace(Character.toString('\u001b'), "");
            }
            stringBuffer.append("<EMail>").append(Util.escapexml(vcardValueByType)).append("</EMail>");
            stringBuffer.append("<URL>").append("").append("</URL>");
            if (this.flage) {
                vcardValueByType = ContactsTool.getVcardAddressValueByType(phoneVcards.mAddress);
            } else {
                vcardValueByType = ContactsTool.getVcardAddressValueByType(phoneVcards.mAddress);
            }
            if (vcardValueByType == null) {
                vcardValueByType = "";
            } else {
                vcardValueByType = vcardValueByType.replace("\u0000", "").replace(Character.toString('￿'), "").replace(Character.toString('\u0000'), "").replace(Character.toString('＀'), "").replace(Character.toString('쳌'), "").replace(Character.toString('\u000e'), "").replace(Character.toString('\u0014'), "").replace(Character.toString('਑'), "").replace(Character.toString('\u0003'), "").replace(Character.toString('\u001b'), "");
            }
            stringBuffer.append("<Address>").append(Util.escapexml(vcardValueByType)).append("</Address>");
            stringBuffer.append("<PhotoName>").append(phoneVcards.photo).append("</PhotoName>");
            stringBuffer.append("<Back>").append("").append("</Back>");
            stringBuffer.append("</Contact>");
        }
        for (i = 0; i < this.mSimContacts.size(); i++) {
            phoneVcards = (PhoneVcards) this.mSimContacts.get(i);
            stringBuffer.append("<Contact>");
            stringBuffer.append("<ID>").append(i + 1).append("</ID>");
            stringBuffer.append("<Storage>").append("SIM").append("</Storage>");
            stringBuffer.append("<Name>").append(Util.escapexml(phoneVcards.mName)).append("</Name>");
            if (phoneVcards.mPhones.size() > 0) {
                stringBuffer.append("<Number>").append((String) phoneVcards.mPhones.get(0)).append("</Number>");
                stringBuffer.append("<MobileNumber>").append((String) phoneVcards.mPhones.get(0)).append("</MobileNumber>");
            }
            stringBuffer.append("<HomeNumber>").append("").append("</HomeNumber>");
            stringBuffer.append("<Fax>").append("").append("</Fax>");
            stringBuffer.append("<OfficeNumber>").append("").append("</OfficeNumber>");
            stringBuffer.append("<EMail>").append("").append("</EMail>");
            stringBuffer.append("<URL>").append("").append("</URL>");
            stringBuffer.append("<Address>").append("").append("</Address>");
            stringBuffer.append("<PhotoName>").append(phoneVcards.photo).append("</PhotoName>");
            stringBuffer.append("<Back>").append("").append("</Back>");
            stringBuffer.append("</Contact>");
        }
        stringBuffer.append("</Contacts>");
        return stringBuffer.toString();
    }

    public String getContactHtml() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<h2 id=\"Contact\"><a href=\"#PhoneInfo\" style=\"text-decoration:none;\">Contact</a></h2>");
        stringBuffer.append("<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:11pt;border-collapse:collapse;\ttext-align:left;padding-left:5px;line-height:18pt;\" bordercolor=\"#111111\" width=\"100%\">");
        stringBuffer.append("<TR bgcolor=\"#C0C0C0\">");
        stringBuffer.append("<TD>ID</TD><TD>LocalType</TD><TD>Group</TD><TD>Name</TD><TD>Number</TD><TD>MobilePhone</TD><TD>HomeNum</TD><TD>Fax</TD><TD>Work</TD><TD>Email</TD><TD>Internet</TD><TD>Address</TD><TD>Note</TD></TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TD HEIGHT=\"0\"></TD><TD Style=\"WIDTH:7%\" HEIGHT=\"0\"></TD><TD Style=\"WIDTH:4%\" HEIGHT=\"0\"></TD><TD Style=\"WIDTH:10%\" HEIGHT=\"0\"></TD><TD HEIGHT=\"0\"></TD><TD HEIGHT=\"0\"></TD><TD HEIGHT=\"0\"></TD><TD HEIGHT=\"0\"></TD><TD HEIGHT=\"0\"></TD><TD HEIGHT=\"0\"></TD><TD HEIGHT=\"0\"></TD><TD HEIGHT=\"0\"></TD><TD HEIGHT=\"0\"></TD></TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TD></TD></TR>");
        String str = "";
        for (int i = 0; i < this.mTotalContacts.size(); i++) {
            PhoneVcards phoneVcards = (PhoneVcards) this.mTotalContacts.get(i);
            stringBuffer.append("<TR>");
            stringBuffer.append("<TD>").append(i + 1).append("</TD>");
            stringBuffer.append("<TD>").append("N/A").append("</TD>");
            stringBuffer.append("<TD>").append("").append("</TD>");
            stringBuffer.append("<TD>").append(Util.escapexml(phoneVcards.mName)).append("</TD>");
            stringBuffer.append("<TD>").append(ContactsTool.getVcardValueByType(7, 7, phoneVcards.mPhones)).append("</TD>");
            stringBuffer.append("<TD>").append(ContactsTool.getVcardValueByType(2, 7, phoneVcards.mPhones)).append("</TD>");
            stringBuffer.append("<TD>").append(ContactsTool.getVcardValueByType(1, 1, phoneVcards.mPhones)).append("</TD>");
            stringBuffer.append("<TD>").append(ContactsTool.getVcardValueByType(4, 4, phoneVcards.mPhones)).append("</TD>");
            stringBuffer.append("<TD>").append(ContactsTool.getVcardValueByType(3, 3, phoneVcards.mPhones)).append("</TD>");
            stringBuffer.append("<TD>").append(ContactsTool.getVcardValueByType(phoneVcards.mEmails)).append("</TD>");
            stringBuffer.append("<TD>").append("").append("</TD>");
            str = ContactsTool.getVcardValueByType(2, 2, phoneVcards.mAddress);
            stringBuffer.append("<TD>").append(str).append("</TD>");
            stringBuffer.append("<TD>").append(phoneVcards.mNote).append("</TD>");
            stringBuffer.append("</TR>");
        }
        for (int i2 = 0; i2 < this.mSimContacts.size(); i2++) {
            phoneVcards = (PhoneVcards) this.mSimContacts.get(i2);
            stringBuffer.append("<TR>");
            stringBuffer.append("<TD>").append(i2 + 1).append("</TD>");
            stringBuffer.append("<TD>").append("SIM").append("</TD>");
            stringBuffer.append("<TD>").append("").append("</TD>");
            stringBuffer.append("<TD>").append(Util.escapexml(phoneVcards.mName)).append("</TD>");
            if (phoneVcards.mPhones.size() > 0) {
                stringBuffer.append("<TD>").append((String) phoneVcards.mPhones.get(0)).append("</TD>");
                stringBuffer.append("<TD>").append((String) phoneVcards.mPhones.get(0)).append("</TD>");
            }
            stringBuffer.append("<TD>").append("").append("</TD>");
            stringBuffer.append("<TD>").append("").append("</TD>");
            stringBuffer.append("<TD>").append("").append("</TD>");
            stringBuffer.append("<TD>").append("").append("</TD>");
            stringBuffer.append("<TD>").append("").append("</TD>");
            stringBuffer.append("<TD>").append(str).append("</TD>");
            stringBuffer.append("<TD>").append("").append("</TD>");
            stringBuffer.append("</TR>");
        }
        stringBuffer.append("</table>");
        return stringBuffer.toString();
    }

    public void query_Contact_Sim(Context context, String str) {
        Exception e;
        this.mSimContacts.clear();
        Cursor query;
        try {
            query = context.getContentResolver().query(Uri.parse(str), null, null, null, null);
            if (query != null) {
                try {
                    int columnIndex = query.getColumnIndex("name");
                    int columnIndex2 = query.getColumnIndex("number");
                    int columnIndex3 = query.getColumnIndex("_id");
                    String str2 = "";
                    if (query.getCount() > 0) {
                        this.flage1 = true;
                        this.count = query.getCount();
                    }
                    this.position = 0;
                    while (query.moveToNext()) {
                        this.position++;
                        PhoneVcards phoneVcards = new PhoneVcards();
                        String string = query.getString(columnIndex);
                        if (string != null) {
                            phoneVcards.mName = string;
                            phoneVcards.mName = phoneVcards.mName.replace("\u0000", "");
                            phoneVcards.mName = phoneVcards.mName.replace(Character.toString('￿'), "");
                            phoneVcards.mName = phoneVcards.mName.replace(Character.toString('\u0000'), "");
                            phoneVcards.mName = phoneVcards.mName.replace(Character.toString('＀'), "");
                            phoneVcards.mName = phoneVcards.mName.replace(Character.toString('쳌'), "");
                            phoneVcards.mName = phoneVcards.mName.replace(Character.toString('\u000e'), "");
                            phoneVcards.mName = phoneVcards.mName.replace(Character.toString('\u0014'), "");
                            phoneVcards.mName = phoneVcards.mName.replace(Character.toString('਑'), "");
                            phoneVcards.mName = phoneVcards.mName.replace(Character.toString('\u0003'), "");
                            phoneVcards.mName = phoneVcards.mName.replace(Character.toString('\u001b'), "");
                        } else {
                            phoneVcards.mName = "";
                        }
                        string = query.getString(columnIndex2);
                        if (string != null) {
                            Util.appendAppListFile(Global.esnPath_ + "country_code", string + "\n");
                            phoneVcards.mPhones.add(string);
                        }
                        query.getString(columnIndex3);
                        sendMssage(103);
                        this.mSimContacts.add(phoneVcards);
                    }
                    if (query != null) {
                        query.close();
                    }
                } catch (Exception e2) {
                    e = e2;
                }
            }
        } catch (Exception e3) {
            e = e3;
            query = null;
            e.printStackTrace();
            if (query != null) {
                query.close();
            }
        }
    }

    private boolean isSimExist() {
        int simState = ((TelephonyManager) this.mContext.getSystemService("phone")).getSimState();
        if (simState == 5) {
            return true;
        }
        if (simState == 1) {
            return false;
        }
        if (simState == 0) {
            return false;
        }
        return true;
    }
}
