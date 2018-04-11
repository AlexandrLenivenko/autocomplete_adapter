package com.example.mylibrary;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AutocompleteAdapter extends ArrayAdapter<SpannableString>
        implements AutocompleteFilter.FilterResultsListener {
    private final Filter filter;
    private final int textColor;
    private final AdapterItemListener adapterItemListener;

    private AutocompleteAdapter(@NonNull Context context,
                                @NonNull List<SpannableString> serverList,
                                int layout, List<String> stringList,
                                int typeface, int textColor, int foundedTextColor, AdapterItemListener adapterItemListener) {
        super(context, layout, serverList);
        this.adapterItemListener = adapterItemListener;
        this.filter = new AutocompleteFilter(stringList, this, typeface, foundedTextColor);
        this.textColor = textColor;
    }

    public static Builder newBuilder(Context context, List<String> list) {
        return new AutocompleteAdapter.Builder(context, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = view.findViewById(android.R.id.text1);
        if (textView != null) {
            textView.setTextColor(textColor);
        }
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public void onResult(List<SpannableString> results) {
        if (results != null && results.size() > 0) {
            clear();
            addAll(results);
            notifyDataSetChanged();
            sendCallBack(results);
        } else {
            notifyDataSetInvalidated();
        }
    }

    private void sendCallBack(List<SpannableString> results) {
        if (adapterItemListener != null) {
            List<String> list = new ArrayList<>(results.size());

            for (int i = 0; i < results.size(); i++) {
                list.add(results.get(i).toString());
            }
            adapterItemListener.onItemsFound(list);
        }
    }

    public interface AdapterItemListener {
        void onItemsFound(List<String> results);
    }

    public static class Builder {

        private final Context context;
        private final List<String> list;
        private int layout = R.layout.item_autocomplite_server;
        private int typeface = Typeface.BOLD;
        private int textColor = Color.BLUE;
        private int foundedTextColor = -1;
        private AdapterItemListener adapterItemListener;

        Builder(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        public Builder setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder setTypeface(int typeface) {
            this.typeface = typeface;
            return this;
        }

        public Builder setLayout(int layout) {
            this.layout = layout;
            return this;
        }

        public Builder setAdapterItemListener(AdapterItemListener adapterItemListener) {
            this.adapterItemListener = adapterItemListener;
            return this;
        }

        public Builder setFoundedTextColor(int foundedTextColor) {
            this.foundedTextColor = foundedTextColor;
            return this;
        }

        public AutocompleteAdapter build() {
            List<SpannableString> spannableList = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); i++) {
                spannableList.add(new SpannableString(list.get(i)));
            }
            return new AutocompleteAdapter(context,
                    spannableList, layout, list, typeface, textColor, foundedTextColor, adapterItemListener);
        }
    }
}
