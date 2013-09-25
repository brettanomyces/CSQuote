package nz.co.curtainsolutions.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.AttributeSet;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

/**
 * A simple child class that provides some useful methods for setting/getting the spinner
 * selection
 */
public class CursorSpinner extends Spinner{

    private boolean set = false;

    public CursorSpinner(Context context) {
        super(context);
    }

    public CursorSpinner(Context context, int mode) {
        super(context, mode);
    }

    public CursorSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CursorSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CursorSpinner(Context context, AttributeSet attrs, int defStyle, int mode) {
        super(context, attrs, defStyle, mode);
    }

    public void setSelectionId(String id){
        Cursor cursor = ((SimpleCursorAdapter)getAdapter()).getCursor();
        // Check each row in the cursor
        for(int pos = 0; pos < cursor.getCount(); pos++){
            cursor.moveToPosition(pos);
            if (id.contentEquals(cursor.getString(cursor.getColumnIndex(BaseColumns._ID)))){
                setSelection(pos);
                set = true;
            }
        }
    }

    public String getSelectionId(){
        Cursor cursor = (Cursor)getSelectedItem();
        return cursor.getString(cursor.getColumnIndex(BaseColumns._ID));
    }

    // See http://stackoverflow.com/q/2562248 for an explanation for why this is necessary.
    public boolean hasBeenSet(){
        return set;
    }
}
