package com.example.zsk.scrollpicture.util;

import android.widget.Toast;

import com.example.zsk.scrollpicture.Config;


/**
 * Created by tk on 2017/7/27.
 */

public class ToastUtil {
    /**
     * 显示短Toast消息
     *
     * @param msg
     */
    public static void showToast(String msg) {
        Toast.makeText(Config.context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示长Toast消息
     *
     * @param msg
     */
    public static void showLongToast(String msg) {
        Toast.makeText(Config.context, msg, Toast.LENGTH_LONG).show();
    }
}
