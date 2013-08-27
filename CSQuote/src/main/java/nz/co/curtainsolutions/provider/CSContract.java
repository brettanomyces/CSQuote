package nz.co.curtainsolutions.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by brettyukich on 20/08/13.
 */
public class CSContract {
    public static final String CONTENT_AUTHORITY = "nz.co.curtainsolutions";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_JOBS = "jobs";
    private static final String PATH_ROOMS = "rooms";
    private static final String PATH_WINDOWS = "windows";
    private static final String PATH_TRACKS = "tracks";

    private CSContract() {
        // private constructor
    }

    interface JobColumns {
        public static final String CUSTOMER = "customer";
        public static final String PROPERTY_NUMBER = "property_number";
        public static final String SITE_ADDRESS = "site_address";
        public static final String TENANT = "tenant";
        public static final String HOME_PHONE = "home_phone";
        public static final String MOBILE_PHONE = "mobile_phone";
        public static final String WORK_PHONE = "work_phone";
        public static final String NOTES = "notes";
    }

    interface RoomColumns {
        public static final String JOB_ID = "job_id";
        public static final String DESCRIPTION = "description";
        public static final String NOTES = "notes";
    }

    interface WindowColumns {
        public static final String BLIND_ID = "blind_id";
        public static final String BLIND_PRICE = "blind_price";
        public static final String CURTAIN_ID = "curtain_id";
        public static final String CURTAIN_PRICE = "curtain_price";
        public static final String CURTAIN_SEW = "curtain_sew";
        public static final String GROSS_HEIGHT = "height";
        public static final String GROSS_WIDTH = "width";
        public static final String HOOK_SIZE = "hook_size";
        public static final String INNER_HEIGHT = "inner_height";
        public static final String INNER_WIDTH = "inner_width";
        public static final String JOB_ID = "job_id";
        public static final String NET_ID = "net_id";
        public static final String NET_PRICE = "net_price";
        public static final String NOTES = "notes";
        public static final String ROOM_ID = "room_id";
        public static final String TRACK_ID = "track_id";
        public static final String TRACK_PRICE = "track_price";
        public static final String TRACK_WIDTH = "track_width";
        public static final String UNIT_PAIR = "unit_pair";
    }

    interface TrackColumns {
        public static final String MIN_WIDTH = "min_width";
        public static final String MAX_WIDTH = "max_width";
        public static final String PRICE = "price";
        public static final String DESCRIPTION = "description";
    }

    interface NetColumns {
        public static final String PRICE = "price";
        public static final String DESCRIPTION = "description";
    }

    interface CurtainColumns {
        public static final String PRICE = "price";
        public static final String DESCRIPTION = "description";
    }

    interface BlindColumns {
        public static final String PRICE = "price";
        public static final String DESCRIPTION = "description";
    }

    public static class Jobs implements BaseColumns, JobColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_JOBS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.curtainsolutions.job";
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.curtainsolutions.job";

        public static Uri buildJobUri(String jobId) {
            return CONTENT_URI.buildUpon().appendPath(jobId).build();
        }

        public static String getJobId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Rooms implements BaseColumns, RoomColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROOMS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.curtainsolutions.room";
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.curtainsolutions.room";

        public static Uri buildRoomUri(String roomId) {
            return CONTENT_URI.buildUpon().appendPath(roomId).build();
        }

        public static String getRoomId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Windows implements BaseColumns, WindowColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WINDOWS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.curtainsolutions.window";
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.curtainsolutions.window";

        public static Uri buildWindowUri(String windowId) {
            return CONTENT_URI.buildUpon().appendPath(windowId).build();
        }

        public static String getWindowId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Tracks implements BaseColumns, TrackColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRACKS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.curtainsolutions.track";
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.curtainsolutions.track";

        public static Uri buildTrackUri(String trackId) {
            return CONTENT_URI.buildUpon().appendPath(trackId).build();
        }

        public static String getTrackId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
