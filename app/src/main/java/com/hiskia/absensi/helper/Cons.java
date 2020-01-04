package com.hiskia.absensi.helper;

/**
 * Created by crocodic on 3/5/18.
 */

public class Cons {

    public static String TAG = "Absensi";

    public final static String URL_BASE = "http://178.128.177.86/siabsensi/public/api/";
//    public final static String URL_BASE = "http://crocodic.net/aplikasi-gudang/public/api/";

    public static Integer SPLASH_TIME_OUT = 3000;
    public static String PREF_EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static String IMAGE_DIRECTORY_NAME = "Siabsensi";

    public static String PARAM_HEADER_TOKEN = "X-Authorization-Token";
    public static String PARAM_HEADER_TIME = "X-Authorization-Time";
    public static String PARAM_HEADER = "Referer";

    public static int INT_STATUS = 1;
    public static int PRIVATE_MODE = 0;
    public static String API_MESSAGE = "api_message";
    public static String HEAD_API_MESSAGE = "api_message_head";
    public static String API_STATUS = "api_status";

    public final static String PREF_NAME = TAG;
    public static final int REQ_INTENT_GENERAL = 1;

    public static final String PREF_LOGIN = "login";
    public static final String PREF_CHECKED = "check_back";
    public static final String KEY_ID = "sid";

    public static final String BARCODE_VALUE = "barcode_value";
    public static final String DET_SCANNED = "detail_scanned";

    public static final String TYPE_OPNAME = "type";
    public static final String OPNAME_DRAF = "DRAFT";
    public static final String OPNAME_PUBLISH = "PUBLISH";
}
