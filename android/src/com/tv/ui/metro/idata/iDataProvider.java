package com.tv.ui.metro.idata;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * local database save
 */
public class iDataProvider extends ContentProvider {
    private static final String     TAG              = "iDataProvider";
    public static final String      DATABASE_NAME    = "idata.db";
    public static final int         DATABASE_VERSION = 1;
    public static final String      AUTHORITY        = iDataORM.AUTHORITY;
    public static SQLiteOpenHelper mOpenHelper;

    private static final String     TABLE_SETTINGS   = "settings";
    private static final String     TABLE_Favor      = "favor";

    public static final String _ID   = "_id";
    public static final String NAME  = "name";
    public static final String VALUE = "value";

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL("CREATE TABLE " + TABLE_SETTINGS + " ("
                        + " _id   INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + " name  TEXT,"
                        + " value TEXT,"
                        + " application TEXT,"
                        + " date_time TEXT);");

                db.execSQL("CREATE TABLE " + TABLE_Favor + " ("
                        + " _id      INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + " res_id   TEXT, "
                        + " ns       TEXT,"
                        + " value    TEXT,"
                        + " date_time TEXT);");

            }catch (Exception ne){}
        }

        private void dropTables(SQLiteDatabase db) {
            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_Favor);
            }catch (Exception ne){}
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }



        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                try {
                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_Favor);
                }catch (Exception ne){}

                onCreate(db);
            } catch (Exception e) {
                dropTables(db);
                onCreate(db);
            }
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, final String selection,
                        final String[] selectionArgs, String sortOrder) {
        final SqlArguments args = new SqlArguments(uri, selection, selectionArgs);
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(args.table);

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor result = qb.query(db, projection, args.where, args.args,
                args.groupby, null, sortOrder);
        return result;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int count = 0;
        SqlArguments args = new SqlArguments(uri, selection, selectionArgs);

        //always update local database
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        count = db.update(args.table, values, args.where, args.args);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SqlArguments args = new SqlArguments(uri, selection, selectionArgs);
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count = db.delete(args.table, args.where, args.args);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public String getType(Uri uri) {
        SqlArguments args = new SqlArguments(uri, null, null);
        if (TextUtils.isEmpty(args.where)) {
            return "vnd.android.cursor.dir/" + args.table;
        } else {
            return "vnd.android.cursor.item/" + args.table;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SqlArguments args = new SqlArguments(uri);
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final long rowId = db.insert(args.table, null, values);
        if (rowId <= 0)
            return null;
        else {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        uri = ContentUris.withAppendedId(uri, rowId);

        return uri;
    }

    static class SqlArguments {
        public final String   table;
        public final String   where;
        public final String[] args;
        public String         groupby = null;

        SqlArguments(Uri url, String where, String[] args) {
            if (url.getPathSegments().size() == 1) {
                this.table = url.getPathSegments().get(0);
                this.where = where;
                this.args = args;
            } else if (url.getPathSegments().size() != 2) {
                throw new IllegalArgumentException("Invalid URI: " + url);
            } else if (!TextUtils.isEmpty(where)) {
                throw new UnsupportedOperationException(
                        "WHERE clause not supported: " + url);
            } else {
                this.table = url.getPathSegments().get(0);
                this.where = "_id=" + ContentUris.parseId(url);
                this.args = null;

            }
        }

        SqlArguments(Uri url) {
            if (url.getPathSegments().size() == 1) {
                table = url.getPathSegments().get(0);
                where = null;
                args = null;
            } else {
                throw new IllegalArgumentException("Invalid URI: " + url);
            }
        }
    }
}
