package com.daws.projects.codamation.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Region;
import android.util.Log;

import androidx.lifecycle.ViewModelProviders;

import com.daws.projects.codamation.GlobalVariable;
import com.daws.projects.codamation.R;
import com.daws.projects.codamation.adapters.SimpleRecyclerAdapter;
import com.daws.projects.codamation.databinding.ActivityLocalCasePatientBinding;
import com.daws.projects.codamation.databinding.ItemPatientBinding;
import com.daws.projects.codamation.models.PatientModel;
import com.daws.projects.codamation.models.RegionModel;
import com.daws.projects.codamation.models.SummaryRegionCaseModel;
import com.daws.projects.codamation.viewmodels.PatientViewModel;
import com.daws.projects.codamation.viewmodels.SummaryRegionCaseViewModel;
import com.daws.projects.codamation.views.bottomsheets.RegionBottomSheet;

import java.util.ArrayList;
import java.util.List;

public class LocalCasePatientActivity extends BaseActivity<ActivityLocalCasePatientBinding> implements RegionBottomSheet.RegionBottomSheetListener {

    private PatientViewModel patientViewModel;
    private SummaryRegionCaseViewModel regionViewModel;

    private List<RegionModel> regionModelList;
    private List<PatientModel> patientList;
    private List<PatientModel> filteredPatientList;
    private SimpleRecyclerAdapter<PatientModel> patientAdapter;

    private RegionBottomSheet regionBottomSheet;

    @Override
    protected int attachLayout() {
        return R.layout.activity_local_case_patient;
    }

    @Override
    protected void initData() {
        super.initData();
        regionModelList = new ArrayList<>();
        patientList = new ArrayList<>();
        filteredPatientList = new ArrayList<>();
        patientAdapter = new SimpleRecyclerAdapter<>(
                R.layout.item_patient,
                ((holder, item) -> {
                    ItemPatientBinding binding = (ItemPatientBinding) holder.getLayoutBinding();

                    if (regionModelList != null){
                        for (RegionModel regionModel : regionModelList){
                            if (regionModel.getRegionId() == item.getRegionId())
                                item.setRegionName(regionModel.getRegionName());
                        }
                    }

                    binding.setPatientModel(item);
                })
        );

        patientViewModel = ViewModelProviders.of(this).get(PatientViewModel.class);
        regionViewModel = ViewModelProviders.of(this).get(SummaryRegionCaseViewModel.class);
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        binding.setLoading(true);

        regionViewModel.getSummaryRegionCaseLiveData(true)
                .observe(this, regionCaseModelList -> {
                    RegionModel netralRegion = new RegionModel();
                    netralRegion.setRegionId(GlobalVariable.CODE_NEUTRAL_REGION);
                    netralRegion.setRegionName("Semua Provinsi");
                    regionModelList.add(netralRegion);

                    for (SummaryRegionCaseModel regionCaseModel : regionCaseModelList){
                        RegionModel regionModel = new RegionModel();
                        regionModel.setRegionId(regionCaseModel.getRegionCode());
                        regionModel.setRegionName(regionCaseModel.getRegionName());

                        regionModelList.add(regionModel);
                    }

                    patientViewModel.getPatientModelLiveData()
                            .observe(this, patientModelList -> {
                                patientList = patientModelList;
                                patientAdapter.setMainData(patientList);

                                binding.setLoading(false);
                            });
        });

        binding.setPatientAdapter(patientAdapter);
        binding.location.setOnClickListener(v -> {

            RegionBottomSheet.Builder regioBottomSheetBuilder =
                    new RegionBottomSheet.Builder()
                            .setRegionModelList(regionModelList).setListener(this);

            regionBottomSheet = regioBottomSheetBuilder.build(this);
            regionBottomSheet.show();
        });
    }

    @Override
    public void onRegionSelected(RegionModel regionModel) {
        if (regionBottomSheet != null){
            if (regionBottomSheet.isShow())
                regionBottomSheet.dismiss();
        }

        if (regionModel != null){
            refreshPatientList(regionModel);
            binding.locationText.setText(regionModel.getRegionName());
        }
    }

    private void refreshPatientList(RegionModel regionModel){
        binding.setLoading(true);

        filteredPatientList = new ArrayList<>();

        if (regionModel.getRegionId() == GlobalVariable.CODE_NEUTRAL_REGION){
            filteredPatientList = patientList;
        }
        else {
            for (PatientModel patientModel : patientList){
                if (patientModel.getRegionId() == regionModel.getRegionId())
                    filteredPatientList.add(patientModel);
            }
        }

        patientAdapter.setMainData(filteredPatientList);
        binding.setLoading(false);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
