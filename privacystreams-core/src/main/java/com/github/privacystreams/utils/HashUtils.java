package com.github.privacystreams.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A helper class to get access to hash-related utilities.
 */

public class HashUtils {
    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA-1";
    public static final String SHA256 = "SHA-256";
    public static final String SHA512 = "SHA-512";

    public static String hash(final String algorithm, final String s) throws NoSuchAlgorithmException {
        // Create Hash
        MessageDigest digest = java.security.MessageDigest.getInstance(algorithm);
        digest.update(s.getBytes());
        byte messageDigest[] = digest.digest();

        // Create Hex String
        StringBuilder hexString = new StringBuilder();
        for (byte aMessageDigest : messageDigest) {
            String h = Integer.toHexString(0xFF & aMessageDigest);
            while (h.length() < 2)
                h = "0" + h;
            hexString.append(h);
        }
        return hexString.toString();
    }

    /**
     * Return the value hash of an object
     * @see Boolean#hashCode()
     */
    public static int valueHash(Object obj) {
        if (obj == null) return 0;
        if (obj instanceof Boolean) return hashCode((boolean) obj);
        if (obj instanceof Double) return hashCode((double) obj);
        if (obj instanceof Float) return hashCode((float) obj);
        if (obj instanceof Long) return hashCode((long) obj);
        if (obj instanceof Number) return hashCode(((Number) obj).doubleValue());
        return obj.hashCode();
    }

    /**
     * Return the hash code of a bool value.
     */
    public static int hashCode(boolean bool) {
        return bool ? 1231 : 1237;
    }

    /**
     * Return the hash code of a double value.
     */
    public static int hashCode(double dbl) {
        long bits = Double.doubleToLongBits(dbl);
        return hashCode(bits);
    }

    /**
     * Return the hash code of a float value.
     */
    public static int hashCode(float flt) {
        return Float.floatToIntBits(flt);
    }

    /**
     * Return the hash code of a long value.
     */
    public static int hashCode(long lng) {
        return (int) (lng ^ (lng >>> 32));
    }

}
