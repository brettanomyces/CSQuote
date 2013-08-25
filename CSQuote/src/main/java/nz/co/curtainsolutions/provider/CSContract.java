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

    private CSContract() {
        // private constructor
    }

    interface JobColumns {
        public static final String CUSTOMER = "customer";
    }

    interface RoomColumns {
        public static final String JOB_ID = "job_id";
        public static final String DESCRIPTION = "description";
    }

    interface WindowColumns {
        public static final String JOB_ID = "job_id";
        public static final String ROOM_ID = "room_id";
        public static final String WIDTH = "width";
        public static final String HEIGHT = "height";
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
}
