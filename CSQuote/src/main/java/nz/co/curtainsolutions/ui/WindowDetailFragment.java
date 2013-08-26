package nz.co.curtainsolutions.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import nz.co.curtainsolutions.R;
import nz.co.curtainsolutions.provider.CSContract;

/**
 * Created by brettyukich on 24/08/13.
 */
public class WindowDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener{

    private static final String TAG = WindowDetailFragment.class.getSimpleName();
    private static final int WINDOW_DETAIL_LOADER = 0x05;
    private String mJobId;
    private String mRoomId;
    private String mWindowId;

    // Views
    private View mLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null
                && args.containsKey(JobActivity.ARG_JOB_ID)
                && args.containsKey(JobActivity.ARG_ROOM_ID)
                && args.containsKey(JobActivity.ARG_WINDOW_ID)){
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

        if (mJobId != null){
            getLoaderManager().initLoader(WINDOW_DETAIL_LOADER, null, this);
        }

        return mLayout;
    }

    @Override
    public void onPause() {
        super.onPause();

        // Commit unsaved changes to the database
        ContentValues contentValues = new ContentValues();
        contentValues.put(CSContract.Windows.HEIGHT, ((EditText)mLayout.findViewById(R.id.height_text)).getText().toString());
        contentValues.put(CSContract.Windows.WIDTH, ((EditText)mLayout.findViewById(R.id.width_text)).getText().toString());

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
        switch (v.getId()){
            case R.id.remove_window_btn:
                removeWindow();
                break;
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
                CSContract.Windows.HEIGHT,
                CSContract.Windows.WIDTH,
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
        ((TextView)mLayout.findViewById(R.id.job_text)).setText("" + data.getInt(data.getColumnIndex(CSContract.Windows.JOB_ID)));
        ((TextView)mLayout.findViewById(R.id.room_text)).setText("" + data.getInt(data.getColumnIndex(CSContract.Windows.ROOM_ID)));
        ((TextView)mLayout.findViewById(R.id.window_text)).setText("" + data.getInt(data.getColumnIndex(CSContract.Windows._ID)));
        ((EditText)mLayout.findViewById(R.id.height_text)).setText("" + data.getInt(data.getColumnIndex(CSContract.Windows.HEIGHT)));
        ((EditText)mLayout.findViewById(R.id.width_text)).setText("" + data.getInt(data.getColumnIndex(CSContract.Windows.WIDTH)));

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Do nothing
    }

    private void removeWindow(){
        String selection = CSContract.Windows._ID + "=?";
        String[] selectionArgs = {mWindowId,};

        getActivity().getContentResolver().delete(
                CSContract.Windows.CONTENT_URI,
                selection,
                selectionArgs
        );

        getFragmentManager().popBackStack();
    }


}
