package com._pi.benepick.domain.draws.service.algorithm;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DoubleToSHA256 {

    public static String getSHA256Hash(double input) throws NoSuchAlgorithmException {
        String inputString = Double.toString(input);
        byte[] inputBytes = inputString.getBytes(StandardCharsets.UTF_8);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(inputBytes);

        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }
}