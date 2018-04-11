package com.example.alexandr_lenivenko.autocomplete;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AutoCompleteTextView;

import com.example.mylibrary.AutocompleteAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> list = new ArrayList<>(9);
        list.add("Hello");
        list.add("Hello w");
        list.add("Hello wo");
        list.add("Hello wor");
        list.add("Hello worl");
        list.add("Hello world");
        list.add("Hello world!");
        list.add("Hello world!!");
        list.add("Hello world!!!");

        AutoCompleteTextView autocomplete = findViewById(R.id.autocomplete);
        autocomplete.setThreshold(1);
        autocomplete.setAdapter(
                AutocompleteAdapter.newBuilder(this, list)
                        .setTypeface(Typeface.BOLD_ITALIC)
                        .setTextColor(Color.DKGRAY)
                        .setFoundedTextColor(Color.BLACK)
                        .build());
    }
}
