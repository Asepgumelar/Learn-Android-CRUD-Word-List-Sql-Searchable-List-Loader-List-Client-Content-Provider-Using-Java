package com.example.list_sql_searchable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.database.Cursor;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = EditWordActivity.class.getSimpleName();
    private WordListOpenHelper mDB;
    private EditText mEditWordView;
    private TextView mTextView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mDB = new WordListOpenHelper(this);
        mEditWordView = ((EditText) findViewById(R.id.search_word));
        mTextView = ((TextView) findViewById(R.id.search_result));
    }
    public void showResult(View view) {
        String word = mEditWordView.getText().toString();
        mTextView.setText("Result for " + word + ":\n\n");
        Cursor cursor = mDB.search(word);
        cursor.moveToFirst();
        if (cursor != null & cursor.getCount() > 0) {
            int index;
            String result;
            do {
                index = cursor.getColumnIndex(WordListOpenHelper.KEY_WORD);
                result = cursor.getString(index);
                mTextView.append(result + "\n");
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            mTextView.append(getString(R.string.no_result));
        }
    }
}
