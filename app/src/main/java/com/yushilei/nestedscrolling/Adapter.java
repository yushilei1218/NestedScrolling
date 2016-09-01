package com.yushilei.nestedscrolling;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * @author by  yushilei.
 * @time 2016/9/1 -11:00.
 * @Desc
 */
public class Adapter extends RecyclerView.Adapter<Adapter.VH> {
    List<String> data;
    Context mContext;

    public Adapter(Context context, List<String> data) {
        mContext = context;
        this.data = data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        VH tag = new VH(view);
        view.setTag(tag);
        return tag;
    }

    String TAG = "Adapter";

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        holder.text.setText(data.get(position));
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class VH extends RecyclerView.ViewHolder {

        private TextView text;

        public VH(View itemView) {
            super(itemView);
            text = ((TextView) itemView.findViewById(R.id.text_item));
        }
    }
}
