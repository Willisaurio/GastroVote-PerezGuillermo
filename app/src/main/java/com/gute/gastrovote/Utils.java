package com.gute.gastrovote;

import android.content.Context;
import android.widget.Toast;

public class Utils {

    public static void sayLong(Context c, String s){
        Toast.makeText(c, s, Toast.LENGTH_LONG).show();
    }
    public static void sayShort(Context c,String s){
        Toast.makeText(c, s, Toast.LENGTH_SHORT).show();
    }
}
