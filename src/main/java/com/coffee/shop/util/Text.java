package com.coffee.shop.util;

import java.nio.charset.Charset;
import java.util.Random;

public class Text {

    public static String getAlphaNumericString(int n) {
        byte[] array = new byte[256];
        new Random().nextBytes(array);

        String randomString
                = new String(array, Charset.forName("UTF-8"));

        StringBuffer r = new StringBuffer();

        String  AlphaNumericString
                = randomString
                .replaceAll("[^A-Za-z0-9]", "");

        for (int k = 0; k < AlphaNumericString.length(); k++) {

            if (Character.isLetter(AlphaNumericString.charAt(k))
                    && (n > 0)
                    || Character.isDigit(AlphaNumericString.charAt(k))
                    && (n > 0)) {

                r.append(AlphaNumericString.charAt(k));
                n--;
            }
        }
        return r.toString();
    }

}
