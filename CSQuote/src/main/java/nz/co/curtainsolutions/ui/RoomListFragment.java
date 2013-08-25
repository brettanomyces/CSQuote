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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import nz.co.curtainsolutions.R;
import nz.co.curtainsolutions.provider.CSContract;

/**
 * Created by brettyukich on 22/08/13.
 */
public class RoomListFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static final String TAG = RoomListFragment.class.getSimpleName();
    private static final int ROOM_LIST_LOADER = 0x03;
    private SimpleCursorAdapter mAdapter;
    private String mJobId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] columns = {
                CSContract.Rooms._ID,
                CSContract.Rooms.DESCRIPTION,
        };

        int[] to = {
                R.id.room_text,
                R.id.description_text,
        };

        mAdapter = new SimpleCursorAdapter(
                getActivity().getApplicationContext(),
                R.layout.room_list_item,
                null,
                columns,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );

        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.room_list, container, false);

        Bundle args = getArguments();
        if (args != null && args.containsKey(JobActivity.ARG_JOB_ID)) {
            mJobId = args.getString(JobActivity.ARG_JOB_ID);
            // Loader is initiated here because we need the args before we can initiate it
            getLoaderManager().initLoader(ROOM_LIST_LOADER, null, this);

        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.add_room_btn).setOnClickListener(this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = (Cursor) getListAdapter().getItem(position);
        String roomId = cursor.getString(cursor.getColumnIndex(CSContract.Rooms._ID));

        showRoomDetails(roomId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_room_btn:
                showRoomDetails(newRoom());
                break;
            default:
                break;
        }

    }

    private void showRoomDetails(String roomId) {
        Bundle args = new Bundle();
        args.putString(JobActivity.ARG_JOB_ID, mJobId);
        args.putString(JobActivity.ARG_ROOM_ID, roomId);

        RoomDetailFragment roomDetailFragment = new RoomDetailFragment();
        roomDetailFragment.setArguments(args);

        ((JobActivity) getActivity()).handleFragmentTransaction(roomDetailFragment);
    }

    private String newRoom() {
        // Create a new room in the database
        ContentValues contentValues = new ContentValues();
        contentValues.put(CSContract.Rooms.JOB_ID, mJobId);

        Uri uri = getActivity().getContentResolver().insert(CSContract.Rooms.CONTENT_URI, contentValues);
        getLoaderManager().restartLoader(ROOM_LIST_LOADER, null, this);

        return CSContract.Rooms.getRoomId(uri);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader called");
        String[] projection = {
                CSContract.Rooms._ID,
                CSContract.Rooms.DESCRIPTION,
        };

        // Get only the rooms belonging to this job
        String selection = CSContract.Rooms.JOB_ID + "=?";
        String[] selectionArgs = {mJobId,};

        CursorLoader cursorLoader = new CursorLoader(
                getActivity(),
                CSContract.Rooms.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished called");
        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset called");
        mAdapter.swapCursor(null);
    }


}
