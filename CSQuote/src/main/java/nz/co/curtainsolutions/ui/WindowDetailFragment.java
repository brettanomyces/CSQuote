package nz.co.curtainsolutions.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import nz.co.curtainsolutions.R;
import nz.co.curtainsolutions.provider.CSContract;

/**
 * Created by brettyukich on 24/08/13.
 */
public class WindowDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static final String TAG = WindowDetailFragment.class.getSimpleName();
    private static final int WINDOW_DETAIL_LOADER = 0x05;

    // Map the cursor columns to the view, used for populating the fields and saving changes.
    private static final Map<String, Integer> viewMap;
    static {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put(CSContract.Windows.JOB_ID, R.id.job_text);
        map.put(CSContract.Windows.ROOM_ID, R.id.room_text);
        map.put(CSContract.Windows._ID, R.id.window_text);
        map.put(CSContract.Windows.GROSS_HEIGHT, R.id.gross_height_text);
        map.put(CSContract.Windows.INNER_HEIGHT, R.id.inner_height_text);
        map.put(CSContract.Windows.GROSS_WIDTH, R.id.gross_width_text);
        map.put(CSContract.Windows.INNER_WIDTH, R.id.inner_width_text);
        map.put(CSContract.Windows.TRACK_WIDTH, R.id.track_width_text);
        viewMap = Collections.unmodifiableMap(map);

    }

    private String mJobId;
    private String mRoomId;
    private String mWindowId;
    private View mLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null
                && args.containsKey(JobActivity.ARG_JOB_ID)
                && args.containsKey(JobActivity.ARG_ROOM_ID)
                && args.containsKey(JobActivity.ARG_WINDOW_ID)) {
            mJobId = args.getString(JobActivity.ARG_JOB_ID);
            mRoomId = args.getString(JobActivity.ARG_ROOM_ID);
            mWindowId = args.getString(JobActivity.ARG_WINDOW_ID);
            Log.d(TAG, "job id: " + mJobId + ", room id; " + mRoomId + " window id: " + mWindowId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.window_detail, container, false);
        mLayout.findViewById(R.id.remove_window_btn).setOnClickListener(this);
        mLayout.findViewById(R.id.done_btn).setOnClickListener(this);
        ((Spinner) mLayout.findViewById(R.id.track_size_spinner)).setAdapter(getTrackSpinnerAdapter());

        if (mJobId != null) {
            getLoaderManager().initLoader(WINDOW_DETAIL_LOADER, null, this);
        }

        return mLayout;
    }

    @Override
    public void onPause() {
        super.onPause();

        // Commit current state to database
        ContentValues contentValues = new ContentValues();
        for (String column : viewMap.keySet()) {
            String value = ((TextView) mLayout.findViewById(viewMap.get(column))).getText().toString();
            Log.d(TAG, "Putting into column '" + column + "', value '" + value + "'");
            contentValues.put(column, value);
        }

        String selection = CSContract.Windows._ID + "=?";
        String[] selectionArgs = {mWindowId,};

        getActivity().getContentResolver().update(
                CSContract.Windows.CONTENT_URI,
                contentValues,
                selection,
                selectionArgs
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remove_window_btn:
                alertRemoveWindow();
                break;
            case R.id.done_btn:
                getFragmentManager().popBackStack();
            default:
                break;

        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                CSContract.Windows._ID,
                CSContract.Windows.JOB_ID,
                CSContract.Windows.ROOM_ID,
                CSContract.Windows.GROSS_HEIGHT,
                CSContract.Windows.INNER_HEIGHT,
                CSContract.Windows.GROSS_WIDTH,
                CSContract.Windows.INNER_WIDTH,
                CSContract.Windows.TRACK_WIDTH,
                CSContract.Windows.TRACK_ID,
        };

        String selection = CSContract.Windows._ID + "=?";
        String[] selectionArgs = {mWindowId,};

        CursorLoader cursorLoader = new CursorLoader(
                getActivity(),
                CSContract.Windows.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Should only be one
        data.moveToFirst();

        // Extract the data from each of the columns
        for (int i = 0; i < data.getColumnCount(); i++) {
            // TODO handle different data types rather than coercing to string
            switch (data.getType(i)) {
                case Cursor.FIELD_TYPE_NULL:
                    // Skip null values
                    break;
                default: {
                    TextView view = (TextView) mLayout.findViewById(viewMap.get(data.getColumnName(i)));
                    view.setText(data.getString(i));
                }

            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Do nothing
    }

    private void alertRemoveWindow() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Remove Window")
                .setMessage("Are you sure you want to remove this window?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeWindow();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .show();
    }

    private void removeWindow() {
        String selection = CSContract.Windows._ID + "=?";
        String[] selectionArgs = {mWindowId,};

        getActivity().getContentResolver().delete(
                CSContract.Windows.CONTENT_URI,
                selection,
                selectionArgs
        );

        getFragmentManager().popBackStack();
    }

    private SimpleCursorAdapter getTrackSpinnerAdapter() {
        String[] projection = {CSContract.Tracks._ID, CSContract.Tracks.DESCRIPTION};
        Cursor cursor = getActivity().getContentResolver().query(
                CSContract.Tracks.CONTENT_URI,
                projection,
                null,
                null,
                null
        );

        String[] from = {CSContract.Tracks.DESCRIPTION,};
        int[] to = {R.id.track_description_text};

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.track_spinner_item,
                cursor,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER

        );

        return simpleCursorAdapter;
    }


}
