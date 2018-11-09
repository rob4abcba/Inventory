package com.example.pwesc62.inventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import static com.example.pwesc62.inventory.data.InvContract.InvEntry;



/**
 * {@link ContentProvider} for Inv app.
 */
public class InvProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = InvProvider.class.getSimpleName();

    /**
     * Global variable for DbHelper
     */
    private InvDbHelper mDbHelper;
    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        mDbHelper = new InvDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case INV:
                // For the Inv code, query the Inv table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the Inv table.
                cursor = database.query(InvContract.InvEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case INV_ID:
                // For the Inv_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.Inv/Inv/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = InvContract.InvEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the Inv table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(InvContract.InvEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        //set notification URI on the cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INV:
                return insertInv(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a Inv into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertInv(Uri uri, ContentValues values) {

        String name = values.getAsString(InvContract.InvEntry.COLUMN_PRODUCT_NAME);
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Product requires a name");
        }
        String creator = values.getAsString(InvContract.InvEntry.COLUMN_PRODUCT_CREATOR);
        if (TextUtils.isEmpty(creator)) {
            throw new IllegalArgumentException("Product requires a creator");
        }
        double price = values.getAsDouble(InvContract.InvEntry.COLUMN_PRICE);
        if (price > 0.01) {
            throw new IllegalArgumentException("Price must be greater than $0.01");
        }
        int quantity = values.getAsInteger(InvContract.InvEntry.COLUMN_QUANTITY);
        if  (quantity < 0) {
            throw new IllegalArgumentException("Item quantity must be greater than 0");
        }

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //insert the Inv with the given values
        long id = db.insert(InvContract.InvEntry.TABLE_NAME, null, values);

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        //Notify listeners data has changed
        getContext().getContentResolver().notifyChange(uri, null);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INV:
                return updateInv(uri, contentValues, selection, selectionArgs);
            case INV_ID:
                // For the Inv_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = InvContract.InvEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateInv(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update Inv in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more Inv).
     * Return the number of rows that were successfully updated.
     */
    private int updateInv(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link InvEntry#COLUMN_Inv_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(InvContract.InvEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(InvContract.InvEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }

        String creator = values.getAsString(InvContract.InvEntry.COLUMN_PRODUCT_CREATOR);
        if (TextUtils.isEmpty(creator)) {
            throw new IllegalArgumentException("Product requires a creator");
        }
        double price = values.getAsDouble(InvContract.InvEntry.COLUMN_PRICE);
        if (price > 0.01) {
            throw new IllegalArgumentException("Price must be greater than $0.01");
        }
        int quantity = values.getAsInteger(InvContract.InvEntry.COLUMN_QUANTITY);
        if  (quantity < 0) {
            throw new IllegalArgumentException("Item quantity must be greater than 0");
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }
        //Notify listeners data has changed
        getContext().getContentResolver().notifyChange(uri, null);
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

       return db.update(InvContract.InvEntry.TABLE_NAME, values, selection, selectionArgs);

    }

        @Override
        public int delete(Uri uri, String selection, String[] selectionArgs) {
            // Get writeable database
            SQLiteDatabase database = mDbHelper.getWritableDatabase();

            final int match = sUriMatcher.match(uri);
            switch (match) {
                case INV:
                    // Delete all rows that match the selection and selection args
                    return database.delete(InvContract.InvEntry.TABLE_NAME, selection, selectionArgs);
                case INV_ID:
                    // Delete a single row given by the ID in the URI
                    selection = InvContract.InvEntry._ID + "=?";
                    selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                    //Notify listeners data has changed
                    getContext().getContentResolver().notifyChange(uri, null);

                    return database.delete(InvContract.InvEntry.TABLE_NAME, selection, selectionArgs);
                default:
                    throw new IllegalArgumentException("Deletion is not supported for " + uri);


            }

        }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INV:
                return InvContract.InvEntry.CONTENT_LIST_TYPE;
            case INV_ID:
                return InvContract.InvEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    /** URI matcher code for the content URI for the Inv table */
    private static final int INV = 100;

    /** URI matcher code for the content URI for a single Inv in the Inv table */
    private static final int INV_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        sUriMatcher.addURI(InvContract.InvEntry.CONTENT_AUTHORITY, InvContract.InvEntry.PATH_INVENTORY, INV);
        sUriMatcher.addURI(InvContract.InvEntry.CONTENT_AUTHORITY, InvContract.InvEntry.PATH_INVENTORY+"/#", INV_ID);
    }
}