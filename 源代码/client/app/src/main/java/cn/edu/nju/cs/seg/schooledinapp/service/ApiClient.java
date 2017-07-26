package cn.edu.nju.cs.seg.schooledinapp.service;


import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import cn.edu.nju.cs.seg.schooledinapp.AppConfig;
import okhttp3.OkHttpClient;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * API 客户端
 *
 * 用于与服务器交互
 */
public class ApiClient {

    // API 基址
    private static final String BASE_URL = AppConfig.SERVER_BASE_URL;

    // retrofit 客户端
    private static Retrofit retrofitClient;

    // okhttp 客户端
    private static OkHttpClient okHttpClient;

    // 主页服务
    private static IndexService indexService;

    // Users 服务
    private static UsersService usersService;

    // Studios 服务
    private static StudiosService studiosService;

    // Questions 服务
    private static QuestionsService questionsService;

    // Comments 服务
    private static CommentsService commentsService;

    // Essays 服务
    private static EssaysService essaysService;

    // Answers 服务
    private static AnswersService answersService;

    // HTTPS 配置
    private static X509TrustManager trustManager;

    private static SSLContext sslContext;

    private static SSLSocketFactory sslSocketFactory;

    // 全局单例
    private ApiClient() { }

    /**
     * 获取全局 API 客户端单例
     *
     * @return
     */
    static Retrofit getRetrofitClient() {
        if (retrofitClient == null) {
            retrofitClient = createRetrofitClient();
        }
        return retrofitClient;
    }

    /**
     * 获取内部 OkHttpClient
     *
     * @return
     */
    static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = createOkHttpClient();
        }
        return okHttpClient;
    }

    /**
     * 获取 主页 API 服务
     *
     * @return 主页 API 服务
     */
    public static IndexService getIndexService() {
        if (indexService == null) {
            indexService = getRetrofitClient().create(IndexService.class);
        }
        return indexService;
    }

    /**
     * 获取 Users API 服务
     *
     * @return Users API 服务
     */
    public static UsersService getUsersService() {
        if (usersService == null) {
            usersService = getRetrofitClient().create(UsersService.class);
        }
        return usersService;
    }

    /**
     * 获取 Studios API 服务
     *
     * @return Studios API 服务
     */
    public static StudiosService getStudiosService() {
        if (studiosService == null) {
            studiosService = getRetrofitClient().create(StudiosService.class);
        }
        return studiosService;
    }

    /**
     * 获取 Questions API 服务
     *
     * @return Questions API 服务
     */
    public static QuestionsService getQuestionsService() {
        if (questionsService == null) {
            questionsService = getRetrofitClient().create(QuestionsService.class);
        }
        return questionsService;
    }

    /**
     * 获取 Comments API 服务
     *
     * @return Comments API 服务
     */
    public static CommentsService getCommentsService() {
        if (commentsService == null) {
            commentsService = getRetrofitClient().create(CommentsService.class);
        }
        return commentsService;
    }


    /**
     * 获取 Essay API 服务
     *
     * @return Essay API 服务
     */
    public static EssaysService getEssaysService() {
        if(essaysService == null) {
            essaysService = getRetrofitClient().create(EssaysService.class);
        }
        return essaysService;
    }

    /**
     * 获取 Answer API 服务
     *
     * @return Answer API 服务
     */
    public static AnswersService getAnswersService() {
        if(answersService == null) {
            answersService = getRetrofitClient().create(AnswersService.class);
        }
        return answersService;
    }


    /**
     * 创建 OkHttpClient
     *
     * @return
     */
    private static OkHttpClient createOkHttpClient() {
        try (InputStream inputStream = new Buffer()
                .writeUtf8(AppConfig.SERVER_SELF_CERT_IN_STRING)
                .inputStream()) {
            // 创建 CA
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Collection<? extends Certificate> certificates = certificateFactory
                    .generateCertificates(inputStream);
            if (certificates == null) {
                return null;
            }

            // 构建一个空的 KeyStore
            char[] password = AppConfig.SERVER_KEYSTORE_PASSWORD.toCharArray();
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, password);

            // 填充 KerStore
            int index = 0;
            for (Certificate certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificate);
            }

            // 构建 TrustManager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                    KeyManagerFactory.getDefaultAlgorithm()
            );
            keyManagerFactory.init(keyStore, password);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm()
            );
            trustManagerFactory.init(keyStore);

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                return null;
            }

            trustManager = (X509TrustManager) trustManagers[0];

            // SSLContext
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();

            // 构建 Client
            okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .connectTimeout(AppConfig.CONNECTION_TIME_OUT_IN_MILLISECONDS, TimeUnit.MILLISECONDS)
                    .build();

            return okHttpClient;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建 RetrofitClient
     *
     * @return
     */
    private static Retrofit createRetrofitClient() {
        retrofitClient = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build();
        return retrofitClient;
    }

}
