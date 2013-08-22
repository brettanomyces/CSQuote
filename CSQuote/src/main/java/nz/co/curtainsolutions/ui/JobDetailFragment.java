package nz.co.curtainsolutions.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nz.co.curtainsolutions.R;
import nz.co.curtainsolutions.provider.CSContract;

/**
 * Created by brettyukich on 21/08/13.
 */
public class JobDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    public static final String ARG_JOB_ID = "job_id";
    private static final int JOB_DETAIL_LOADER = 0x02;
    private static View mLayout;
    private String jobId;
    private SimpleCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(ARG_JOB_ID)) {
            jobId = getArguments().getString(ARG_JOB_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.job_detail, container, false);


        if (jobId != null) {
            getLoaderManager().initLoader(JOB_DETAIL_LOADER, null, this);
        }

        return mLayout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load the jobs rooms
        if (jobId != null) {
            FragmentManager fragmentManager = getChildFragmentManager();
            Fragment fragment = new RoomListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.room_frame, fragment)
                    .commit();
        }


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                CSContract.Jobs._ID,
                CSContract.Jobs.CUSTOMER,
        };

        String selection = CSContract.Jobs._ID + "=?";
        String[] selectionArgs = {jobId};

        CursorLoader loader = new CursorLoader(
                getActivity(),
                CSContract.Jobs.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_room_btn:
                addRoom();
                break;
            default:
                break;
        }
    }

    private void addRoom() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CSContract.Rooms.JOB_ID, jobId);
        getActivity().getContentResolver().insert(CSContract.Rooms.CONTENT_URI, contentValues);
    }
}
