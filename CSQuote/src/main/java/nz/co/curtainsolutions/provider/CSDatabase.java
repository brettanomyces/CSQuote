package nz.co.curtainsolutions.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import nz.co.curtainsolutions.provider.CSContract.JobColumns;
import nz.co.curtainsolutions.provider.CSContract.RoomColumns;
import nz.co.curtainsolutions.provider.CSContract.WindowColumns;

/**
 * Created by brettyukich on 20/08/13.
 */
public class CSDatabase extends SQLiteOpenHelper {
    private static final String TAG = CSDatabase.class.getSimpleName();
    private static final String DB_NAME = "curtainsolutions.db";
    private static final int DB_VERSION = 2;

    public CSDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.JOBS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + JobColumns.CUSTOMER + " TEXT,"
                + "UNIQUE (" + BaseColumns._ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE " + Tables.ROOMS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RoomColumns.DESCRIPTION + " TEXT,"
                + RoomColumns.JOB_ID + " INTEGER NOT NULL,"
                + "UNIQUE (" + BaseColumns._ID + "," + RoomColumns.JOB_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE " + Tables.WINDOWS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + WindowColumns.JOB_ID + " INTERGER NOT NULL,"
                + WindowColumns.ROOM_ID + " INTEGER NOT NULL,"
                + WindowColumns.HEIGHT + " INTEGER,"
                + WindowColumns.WIDTH + " INTEGER,"
                + "UNIQUE (" + BaseColumns._ID + "," + WindowColumns.ROOM_ID + ") ON CONFLICT REPLACE)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade() from " + oldVersion + " to " + newVersion);

        if (oldVersion != DB_VERSION) {
            Log.w(TAG, "Destroying old data during upgrade");

            db.execSQL("DROP TABLE IF EXISTS " + Tables.JOBS);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.ROOMS);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.WINDOWS);

            onCreate(db);

        }

    }

    interface Tables {
        String JOBS = "jobs";
        String ROOMS = "rooms";
        String WINDOWS = "windows";

        // Define joins here
    }
}
