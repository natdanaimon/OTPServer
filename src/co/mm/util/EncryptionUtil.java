/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mm.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author
 */
public class EncryptionUtil {

    public static String encrypt(String value) {
        try {
            byte[] encodedBytes1 = Base64.encodeBase64(value.getBytes());
            String end1 = new String(encodedBytes1);
            byte[] encodedBytes2 = Base64.encodeBase64(end1.getBytes());
            String end2 = new String(encodedBytes2);
            byte[] encodedBytes3 = Base64.encodeBase64(end2.getBytes());
            return new String(encodedBytes3);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String encrypted) {
        try {
            byte[] decodedBytes1 = Base64.decodeBase64(encrypted);
            String de1 = new String(decodedBytes1);
            byte[] decodedBytes2 = Base64.decodeBase64(de1);
            String de2 = new String(decodedBytes2);
            byte[] decodedBytes3 = Base64.decodeBase64(de2);
            return new String(decodedBytes3);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
