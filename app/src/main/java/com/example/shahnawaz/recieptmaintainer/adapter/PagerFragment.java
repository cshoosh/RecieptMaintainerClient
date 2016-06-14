package com.example.shahnawaz.recieptmaintainer.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shahnawaz.recieptmaintainer.MainActivity;
import com.example.shahnawaz.recieptmaintainer.R;
import com.example.shahnawaz.recieptmaintainer.model.Data;
import com.example.shahnawaz.recieptmaintainer.retro.API;

import java.util.List;

/**
 * Created by Shahnawaz on 6/14/2016.
 */
public class PagerFragment extends Fragment {
    public static final String KEY_TYPE = "keyTyope";
    private int type = 0;
    private RecyclerView recyclerView;

    API.ListResponse<List<Data>> response = new API.ListResponse<List<Data>>() {
        @Override
        public void onResponse(List<Data> data) {
            recyclerView.setAdapter(new ListAdapter(data));
        }

        @Override
        public void onFailure() {
            ((MainActivity) getActivity()).showMessage();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            type = getArguments().getInt(KEY_TYPE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.recycler, container, false);
        recyclerView = (RecyclerView) ret.findViewById(android.R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refresh();
        return ret;
    }

    public void refresh() {
        if (type == 0)
            new API().getCredit(response);
        else
            new API().getDebit(response);
    }
}
