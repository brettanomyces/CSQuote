package nz.co.curtainsolutions.ui;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

import nz.co.curtainsolutions.R;
import nz.co.curtainsolutions.provider.CSContract;
import nz.co.curtainsolutions.ui.JobDetailFragment;

/**
 * Created by brettyukich on 21/08/13.
 */
public class JobListFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private static final int JOB_LIST_LOADER = 0x01;
    private SimpleCursorAdapter mAdapter;
    private  int mActivatedPosition = ListView.INVALID_POSITION;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(JOB_LIST_LOADER, null, this);

        String[] columns =  {CSContract.Jobs._ID, CSContract.Jobs.CUSTOMER};
        int[] to = {R.id.job_id, R.id.job_customer};

        mAdapter = new SimpleCursorAdapter(
                getActivity().getApplicationContext(),
                R.layout.job_list_item,
                null,
                columns,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );

        setListAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)){
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    public void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Get the ID of the selected job
        Cursor cursor = (Cursor) getListAdapter().getItem(position);
        String jobId =  cursor.getString(cursor.getColumnIndex(CSContract.Jobs._ID));

        Bundle args = new Bundle();
        args.putString(JobDetailFragment.ARG_JOB_ID, jobId);
        JobDetailFragment jobDetailFragment = new JobDetailFragment();
        jobDetailFragment.setArguments(args);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, jobDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                CSContract.Jobs._ID,
                CSContract.Jobs.CUSTOMER,
        };

        CursorLoader cursorLoader = new CursorLoader(
                getActivity(),
                CSContract.Jobs.CONTENT_URI,
                projection,
                null,
                null,
                null
        );

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);

    }
}
