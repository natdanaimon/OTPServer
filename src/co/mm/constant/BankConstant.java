package co.mm.constant;

import co.mm.util.PropertiesConfig;

public class BankConstant {

    public static String SYSTEM = PropertiesConfig.getConfig("bot.system");
    public static String API_DECODE_SCB = PropertiesConfig.getConfig("api.decode.scb");

    public static String CODE_SUCCESS = "0000";
    public static String PREFIX_BANK_FUCN = "InquiryTransaction";
    public static String PREFIX_BANK_DOMAIN = "www.77up.bet";
    public static String PREFIX_BANK_LICENSE = "77up";

    public static int MAX_ERROR = 20;
    public static int SLEEP = 1000;
    public static int SLEEP_3SEC = 3000;
    public static int SLEEP_10SEC = 10000;
    public static int SLEEP_30SEC = 30 * 1000;
    public static int SLEEP_WAIT_RELOAD_PAGE = 10 * 1000;
    public static String STEP_LOGIN = "LOGIN";
    public static String STEP_MAIN_PAGE = "MAIN_PAGE";
    public static String STEP_DEPOSIT_LOAD_PAGE = "DEPOSIT_LOAD_PAGE";
    public static String STEP_DEPOSIT_APPROVE_CREDIT = "DEPOSIT_APPROVE_CREDIT";
    public static String LOG_SEPARATE_SYMBOL = "|";

    public static String BANK_SCB = "SCB";
    public static String BANK_KBANK = "KBANK";
    public static String BANK_BAY = "BAY";
    public static String BANK_BBL = "BBL";
    public static String BANK_TMB = "TMB";

    public static String SENDER_BANK_KBANK = "KBANK";
    public static String SENDER_BANK_SCB = "027777777";

    public static String INDEX_KBANK = "2";
    public static String COMMON_MESSAGE_REF_NO = "Ref=";
    public static String COMMON_MESSAGE_OTP = "OTP=";

    public static int KBANK_INDEX_AMOUNT = 3;
    public static int SCB_INDEX_AMOUNT = 1;

    public static String BANK_SCB_INDEX_KEY = "1";
    public static String BANK_KBANK_INDEX_KEY = "2";

    public static String CHANEL_ATM = "ATM";

}
