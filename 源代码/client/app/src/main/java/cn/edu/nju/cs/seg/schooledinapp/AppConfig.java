package cn.edu.nju.cs.seg.schooledinapp;

/**
 * 全局配置类
 *
 */
public class AppConfig {

    // root path

    public static final String APP_ROOT_PATH = "schooledin/";

    // server config

    public static final int SERVER_BASE_PORT = 8443;

    public static final String SERVER_BASE_HOME = "https://120.24.19.11";

    public static final String SERVER_BASE_PATH = "/v2/api";

    public static final String SERVER_BASE_URL =
            SERVER_BASE_HOME + ":" + SERVER_BASE_PORT + SERVER_BASE_PATH + '/';

    public static final String SERVER_SELF_CERT_IN_STRING =
            "-----BEGIN CERTIFICATE-----\n" +
            "MIIDlDCCAnygAwIBAgIJANbuUMkN1QheMA0GCSqGSIb3DQEBBQUAMG4xCzAJBgNV\n" +
            "BAYTAkNOMRAwDgYDVQQIDAdVeGFtcGxlMRAwDgYDVQQHDAdWeGFtcGxlMRkwFwYD\n" +
            "VQQKDBBXeGFtcGxlIFRlY2ggSW5jMSAwHgYDVQQDDBdzY2hvb2xlZGluLnBzZXVk\n" +
            "b3NlcnZlcjAeFw0xNzA3MTQwMjE4MzVaFw0yNzA3MTIwMjE4MzVaMG4xCzAJBgNV\n" +
            "BAYTAkNOMRAwDgYDVQQIDAdVeGFtcGxlMRAwDgYDVQQHDAdWeGFtcGxlMRkwFwYD\n" +
            "VQQKDBBXeGFtcGxlIFRlY2ggSW5jMSAwHgYDVQQDDBdzY2hvb2xlZGluLnBzZXVk\n" +
            "b3NlcnZlcjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMFO6yUBXOLM\n" +
            "z/kAP2pxllSWYy+Bad7hSfeSf3niamNfNj2gsCPMvdMSzSFXWfMIF/zPCRgn+q9g\n" +
            "nZKXfsQ0Elc6ZxZCuREAs3E4Lu3k+lLj9eAAZq2/Ov4X2h3OiVxGoVohjD7JvcG+\n" +
            "ZMHYEGHrLDS9ZOmIICPudO7Y2l/FvdvcA9Tw1Yvh74HSmRGNCY3kMx/Fc+rQOa4s\n" +
            "IcUhdSwkf+GWxLERQzyZDlWBchIgGIPJWE6RqV/X4BUXaUE1Pfgvwece3WTtwMuD\n" +
            "nqX3VBF1zf8E86qV58MfcMZ7lxhiuoN804RzAIFAmo5Qu6iBl3hjJdwWOLRNUTvG\n" +
            "ifMTjohtjI8CAwEAAaM1MDMwEgYDVR0TAQH/BAgwBgEB/wIBADAdBgNVHREEFjAU\n" +
            "hwR4GBMLggwxMjAuMjQuMTkuMTEwDQYJKoZIhvcNAQEFBQADggEBACk4HcCzajNO\n" +
            "4Deh8Amm0lymbv+dqO6q3Ryk6RUByvdxkpVswBRQ/ZeMEBTFhREY+e60VEXFFf4G\n" +
            "MzbNrKQXzHeceKZISqQeLMK5nfHUhXOpyNz4TVBFzfCR1PsUO+cPZygRt6OeYV0f\n" +
            "i7CzmNXg8z9DGOoyoNcjyfvryR2OYtYyrX/gvEJPeo8vsXEV4Svc2KeiK6JDeUzm\n" +
            "gZCpzqMRBUZczP1CJpnxd3qOrMsLw4j9aCWeMw9tdm0TJMbGGEWsmMe4ncN3OQAH\n" +
            "W3WW0fjF+j7KyJiW87P/nclIILHPjARCHKd8MqwNeW934G8TizrfUIoWU3OAYVaH\n" +
            "+Uf4/LMN1sQ=\n" +
            "-----END CERTIFICATE-----";

    public static final String SERVER_KEYSTORE_PASSWORD = "cn.edu.nju.cs.seg.schooledinapp";

    // record config

    public static final String APP_RECORD_DIR = "record/";

    public static final String APP_RECORD_PATH = APP_ROOT_PATH + APP_RECORD_DIR;

    // network connection

    public static final int CONNECTION_TIME_OUT_IN_MILLISECONDS = 2000;

}