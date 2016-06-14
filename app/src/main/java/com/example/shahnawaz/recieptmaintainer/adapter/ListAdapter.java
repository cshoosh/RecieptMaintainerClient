package com.example.shahnawaz.recieptmaintainer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shahnawaz.recieptmaintainer.R;
import com.example.shahnawaz.recieptmaintainer.model.Data;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shahnawaz on 6/13/2016.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<Data> mData;

    public ListAdapter(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Data>>() {
        }.getType();
        mData = gson.fromJson(json, type);
    }

    public ListAdapter(List<Data> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.amount.setText(String.format(Locale.US, "%d", mData.get(position).getAmount()));
        holder.description.setText(mData.get(position).getDescription());
        holder.itemView.setTag(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView amount;
        TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            amount = (TextView) itemView.findViewById(R.id.txtAmount);
            description = (TextView) itemView.findViewById(R.id.txtDesc);
        }
    }
}
