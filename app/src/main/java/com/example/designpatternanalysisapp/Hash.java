package com.example.designpatternanalysisapp;

import android.util.Log;

import java.math.BigInteger;
import java.security.MessageDigest;

class Hash {
    /**
     * Hash String to md5
     *
     * @see https://www.baeldung.com/java-md5
     *
     * @param input string to hash
     *
     * @return String
     */
    static String md5(String input) {
        try {
            MessageDigest objMD5 = MessageDigest.getInstance("MD5");
            byte[] bytMD5 = objMD5.digest(input.getBytes());
            BigInteger intNumMD5 = new BigInteger(1, bytMD5);
            String hcMD5 = intNumMD5.toString(16);
            while (hcMD5.length() < 32) {
                hcMD5 = "0" + hcMD5;
            }
            return hcMD5;
        } catch (Exception e) {
            Log.d("Hash", e.toString());
        }
        return "";
    }
}
