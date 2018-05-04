package com.example.hongjo.myfinancialmanager;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hongjo.myfinancialmanager.database.DataProvider;
import com.example.hongjo.myfinancialmanager.database.InCategoryTable;
import com.example.hongjo.myfinancialmanager.tools.BigDecimalCalculator;

public class EarningPlansAddFragment extends DialogFragment {

        private DataEnterListener mListener;

        EditText category_name;
        EditText category_amount;
        Button cancel;
        ImageView delete, edit;

        public void onAttach(Activity activity){
            super.onAttach(activity);
            mListener = (DataEnterListener) activity;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){

            View v = inflater.inflate(R.layout.fragment_category_add, container, false);

            TextView title = v.findViewById(R.id.category_dialog_title);

            delete = v.findViewById(R.id.category_delete);
            edit = v.findViewById(R.id.category_edit);
            delete.setVisibility(View.INVISIBLE);
            edit.setColorFilter(R.color.colorPrimaryDark);
            cancel = v.findViewById(R.id.category_detail_dialog_cancel);
            edit.setOnClickListener(listener);
            cancel.setOnClickListener(listener);
            title.setText(R.string.add_category);

            category_name = v.findViewById(R.id.category_dialog_name);
            category_amount = v.findViewById(R.id.category_dialog_amount);

            return v;
        }

        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int id = v.getId();
                switch(id){
                    case R.id.category_detail_dialog_cancel:
                        dismiss();
                        break;
                    case R.id.category_edit:
                        String name = category_name.getText().toString();
                        double amount;

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        String currencyCode = prefs.getString("CURRENCY", "Canada");

                        if(category_amount.getText().toString().trim().length()==0) {
                            amount = BigDecimalCalculator.roundValue(0.0, currencyCode);
                        }
                        else{
                            try {
                                amount = BigDecimalCalculator.roundValue(Double.parseDouble
                                        (category_amount.getText().toString()), currencyCode);
                            }catch(Exception e){
                                amount = BigDecimalCalculator.roundValue(0.0, currencyCode);
                            }
                        }
                        ContentValues values = new ContentValues();
                        values.put(InCategoryTable.COL2, name);
                        values.put(InCategoryTable.COL3, String.valueOf(amount));
                        getActivity().getContentResolver().insert(DataProvider.IN_CATEGORY_URI, values);
                        mListener.onDataEnterComplete();
                        dismiss();
                        break;
                }
            }
        };

        public interface DataEnterListener{
            void onDataEnterComplete();
        }
    }
