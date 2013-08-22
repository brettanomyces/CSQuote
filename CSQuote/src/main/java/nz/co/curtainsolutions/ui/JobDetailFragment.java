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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    // Views
    private Button mAddRoomBtn;
    private TextView mJobId;
    private EditText mJobCustomer;

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
        mAddRoomBtn = (Button) mLayout.findViewById(R.id.add_room_btn);
        mAddRoomBtn.setOnClickListener(this);
        mJobId = (TextView) mLayout.findViewById(R.id.job_id);
        mJobCustomer = (EditText) mLayout.findViewById(R.id.job_customer);
        mJobCustomer.setOnFocusChangeListener(new OnFocusChangeListenerImpl(CSContract.Jobs.CUSTOMER));

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

            Bundle bundle = new Bundle();
            bundle.putString(JobDetailFragment.ARG_JOB_ID, jobId);

            Fragment fragment = new RoomListFragment();
            fragment.setArguments(bundle);
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
        System.out.println(data.getColumnCount());
        System.out.println(data.getColumnNames().toString());
        data.moveToFirst();
        mJobId.setText("" + data.getInt(data.getColumnIndex(CSContract.Jobs._ID)));
        mJobCustomer.setText(data.getString(data.getColumnIndex(CSContract.Jobs.CUSTOMER)));
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
        System.out.println("add room!");
        ContentValues contentValues = new ContentValues();
        contentValues.put(CSContract.Rooms.JOB_ID, jobId);

        getActivity().getContentResolver().insert(
                CSContract.Rooms.CONTENT_URI,
                contentValues
        );
    }

    private class OnFocusChangeListenerImpl implements View.OnFocusChangeListener {
        private String lastValue;
        private String mColumn;

        public OnFocusChangeListenerImpl(String column) {
            mColumn = column;

        }



        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            String newValue = ((EditText)v).getText().toString();
            if (!newValue.equals(lastValue)) {
                // Update lastValue
                lastValue = newValue;

                // Persist
                ContentValues contentValues = new ContentValues();
                contentValues.put(mColumn, newValue);

                String selection = CSContract.Jobs._ID + "=?";
                String[] selectionArgs = {jobId,};

                getActivity().getContentResolver().update(
                        CSContract.Jobs.CONTENT_URI,
                        contentValues,
                        selection,
                        selectionArgs
                );

            }
        }
    }
}
