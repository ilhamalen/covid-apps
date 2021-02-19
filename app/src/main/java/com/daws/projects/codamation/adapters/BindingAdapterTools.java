package com.daws.projects.codamation.adapters;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daws.projects.codamation.GlobalVariable;
import com.daws.projects.codamation.R;
import com.daws.projects.codamation.helpers.ValidationHelper;
import com.daws.projects.codamation.utils.components.CenterZoomLinearLayout;
import com.daws.projects.codamation.utils.components.GridSpacingItemDecorationUtil;

import java.text.DecimalFormat;

public class BindingAdapterTools {

    @BindingAdapter({"srcCompat"})
    public static void setImageSrcCompat(ImageView imageView, int drawable){
        imageView.setImageResource(drawable);
    }

    @BindingAdapter("setupHorizontalZoomRecyclerView")
    public static void setupHorizontalZoomRecyclerView(RecyclerView recyclerView, float margin){
        CenterZoomLinearLayout centerZoomLinearLayout = new CenterZoomLinearLayout(recyclerView.getContext(), RecyclerView.HORIZONTAL, false);
        centerZoomLinearLayout.setmShrinkDistance(margin);
        recyclerView.setLayoutManager(centerZoomLinearLayout);
        recyclerView.addItemDecoration(
                new GridSpacingItemDecorationUtil(margin, "horizontal")
        );
    }

    @BindingAdapter("setupHorizontalRecyclerView")
    public static void setupHorizontalRecyclerView(RecyclerView recyclerView, float margin){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.addItemDecoration(
                new GridSpacingItemDecorationUtil(margin, "horizontal")
        );
    }

    @BindingAdapter({"setupVerticalRecyclerView"})
    public static void setupVerticalRecyclerView(RecyclerView recyclerView, float margin){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(
                new GridSpacingItemDecorationUtil(margin, "vertical")
        );
    }

    @BindingAdapter({"setupGrowthTextColor"})
    public static void setupGrowthTextColor(TextView textView, double growthValue){
        Integer textColor;
        Double value;
        String valueInString;

        if (ValidationHelper.checkNotNullOrEmpty(growthValue)){
            value = growthValue;

            if (value > 0)
                textColor = ContextCompat.getColor(textView.getContext(), R.color.error);
            else
                textColor = ContextCompat.getColor(textView.getContext(), R.color.colorPrimary);
        }
        else {
            value = 0D;
            textColor = ContextCompat.getColor(textView.getContext(), R.color.colorPrimary);
        }

        valueInString = new DecimalFormat("##.##").format(value) + "%";
        textView.setText(valueInString);
        textView.setTextColor(textColor);
    }

    @BindingAdapter({"setupGrowthIcon"})
    public static void setupGrowthIcon(ImageView imageView, double growthValue){
        if (growthValue > 0)
            imageView.setImageResource(R.drawable.ic_arrow_upward_red);
        else
            imageView.setImageResource(R.drawable.ic_arrow_downward_green);
    }

    @BindingAdapter({"setupStatusTextColor"})
    public static void setupStatusTextColor(TextView textView, String status){
        int textColor;

        if (ValidationHelper.checkNotNullOrEmpty(status)){
            if (status.equalsIgnoreCase(GlobalVariable.STATUS_ACTIVE))
                textColor = ContextCompat.getColor(textView.getContext(), R.color.pending);
            else if (status.equalsIgnoreCase(GlobalVariable.STATUS_RECOVER))
                textColor = ContextCompat.getColor(textView.getContext(), R.color.colorPrimary);
            else if (status.equalsIgnoreCase(GlobalVariable.STATUS_DEATH))
                textColor = ContextCompat.getColor(textView.getContext(), R.color.error);
            else
                textColor = ContextCompat.getColor(textView.getContext(), R.color.textGrey);
        }
        else{
            textColor = ContextCompat.getColor(textView.getContext(), R.color.textGrey);
            textView.setText("-");
        }

        textView.setTextColor(textColor);
        textView.setText(status);
    }

    @BindingAdapter({"setupGenderIcon"})
    public static void setupGenderIcon(ImageView imageView, String gender){
        if (ValidationHelper.checkNotNullOrEmpty(gender)){
            if (gender.equalsIgnoreCase(GlobalVariable.GENDER_FEMALE))
                imageView.setImageResource(R.drawable.ic_female);
            else
                imageView.setImageResource(R.drawable.ic_male);
        }
        else
            imageView.setImageResource(R.drawable.ic_male);
    }
}
