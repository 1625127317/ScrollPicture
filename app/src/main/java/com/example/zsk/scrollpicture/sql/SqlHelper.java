package com.example.zsk.scrollpicture.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZSK
 * @date 2018/3/29
 * @function
 */

public class SqlHelper {
    /**
     * 登录记录表
     */
    private static String SRCOLL_PICTURE_MAIN = "CREATE TABLE if not exists t_app_scroll_picture (\n" +
            "	scroll INT ,\n" +
            "	updatetime INT\n" +
            ")";

    public void createTables(SQLiteDatabase db) {
        db.execSQL(SRCOLL_PICTURE_MAIN);
    }

    private static String INSERT_SRCOLL_PICTURE = "INSERT INTO t_app_scroll_picture(scroll,updatetime) VALUES (?,?)";
    private static String DROP_SRCOLL_PICTURE = "delete  FROM t_app_scroll_picture";
    private static String GET_SCROLL_PICTURE = "SELECT * FROM t_app_scroll_picture";

    //插入数据
    public static void saveProcess(int scroll,int update) {
        List<Map<String, Object[]>> sqls = new ArrayList<Map<String, Object[]>>();
        Object[] param = new Object[2];
        param[0] = scroll;
        param[1] = update;
        Map<String, Object[]> sqli1 = new HashMap<String, Object[]>();
        Map<String, Object[]> sqli2 = new HashMap<String, Object[]>();
        sqli1.put(DROP_SRCOLL_PICTURE,null);
        sqli2.put(INSERT_SRCOLL_PICTURE,param);
        sqls.add(sqli1);
        sqls.add(sqli2);
        DatabaseHelper.execWriteSQLs(sqls);
    }

    public static int[] getProcess() {
        int[]process = new int[2];
        try {
            DatabaseHelper dh = new DatabaseHelper();
            SQLiteDatabase database = dh.getReadableDatabase();
            Cursor cursor = database.rawQuery(GET_SCROLL_PICTURE, null);
            if(cursor.moveToFirst()) {
                process[0] = cursor.getInt(cursor.getColumnIndex("scroll"));
                process[1] = cursor.getInt(cursor.getColumnIndex("updatetime"));
            }
            cursor.close();
            database.close();
            dh.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return process;
    }
}
