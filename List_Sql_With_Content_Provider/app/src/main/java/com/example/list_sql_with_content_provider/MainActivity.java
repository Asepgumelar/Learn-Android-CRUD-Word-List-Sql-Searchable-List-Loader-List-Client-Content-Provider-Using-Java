package com.example.list_sql_with_content_provider;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.ContentValues;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import static com.example.list_sql_with_content_provider.Contract.CONTENT_URI;
import static com.example.list_sql_with_content_provider.Contract.WordList.KEY_ID;
import static com.example.list_sql_with_content_provider.Contract.WordList.KEY_WORD;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int WORD_EDIT = 1;
    public static final int WORD_ADD = -1;
    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new WordListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), EditWordActivity.class);
                startActivityForResult(intent, WORD_EDIT);
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WORD_EDIT) {
            if (resultCode == RESULT_OK) {
                String word = data.getStringExtra(EditWordActivity.EXTRA_REPLY);
                if (word.length() != 0) {
                    ContentValues values = new ContentValues();
                    values.put(KEY_WORD, word);
                    int id = data.getIntExtra(WordListAdapter.EXTRA_ID, -99);
                    if (id == WORD_ADD) {
                        getContentResolver().insert(CONTENT_URI, values);
                    } else if (id >= 0) {
                        String[] selectionArgs = {Integer.toString(id)};
                        getContentResolver().update(CONTENT_URI, values, KEY_ID, selectionArgs);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(
                            getApplicationContext(), R.string.empty_word_not_saved,
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
