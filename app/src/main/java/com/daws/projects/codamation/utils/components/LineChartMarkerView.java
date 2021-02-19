package com.daws.projects.codamation.utils.components;

import android.content.Context;
import android.widget.TextView;

import com.daws.projects.codamation.R;
import com.daws.projects.codamation.helpers.TextHelper;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class LineChartMarkerView extends MarkerView {
    private TextView valueText;
    private MPPointF mOffset;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public LineChartMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        valueText = findViewById(R.id.chartValue);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        valueText.setText(TextHelper.newInstance().separatorValue((int) e.getY())); // set the entry-value as the display text

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
}
