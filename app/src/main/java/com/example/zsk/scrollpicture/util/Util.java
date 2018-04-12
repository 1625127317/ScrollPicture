package com.example.zsk.scrollpicture.util;

import android.util.DisplayMetrics;

import com.example.zsk.scrollpicture.Config;

/**
 * Created by tk on 2017/6/26.
 */

public class Util {
    private static Screen screen;

    public static Screen getScreen() {
        if (screen == null) {
            DisplayMetrics metrics = Config.context.getResources().getDisplayMetrics();
            screen = new Screen();
            screen.width = metrics.widthPixels;
            screen.height = metrics.heightPixels;
            screen.density = metrics.density;
            screen.dpi = metrics.densityDpi;
        }
        return screen;
    }
    public static class Screen {
        public int width;
        public int height;
        public float density;
        public int dpi;
    }

    /**
     * dp转为px
     *
     * @param dp
     * @return
     */
    public static int dip2px(float dp) {
        final float scale = getScreen().density;
        return (int) (dp * scale + 0.5f);
    }
}
