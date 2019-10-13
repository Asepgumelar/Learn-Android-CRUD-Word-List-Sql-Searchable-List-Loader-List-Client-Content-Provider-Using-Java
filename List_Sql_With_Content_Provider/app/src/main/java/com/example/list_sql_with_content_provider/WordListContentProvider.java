package com.example.list_sql_with_content_provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import static com.example.list_sql_with_content_provider.Contract.ALL_ITEMS;
import static com.example.list_sql_with_content_provider.Contract.AUTHORITY;
import static com.example.list_sql_with_content_provider.Contract.CONTENT_PATH;
import static com.example.list_sql_with_content_provider.Contract.CONTENT_URI;
import static com.example.list_sql_with_content_provider.Contract.COUNT;
import static com.example.list_sql_with_content_provider.Contract.MULTIPLE_RECORDS_MIME_TYPE;
import static com.example.list_sql_with_content_provider.Contract.SINGLE_RECORD_MIME_TYPE;
import static java.lang.Integer.parseInt;

public class WordListContentProvider  extends ContentProvider {
    private static final String TAG = WordListContentProvider.class.getSimpleName();
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private WordListOpenHelper mDB;
    private static final int URI_ALL_ITEMS_CODE = 10;
    private static final int URI_ONE_ITEM_CODE = 20;
    private static final int URI_COUNT_CODE = 30;
    @Override
    public boolean onCreate() {
        mDB = new WordListOpenHelper(getContext());
        initializeUriMatching();
        return true;
    }
    private void initializeUriMatching() {
        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH, URI_ALL_ITEMS_CODE);
        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH + "/#", URI_ONE_ITEM_CODE);
        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH + "/" + COUNT, URI_COUNT_CODE);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor cursor = null;
        switch (sUriMatcher.match(uri)) {
            case URI_ALL_ITEMS_CODE:
                cursor =  mDB.query(ALL_ITEMS);
                Log.d(TAG, "case all items " + cursor);
                break;
            case URI_ONE_ITEM_CODE:
                cursor =  mDB.query(parseInt(uri.getLastPathSegment()));
                Log.d(TAG, "case one item " + cursor);
                break;
            case URI_COUNT_CODE:
                cursor = mDB.count();
                Log.d(TAG, "case count " + cursor);
                break;
            case UriMatcher.NO_MATCH:
                Log.d(TAG, "NO MATCH FOR THIS URI IN SCHEME: " + uri);
                break;
            default:
                Log.d(TAG, "INVALID URI - URI NOT RECOGNIZED: "  + uri);
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case URI_ALL_ITEMS_CODE:
                return MULTIPLE_RECORDS_MIME_TYPE;
            case URI_ONE_ITEM_CODE:
                return SINGLE_RECORD_MIME_TYPE;
            default:
                return null;
        }
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = mDB.insert(values);
        return Uri.parse(CONTENT_URI + "/" + id);
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return mDB.delete(parseInt(selectionArgs[0]));
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return mDB.update(parseInt(selectionArgs[0]), values.getAsString("word"));
    }
}
