package com.noor.mystore99;

import android.widget.ArrayAdapter;

    import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SpinnerAdapter extends ArrayAdapter<cartItem> {
        public SpinnerAdapter(Context context, ArrayList<cartItem> countryList) {
            super(context, 0, countryList);
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return initView(position, convertView, parent);
        }
        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return initView(position, convertView, parent);
        }
        private View initView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.spinner_row, parent, false
                );
            }
            TextView textViewName = convertView.findViewById(R.id.lName);
            TextView textViewName1 = convertView.findViewById(R.id.lQuant);
            TextView textViewName2 = convertView.findViewById(R.id.lTotal);
            TextView textViewName3 = convertView.findViewById(R.id.lWeight);
            cartItem currentItem = getItem(position);
            if (currentItem != null) {
                textViewName.setText(String.valueOf(currentItem.getName()));
                textViewName1.setText(String.valueOf(currentItem.getQuant()));
                textViewName2.setText(String.valueOf("₹ "+currentItem.getTotal()));
                if(currentItem.getWeight().equals("Per Kg")){
                    textViewName3.setText(("1 KG"));
                }
                else {
                    textViewName3.setText(String.valueOf(currentItem.getWeight()));
                }
            }
            return convertView;
        }
    }
