package nz.co.curtainsolutions.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import nz.co.curtainsolutions.R;
import nz.co.curtainsolutions.provider.CSContract;

/**
 * Created by brettyukich on 21/08/13.
 */
public class JobDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static final int JOB_DETAIL_LOADER = 0x02;
    private View mLayout;
    private String mJobId;
    // Views
    private TextView mJobText;
    private EditText mCustomerText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey(JobActivity.ARG_JOB_ID)) {
            mJobId = args.getString(JobActivity.ARG_JOB_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.job_detail, container, false);
        mLayout.findViewById(R.id.rooms_btn).setOnClickListener(this);

        mJobText = (TextView) mLayout.findViewById(R.id.job_text);
        mCustomerText = (EditText) mLayout.findViewById(R.id.customer_text);

        if (mJobId != null) {
            getLoaderManager().initLoader(JOB_DETAIL_LOADER, null, this);
        }

        return mLayout;
    }

    @Override
    public void onPause() {
        super.onPause();

        // Commit unsaved changes to the database
        ContentValues contentValues = new ContentValues();
        contentValues.put(CSContract.Jobs.CUSTOMER, mCustomerText.getText().toString());

        String selection = CSContract.Jobs._ID + "=?";
        String[] selectionArgs = {mJobId,};

        getActivity().getContentResolver().update(
                CSContract.Jobs.CONTENT_URI,
                contentValues,
                selection,
                selectionArgs
        );
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                CSContract.Jobs._ID,
                CSContract.Jobs.CUSTOMER,
        };

        String selection = CSContract.Jobs._ID + "=?";
        String[] selectionArgs = {mJobId,};

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

        // Should only be one
        data.moveToFirst();
        mJobText.setText("" + data.getInt(data.getColumnIndex(CSContract.Jobs._ID)));
        mCustomerText.setText(data.getString(data.getColumnIndex(CSContract.Jobs.CUSTOMER)));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Do nothing
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rooms_btn:
                showRoomList();
                break;
            default:
                break;
        }
    }

    private void showRoomList() {

        Bundle args = new Bundle();
        args.putString(JobActivity.ARG_JOB_ID, mJobId);

        Fragment fragment = new RoomListFragment();
        fragment.setArguments(args);

        ((JobActivity) getActivity()).handleFragmentTransaction(fragment);
    }
}
