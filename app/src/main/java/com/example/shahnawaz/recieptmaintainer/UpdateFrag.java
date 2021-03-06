package com.example.shahnawaz.recieptmaintainer;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shahnawaz.recieptmaintainer.retro.API;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class UpdateFrag extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String KEY_TYPE = "keyUpdateType";
    private static final String KEY_ID = "keyID";
    private static final String KEY_AMOUNT = "keyAmount";
    private static final String KEY_DESC = "keyDesc";

    public static UpdateFrag newInstance(int id, int type, int amount, @NonNull String desc) {

        Bundle args = new Bundle();

        UpdateFrag fragment = new UpdateFrag();
        args.putInt(KEY_ID, id);
        args.putInt(KEY_TYPE, type);
        args.putInt(KEY_AMOUNT, amount);
        args.putString(KEY_DESC, desc);
        fragment.setArguments(args);
        return fragment;
    }

    private EditText amount, desc;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, getTheme());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.dialog_update, container, false);
        ret.findViewById(R.id.btnOK).setOnClickListener(this);
        amount = (EditText) ret.findViewById(R.id.edtAmount);
        desc = (EditText) ret.findViewById(R.id.edtDesc);

        if (getArguments().getInt(KEY_AMOUNT) != 0)
            amount.setText(String.format(Locale.US, "%d", getArguments().getInt(KEY_AMOUNT)));
        desc.setText(getArguments().getString(KEY_DESC));
        final TextView labelAmount = (TextView) ret.findViewById(R.id.lblAmount);

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    int number = Integer.parseInt(s.toString());
                    labelAmount.setText(MainActivity.NUMBER_FORMAT.format(number));
                } else {
                    labelAmount.setText("");
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
                    map.put("id", getArguments().getInt(KEY_ID) + "");
                    map.put("amount", amount.getText().toString());
                    map.put("desc", desc.getText().toString());
                    map.put("credit", getArguments().getInt(KEY_TYPE) + "");
                    map.put("date", Details.FORMAT_FROM.format(new Date()));
                    new API().addUpdate(new API.ListResponse<String>() {
                        @Override
                        public void onResponse(String data) {
                            if (data.equals("1") && getActivity() instanceof MainActivity)
                                ((MainActivity) getActivity()).refresh();
                        }

                        @Override
                        public void onFailure() {

                        }
                    }, map, getArguments().getInt(KEY_ID) != 0);
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), "Please Insert Amount and Description", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }
}
