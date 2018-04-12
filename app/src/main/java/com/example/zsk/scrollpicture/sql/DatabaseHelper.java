package com.example.zsk.scrollpicture.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.zsk.scrollpicture.Config;

import java.util.List;
import java.util.Map;

/**
 * @author ZSK
 * @date 2018/3/29
 * @function
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String dbname = "scroll";
    private static int version = 1;
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        super(context, dbname, null, version);
    }

    public DatabaseHelper() {
        this(Config.context);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        SqlHelper sh = new SqlHelper();
        sh.createTables(db);
    }

    /**
     * 执行insert、update、delete  sql语句
     *
     * @param sql
     * @param param
     */
    public static void execWriteSQL(String sql, Object[] param) {
        try {
            if (databaseHelper == null) {
                synchronized (DatabaseHelper.class) {
                    databaseHelper = new DatabaseHelper();
                }
            }
            if (database == null) {
                synchronized (SQLiteDatabase.class) {
                    database = databaseHelper.getWritableDatabase();
                }
            }
            synchronized (database) {
                database = databaseHelper.getWritableDatabase();
                if (param == null) {
                    database.execSQL(sql);
                } else {
                    database.execSQL(sql, param);
                }
                database.close();
            }
        } catch (Exception e) {
            Log.e("WritableDatabase", "WritableDatabase执行sql时发生了错误！");
            Log.e("WritableDatabase", e.getMessage());
        }
    }

    /**
     * 批量执行insert、update、delete  sql语句
     * @param sqls
     */
    public static void execWriteSQLs(List<Map<String, Object[]>> sqls) {
        try {
            if (databaseHelper == null) {
                synchronized (DatabaseHelper.class) {
                    databaseHelper = new DatabaseHelper();
                }
            }
            if (database == null) {
                synchronized (SQLiteDatabase.class) {
                    database = databaseHelper.getWritableDatabase();
                }
            }
            synchronized (database) {
                database = databaseHelper.getWritableDatabase();
                try {
                    database.beginTransaction();
                    for (Map<String, Object[]> sql : sqls) {
                        for (Map.Entry<String, Object[]> e : sql.entrySet()) {
                            if (e.getValue() == null) {
                                database.execSQL(e.getKey());
                            } else {
                                database.execSQL(e.getKey(), e.getValue());
                            }
                        }
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    Log.e("execWriteSQLs", "批量执行SQL出错！");
                    Log.e("execWriteSQLs",e.getMessage());
                } finally {
                    database.endTransaction();
                }
                database.close();
            }
        } catch (Exception e) {
            Log.e("WritableDatabase", "WritableDatabase执行sql时发生了错误！");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
