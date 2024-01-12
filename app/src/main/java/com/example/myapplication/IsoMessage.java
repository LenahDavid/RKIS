package com.example.myapplication;

import android.os.RemoteException;
import android.util.Log;
import org.jpos.core.Configuration;
import org.jpos.core.SimpleConfiguration;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOFieldPackager;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.util.LogListener;
import org.jpos.util.Logger;
import org.jpos.util.ProtectedLogListener;
import org.jpos.util.SimpleLogListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
//import java.util.Base64;
import android.util.Base64;
import com.example.myapplication.Packager;
import com.usdk.apiservice.aidl.device.DeviceInfo;
import com.usdk.apiservice.aidl.device.UDeviceManager;
import com.example.myapplication.DeviceHelper;

import org.json.JSONObject;



public class IsoMessage {


    public boolean sendPublicKey(String publicKey) {
        try {

            Packager packager = new Packager();
            ISOMsg rspIsoMsg = new ISOMsg();

            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);

            Logger logger = new Logger();
            ProtectedLogListener protLog = new ProtectedLogListener();
            Configuration conf = new SimpleConfiguration();
            conf.put("protect", "2 35");
            conf.put("wipe", "52");
            protLog.setConfiguration(conf);
            logger.addListener((LogListener) new SimpleLogListener(System.out));
            logger.addListener(protLog);

            ASCIIChannel channel = new ASCIIChannel("167.99.254.180", 32020, packager);
            channel.setLogger(logger, "TestLogger");

            try {
                channel.connect();
            } catch (Exception e) {
                e.printStackTrace();
                return false; // Return false on connection failure
            }


            if (channel.isConnected()) {
                // Get the current date and time
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMdd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
                String currentDateStr = dateFormat.format(currentDate);
                String currentTimeStr = timeFormat.format(currentDate);

                isoMsg.setPackager(packager);
//                isoMsg.setMTI("0800");
//                isoMsg.set(3, "560000"); // Processing Code
//                isoMsg.set(11, "123456"); // STAN
//                isoMsg.set(12, currentTimeStr); // Time
//                isoMsg.set(13, currentDateStr); // Date
                isoMsg.set(62, publicKey);
//                isoMsg.set(47, getSerialNumber());
                isoMsg.set(47, "123456");
//                duplicate = isoMsg;


            }
            channel.send(isoMsg);


            try {
                rspIsoMsg = channel.receive();
            } catch (Exception e) {
                e.printStackTrace();
                return false; // Return false on receive failure
            }

            channel.disconnect();

            return rspIsoMsg.getString(39).equals("00"); // Return true for success, false for failure
        } catch (Exception e) {
            Log.e("MyApp", "Error: " + e.getMessage());
            e.printStackTrace();
            return false; // Return false on exception
        }
    }

    public static String getSerialNumber() throws RemoteException {
        UDeviceManager deviceManager = DeviceHelper.me().getDeviceManager();
        DeviceInfo deviceInfo = deviceManager.getDeviceInfo();

        return deviceInfo.getSerialNo();

    }
}



