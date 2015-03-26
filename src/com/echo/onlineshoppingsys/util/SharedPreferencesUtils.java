package com.echo.onlineshoppingsys.util;

import com.echo.onlineshoppingsys.activity.MainActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtils {

    public static final String NAME = "onlineSystem";

    //保存上限金额到本地
    public static void saveTotalMoney(Context context, int total) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();

        editor.putInt("total", total);
        editor.commit();
        
        MainActivity.totalMoney = total;
    }

    public static void getTotalMoney(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME,
                Context.MODE_PRIVATE);

        MainActivity.totalMoney = sharedPreferences.getInt("total", 1000);
    }
}
