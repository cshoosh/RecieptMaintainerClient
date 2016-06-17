package com.example.shahnawaz.recieptmaintainer.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shahnawaz.recieptmaintainer.R;
import com.example.shahnawaz.recieptmaintainer.UpdateFrag;
import com.example.shahnawaz.recieptmaintainer.model.Data;
import com.example.shahnawaz.recieptmaintainer.retro.API;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shahnawaz on 6/13/2016.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> implements View.OnClickListener {
    private List<Data> mData;
    private Context mContext;

    public ListAdapter(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Data>>() {
        }.getType();
        mData = gson.fromJson(json, type);
    }

    public ListAdapter(Context context, List<Data> data) {
        mData = data;
        mContext = context;
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
        holder.overflow.setOnClickListener(this);
        holder.overflow.setTag(mData.get(position));
        holder.itemView.setTag(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(final View v) {

        switch (v.getId()) {
            case android.R.id.icon:
                PopupMenu menu = new PopupMenu(v.getContext(), v);
                menu.inflate(R.menu.menu_list);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final Data data = (Data) v.getTag();
                        switch (item.getItemId()) {
                            case R.id.action_delete:
                                new AlertDialog.Builder(mContext).setMessage("Are you sure you want to continue")
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                new API().delete(new API.ListResponse<String>() {
                                                    @Override
                                                    public void onResponse(String data) {
                                                        mData.remove(data);
                                                        notifyDataSetChanged();
                                                    }

                                                    @Override
                                                    public void onFailure() {
                                                    }
                                                }, data.get_id());
                                            }
                                        }).setNegativeButton("NO", null).show();


                                break;
                            case R.id.action_update:
                                UpdateFrag frag = UpdateFrag.newInstance(data.get_id(), data.getCredit()
                                        , data.getAmount(), data.getDescription());
                                if (mContext instanceof FragmentActivity)
                                    frag.show(((FragmentActivity) mContext).getSupportFragmentManager(), null);

                                break;
                        }
                        return false;
                    }
                });
                menu.show();
                break;
            default:
                break;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView amount;
        TextView description;
        ImageView overflow;

        public ViewHolder(View itemView) {
            super(itemView);
            amount = (TextView) itemView.findViewById(R.id.txtAmount);
            description = (TextView) itemView.findViewById(R.id.txtDesc);
            overflow = (ImageView) itemView.findViewById(android.R.id.icon);
        }
    }
}
