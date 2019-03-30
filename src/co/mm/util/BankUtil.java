/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mm.util;

import co.mm.bean.BankBean;
import co.mm.bean.BankSaveBean;
import co.mm.constant.BankConstant;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;

/**
 *
 * @author 
 */
public class BankUtil {

    public static JSONObject PostToAPI(String BANK_URL, String urlencoded) {
        JSONObject jsonObj = null;
        System.out.println("        Url : " + BANK_URL);
        LogUtil.getLogService().info("PostToAPI : Url : " + BANK_URL);
        System.out.println("        Req Datetime: " + FormatUtil.getCurrentDatetime());
        LogUtil.getLogService().info("PostToAPI : Req Datetime: " + FormatUtil.getCurrentDatetime());
        try {

            Builder b = new Builder();
            b.readTimeout(45, TimeUnit.SECONDS);
            b.writeTimeout(45, TimeUnit.SECONDS);
            OkHttpClient client = b.build();
//            OkHttpClient client = new OkHttpClient();
            okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, urlencoded);

            Request request = new Request.Builder()
                    .url(BANK_URL)
                    .post(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    .build();
            okhttp3.Response response = client.newCall(request).execute();
            System.out.println("        Res Datetime: " + FormatUtil.getCurrentDatetime());
            LogUtil.getLogService().info("PostToAPI : Res Datetime: " + FormatUtil.getCurrentDatetime());
            jsonObj = new JSONObject(response.body().string());
        } catch (JSONException je) {
            LogUtil.getLogService().error("PostToAPI : JSONException -> " + je);
            System.out.println("PostToAPI : JSONException e : " + je);
        } catch (IOException ex) {
            LogUtil.getLogService().error("PostToAPI : IOException -> " + ex);
            System.out.println("PostToAPI : IOException e : " + ex);
        }
        return jsonObj;
    }

    public static String UrlFromJson(BankBean b) {
        String urlencoded = "";
        try {
            JSONObject json = new JSONObject();
            json.put("func", b.getFunc());
            json.put("key", b.getKey());
            json.put("username", b.getUsername());
            json.put("password", b.getPassword());
            json.put("account", b.getAccount());
            json.put("d_start", b.getD_start());
            json.put("d_end", b.getD_end());
            json.put("domain", b.getDomain());
            json.put("license", b.getLicense());
            for (int i = 0; i < json.names().length(); i++) {
                urlencoded += json.names().getString(i) + "=" + URLEncoder.encode(json.get(json.names().getString(i)).toString(), "UTF-8");
                if (i != json.names().length() - 1) {
                    urlencoded += "&";
                }
            }
        } catch (JSONException je) {
            LogUtil.getLogService().error("UrlFromJson : JSONException -> " + je);
        } catch (UnsupportedEncodingException uje) {
            LogUtil.getLogService().error("UrlFromJson : UnsupportedEncodingException -> " + uje);
        }
        return urlencoded;
    }

    public static boolean isSuccess(JSONObject obj) {
        boolean isSuccess = false;

        try {
            if (obj.get("code").equals(BankConstant.CODE_SUCCESS)) {
                isSuccess = true;
            }
        } catch (Exception e) {
            LogUtil.getLogService().error("isSuccess : Exception -> " + e);
        }

        return isSuccess;
    }

    public static List<BankSaveBean> convertJSONToBankSaveBean(BankBean b, JSONObject obj) {
        List<BankSaveBean> list = new ArrayList<BankSaveBean>();
        BankSaveBean save = new BankSaveBean();
        if (obj != null) {
            try {
                if (obj.getJSONArray("transaction") != null) {
                    int size = obj.getJSONArray("transaction").length();
                    for (int i = 0; i < size; i++) {
                        JSONObject d = new JSONObject(obj.getJSONArray("transaction").get(i).toString());
                        save = new BankSaveBean();
                        save.setSystem(BankConstant.SYSTEM);
                        save.setBankAgentCode(b.getBankCode());
                        save.setBankAgentName(b.getBankAgentName());
                        save.setBankAgentAccountNo(b.getAccount().replaceAll("-", ""));
                        save.setDateTime(setDatetime(d.getString("datetime"), b.getBankName()));
                        save.setIn(FormatUtil.formatAmount(String.valueOf(d.getDouble("in"))));
                        save.setOut(FormatUtil.formatAmount(String.valueOf(d.getDouble("out"))));
                        save.setRefNo(setRefNo(d, b.getBankName()));
                        save.setInfo(setInfo(d, b.getBankName()));
                        save.setChanel(setChanel(d, b.getBankName()));
                        list.add(save);
                    }

                }
                LogUtil.getLogService().info("convertJSONToBankSaveBean : Status Code: " + obj.getString("code"));
                System.out.println("        Status Code: " + obj.getString("code"));
            } catch (JSONException je) {
                LogUtil.getLogService().error("convertJSONToBankSaveBean : JSONException -> " + je);
                System.out.println("        JSONException: " + je);
            }

        } else {
            LogUtil.getLogService().info("convertJSONToBankSaveBean : Status: JSONObject Fail");
            System.out.println("        Status: JSONObject Fail");
        }

        LogUtil.getLogService().info("convertJSONToBankSaveBean : List Size Convert to Bean:" + list.size());
        System.out.println("        List Size Convert to Bean:" + list.size());
        return list;
    }

    public static String convertJSONToBankAmount(BankBean b, JSONObject obj) {
        String amount = "0.00";
        String pattern = "0.00";
        DecimalFormat df = new DecimalFormat(pattern);
        if (obj != null) {
            try {
                if (obj.getJSONArray("total") != null) {
                    int index = 0;
                    if (b.getBankName().equals(BankConstant.BANK_KBANK)) {
                        index = BankConstant.KBANK_INDEX_AMOUNT;
                    } else if (b.getBankName().equals(BankConstant.BANK_SCB)) {
                        index = BankConstant.SCB_INDEX_AMOUNT;
                    }
                    JSONObject d = new JSONObject(obj.getJSONArray("total").get(index).toString());
                    String tmp = d.getString("value");
                    tmp = tmp.replace("+", "");
                    tmp = tmp.replace("-", "");
                    tmp = tmp.replace(",", "");
                    if (tmp != null) {
                        LogUtil.getLogService().info("convertJSONToBankAmount : convertJSONToBankAmount -> " + tmp);
                    }
                    if (tmp.equals("*")) {
                        amount = tmp;
                    } else {
                        amount = df.format(Double.parseDouble(tmp));
                    }

                }
            } catch (JSONException je) {
                System.out.println("        JSONException: " + je);
                LogUtil.getLogService().error("convertJSONToBankAmount : JSONException -> " + je);
            }

        } else {
            System.out.println("        Status: JSONObject Fail");
            LogUtil.getLogService().info("convertJSONToBankAmount : Status: JSONObject Fail ");
        }

        return amount;
    }

    public static String setDatetime(String datetime, String BANK) {

        String tmp = "";
        tmp = datetime.trim();
        String date = "";
        String time = "";

        if (BankConstant.BANK_KBANK.equals(BANK)) {
            date = tmp.substring(0, 10);
            //time = tmp.substring(tmp.length() - 8, tmp.length() - 3);
            time = tmp.substring(tmp.length() - 8, tmp.length());
            date = date.replaceAll("/", "-");
            tmp = date + " " + time;
        } else if (BankConstant.BANK_SCB.equals(BANK)) {
            date = tmp;
            date = date.replaceAll("/", "-");
            tmp = date;
        }

        return tmp;

    }

    public static String setRefNo(JSONObject j, String BANK) {
        String tmp = "";
        try {
            if (BankConstant.BANK_KBANK.equals(BANK)) {
                if (j.get("bankno") != null) {
                    if (j.get("bankno").toString().length() > 7) {
                        tmp = j.get("bankno").toString();
                        tmp = findNumber(tmp);
                    } else {
                        tmp = j.get("bankno").toString();
                    }

                }
            } else if (BankConstant.BANK_SCB.equals(BANK)) {
                if (j.get("info") != null) {
                    if (j.get("info").toString().length() > 7) {
                        tmp = j.get("info").toString();
                        tmp = findNumber(tmp);
                    } else {
                        tmp = j.get("info").toString();
                    }
                }
            }
        } catch (JSONException je) {
        }

        return tmp;
    }

    public static String setInfo(JSONObject j, String BANK) {
        String tmp = "";
        try {
            if (BankConstant.BANK_KBANK.equals(BANK)) {
                if (j.get("bankno") != null) {
//                    tmp = j.get("bankno").toString();
                    tmp = j.get("info").toString();
                }
            } else if (BankConstant.BANK_SCB.equals(BANK)) {
                if (j.get("info") != null) {
                    tmp = j.get("info").toString();
                }
            }
        } catch (JSONException je) {
        }

        return tmp;
    }

    public static String setChanel(JSONObject j, String BANK) {
        String tmp = "";
        try {
            if (BankConstant.BANK_KBANK.equals(BANK)) {
//                if (j.get("channel") != null) {
//                    tmp = j.get("channel").toString();
//                }
            } else if (BankConstant.BANK_SCB.equals(BANK)) {
                if (j.get("channel") != null) {
                    //show chanel atm scb only  -> 2018-09-18 case +1 minute SCB
                    tmp = j.get("channel").toString().trim();
//                    tmp = (tmp.equals(BankConstant.CHANEL_ATM) ? BankConstant.CHANEL_ATM : "");
                }
            }
        } catch (JSONException je) {
        }

        return tmp;
    }

    public static String findNumber(String txt) {
        String tmp = "";
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(txt);
        while (m.find()) {
            tmp += m.group();
        }

        return tmp;
    }

}
