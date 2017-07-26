package cn.edu.nju.cs.seg.schooledinapp;

import android.app.Application;
import android.content.Context;

import com.squareup.otto.Bus;

import java.util.HashSet;
import java.util.Set;

import cn.edu.nju.cs.seg.schooledinapp.model.OnlineUser;
import cn.edu.nju.cs.seg.schooledinapp.model.User;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.UsersService;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppContext extends Application {

    private static final String TAG = "AppContext";

    private static Context context;

    private static OnlineUser onlineUser;

    private static boolean onlineUserUpdated = false;

    private static Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();

        // context
        context = getApplicationContext();

        // Bus
        bus = new Bus();

        // 初始化极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

    }

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    public static Context getContext() {
        return context;
    }

    /**
     * 保存登录用户
     *
     * @param u 登录的用户
     * @return 成功登录返回 true；登录冲突返回 false
     */
    public static boolean signIn(OnlineUser u) {
        if (onlineUser != null) {
            return false;
        } else {
            onlineUser = u;
            return true;
        }
    }

    /**
     * 注销用户
     *
     * @return 成功注销返回 true
     */
    public static boolean signOut() {
        onlineUser = null;
        return true;
    }

    /**
     * 获得当前登陆的用户
     *
     * @return 当前登录的用户
     */
    public static OnlineUser getOnlineUser() {
        return onlineUser;
    }

    /**
     * 重新获取用户信息
     *
     * @return 更新是否成功
     */
    public static boolean updateOnlineUser() {
        UsersService usersService = ApiClient.getUsersService();
        usersService.fetchOne(onlineUser.getId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                switch (response.code()) {
                    case 200: {
                        User u = response.body();
                        updateOnlineUser(u);
                    } break;

                    default: {
                        onlineUserUpdated = false;
                    } break;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                onlineUserUpdated = false;
            }
        });
        return true;
    }

    /**
     * 查看用户信息是否更新
     *
     * @return 是否更新
     */
    public static boolean isOnlineUserUpdated() {
        return onlineUserUpdated;
    }

    /**
     * 清空更新信息
     *
     */
    public static void clearOnlineUserUpdated() {
        onlineUserUpdated = false;
    }

    /**
     * 向 JPush 服务器设置 别名 和 标签
     *
     */
    public static void bindAliasAndTags(String alias, Set<String> tags, TagAliasCallback callback) {
        JPushInterface.setAliasAndTags(
                AppContext.getContext(),
                alias,
                tags,
                callback);
    }

    /**
     * 向 JPush 服务器取消 别名 和 标签
     *
     */
    public static void unbindAliasAndTags(TagAliasCallback callback) {
        if (onlineUser != null) {
            JPushInterface.setAliasAndTags(
                    AppContext.getContext(),
                    "",
                    new HashSet<String>(),
                    callback);
        }
    }

    /**
     * 获取全局消息总线
     *
     * @return
     */
    public static Bus getBus() {
        return bus;
    }

    /**
     * 更新当前用户
     *
     * @param u
     */
    private static void updateOnlineUser(User u) {
        if (u.getAge() != onlineUser.getAge()) {
            onlineUser.setAge(u.getAge());
        }

        if (u.getAnswers() != onlineUser.getAnswers()) {
            onlineUser.setAnswers(u.getAnswers());
        }

        if (!u.getAvatarUrl().equals(onlineUser.getAvatarUrl())) {
            onlineUser.setAvatarUrl(u.getAvatarUrl());
        }

        if (!u.getBio().equals(onlineUser.getBio())) {
            onlineUser.setBio(u.getBio());
        }

        if (!u.getDepartment().equals(onlineUser.getDepartment())) {
            onlineUser.setDepartment(u.getDepartment());
        }

        if (!u.getEmail().equals(onlineUser.getEmail())) {
            onlineUser.setEmail(u.getEmail());
        }

        if (u.getFavoriteAnswers() != onlineUser.getFavoriteAnswers()) {
            onlineUser.setFavoriteAnswers(u.getFavoriteAnswers());
        }

        if (u.getFavoriteEssays() != onlineUser.getFavoriteEssays()) {
            onlineUser.setFavoriteEssays(u.getFavoriteEssays());
        }

        if (u.getFavoriteQuestions() != onlineUser.getFavoriteQuestions()) {
            onlineUser.setFavoriteQuestions(u.getFavoriteQuestions());
        }

        if (!u.getLocation().equals(onlineUser.getLocation())) {
            onlineUser.setLocation(u.getLocation());
        }

        if (!u.getName().equals(onlineUser.getName())) {
            onlineUser.setName(u.getName());
        }

        if (!u.getPhone().equals(onlineUser.getPhone())) {
            onlineUser.setPhone(u.getPhone());
        }

        if (!u.getSex().equals(onlineUser.getSex())) {
            onlineUser.setSex(u.getSex());
        }

        if (u.getStudios() != onlineUser.getStudios()) {
            onlineUser.setStudios(u.getStudios());
        }

        if (u.getQuestions() != onlineUser.getQuestions()) {
            onlineUser.setQuestions(u.getQuestions());
        }

        onlineUserUpdated = true;
    }

}