/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mm.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author
 */
public class t {

    public static Locale thaiLocal = new Locale("th", "TH");
    private static SimpleDateFormat dateFormat2TH = new SimpleDateFormat("yyyy-MM-dd", thaiLocal);

    public static String DateDelFileTH(int d) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance(thaiLocal);
        cal.setTime(date);
        cal.add(Calendar.DATE, -d);
        Date d1 = cal.getTime();
        return dateFormat2TH.format(d1);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        t a = new t();
//        a.run();
//        System.out.println(DateDelFileTH(2));

        String a = "1,000,000.00";
        String b = a.replaceAll(",", "");
        System.out.println(b);

    }

    public void run() {
        String a = "01/10/61 00:15 บชX249514X รับโอนจากX386392X 200000.00บ";

        System.out.print(getRefAcc(a));

    }

    public String getInAmount(String txt) {
        String res = null;
        if (txt != null) {
            res = "0";
        }
        return res;
    }

    public String getOutAmount(String txt) {
        String res = null;
        if (txt != null) {
            res = "0";
        }
        return res;
    }

    public String getRefAcc(String txt) {
        String res = null;
        if (txt != null) {
            res = "xxx";
        }
        return res;
    }

    public String getTotal(String txt) {
        String res = null;
        if (txt != null) {
            res = "xxx";
        }
        return res;
    }

    public String findNumber(String txt) {
        String tmp = "";
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(txt);
        while (m.find()) {
            tmp += m.group();
//            System.out.println(m.group());
        }

        return tmp;
    }

    public String flgIn(String in) {
        String flg = "N";
        if (in != null) {
            if (!in.equals("")) {
                try {
                    in = in.replace(",", "");
                    in = in.replace("-", "");
                    double d = Double.parseDouble(in);
                    if (d > 0.00) {
                        flg = "Y";
                    }
                } catch (Exception e) {
                }
            }
        }

        return flg;
    }

}
