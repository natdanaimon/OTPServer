
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mm.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author Adisorn
 */
public class FormatUtil {

    private static SimpleDateFormat dateTimeFormatMM88 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
    private static SimpleDateFormat dateTimeFormatMM88_MMYYYY = new SimpleDateFormat("MMyyyy", Locale.US);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyyMMdd", Locale.US);
    private static SimpleDateFormat dateFormat4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US);
    private static SimpleDateFormat dateTimeFormat2 = new SimpleDateFormat("yyyyMMdd HHmm", Locale.US);
    private static SimpleDateFormat timeFormat2 = new SimpleDateFormat("HH:mm", Locale.US);
    private static SimpleDateFormat timeFormat3 = new SimpleDateFormat("HHmmss", Locale.US);
    private static SimpleDateFormat timeFormat4 = new SimpleDateFormat("HHmm", Locale.US);
    private static SimpleDateFormat timeFormat5 = new SimpleDateFormat("HH:mm:ss", Locale.US);
    private static SimpleDateFormat dateReqID = new SimpleDateFormat("HHmmssSSS", Locale.US);

    private static DecimalFormat formatAmount = new DecimalFormat("#,##0.00");
    public static Locale thaiLocal = new Locale("th", "TH");
    private static SimpleDateFormat dateFormat2TH = new SimpleDateFormat("yyyy-MM-dd", thaiLocal);
    private static SimpleDateFormat dateFormat2TH_KBank = new SimpleDateFormat("dd/MM/yy HH:mm", thaiLocal);
    private static SimpleDateFormat dateTimeFormatBotBank = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private static SimpleDateFormat dateTimeFormatMySQL = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
    private static SimpleDateFormat dateFormatYY = new SimpleDateFormat("yy", thaiLocal);

    public static Date setDateTime(String date) {
        try {
            Date dateT = dateTimeFormatMM88.parse(date);
            return dateT;
        } catch (ParseException p) {
            return null;
        }
    }

    public static String plusDayGetMMYYYY(String date, int day) {
        try {
            Date tmp = dateTimeFormatMM88.parse(date);
            Calendar cal = Calendar.getInstance(Locale.US);
            cal.setTime(tmp);
            cal.add(Calendar.DATE, day);
            Date pMinute = cal.getTime();
            return dateTimeFormatMM88_MMYYYY.format(pMinute);
        } catch (ParseException p) {
            return date;
        }
    }

    public static Date dateTimeFormatBotBank(String date) throws ParseException {
        return dateTimeFormatBotBank.parse(date);
    }

    public static String convertDateSMSKBank(String date) {
        String res = null;
        try {
            res = dateFormat4.format(dateFormat2TH_KBank.parse(date));
        } catch (ParseException e) {
        }
        return res;
    }

    public static boolean compareOvertimeBotBankSMS(Date deposit, String BeforHOURMM88) {
        boolean flg = false;

        try {

            Date dateDeposit = deposit;
            Calendar calendarDeposit = Calendar.getInstance(Locale.US);
            calendarDeposit.setTime(dateDeposit);

            Date dateBeforHOURMM88 = dateTimeFormatMySQL.parse(BeforHOURMM88);
            Calendar calendarBeforHOURMM88 = Calendar.getInstance(Locale.US);
            calendarBeforHOURMM88.setTime(dateBeforHOURMM88);

            if (calendarDeposit.getTimeInMillis() < calendarBeforHOURMM88.getTimeInMillis()) {
                flg = true;
            }

        } catch (ParseException e) {

        }

        return flg;
    }

    public static String beforDateHOUR() {
        int min = 60;
        min = Integer.parseInt(PropertiesConfig.getConfig("bot.befor.time.min"));
        Date date = new Date();
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
//        cal.add(Calendar.HOUR, -hour);
        cal.add(Calendar.MINUTE, -min);
        Date befor12HOUR = cal.getTime();
        return dateFormat4.format(befor12HOUR);
    }

    public static String plusDay(String date, int day) {
        try {
            Date tmp = dateTimeFormatMM88.parse(date);
            Calendar cal = Calendar.getInstance(Locale.US);
            cal.setTime(tmp);
            cal.add(Calendar.DATE, day);
            Date pMinute = cal.getTime();
            return dateTimeFormatMM88.format(pMinute);
        } catch (ParseException p) {
            return date;
        }

    }

    public static String currentDateBotBank() {
        Calendar cal = Calendar.getInstance(Locale.US);
        return dateTimeFormatBotBank.format(cal.getTime());
    }

    public static String currentYearYY() {
        Calendar cal = Calendar.getInstance(Locale.US);
        return dateFormatYY.format(cal.getTime());
    }

    public static String beforDatetimeConfig() {
        int min = 5;
        min = Integer.parseInt(PropertiesConfig.getConfig("bot.befor.datetime.min"));
        Date date = new Date();
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
//        cal.add(Calendar.HOUR, -hour);
        cal.add(Calendar.MINUTE, -min);
        Date befor12HOUR = cal.getTime();
        return dateFormat4.format(befor12HOUR);
    }

    public static String DateDelFileTH(int d) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance(thaiLocal);
        cal.setTime(date);
        cal.add(Calendar.DATE, -d);
        Date d1 = cal.getTime();
        return dateFormat2TH.format(d1);
    }

    public static String DateDelFileEN(int d) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        cal.add(Calendar.DATE, -d);
        Date d1 = cal.getTime();
        return dateFormat2.format(d1);
    }

    public static String dateMM88(String dateMM88) throws ParseException {

        return dateTimeFormatMySQL.format(dateTimeFormatBotBank.parse(dateMM88));
    }

    public static boolean compareOvertimeBotBank(String deposit, String BeforHOURMM88) {
        boolean flg = false;

        try {

            Date dateDeposit = dateTimeFormatBotBank.parse(deposit);
            Calendar calendarDeposit = Calendar.getInstance(Locale.US);
            calendarDeposit.setTime(dateDeposit);

            Date dateBeforHOURMM88 = dateTimeFormatMySQL.parse(BeforHOURMM88);
            Calendar calendarBeforHOURMM88 = Calendar.getInstance(Locale.US);
            calendarBeforHOURMM88.setTime(dateBeforHOURMM88);

            if (calendarDeposit.getTimeInMillis() < calendarBeforHOURMM88.getTimeInMillis()) {
                flg = true;
            }

        } catch (ParseException e) {

        }

        return flg;
    }

    public static boolean compareOvertimeKBANK(String deposit) {
        boolean flg = false;

        try {

            Date dateDeposit = dateTimeFormatMySQL.parse(deposit);
            Calendar calendarDeposit = Calendar.getInstance(Locale.US);
            calendarDeposit.setTime(dateDeposit);

            Calendar calendarCurrentTime = Calendar.getInstance(Locale.US);

            if (calendarDeposit.getTimeInMillis() > calendarCurrentTime.getTimeInMillis()) {
                flg = true;
            }

        } catch (ParseException e) {

        }

        return flg;
    }

    public static int getSec(String sec) {
        int defaultSec = 0;
        try {
            defaultSec = Integer.parseInt(sec);
            defaultSec = defaultSec * 1000;
        } catch (NumberFormatException e) {

        }
        return defaultSec;
    }

    public static String formatAmount(String str) {
        if (str != null) {
            str = str.replaceAll("\\+", "").replaceAll("\\-", "");
        }
        BigDecimal amount;
        try {
            amount = new BigDecimal(str);
            return formatAmount.format(amount);
        } catch (Exception e) {
            return "0.00";
        }

    }

    public static String objectToUrlEncodedString(Object object, Gson gson) {
        return jsonToUrlEncodedString((JsonObject) new JsonParser().parse(gson.toJson(object)));
    }

    public static String jsonToUrlEncodedString(JsonObject jsonObject) {
        return jsonToUrlEncodedString(jsonObject, "");
    }

    public static String jsonToUrlEncodedString(JsonObject jsonObject, String prefix) {
        String urlString = "";
        for (Map.Entry<String, JsonElement> item : jsonObject.entrySet()) {
            if (item.getValue() != null && item.getValue().isJsonObject()) {
                urlString += jsonToUrlEncodedString(
                        item.getValue().getAsJsonObject(),
                        prefix.isEmpty() ? item.getKey() : prefix + "[" + item.getKey() + "]"
                );
            } else {
                urlString += prefix.isEmpty()
                        ? item.getKey() + "=" + item.getValue().getAsString() + "&"
                        : prefix + "[" + item.getKey() + "]=" + item.getValue().getAsString() + "&";
            }
        }
        return urlString;
    }

    public static BigDecimal DecimalFormat(String value, int decimal) {
        BigDecimal latlng = new BigDecimal(value);
        return latlng.setScale(decimal, BigDecimal.ROUND_HALF_EVEN);
    }

    public static BigDecimal DecimalFormat(String value, int decimal, String pattern, int condition) {
        BigDecimal latlng = new BigDecimal(value);
        return latlng.setScale(decimal, condition);
    }

    public static String getReqId() {
        Date datetime = new Date();
        return dateReqID.format(datetime);
    }

    public static String getReqDate() {
        Date datetime = new Date();
        return dateFormat2.format(datetime);
    }

    public static Date timeFormat4(String date) throws ParseException {
        return timeFormat4.parse(date);
    }

    public static String timeFormat4(Date date) {
        return timeFormat4.format(date);
    }

    public static String timeFormat5(Date date) {
        return timeFormat5.format(date);
    }

    public static String dateFormat(Date date) {
        return dateFormat.format(date);
    }

    public static String dateFormat4(Date date) {
        return dateFormat4.format(date);
    }

    public static String dateFormat3(Date date) {
        return dateFormat3.format(date);
    }

    public static String dateTimeFormat(Date date) {
        return dateTimeFormat.format(date);
    }

    public static String dateTimeFormat2(Date date) {
        return dateTimeFormat2.format(date);
    }

    public static Date dateFormat(String date) throws ParseException {
        return dateFormat.parse(date);
    }

    public static Date dateFormat2(String date) throws ParseException {
        return dateFormat2.parse(date);
    }

    public static Date dateFormat3(String date) throws ParseException {
        return dateFormat3.parse(date);
    }

    public static Date dateFormat4(String date) throws ParseException {
        return dateFormat4.parse(date);
    }

    public static Date dateTimeFormat(String date) throws ParseException {
        return dateTimeFormat.parse(date);
    }

    public static Date dateTimeFormat2(String date) throws ParseException {
        return dateTimeFormat2.parse(date);
    }

    public static String timeFormat2(Date date) {
        return timeFormat2.format(date);
    }

    public static String timeFormat3(Date date) {
        return timeFormat3.format(date);
    }

    public static String SQLDate(String date, String format1, String format2) throws ParseException {
        String r = "";
        SimpleDateFormat dformat = new SimpleDateFormat(format1, Locale.US);
        SimpleDateFormat drformat = new SimpleDateFormat(format2, Locale.US);
        Date d = dformat.parse(date);
        r = drformat.format(d);
        return r;
    }

    public static String getNullVal(String text) {
        return text == null ? "" : text;
    }

    public static String addZeroDigit(int totalDigit, String text) {
        int len = text.length();
        int addDigit = totalDigit - len;
        for (int i = 0; i < addDigit; i++) {
            text = "0" + text;
        }
        return text;
    }

    public static String numberPrecision(int precistion, String number) {
        BigDecimal result = new BigDecimal(number);
        result = result.setScale(precistion, RoundingMode.CEILING);
        return result.toString();
    }

    public static String unit(int no) {
        String str = String.format("%,d", no);
        return str;
    }

    public static String amount(double amount) {
        return amount(new BigDecimal(amount));
    }

    public static String amount(String amount) {
        if (amount == null || amount.length() <= 0) {
            amount = "0";
        }
        return amount(new BigDecimal(amount));
    }

    public static String amount(BigDecimal amount) {
        if (amount == null || amount.toString().equals("")) {
            return "0.00";
        }
        return new java.text.DecimalFormat("#,##0.00").format(amount.doubleValue());
    }

    public static Date convertFormatDate3(String dateText) throws Exception {
        Date date = dateFormat.parse(dateText);
        return date;
    }

    public static String getCurrentDate_DDMMYYYY() {
        Calendar cal = Calendar.getInstance(Locale.US);
        return dateFormat.format(cal.getTime());
    }

    public static String getCurrentDatetime() {
        Calendar cal = Calendar.getInstance(Locale.US);
        return dateFormat4.format(cal.getTime());
    }

    public static String getCurrentDate_YYYYMMDD_CBSWS() {
        Calendar cal = Calendar.getInstance(Locale.US);
        return dateFormat3.format(cal.getTime());
    }

    public static String getDatePrevious90Day_DDMMYYYY() {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(Calendar.DAY_OF_MONTH, -90);
        return dateFormat.format(cal.getTime());
    }

    public static Date getDatePrevious90Day2() {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(Calendar.DAY_OF_MONTH, -90);
        return cal.getTime();
    }

    public static String getDatePrevious30Day_DDMMYYYY() {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.add(Calendar.DAY_OF_MONTH, -30);
        return dateFormat.format(cal.getTime());
    }

    public static Date getDatePrevious1Year() {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(Calendar.YEAR, -1);
        return cal.getTime();
    }

    public static Calendar convertStringToDate1(String dateStr) throws Exception {
        Date date = dateFormat.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static String accountFormat(String accountNumber) {
        if (accountNumber == null) {
            accountNumber = "";
            return accountNumber;
        }
        if (accountNumber != null && accountNumber.length() == 10) {
            StringBuffer sb = new StringBuffer(accountNumber);
            sb.insert(3, "-");
            sb.insert(5, "-");
            sb.insert(11, "-");
            return sb.toString();
        }
        return accountNumber;
    }

    public static String getAttributeChangeStyle(String isChange) {
        if (isChange != null) {
            if (isChange.equalsIgnoreCase("Y")) {
                return "color:red";
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public static Calendar convertStringToDate1(Date date) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Calendar getCurrentCalendar() {
        return Calendar.getInstance(Locale.US);
    }

    public static List<String> transformMapToUserIdList(Map<String, String> map) {
        List<String> result = new ArrayList<String>();
        if (map.size() != 0) {
            for (String key : map.keySet()) {
                result.add(map.get(key));
            }
        }
        return result;
    }

    public static String setUserIdListForFilterLDAP(List<String> userIdList) {
        String filter = "";
        if (userIdList.size() != 0) {
            filter += "(|";// (uid=10006)(uid=10007)(uid=10009)";
            for (String userId : userIdList) {
                filter += "(uid=" + userId + ")";
            }
            filter += ")";
        }
        return filter;
    }

    public static final String atmcardFormat(String cardNumber, boolean isHiddenText) {
        if (isHiddenText) {
            cardNumber = atmcardFormatHide(cardNumber);
        }
        if (cardNumber != null && cardNumber.length() == 16) {
            StringBuffer sb = new StringBuffer(cardNumber);
            sb.insert(4, "-");
            sb.insert(9, "-");
            sb.insert(14, "-");
            return sb.toString();
        }
        return cardNumber;
    }

    public static final String atmcardFormat(String cardNumber) {
        return atmcardFormat(cardNumber, true);
    }

    public static final String atmcardFormatHide(String cardNumber) {
        if (cardNumber != null && cardNumber.length() == 16) {
            StringBuffer sb = new StringBuffer(cardNumber);
            sb.replace(6, 12, "XXXXXX");
            return sb.toString();
        }
        return cardNumber;
    }

    public static final String citizenIdFormatMarking(String citizenId) {
        try {

            if (citizenId != null && citizenId.trim().length() == 13) {
                // StringBuffer sb = new StringBuffer(citizenId);
                // sb.replace(0, citizenId.length() - 3, "XXXXXXXXXX");
                // return sb.toString();
                StringBuilder sb = new StringBuilder();
                sb.append("XXXXXXXXX");
                sb.append(citizenId.substring(9));
                citizenId = sb.toString();
            }
        } catch (Exception e) {
            System.out.println("ex: " + e);
        }
        return citizenId;
    }

    public static final String accountFormatHideWithFormatAccount(String accountNO) {
        String accountNumber = accountNO;
        if (accountNumber == null) {
            accountNumber = "";
        } else {
            if (accountNumber.length() == 10) {
                StringBuilder sb = new StringBuilder();
                sb.append(accountNumber.substring(0, 4));
                sb.append("XXXXX");
                sb.append(accountNumber.substring(accountNumber.length() - 1));
                accountNumber = sb.toString();
            } else if (accountNumber.length() == 12) {
                StringBuilder sb = new StringBuilder();
                sb.append(accountNumber.substring(0, 4));
                sb.append("XXXXXXX");
                sb.append(accountNumber.substring(accountNumber.length() - 1));
                accountNumber = sb.toString();
            } else {
                if (accountNumber.length() >= 6) {
                    int endLength = (accountNumber.length() - (6));
                    StringBuilder sb = new StringBuilder();
                    sb.append(accountNumber.substring(0, (endLength)));
                    sb.append("XXXXX");
                    sb.append(accountNumber.substring(accountNumber.length() - 1));
                    accountNumber = sb.toString();
                }
            }
        }
        return accountFormat(accountNumber);
    }

    public static String convertFormat2Format(SimpleDateFormat forOld, SimpleDateFormat forNew, String dateText)
            throws Exception {
        try {
            Date date = forOld.parse(dateText);
            return forNew.format(date);
        } catch (Exception e) {
            return dateText;
        }
    }

    public static String getResponseTime(Calendar start, Calendar end) {
        try {
            BigDecimal result = BigDecimal.ZERO;
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SS");
            String timeStart = dateFormat.format(start.getTime());
            String timeEnd = dateFormat.format(end.getTime());
            Date d1 = dateFormat.parse(timeStart);
            Date d2 = dateFormat.parse(timeEnd);

            long difference = d2.getTime() - d1.getTime();
            result = BigDecimal.valueOf(difference);
            BigDecimal milli = BigDecimal.valueOf(1000);

            result = result.divide(milli);
            result = result.setScale(2, BigDecimal.ROUND_HALF_UP);

            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String mobileFormat(String var) {
        String strReturn = var;
        if (var != null) {
            if (var.length() == 10) {
                strReturn = var.substring(0, 3) + "-" + var.substring(3, 6) + "-" + var.substring(6, 10);
            }
        } else {
            strReturn = "";
        }
        return strReturn;
    }

    public static String mobileFormatMark(String var) {
        String strReturn = var;
        if (var != null) {
            if (var.length() == 10) {
                strReturn = var.substring(0, 3) + "-xxx-x" + var.substring(7, 10);
            }
        } else {
            strReturn = "";
        }
        return strReturn;
    }

    public static String citizenFormat(String var) {
        String strReturn = var;
        if (var != null) {
            if (var.length() == 13) {
                strReturn = var.substring(0, 1) + "-" + var.substring(1, 5) + "-" + var.substring(5, 10) + "-"
                        + var.substring(10, 13);
            }
        } else {
            strReturn = "";
        }
        return strReturn;
    }

    public static String dateFormat_ddMMMyyyy(String var, String locale) throws Exception {
        Format formatter;
        formatter = new SimpleDateFormat("dd MMMM yyyy", new Locale(locale.toLowerCase(), locale.toUpperCase()));
        Date date = dateFormat3.parse(var);
        var = formatter.format(date);
        return var;
    }

    public static String dateFormat_ddMMMyyyyHHmm(String locale) throws Exception {
        String var;
        Format formatter;
        formatter = new SimpleDateFormat("dd MMMM yyyy HH:mm", new Locale(locale.toLowerCase(), locale.toUpperCase()));
        Date date = new Date();
        var = formatter.format(date);
        return var;
    }

    public static String paymentNoFormat(String var) {
        String strReturn = var;
        if (var != null) {
            if (var.length() == 9) {
                strReturn = var.substring(0, 1) + "-" + var.substring(1, 3) + "-" + var.substring(3);
            }
        } else {
            strReturn = "";
        }
        return strReturn;
    }

    public static String sqlDDLEmpty(String val) {
        String result = "";
        if (val != null) {
            if (val.equals("")) {
                result = "-1";
            } else {
                result = val;
            }
        }
        return result;
    }

    public static int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }

    public static String plusMinute_dateFormat4(String date, int minute) {
        try {
            Date tmp = dateFormat4.parse(date);
            Calendar cal = Calendar.getInstance(Locale.US);
            cal.setTime(tmp);
            cal.add(Calendar.MINUTE, minute);
            Date pMinute = cal.getTime();
            return dateTimeFormatMM88.format(pMinute);
        } catch (ParseException p) {
            return date;
        }
    }

    public static String plusMinute(String date, int minute) {
        try {
            Date tmp = dateTimeFormatMM88.parse(date);
            Calendar cal = Calendar.getInstance(Locale.US);
            cal.setTime(tmp);
            cal.add(Calendar.MINUTE, minute);
            Date pMinute = cal.getTime();
            return dateTimeFormatMM88.format(pMinute);
        } catch (ParseException p) {
            return date;
        }
    }

    public static String plusDay_dateFormat4(String date, int day) {
        try {
            Date tmp = dateFormat4.parse(date);
            Calendar cal = Calendar.getInstance(Locale.US);
            cal.setTime(tmp);
            cal.add(Calendar.DATE, day);
            Date pMinute = cal.getTime();
            return dateFormat4.format(pMinute);
        } catch (ParseException p) {
            return date;
        }
    }

    public static boolean isNumberic(String text) {
        try {
            return text.matches("-?\\d+(\\.\\d+)?");
        } catch (Exception p) {
            return false;
        }
    }

    public static boolean isEmpty(String txt) {
        if (txt != null) {
            if (!txt.trim().equals("")) {
                return false;
            }
        }
        return true;
    }

}
