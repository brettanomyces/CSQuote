package nz.co.curtainsolutions.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import nz.co.curtainsolutions.provider.CSContract.JobColumns;
import nz.co.curtainsolutions.provider.CSContract.RoomColumns;
import nz.co.curtainsolutions.provider.CSContract.TrackColumns;
import nz.co.curtainsolutions.provider.CSContract.WindowColumns;

/**
 * Created by brettyukich on 20/08/13.
 */
public class CSDatabase extends SQLiteOpenHelper {
    private static final String TAG = CSDatabase.class.getSimpleName();
    private static final String DB_NAME = "curtainsolutions.db";
    private static final int DB_VERSION = 4;

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
                + WindowColumns.GROSS_HEIGHT + " DECIMAL,"
                + WindowColumns.INNER_HEIGHT + " DECIMAL,"
                + WindowColumns.GROSS_WIDTH + " DECIMAL,"
                + WindowColumns.INNER_WIDTH + " DECIMAL,"
                + WindowColumns.TRACK_WIDTH + " DECIMAL,"
                + WindowColumns.TRACK_ID + " INTEGER,"
                + "UNIQUE (" + BaseColumns._ID + "," + WindowColumns.ROOM_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE " + Tables.TRACKS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TrackColumns.MAX_WIDTH + " DECIMAL,"
                + TrackColumns.MIN_WIDTH + " DECIMAL,"
                + TrackColumns.DESCRIPTION + " TEXT, "
                + TrackColumns.PRICE + " DECIMAL"
                + ")"
        );

        seed(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade() from " + oldVersion + " to " + newVersion);

        if (oldVersion != DB_VERSION) {
            Log.w(TAG, "Destroying old data during upgrade");

            db.execSQL("DROP TABLE IF EXISTS " + Tables.JOBS);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.ROOMS);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.WINDOWS);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.TRACKS);

            onCreate(db);

        }

    }

    private void seed(SQLiteDatabase db) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(TrackColumns.MIN_WIDTH, 0);
        contentValues.put(TrackColumns.MAX_WIDTH, 1200);
        contentValues.put(TrackColumns.PRICE, 30);
        contentValues.put(TrackColumns.DESCRIPTION, "Size 1 (up to 1200)");
        db.insert(Tables.TRACKS, TrackColumns.DESCRIPTION, contentValues);

        contentValues.clear();
        contentValues.put(TrackColumns.MIN_WIDTH, 1200);
        contentValues.put(TrackColumns.MAX_WIDTH, 1700);
        contentValues.put(TrackColumns.PRICE, 38);
        contentValues.put(TrackColumns.DESCRIPTION, "Size 2 (1200 to 1700)");
        db.insert(Tables.TRACKS, TrackColumns.DESCRIPTION, contentValues);

        contentValues.clear();
        contentValues.put(TrackColumns.MIN_WIDTH, 1700);
        contentValues.put(TrackColumns.MAX_WIDTH, 2900);
        contentValues.put(TrackColumns.PRICE, 40);
        contentValues.put(TrackColumns.DESCRIPTION, "Size 3 (1700 to 2900)");
        db.insert(Tables.TRACKS, TrackColumns.DESCRIPTION, contentValues);

        contentValues.clear();
        contentValues.put(TrackColumns.MIN_WIDTH, 2900);
        contentValues.put(TrackColumns.MAX_WIDTH, 3800);
        contentValues.put(TrackColumns.PRICE, 45);
        contentValues.put(TrackColumns.DESCRIPTION, "Size 4 (2900 to 3800)");
        db.insert(Tables.TRACKS, TrackColumns.DESCRIPTION, contentValues);

        contentValues.clear();
        contentValues.put(TrackColumns.MIN_WIDTH, 3800);
        contentValues.put(TrackColumns.MAX_WIDTH, 4700);
        contentValues.put(TrackColumns.PRICE, 55);
        contentValues.put(TrackColumns.DESCRIPTION, "Size 5 (3800 to 4700)");
        db.insert(Tables.TRACKS, TrackColumns.DESCRIPTION, contentValues);
    }

    interface Tables {
        String JOBS = "jobs";
        String ROOMS = "rooms";
        String WINDOWS = "windows";
        String TRACKS = "tracks";

        // Define joins here
    }
}
