package com.daws.projects.codamation.helpers;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class TextHelper {

    private TextHelper(){}

    public static TextHelper newInstance(){
        TextHelper textHelper = new TextHelper();

        return textHelper;
    }

    public String separatorValue(int originalValue) {
        String pattern = "###,###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        return decimalFormat.format(originalValue);
    }
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "K");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public String formatSimpleNumber(int originalValue) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        Long value = Long.valueOf(originalValue);
        if (value == Long.MIN_VALUE) return formatSimpleNumber((int)Long.MIN_VALUE + 1);
        if (value < 0) return "-" + formatSimpleNumber((int) -value);
        if (value < 9999) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public String requestString(List<String> requestStringList){
        StringBuilder requestStringBuilder = new StringBuilder();

        for (String requestString : requestStringList){
            requestStringBuilder.append(requestString);
            if (requestStringList.indexOf(requestString) != requestStringList.size() - 1)
                requestStringBuilder.append(";");
        }

        return requestStringBuilder.toString();
    }
}
