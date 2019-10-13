package com.example.list_loader;

import android.content.Context;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.example.list_loader.R.id.word;

public class WordListAdapter
        extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    class WordViewHolder extends RecyclerView.ViewHolder {
        public final TextView wordItemView;

        public WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = (TextView) itemView.findViewById(word);
        }
    }

    private static final String TAG = WordListAdapter.class.getSimpleName();

    private Cursor mCursor = null;

    private final LayoutInflater mInflater;
    private Context mContext;

    public WordListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setData(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.wordlist_item, parent, false);
        return new WordViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {

        String word = "";

        if (mCursor != null) {
            if (mCursor.moveToPosition(position)) {
                int indexWord = mCursor.getColumnIndex(Contract.WordList.KEY_WORD);
                word = mCursor.getString(indexWord);
                holder.wordItemView.setText(word);
            } else {
                holder.wordItemView.setText(R.string.error_no_word);
            }
        } else {
            Log.e (TAG, "onBindViewHolder: Cursor is null.");
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        } else {
            return -1;
        }
    }
}
