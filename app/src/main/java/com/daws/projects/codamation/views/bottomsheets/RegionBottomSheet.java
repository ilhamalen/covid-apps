package com.daws.projects.codamation.views.bottomsheets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.DataBindingUtil;

import com.daws.projects.codamation.R;
import com.daws.projects.codamation.adapters.SimpleRecyclerAdapter;
import com.daws.projects.codamation.databinding.BottomsheetRegionListBinding;
import com.daws.projects.codamation.databinding.ItemRegionBinding;
import com.daws.projects.codamation.models.RegionModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class RegionBottomSheet extends BaseObservable {

    private Context context;
    private BottomSheetDialog bottomSheetDialog;
    private List<RegionModel> regionModelList;
    private RegionBottomSheetListener listener;
    private RegionModel selectedRegion;

    private SimpleRecyclerAdapter<RegionModel> regionAdapter;

    public interface RegionBottomSheetListener{
        void onRegionSelected(RegionModel regionModel);
    }

    public static class Builder {
        private List<RegionModel> regionModelList;
        private RegionBottomSheetListener listener;

        public Builder setRegionModelList(List<RegionModel> regionModelList){
            this.regionModelList = regionModelList;
            return this;
        }

        public Builder setListener(RegionBottomSheetListener listener){
            this.listener = listener;
            return this;
        }

        public RegionBottomSheet build (Context context) {
            return new RegionBottomSheet(context, this);
        }
    }

    public RegionBottomSheet(Context context, Builder builder){
        setContext(context);
        setRegionModelList(builder.regionModelList);
        setListener(builder.listener);

        bindLayout();
    }

    private void bindLayout(){
        BottomsheetRegionListBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.bottomsheet_region_list,
                null, false
        );

        regionAdapter = new SimpleRecyclerAdapter<>(
                getRegionModelList(),
                R.layout.item_region,
                ((holder, item) -> {
                    ItemRegionBinding itemRegionBinding = (ItemRegionBinding) holder.getLayoutBinding();
                    itemRegionBinding.setRegionModel(item);
                    itemRegionBinding.mainLayout.setOnClickListener(v -> {
                        selectedRegion = item;
                        item.setSelected(true);
                        refreshAdapter(item);
                        listener.onRegionSelected(selectedRegion);
                    });
                })
        );

        binding.setRegionAdapter(regionAdapter);
        binding.close.setOnClickListener(v -> dismiss());

        bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomAlertTheme);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setContentView(binding.getRoot());
    }

    private void refreshAdapter(RegionModel regionModel){

        for (RegionModel model : regionAdapter.getMainData()){
            int menuPosition = regionAdapter.getMainData().indexOf(model);
            model.setSelected(false);
            changeItemValueInsideAdapter(menuPosition, model);
        }

        int position = regionAdapter.getMainData().indexOf(regionModel);
        regionModel.setSelected(true);
        changeItemValueInsideAdapter(position, regionModel);
    }

    private void changeItemValueInsideAdapter(int position, RegionModel regionModel){
        regionAdapter.getMainData().set(position, regionModel);
        regionAdapter.notifyItemChanged(position);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setRegionModelList(List<RegionModel> regionModelList) {
        this.regionModelList = regionModelList;
    }

    public List<RegionModel> getRegionModelList() {
        return regionModelList;
    }

    public void setListener(RegionBottomSheetListener listener) {
        this.listener = listener;
    }

    public RegionBottomSheetListener getListener() {
        return listener;
    }

    public boolean isShow(){
        return bottomSheetDialog.isShowing();
    }

    public void show(){
        bottomSheetDialog.show();
    }

    public void dismiss(){
        bottomSheetDialog.dismiss();
    }
}
