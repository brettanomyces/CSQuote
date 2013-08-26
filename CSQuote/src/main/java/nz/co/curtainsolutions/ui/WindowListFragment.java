package nz.co.curtainsolutions.ui;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import nz.co.curtainsolutions.R;
import nz.co.curtainsolutions.provider.CSContract;

/**
 * Created by brettyukich on 24/08/13.
 */
public class WindowListFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static final String TAG = WindowListFragment.class.getSimpleName();
    private static final int WINDOW_LIST_LOADER = 0x04;
    private SimpleCursorAdapter mAdapter;
    private String mJobId;
    private String mRoomId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] columns = {
                CSContract.Windows._ID,
        };

        int[] to = {
                R.id.window_text,
        };

        mAdapter = new SimpleCursorAdapter(
                getActivity().getApplicationContext(),
                R.layout.window_list_item,
                null,
                columns,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );

        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.window_list, container, false);

        Bundle args = getArguments();
        if (args != null
                && args.containsKey(JobActivity.ARG_JOB_ID)
                && args.containsKey(JobActivity.ARG_ROOM_ID)
                ){
            mJobId = args.getString(JobActivity.ARG_JOB_ID);
            mRoomId = args.getString(JobActivity.ARG_ROOM_ID);

            getLoaderManager().initLoader(WINDOW_LIST_LOADER, null, this);
        }

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.add_window_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_window_btn:
                showWindowDetails(newWindow());
                break;
            default:
                break;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = (Cursor) getListAdapter().getItem(position);
        String windowId = "" + cursor.getInt(cursor.getColumnIndex(CSContract.Windows._ID));

        showWindowDetails(windowId);
    }

    private void showWindowDetails(String windowId){
        Bundle args = new Bundle();
        args.putString(JobActivity.ARG_JOB_ID, mJobId);
        args.putString(JobActivity.ARG_ROOM_ID, mRoomId);
        args.putString(JobActivity.ARG_WINDOW_ID, windowId);

        WindowDetailFragment windowDetailFragment = new WindowDetailFragment();
        windowDetailFragment.setArguments(args);

        ((JobActivity)getActivity()).handleFragmentTransaction(windowDetailFragment);
    }

    private String newWindow(){
        // Create a new window in the database
        ContentValues contentValues = new ContentValues();
        contentValues.put(CSContract.Windows.JOB_ID, mJobId);
        contentValues.put(CSContract.Windows.ROOM_ID, mRoomId);

        Uri uri = getActivity().getContentResolver().insert(CSContract.Windows.CONTENT_URI, contentValues);
        getLoaderManager().restartLoader(WINDOW_LIST_LOADER, null, this);

        return  CSContract.Windows.getWindowId(uri);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                CSContract.Windows._ID,
                CSContract.Windows.JOB_ID,
                CSContract.Windows.ROOM_ID,
                CSContract.Windows.GROSS_HEIGHT,
                CSContract.Windows.GROSS_WIDTH,
        };

        String selection =
                CSContract.Windows.JOB_ID + "=? AND " + CSContract.Windows.ROOM_ID + "=?";

        String[] selectionArgs = {
                mJobId,
                mRoomId,
        };

        CursorLoader cursorLoader = new CursorLoader(
                getActivity(),
                CSContract.Windows.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        return  cursorLoader;
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
