package com.sxds.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

/**
 * 语言工具类
 * Created by littlejie on 2017/5/17.
 */

public class LanguageUtil {
    private static LanguageUtil instance = new LanguageUtil();

    public static final String TAG = LanguageUtil.class.getSimpleName();
    private static final String LANGUAGE = "Language";
    private static final String COUNTRY = "country";
    private static final String SCRIPT = "script";

    private BroadcastReceiver receiver = new LocaleChangeReceiver();
    private LanguageChangeReceiver languageChangeReceiver;
    private Context context;

    private LanguageUtil() {
    }

    public static LanguageUtil getInstance() {
        return instance;
    }

    public Context initAttachBaseContext(Context context, LanguageChangeReceiver languageChangeReceiver) {
        this.context = context;
        this.languageChangeReceiver = languageChangeReceiver;
        IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
        context.registerReceiver(receiver, filter);

        if (checkDiff(context)) {
            context = changeAppLanguage(context, getAppLocale(context));
        }
        return context;
    }

    public Context onAttachBaseContext(Context context) {
        if (checkDiff(context)) {
            context = changeAppLanguage(context, getAppLocale(context));
        }
        return context;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Context updateResources(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private Context updateResourcesLegacy(Context context,
                                          Locale locale) {
        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }

    public Context changeAppLanguage(Context context, Locale locale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, locale);
        }
        return updateResourcesLegacy(context, locale);
    }

    public void changeAppLanguageSetting(Activity activity, Class mainClass, Locale locale) {
        saveLocale(activity, locale);
        Intent intent = new Intent(activity, mainClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    class LocaleChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //改变系统语言会接收到 ACTION_LOCALE_CHANGED
            //改变应用内部语言不会收到 ACTION_LOCALE_CHANGED
            if (!Intent.ACTION_LOCALE_CHANGED.equals(intent.getAction())) {
                return;
            }

            //如果用户手动更改过应用语言设置，则收到系统语言切换广播时，需要保持语言不变
            if (checkDiff(context)) {
                changeAppLanguage(context, getAppLocale(context));
            } else {
                languageChangeReceiver.onReceive(context, intent);
            }
        }
    }

    private void saveLocale(Context context, Locale locale) {
        SharedPreferences preferences = context.getSharedPreferences(TAG,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LANGUAGE, locale.getLanguage());
        editor.putString(COUNTRY, locale.getCountry());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editor.putString(SCRIPT, locale.getScript());
        }
        editor.apply();
    }

    private Locale getAppLocale(Context context) {
        Locale locale = new Locale(getAppLanguage(context), getAppCountry(context));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new Locale.Builder()
                    .setLocale(locale)
                    .setScript(getAppScript(context))
                    .build();
        } else {
            return locale;
        }
    }

    public String getAppLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(TAG,
                Context.MODE_PRIVATE);
        return preferences.getString(LANGUAGE,
                Locale.getDefault().getLanguage());
    }

    private String getAppCountry(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(TAG,
                Context.MODE_PRIVATE);
        return preferences.getString(COUNTRY,
                Locale.getDefault().getCountry());
    }

    private String getAppScript(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(TAG,
                Context.MODE_PRIVATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return preferences.getString(SCRIPT,
                    Locale.getDefault().getScript());
        } else {
            return "";
        }
    }

    private boolean checkDiff(Context context) {
        Locale appLocale = getAppLocale(context);
        return !appLocale.equals(Locale.getDefault());

    }

    public interface LanguageChangeReceiver {
        void onReceive(Context context, Intent intent);
    }
}
