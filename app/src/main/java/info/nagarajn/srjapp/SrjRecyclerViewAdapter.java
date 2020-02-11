package info.nagarajn.srjapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SrjRecyclerViewAdapter extends RecyclerView.Adapter<SrjRecyclerViewAdapter.ViewHolder> {

    private List<String> stringList;
    private Context context;

    public SrjRecyclerViewAdapter(List<String> stringList, MainActivity mainActivity) {
        this.stringList = stringList;
        this.context = mainActivity;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_srj, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(stringList.get(position));
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.srj);
        }
    }
}
