package com.example.mylibrary;


import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class AutocompleteFilter extends Filter {
    private final int foundedTextColor;
    private final FilterResultsListener filterResultsListener;
    private final int typeface;
    private String[] strings;

    AutocompleteFilter(@NonNull List<String> serverList, @NonNull FilterResultsListener filterResultsListener, int typeface, int foundedTextColor) {
        strings = new String[serverList.size()];
        this.typeface = typeface;
        serverList.toArray(strings);
        this.filterResultsListener = filterResultsListener;
        this.foundedTextColor = foundedTextColor;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        if (constraint != null) {
            List<SpannableString> servers = new ArrayList<>();
            for (String server : strings) {
                SpannableString result = findAndModifySameRegion(server, constraint);
                if (result != null) {
                    servers.add(result);
                }
            }
            // Now assign the values and count to the FilterResults object
            filterResults.values = servers;
            filterResults.count = servers.size();
        }
        return filterResults;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence contraint, FilterResults results) {
        filterResultsListener.onResult((List<SpannableString>) results.values);
    }

    /**
     * @param currentRegion Origin word
     * @param constraint    Object which should be found.
     *                      In case if find, it will be modified bold style.
     * @return SpannableString result  part of modified to bolt style.
     * Will be returned null if not found same region.
     */
    private SpannableString findAndModifySameRegion(@NonNull String currentRegion, CharSequence constraint) {
        String findMe = constraint.toString();
        int searchMeLength = currentRegion.length();
        int findMeLength = findMe.length();
        for (int i = 0; i <= (searchMeLength - findMeLength); i++) {
            if (currentRegion.regionMatches(i, findMe, 0, findMeLength)) {
                return getSpannableString(currentRegion, findMeLength, i);
            }
        }
        return null;
    }

    @NonNull
    private SpannableString getSpannableString(@NonNull String currentRegion, int findMeLength, int i) {
        SpannableString spannableString = new SpannableString(currentRegion);
        spannableString.setSpan(new StyleSpan(typeface), i, i + findMeLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (foundedTextColor != -1) {
            spannableString.setSpan(new ForegroundColorSpan(foundedTextColor), i, i + findMeLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    /**
     * Callback with will be called when new publishResults exists
     */
    interface FilterResultsListener {

        /**
         * @param results list of String with matches with input text
         */
        void onResult(List<SpannableString> results);
    }
}