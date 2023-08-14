package com.travel.travellingbug.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedHelper {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    public static void putKey(Context context, String Key, String Value) {
        if(context != null){
            sharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString(Key, Value);
            editor.commit();
        }
    }

    public static String getKey(Context contextGetKey, String Key) {
        String Value= "";
        if(contextGetKey != null){
            sharedPreferences = contextGetKey.getSharedPreferences("Cache", Context.MODE_PRIVATE);
            Value = sharedPreferences.getString(Key, "");
        }
        return Value;

    }


    public static void clearSharedPreferences(Context context) {
        if(context != null){
            sharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE);
            sharedPreferences.edit().clear().commit();

        }
    }


}
