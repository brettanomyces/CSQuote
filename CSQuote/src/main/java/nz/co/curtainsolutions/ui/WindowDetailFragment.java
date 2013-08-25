package nz.co.curtainsolutions.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

/**
 * Created by brettyukich on 24/08/13.
 */
public class WindowDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener{

    @Override
    public void onClick(View v) {

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


}
