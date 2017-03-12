package com.xilingyuli.androidtips.model;

import android.content.Context;
import android.util.Base64;

import com.tencent.cos.COSClient;
import com.tencent.cos.COSClientConfig;
import com.tencent.cos.common.COSEndPoint;

import java.util.Locale;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by xilingyuli on 2017/3/11.
 */

public class CloudDataUtil {
    private static final String HMAC_SHA1 = "HmacSHA1";
    public static final String appId = "1253429367";
    public static final String bucket = "androidtips";
    public static final String secretId = "AKIDRnyqaAIkkbHl2CJyZ0H5pOlwvnY7hev5";
    private static final String secretKey = "6LZeXEj5FDyWs1BDOVRBmEUZWHQPCmRw";

    public static synchronized COSClient createCOSClient(Context context) {
        COSClientConfig config = new COSClientConfig();
        config.setEndPoint(COSEndPoint.COS_TJ);
        return new COSClient(context, appId, config, secretId);
    }

    public static String sign(boolean single, String file) {
        long currentTime = System.currentTimeMillis() / 1000;
        long expired = single ? 0 : (currentTime + 7 * 24 * 60 * 60);
        file = single ? file : "";
        String base = String.format(Locale.CHINESE, "a=%s&k=%s&e=%d&t=%d&r=%d&f=%s&b=%s",
                appId, secretId, expired, currentTime, new Random().nextInt(), file, bucket);
        return encode(base.getBytes());
    }

    private static String encode(byte[] base) {
        try {
            //hmac_sha1
            Mac mac = Mac.getInstance(HMAC_SHA1);
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), HMAC_SHA1);
            mac.init(key);
            byte[] hmacDigest = mac.doFinal(base);
            //encode
            byte[] signContent = new byte[hmacDigest.length + base.length];
            System.arraycopy(hmacDigest, 0, signContent, 0, hmacDigest.length);
            System.arraycopy(base, 0, signContent, hmacDigest.length, base.length);
            return Base64.encodeToString(signContent, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
