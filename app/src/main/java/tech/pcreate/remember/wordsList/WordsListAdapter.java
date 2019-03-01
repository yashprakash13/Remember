package tech.pcreate.remember.wordsList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import tech.pcreate.remember.R;
import tech.pcreate.remember.database.Word;

public class WordsListAdapter  extends PagedListAdapter<Word, WordsListAdapter.WordListViewHolder> {

    private WordListClickListener wordListClickListener;

    private static final DiffUtil.ItemCallback<Word> Diff_callback
            = new DiffUtil.ItemCallback<Word>() {
        @Override
        public boolean areItemsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
            return oldItem.getName().equalsIgnoreCase(newItem.getName());
        }
    };

    protected WordsListAdapter(WordListClickListener wordListClickListener) {
        super(Diff_callback);
        this.wordListClickListener = wordListClickListener;
    }

    public Word getWordAtPosition(int position){
        return getItem(position);
    }

    @NonNull
    @Override
    public WordListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_word_item_layout, parent, false);

        return new WordListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordListViewHolder holder, int position) {
        Word word = getWordAtPosition(position);
        if (word != null){
            holder.mName.setText(word.getName());
            holder.mMeaning.setText(word.getMeaning());
        }
    }

    public class WordListViewHolder extends RecyclerView.ViewHolder{

        public TextView mName, mMeaning;

        public WordListViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.nameText);
            mMeaning = itemView.findViewById(R.id.meaningText);

            itemView.setOnClickListener(view -> {
                try {
                    wordListClickListener.onClick(getWordAtPosition(getLayoutPosition()).getId());
                    notifyItemChanged(getLayoutPosition());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    try {
                        wordListClickListener.onLongClick(getWordAtPosition(getLayoutPosition()).getId());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
        }
    }

    public interface WordListClickListener{
        void onClick(int id) throws ExecutionException, InterruptedException;
        void onLongClick(int id) throws ExecutionException, InterruptedException;
    }
}
