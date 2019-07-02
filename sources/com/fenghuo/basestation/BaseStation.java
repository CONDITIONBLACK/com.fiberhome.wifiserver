package com.fenghuo.basestation;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import com.fenghuo.utils.Global;
import com.fenghuo.utils.Util;

public class BaseStation {
    private static PhoneStateListener phoneStateListener = new C01521();
    private static TelephonyManager telephonyManager;

    /* renamed from: com.fenghuo.basestation.BaseStation$1 */
    static class C01521 extends PhoneStateListener {
        C01521() {
        }

        public void onCellLocationChanged(CellLocation cellLocation) {
            BaseStationBean baseStationBean = new BaseStationBean();
            String networkOperator;
            if (cellLocation instanceof GsmCellLocation) {
                GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
                baseStationBean.setLAC(String.valueOf(gsmCellLocation.getLac()));
                baseStationBean.setCellID(String.valueOf(gsmCellLocation.getCid()));
                networkOperator = BaseStation.telephonyManager.getNetworkOperator();
                if (networkOperator != null && networkOperator.length() >= 5) {
                    baseStationBean.setMCC(networkOperator.substring(0, 3));
                    baseStationBean.setMNC(networkOperator.substring(3, 5));
                }
                baseStationBean.setECGI(networkOperator);
            } else {
                try {
                    Class.forName("android.telephony.cdma.CdmaCellLocation");
                    CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cellLocation;
                    baseStationBean.setBID(cdmaCellLocation.getBaseStationId() >= 0 ? String.valueOf(cdmaCellLocation.getBaseStationId()) : "");
                    baseStationBean.setNID(cdmaCellLocation.getNetworkId() >= 0 ? String.valueOf(cdmaCellLocation.getNetworkId()) : "");
                    baseStationBean.setSID(cdmaCellLocation.getSystemId() >= 0 ? String.valueOf(cdmaCellLocation.getSystemId()) : "");
                    networkOperator = BaseStation.telephonyManager.getNetworkOperator();
                    if (networkOperator != null && networkOperator.length() >= 5) {
                        baseStationBean.setMCC(networkOperator.substring(0, 3));
                        baseStationBean.setMNC(networkOperator.substring(3, 5));
                    }
                    baseStationBean.setECGI(networkOperator);
                } catch (Exception e) {
                }
            }
            Util.writeFile(Global.esnPath_ + "base_station", baseStationBean.toString());
        }
    }

    public static void saveStation(Context context) {
        try {
            getBaseStation(context);
        } catch (Exception e) {
        }
    }

    private static void getBaseStation(Context context) {
        BaseStationBean baseStationBean = new BaseStationBean();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        telephonyManager.getNetworkType();
        CellLocation cellLocation = telephonyManager.getCellLocation();
        String networkOperator;
        int baseStationId;
        int networkId;
        if (cellLocation instanceof CdmaCellLocation) {
            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cellLocation;
            networkOperator = telephonyManager.getNetworkOperator();
            baseStationId = cdmaCellLocation.getBaseStationId();
            networkId = cdmaCellLocation.getNetworkId();
            String valueOf = String.valueOf(cdmaCellLocation.getSystemId());
            int parseInt = Integer.parseInt(valueOf.toString());
            if (TextUtils.isEmpty(networkOperator) || networkOperator.length() < 3) {
                baseStationBean.setMCC("");
            } else {
                baseStationBean.setMCC(String.valueOf(Integer.parseInt(networkOperator.substring(0, 3))));
            }
            baseStationBean.setMNC(String.valueOf(parseInt));
            baseStationBean.setCellID("");
            baseStationBean.setBID(String.valueOf(baseStationId));
            baseStationBean.setSID(String.valueOf(valueOf));
            baseStationBean.setNID(String.valueOf(networkId));
            baseStationBean.setECGI(networkOperator);
            baseStationBean.setLAC("");
            Util.writeFile(Global.esnPath_ + "base_station", baseStationBean.toString());
        } else if (cellLocation instanceof GsmCellLocation) {
            GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
            networkOperator = telephonyManager.getNetworkOperator();
            if (TextUtils.isEmpty(networkOperator) || networkOperator.length() < 3) {
                baseStationBean.setMCC("");
                baseStationBean.setMNC("");
            } else {
                baseStationId = Integer.parseInt(networkOperator.substring(0, 3));
                networkId = Integer.parseInt(networkOperator.substring(3));
                baseStationBean.setMCC(String.valueOf(baseStationId));
                baseStationBean.setMNC(String.valueOf(networkId));
            }
            baseStationId = gsmCellLocation.getCid();
            int lac = gsmCellLocation.getLac();
            baseStationBean.setCellID(String.valueOf(baseStationId));
            baseStationBean.setBID("");
            baseStationBean.setSID("");
            baseStationBean.setNID("");
            baseStationBean.setECGI(networkOperator);
            baseStationBean.setLAC(String.valueOf(lac));
            Util.writeFile(Global.esnPath_ + "base_station", baseStationBean.toString());
        } else if (cellLocation == null) {
            getFromListener(context);
        }
    }

    private static void getFromListener(Context context) {
        telephonyManager = (TelephonyManager) context.getSystemService("phone");
        telephonyManager.listen(phoneStateListener, 256);
        telephonyManager.listen(phoneStateListener, 16);
        if (telephonyManager.getCellLocation() != null) {
            phoneStateListener.onCellLocationChanged(telephonyManager.getCellLocation());
        }
    }
}
