package com.example.pascal.bierlijst20;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class MyFormatterThingy extends ValueFormatter {

    public MyFormatterThingy(){

    }
    @Override
    public String getFormattedValue(float value) {
        return "" + ((int) value);
    }
}
