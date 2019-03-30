/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mm.service;

import com.google.gson.Gson;

import co.mm.cookie.CookieHashSet;
import co.mm.cookie.DefaultCookieJar;
import co.mm.cookie.UserAgentInterceptor;
import co.mm.bean.Statement;
import co.mm.constant.BankConstant;
import co.mm.util.FormatUtil;
import okhttp3.*;
import okhttp3.FormBody.Builder;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author tanad
 */
public class SCBService {

    private static DefaultCookieJar cookieJar = new DefaultCookieJar(new CookieHashSet());
    static OkHttpClient httpClient;

    protected Logger log = Logger.getLogger(this.getClass().getName());
    private final int MAX_LOGIN = 3;
    public Gson gson;
    private String SESSIONEASY;
    private String accountNo = "797679";
    private String __EVENTTARGET;
    private Map<String, String> PARAM;
    private String dateTime;
    Document document;
    private String in;
    private String username;
    private String password;
    ArrayList<Statement> statements;
    UserInfo userInfo;

    public SCBService(String dateTime, String in) {

        statements = null;
        this.in = in;
        this.statements = new ArrayList<Statement>();
        this.dateTime = dateTime;
        this.httpClient = new OkHttpClient().newBuilder()
                .addInterceptor(new UserAgentInterceptor("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36"))
                .cookieJar(cookieJar)
                .build();
        this.gson = new Gson();

    }

    public boolean decode(String key, String username, String password) throws IOException {

        RequestBody formBody = new FormBody.Builder()
                .add("key", key)
                .add("username", username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(BankConstant.API_DECODE_SCB)
                .post(formBody)
                .build();

        Response response = this.httpClient.newCall(request).execute();
        String json = response.body().string();
        response.body().close();

        if (response.code() == 200) {

            this.userInfo = new Gson().fromJson(json, UserInfo.class);
            this.username = this.userInfo.getUsername();
            this.password = this.userInfo.getPassword();
//            log.info(this.username);
//            log.info(this.password);
        }

        return true;
    }

    public boolean setParam(String json) {

        PARAM = null;
        PARAM = new HashMap<String, String>();
        document = Jsoup.parse(json);
        Element form1 = document.getElementById("form1");
        Elements allInput = form1.getElementsByTag("input");
        for (Element element : allInput) {
            PARAM.put(element.id(), element.val());
        }

        return true;
    }

    public void basePageScb() throws IOException {
        Request request = new Request.Builder()
                .url("https://www.scbeasy.com")
                .build();

        Response response = this.httpClient.newCall(request).execute();
        response.body().close();
    }

    public boolean loginScb() throws IOException {
        if (username == null || password == null) {
//            log.info("username == null || password == null");
        }
        basePageScb();
        RequestBody formBody = new FormBody.Builder()
                .add("LOGIN", username)
                .add("PASSWD", password)
                .add("LANG", "T")
                .build();

        Request request = new Request.Builder()
                .url("https://www.scbeasy.com/online/easynet/page/lgn/login.aspx")
                .header("Referer", "https://www.scbeasy.com/v1.4/site/presignon/index.asp")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody)
                .build();

        Response response = this.httpClient.newCall(request).execute();
        String json = response.body().string();
        response.body().close();
//        log.info("login.aspx : response code " + response.code());

        SESSIONEASY = "";
        if (response.code() == 200) {

            document = Jsoup.parse(json);
            Elements eleSESSIONEASY = document.getElementsByAttributeValueContaining("name", "SESSIONEASY");
            for (Element element : eleSESSIONEASY) {
                SESSIONEASY = element.val();
            }

//            log.info("SESSIONEASY : " + SESSIONEASY);
        }

        boolean status = true;

        if (StringUtil.isBlank(SESSIONEASY)) {
            status = false;
        }
        return status;
    }

    public boolean postAccMpg() throws IOException {

        RequestBody formBody = new FormBody.Builder()
                .add("SESSIONEASY", SESSIONEASY)
                .build();

        Request request = new Request.Builder()
                .url("https://www.scbeasy.com/online/easynet/page/acc/acc_mpg.aspx")
                .post(formBody)
                .build();

        Response response = this.httpClient.newCall(request).execute();
        String json = response.body().string();
        response.body().close();
//        log.info("acc_mpg.aspx : response code " + response.code());

        if (response.code() == 200) {

            setParam(json);
            document = Jsoup.parse(json);

            Elements accountList = document.getElementsByAttributeValueContaining("id", "DataProcess_SaCaGridView_SaCa_LinkButton_");
            for (Element element : accountList) {
                if (element.text().substring(element.text().length() - 6, element.text().length()).equals(accountNo)) {
                    PARAM.put(__EVENTTARGET, element.attr("href").replace("javascript:__doPostBack('", "").replace("','')", ""));
                }
            }
        }

        return true;
    }

    public boolean postSelectAccMpg(boolean tabToday, boolean tabYesterday) throws IOException {

        Builder builder = new FormBody.Builder();

        if (PARAM != null) {
            for (String key : PARAM.keySet()) {
                if (key != null && PARAM.get(key) != null) {
                    builder.add(key, PARAM.get(key));
                }
            }
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url("https://www.scbeasy.com/online/easynet/page/acc/acc_bnk_bln.aspx")
                .post(formBody)
                .build();

        Response response = this.httpClient.newCall(request).execute();
        String json = response.body().string();
        response.body().close();
//        log.info("select : postSelectAccMpg : response code " + response.code());

        if (response.code() == 200) {

            setParam(json);
            document = Jsoup.parse(json);

            Element Link2 = document.getElementById("DataProcess_Link2");
            Element Link3 = document.getElementById("DataProcess_Link3");
            if (Link2 != null && tabToday) {
                PARAM.put(__EVENTTARGET, Link2.attr("href").replace("javascript:__doPostBack('", "").replace("','')", ""));
            } else if (Link2 != null && tabYesterday) {
                PARAM.put(__EVENTTARGET, Link3.attr("href").replace("javascript:__doPostBack('", "").replace("','')", ""));
            }
        }
        return true;
    }

    public boolean postToLink2WithSelectAccMpg() throws IOException {

        Builder builder = new FormBody.Builder();

        if (PARAM != null) {
            for (String key : PARAM.keySet()) {
                if (key != null && PARAM.get(key) != null) {
                    builder.add(key, PARAM.get(key));
                }
            }
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url("https://www.scbeasy.com/online/easynet/page/acc/acc_bnk_bln.aspx")
                .post(formBody)
                .build();

        Response response = this.httpClient.newCall(request).execute();
        String json = response.body().string();
        response.body().close();
//        log.info("select : postToLink2WithSelectAccMpg : response code " + response.code());

        if (response.code() == 200) {

            setParam(json);
        }
        return true;
    }

    public boolean postAccBnkTst() throws IOException {

        Builder builder = new FormBody.Builder();

        if (PARAM != null) {
            for (String key : PARAM.keySet()) {
                if (key != null && PARAM.get(key) != null) {
                    builder.add(key, PARAM.get(key));
                }
            }
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url("https://www.scbeasy.com/online/easynet/page/acc/acc_bnk_tst.aspx")
                .post(formBody)
                .build();

        Response response = this.httpClient.newCall(request).execute();
        String json = response.body().string();
        response.body().close();
//        log.info("select : postSelectAccMpg : response code " + response.code());

        if (response.code() == 200) {

            setParam(json);
            document = Jsoup.parse(json);

            Element dataTable = document.getElementById("DataProcess_GridView");

            if (dataTable != null) {
//                log.info(dataTable.text());
                Elements trs = dataTable.getElementsByTag("tr");
                if (trs != null) {
                    for (Element elementTr : trs) {
                        Elements tds = elementTr.getElementsByTag("td");

                        if (tds != null && tds.size() > 0 && !tds.get(0).text().trim().equals("รวม")) {

                            Statement statement = new Statement();
                            String dateTime = tds.get(0).text() + " " + tds.get(1).text();
                            statement.setDateTime(dateTime);
                            statement.setOut(tds.get(4).text().replace("-", ""));
                            statement.setIn(tds.get(5).text().replace("+", ""));
                            statement.setDetailAccount(tds.get(6).text().substring(tds.get(6).text().length() - 6, tds.get(6).text().length()));
                            statements.add(statement);
//                            log.info(statement.toString());

                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean postAccBnkHst() throws IOException {

        Builder builder = new FormBody.Builder();

        if (PARAM != null) {
            for (String key : PARAM.keySet()) {
                if (key != null && PARAM.get(key) != null) {
                    builder.add(key, PARAM.get(key));
                }
            }
        }

        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url("https://www.scbeasy.com/online/easynet/page/acc/acc_bnk_hst.aspx")
                .post(formBody)
                .build();

        Response response = this.httpClient.newCall(request).execute();
        String json = response.body().string();
        response.body().close();
//        log.info("select : postSelectAccMpg : response code " + response.code());

        if (response.code() == 200) {
            setParam(json);
            document = Jsoup.parse(json);
            PARAM.remove("__EVENTTARGET");
            PARAM.remove("DataProcess_btnBack");
            PARAM.remove("DataProcess_btnPrint");
            PARAM.put("__EVENTTARGET", "ctl00$DataProcess$ddlMonth");
            PARAM.put("ctl00$DataProcess$ddlMonth", FormatUtil.plusDayGetMMYYYY(this.dateTime, -1));
        }
        return true;
    }

    public boolean postSelectMonthAccBnkHst() throws IOException {

        Builder builder = new FormBody.Builder();
//        log.info(PARAM.toString());

        if (PARAM != null) {
            for (String key : PARAM.keySet()) {
                if (key != null && PARAM.get(key) != null) {
                    builder.add(key, PARAM.get(key));
                }
            }
        }

        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url("https://www.scbeasy.com/online/easynet/page/acc/acc_bnk_hst.aspx")
                .post(formBody)
                .build();

        Response response = this.httpClient.newCall(request).execute();
        String json = response.body().string();
        response.body().close();
//        log.info("select : postSelectAccMpg : response code " + response.code());

        if (response.code() == 200) {

            setParam(json);
            document = Jsoup.parse(json);

            Element dataTable = document.getElementById("DataProcess_GridView");
            if (dataTable != null) {
//                log.info(dataTable.text());
                Elements trs = dataTable.getElementsByTag("tr");
                if (trs != null) {
                    for (Element elementTr : trs) {
                        Elements tds = elementTr.getElementsByTag("td");
                        if (tds != null && tds.size() > 0 && !tds.get(0).text().trim().equals("รวม")) {
                            String dateTime = tds.get(0).text() + " " + tds.get(1).text();
                            if (FormatUtil.setDateTime(FormatUtil.plusDay(this.dateTime, -1)).before((FormatUtil.setDateTime(dateTime)))) {
                                Statement statement = new Statement();
                                statement.setDateTime(dateTime);
                                statement.setOut(tds.get(6).text().replace("-", ""));
                                statement.setIn(tds.get(7).text().replace("+", ""));
                                statement.setDetailAccount(tds.get(4).text().substring(tds.get(4).text().length() - 6, tds.get(4).text().length()));
                                statements.add(statement);
//                                log.info(statement.toString());
                            }

                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean postToLink2WithSelectAccBnkTst() throws IOException {

        Builder builder = new FormBody.Builder();

        if (PARAM != null) {
            for (String key : PARAM.keySet()) {
                if (key != null && PARAM.get(key) != null) {
                    builder.add(key, PARAM.get(key));
                }
            }
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url("https://www.scbeasy.com/online/easynet/page/acc/acc_bnk_tst.aspx")
                .post(formBody)
                .build();

        Response response = this.httpClient.newCall(request).execute();
        String json = response.body().string();
        response.body().close();
//        log.info("select : postToLink2WithSelectAccMpg : response code " + response.code());

        if (response.code() == 200) {

            setParam(json);
        }
        return true;
    }

    public String findAccountNo() {
        
        String accountNo = "", tomorowDate = "";
        boolean validate1, validate2, validate3, validate4 , validate5;
        
        if (this.statements != null) {
            for (Statement statement : statements) {
                if (!StringUtil.isBlank(statement.getIn())) {
//                    log.info("IN:" + (this.in.equals(statement.getIn())));
                    validate1 = FormatUtil.setDateTime(FormatUtil.plusMinute_dateFormat4(this.dateTime, -1)).before((FormatUtil.setDateTime(statement.getDateTime())));
                    validate2 = FormatUtil.setDateTime(FormatUtil.plusMinute_dateFormat4(this.dateTime, 1)).after((FormatUtil.setDateTime(statement.getDateTime())));
                    validate3 = this.in.trim().equals(statement.getIn().trim());
                    
                    tomorowDate = FormatUtil.plusDay_dateFormat4(this.dateTime, 1);
                    validate4 = FormatUtil.setDateTime(FormatUtil.plusMinute_dateFormat4(tomorowDate, -1)).before((FormatUtil.setDateTime(statement.getDateTime())));
                    validate5 = FormatUtil.setDateTime(FormatUtil.plusMinute_dateFormat4(tomorowDate, 1)).after((FormatUtil.setDateTime(statement.getDateTime())));
                    
                    //System.out.println(validate1 + ":" + validate2 + ":" + validate3 + ":" +validate4+ ":" + this.in + ":" + statement.getIn());
                    
                    if (validate1 && validate2 && validate3) {
                        accountNo = statement.getDetailAccount();
                    } else if (validate4 && validate5 && validate3) {
                        accountNo = statement.getDetailAccount();
                    }
                }

            }
        }
        return accountNo;
    }

    public class UserInfo {

        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }

}
