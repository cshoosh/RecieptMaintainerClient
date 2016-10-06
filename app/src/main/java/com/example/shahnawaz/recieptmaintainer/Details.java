package com.example.shahnawaz.recieptmaintainer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shahnawaz.recieptmaintainer.model.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Details extends BottomSheetDialogFragment {

    public static final SimpleDateFormat FORMAT_TO = new SimpleDateFormat("dd/MM/yyy HH:mm", Locale.US);
    public static final SimpleDateFormat FORMAT_FROM = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    public static Details newInstance(Data data) {

        Bundle args = new Bundle();
        args.putParcelable("data", data);

        Details fragment = new Details();
        fragment.setArguments(args);
        return fragment;
    }

    private Data mData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = getArguments().getParcelable("data");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.details, container, false);
        String user = mData.getUser() == 1 ? "Shahnawaz" : "Usaid";
        ((TextView) ret.findViewById(R.id.txtUpdate)).setText(user);
        ((TextView) ret.findViewById(R.id.txtDesc)).setText(mData.getDescription());
        ((TextView) ret.findViewById(R.id.txtAmount)).setText(String.format(Locale.US, "%d", mData.getAmount()));
        String date = FORMAT_TO.format(new Date());
        try {
            date = FORMAT_TO.format(FORMAT_FROM.parse(mData.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ((TextView) ret.findViewById(R.id.txtDate)).setText(date);
        String type = mData.getCredit() == 0 ? "Credit" : "Paid";
        ((TextView) ret.findViewById(R.id.txtType)).setText(type);
        return ret;
    }
}
