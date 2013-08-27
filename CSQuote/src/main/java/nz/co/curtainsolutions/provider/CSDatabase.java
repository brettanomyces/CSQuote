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
import nz.co.curtainsolutions.provider.CSContract.*;

/**
 * Created by brettyukich on 20/08/13.
 */
public class CSDatabase extends SQLiteOpenHelper {
    private static final String TAG = CSDatabase.class.getSimpleName();
    private static final String DB_NAME = "curtainsolutions.db";
    private static final int DB_VERSION = 11;

    public CSDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "creating database: " + DB_NAME);
        db.execSQL("CREATE TABLE " + Tables.JOBS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + JobColumns.CUSTOMER + " TEXT,"
                + JobColumns.PROPERTY_NUMBER + " TEXT,"
                + JobColumns.SITE_ADDRESS + " TEXT,"
                + JobColumns.TENANT + " TEXT,"
                + JobColumns.HOME_PHONE + " TEXT,"
                + JobColumns.WORK_PHONE + " TEXT,"
                + JobColumns.MOBILE_PHONE + " TEXT,"
                + JobColumns.NOTES + " TEXT,"
                + "UNIQUE (" + BaseColumns._ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE " + Tables.ROOMS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RoomColumns.DESCRIPTION + " TEXT,"
                + RoomColumns.NOTES + " TEXT,"
                + RoomColumns.JOB_ID + " INTEGER NOT NULL,"
                + "UNIQUE (" + BaseColumns._ID + "," + RoomColumns.JOB_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE " + Tables.WINDOWS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + WindowColumns.BLIND_ID + " INTEGER,"
                + WindowColumns.BLIND_PRICE + " DECIMAL,"
                + WindowColumns.CURTAIN_ID + " INTEGER,"
                + WindowColumns.CURTAIN_SEW + " DECIMAL,"
                + WindowColumns.CURTAIN_PRICE + " DECIMAL,"
                + WindowColumns.GROSS_HEIGHT + " DECIMAL,"
                + WindowColumns.GROSS_WIDTH + " DECIMAL,"
                + WindowColumns.HOOK_SIZE + " TEXT,"
                + WindowColumns.INNER_HEIGHT + " DECIMAL,"
                + WindowColumns.INNER_WIDTH + " DECIMAL,"
                + WindowColumns.JOB_ID + " INTEGER NOT NULL,"
                + WindowColumns.NET_ID + " INTEGER,"
                + WindowColumns.NET_PRICE + " DECIMAL,"
                + WindowColumns.NOTES + " TEXT,"
                + WindowColumns.ROOM_ID + " INTEGER NOT NULL,"
                + WindowColumns.TRACK_ID + " INTEGER,"
                + WindowColumns.TRACK_PRICE + " DECIMAL,"
                + WindowColumns.TRACK_WIDTH + " DECIMAL,"
                + WindowColumns.UNIT_PAIR + " INTEGER,"
                + "UNIQUE (" + BaseColumns._ID + "," + WindowColumns.ROOM_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE " + Tables.TRACKS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TrackColumns.MAX_WIDTH + " DECIMAL,"
                + TrackColumns.MIN_WIDTH + " DECIMAL,"
                + TrackColumns.DESCRIPTION + " TEXT, "
                + TrackColumns.PRICE + " DECIMAL"
                + ")"
        );

        db.execSQL("CREATE TABLE " + Tables.BLINDS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BlindColumns.DESCRIPTION + " TEXT, "
                + BlindColumns.PRICE + " DECIMAL"
                + ")"
        );

        db.execSQL("CREATE TABLE " + Tables.NETS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NetColumns.DESCRIPTION + " TEXT, "
                + NetColumns.PRICE + " DECIMAL"
                + ")"
        );

        db.execSQL("CREATE TABLE " + Tables.CURTAINS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CurtainColumns.DESCRIPTION + " TEXT, "
                + CurtainColumns.PRICE + " DECIMAL"
                + ")"
        );


        seed(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade() from " + oldVersion + " to " + newVersion);

        if (oldVersion != DB_VERSION) {
            Log.w(TAG, "Destroying old data during upgrade");

            db.execSQL("DROP TABLE IF EXISTS " + Tables.BLINDS);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.CURTAINS);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.JOBS);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.NETS);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.ROOMS);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.TRACKS);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.WINDOWS);

            onCreate(db);

        }

    }

    private void seed(SQLiteDatabase db) {
        Log.d(TAG, "Seeding database " + DB_NAME);
        seedTracks(db);
        seedNets(db);
    }

    private void seedNets(SQLiteDatabase db){
        Log.d(TAG, "Seeding Nets");
        ContentValues contentValues = new ContentValues();
        contentValues.put(NetColumns.DESCRIPTION, "N69");
        contentValues.put(NetColumns.PRICE, "2.40");
        db.insert(Tables.NETS, NetColumns.DESCRIPTION, contentValues);

        contentValues.clear();
        contentValues.put(NetColumns.DESCRIPTION, "N90");
        contentValues.put(NetColumns.PRICE, "3.70");
        db.insert(Tables.NETS, NetColumns.DESCRIPTION, contentValues);

        contentValues.clear();
        contentValues.put(NetColumns.DESCRIPTION, "N116");
        contentValues.put(NetColumns.PRICE, "4.50");
        db.insert(Tables.NETS, NetColumns.DESCRIPTION, contentValues);

        contentValues.clear();
        contentValues.put(NetColumns.DESCRIPTION, "N136");
        contentValues.put(NetColumns.PRICE, "5.20");
        db.insert(Tables.NETS, NetColumns.DESCRIPTION, contentValues);

        contentValues.clear();
        contentValues.put(NetColumns.DESCRIPTION, "N160");
        contentValues.put(NetColumns.PRICE, "6.60");
        db.insert(Tables.NETS, NetColumns.DESCRIPTION, contentValues);

        contentValues.clear();
        contentValues.put(NetColumns.DESCRIPTION, "N196");
        contentValues.put(NetColumns.PRICE, "7.60");
        db.insert(Tables.NETS, NetColumns.DESCRIPTION, contentValues);

        contentValues.clear();
        contentValues.put(NetColumns.DESCRIPTION, "N213");
        contentValues.put(NetColumns.PRICE, "8.00");
        db.insert(Tables.NETS, NetColumns.DESCRIPTION, contentValues);

        contentValues.clear();
        contentValues.put(NetColumns.DESCRIPTION, "Cafe Net");
        contentValues.put(NetColumns.PRICE, "7.50");
        db.insert(Tables.NETS, NetColumns.DESCRIPTION, contentValues);

    }

    private void seedTracks(SQLiteDatabase db){
        Log.d(TAG, "Seeding Tracks");
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
        String NETS = "nets";
        String CURTAINS = "curtains";
        String BLINDS = "blinds";

        // Define joins here
        // String INVENTORY = A join of tracks/nets/curtains/blinds
    }
}
