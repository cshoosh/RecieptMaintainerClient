package com.example.shahnawaz.recieptmaintainer;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shahnawaz.recieptmaintainer.retro.API;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Shahnawaz on 6/13/2016.
 */
public class UpdateFrag extends DialogFragment implements View.OnClickListener {
    public static final String KEY_TYPE = "keyUpdateType";
    public static final String KEY_ID = "keyID";

    private int type = 0;
    private int id = 0;

    private EditText amount, desc;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, getTheme());
        if (getArguments() != null) {
            type = getArguments().getInt(KEY_TYPE, 0);
            id = getArguments().getInt(KEY_ID, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.dialog_update, container, false);
        ret.findViewById(R.id.btnOK).setOnClickListener(this);
        amount = (EditText) ret.findViewById(R.id.edtAmount);
        desc = (EditText) ret.findViewById(R.id.edtDesc);
        final TextView labelAmount = (TextView) ret.findViewById(R.id.lblAmount);

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    int number = Integer.parseInt(s.toString());
                    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "PK"));
                    labelAmount.setText(formatter.format(number));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return ret;
    }

    @Override
    public Dialog getDialog() {
        Dialog dialog = super.getDialog();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOK:
                if (!amount.getText().toString().isEmpty() && !desc.getText().toString().isEmpty()) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("id", id + "");
                    map.put("amount", amount.getText().toString());
                    map.put("desc", desc.getText().toString());
                    map.put("credit", type + "");
                    new API().addUpdate(new API.ListResponse<String>() {
                        @Override
                        public void onResponse(String data) {
                            if (data.equals("1") && getActivity() instanceof MainActivity)
                                ((MainActivity) getActivity()).refresh();
                        }

                        @Override
                        public void onFailure() {

                        }
                    }, map, id != 0);
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), "Please Insert Amount and Description", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }
}
