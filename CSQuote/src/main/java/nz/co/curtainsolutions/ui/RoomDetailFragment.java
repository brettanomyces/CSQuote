package nz.co.curtainsolutions.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import nz.co.curtainsolutions.R;
import nz.co.curtainsolutions.provider.CSContract;

/**
 * Created by brettyukich on 23/08/13.
 */
public class RoomDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener{

    public static final int ROOM_DETAIL_LOADER = 0x04;
    private static final String TAG = RoomDetailFragment.class.getSimpleName();

    private String mJobId;
    private String mRoomId;

    // Views
    private View mLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle args = getArguments();
        if (args != null ){
            if (args.containsKey(JobActivity.ARG_JOB_ID) ){
                mJobId = args.getString(JobActivity.ARG_JOB_ID);
            }
            if (args.containsKey(JobActivity.ARG_ROOM_ID)){
                mRoomId = args.getString(JobActivity.ARG_ROOM_ID);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.room_detail, container, false);
        mLayout.findViewById(R.id.remove_room_btn).setOnClickListener(this);

        if (mRoomId != null){
            getLoaderManager().initLoader(ROOM_DETAIL_LOADER, null, this);
        }

        return mLayout;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
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
            case R.id.remove_room_btn:
                Log.d(TAG, "removing room " + mRoomId);
                removeRoom();
        }
    }

    private void removeRoom(){

        // Remove the room from the database
        String selection = CSContract.Rooms._ID + "=?";
        String[] selectionArgs = {mRoomId,};

        getActivity().getContentResolver().delete(
                CSContract.Rooms.CONTENT_URI,
                selection,
                selectionArgs
        );

        // Go Back to room list
        getFragmentManager().popBackStack();
    }
}
