package io.github.privacystreams.communication;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Signatory {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    private String secretKey;

    public Signatory(String secretKey) {
        this.secretKey = secretKey;
    }

    public String generateSignature(String httpMethod, String path, Map<String, Object> params) {
        StringBuilder baseString = new StringBuilder();
        baseString.append(httpMethod.toUpperCase()).append("&").append(path);

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        for(String key: keys) {
            baseString.append("&").append(key).append("=").append(params.get(key));
        }

        String signature = signData(baseString.toString(), secretKey);
        return signature;
    }

    /**
     * Converts a byte array to a hex string.
     * @param bytes
     * The byte array to be converted.
     * @return
     * The converted hex string.
     */
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++)
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars).toLowerCase();
    }

    private static String signData(String data, String key) {
        String result = "";

        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(Charset.forName("UTF-8")), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes(Charset.forName("UTF-8")));
            result = bytesToHex(rawHmac);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to generate HMAC sig", ex);
        }
        return result;
    }
}