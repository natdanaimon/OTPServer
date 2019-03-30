/*
 * Copyright (c) 2005. Hewlett-Packard (Thailand) Ltd. All Rights Reserved.
 *
 * Application : KTBPortal
 * Class Name  : Randomizer
 * Date Created: 03 Mar 2005
 *
 */
package co.mm.infa;

import java.security.SecureRandom;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Randomizer {

    public static final int RANDOM_STRING = -1000;
    public static final int ALPHA_NUMERIC = -1001;
    public static final int NUMERIC_ONLY = -1002;

    public static String Randomize(int len) {
        return Randomize(ALPHA_NUMERIC, len);
    }

    public static String Randomize(int len, boolean alphaFilter) {
        return Randomize(ALPHA_NUMERIC, len, alphaFilter);
    }

    public static String genTOP() {
        //20100803 : SR01-10-1451: TOP Only Number and 6 digit
        return Randomize(NUMERIC_ONLY, 6, true);
    }

    public static String genLogRefId() {
        return Randomize(NUMERIC_ONLY, 8, true);
    }

    public static String genRequestId() {
        return Randomize(NUMERIC_ONLY, 8, true);
    }

    public static String Randomize(int type, int len, boolean alphaFilter) {
        switch (type) {
            case ALPHA_NUMERIC:
                return RandomizeAlphaNumeric(len, alphaFilter);
            case NUMERIC_ONLY:
                return RandomizeNumbers(len);
            default:
                return RandomizeString(len);
        }
    }

    public static String Randomize(int type, int len) {
        return Randomize(type, len, false);
    }

    protected static String RandomizeString(int len) {
        SecureRandom secureRand = new SecureRandom();

        return new String(secureRand.generateSeed(len));
    }

    protected static String RandomizeNumbers(int len) {
        final int ubound1 = 10;
        SecureRandom secureRand = new SecureRandom();

        StringBuffer generated = new StringBuffer();

        for (int i = 0; i < len; i++) {
            generated.append(secureRand.nextInt(ubound1));
        }
        return generated.toString();
    }

    protected static String RandomizeAlphaNumeric(int len, boolean alphaFilter) {
        final int ubound1 = 3;
        final int ubound2 = 25;
        String[][] fullStore = {
            {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "0", "1", "2", "3", "4", "5"},
            {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z"},
            {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
                "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
                "u", "v", "w", "x", "y", "z"}
        };
        String[][] filteredStore = {
            {"6", "2", "3", "4", "5", "6", "7", "8", "9", "7",
                "9", "2", "3", "4", "5", "6", "7", "8", "9", "8",
                "7", "2", "3", "4", "5", "6"},
            {"A", "B", "C", "D", "E", "F", "G", "H", "J", "A",
                "K", "L", "M", "N", "P", "Q", "R", "S", "T", "B",
                "U", "V", "W", "X", "Y", "Z"},
            {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
                "k", "m", "n", "p", "q", "r", "s", "t", "a", "b",
                "u", "v", "w", "x", "y", "z"}
        };
        String[][] store = (alphaFilter) ? filteredStore : fullStore;

        SecureRandom secureRand = new SecureRandom();

        StringBuffer generated = new StringBuffer();

        for (int i = 0; i < len; i++) {
            generated.append(store[secureRand.nextInt(ubound1)][secureRand.nextInt(ubound2)]);
        }
        return generated.toString();
    }
}
