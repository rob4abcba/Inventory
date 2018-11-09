package com.example.pwesc62.inventory;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.pwesc62.inventory.data.InvContract;

/**
 * {@link InvCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class InvCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link InvCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public InvCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //Find views
        TextView tvName =  view.findViewById(R.id.name);
        TextView tvSummary =  view.findViewById(R.id.summary);
        //Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow(InvContract.InvEntry.COLUMN_PRODUCT_NAME));
        String summary = cursor.getString(cursor.getColumnIndexOrThrow(InvContract.InvEntry.COLUMN_PRODUCT_CREATOR));

        //Set text from cursor to views
        tvName.setText(name);
        tvSummary.setText(summary);
    }
}