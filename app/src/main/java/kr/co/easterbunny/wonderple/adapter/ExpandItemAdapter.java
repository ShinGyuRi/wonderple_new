package kr.co.easterbunny.wonderple.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import kr.co.easterbunny.wonderple.model.ExpandItem;

/**
 * Created by scona on 2017-05-16.
 */

public class ExpandItemAdapter extends RecyclerView.Adapter<ExpandItemAdapter.BindingHolder> {

    private final List<ExpandItem> data;
    private Context context;
    private SparseBooleanArray expandState = new SparseBooleanArray();

    public ExpandItemAdapter(final List<ExpandItem> data) {
        this.data = data;
        for (int i=0; i<data.size(); i++) {
            expandState.append(i, false);
        }
    }

    public class BindingHolder extends RecyclerView.ViewHolder {
        public BindingHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
