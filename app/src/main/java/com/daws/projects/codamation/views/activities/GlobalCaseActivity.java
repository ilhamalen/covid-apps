package com.daws.projects.codamation.views.activities;

import android.app.Activity;
import android.content.Intent;

import androidx.lifecycle.ViewModelProviders;

import com.daws.projects.codamation.R;
import com.daws.projects.codamation.adapters.SimpleRecyclerAdapter;
import com.daws.projects.codamation.databinding.ActivityGlobalCaseBinding;
import com.daws.projects.codamation.databinding.ItemCaseBinding;
import com.daws.projects.codamation.helpers.UIHelper;
import com.daws.projects.codamation.models.RegionDailyCaseModel;
import com.daws.projects.codamation.models.SummaryRegionCaseModel;
import com.daws.projects.codamation.viewmodels.DailyCaseViewModel;
import com.daws.projects.codamation.viewmodels.SummaryGlobalCaseViewModel;
import com.daws.projects.codamation.viewmodels.SummaryRegionCaseViewModel;
import com.github.mikephil.charting.data.Entry;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GlobalCaseActivity extends BaseActivity<ActivityGlobalCaseBinding> {

    private SummaryGlobalCaseViewModel summaryGlobalCaseViewModel;
    private SummaryRegionCaseViewModel regionCaseViewModel;
    private DailyCaseViewModel dailyCaseViewModel;

    private ArrayList<Entry> dailyValues;
    private List<SummaryRegionCaseModel> regionCaseList;
    private List<SummaryRegionCaseModel> filteredRegionCaseList;
    private SimpleRecyclerAdapter<SummaryRegionCaseModel> regionAdapter;
    private List<RegionDailyCaseModel> regionDailyCaseModelList;

    private double caseGrowthValue;
    private int currentCaseValue;
    private int comparatorCaseValue;
    private int incrementIndex;
    private int lastIndex;
    private boolean refreshed;

    @Override
    protected int attachLayout() {
        return R.layout.activity_global_case;
    }

    @Override
    protected void initData() {
        super.initData();

        dailyValues = new ArrayList<>();
        regionDailyCaseModelList = new ArrayList<>();
        filteredRegionCaseList = new ArrayList<>();
        regionCaseList = new ArrayList<>();
        lastIndex = 0;
        incrementIndex = 25;
        regionAdapter = new SimpleRecyclerAdapter<>(
                R.layout.item_case,
                ((holder, item) -> {
                    ItemCaseBinding binding = (ItemCaseBinding) holder.getLayoutBinding();
                    binding.setRegionalCaseModel(item);
                    binding.date.setText(item.getLastUpdateString());
                })
        );

        regionCaseViewModel = ViewModelProviders.of(this).get(SummaryRegionCaseViewModel.class);
        summaryGlobalCaseViewModel = ViewModelProviders.of(this).get(SummaryGlobalCaseViewModel.class);
        dailyCaseViewModel = ViewModelProviders.of(this).get(DailyCaseViewModel.class);
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        getRegionData();
        binding.setCaseAdapter(regionAdapter);
        binding.refreshRegion.setOnRefreshListener(direction -> {
            binding.refreshRegion.setRefreshing(true);
            refreshed = true;

            if (direction == SwipyRefreshLayoutDirection.BOTTOM)
                loadMoreRegionData(lastIndex, incrementIndex);
            else
                getRegionData();
        });
        binding.back.setOnClickListener(v -> onBackPressed());
    }

    private void getRegionData(){

        binding.setLoading(true);

        summaryGlobalCaseViewModel.getGlobalSummaryCaseLiveData()
                .observe(this, globalSummaryCaseModel -> {

                    int activeCase = globalSummaryCaseModel.getConfirmed() - globalSummaryCaseModel.getRecovered() - globalSummaryCaseModel.getDeath();

                    RegionDailyCaseModel summaryCaseModel = new RegionDailyCaseModel();
                    summaryCaseModel.setTotalCase(globalSummaryCaseModel.getConfirmed());
                    summaryCaseModel.setActiveCase(activeCase);
                    summaryCaseModel.setActiveCasePercentage(((double) activeCase / (double) globalSummaryCaseModel.getConfirmed() * 100));
                    summaryCaseModel.setRecoveredCase(globalSummaryCaseModel.getRecovered());
                    summaryCaseModel.setRecoveryCasePercentage(((double) globalSummaryCaseModel.getRecovered() / (double) globalSummaryCaseModel.getConfirmed() * 100));
                    summaryCaseModel.setDeathCase(globalSummaryCaseModel.getDeath());
                    summaryCaseModel.setDeathCasePercentage(((double)globalSummaryCaseModel.getDeath() / (double)globalSummaryCaseModel.getConfirmed() * 100));
                    binding.setGlobalModel(summaryCaseModel);
                });

        regionCaseViewModel.getSummaryRegionCaseLiveData(false)
                .observe(this, countryModelList -> {
                    regionCaseList = countryModelList;
                    filteredRegionCaseList = new ArrayList<>();

                    Collections.sort(regionCaseList, (regionA, regionB) -> regionB.getCasePositive()-regionA.getCasePositive());

                    for (SummaryRegionCaseModel regionCaseModel : regionCaseList){
                        if (regionCaseList.indexOf(regionCaseModel) < incrementIndex)
                            filteredRegionCaseList.add(regionCaseModel);
                    }
                    lastIndex = incrementIndex;

                    regionAdapter.setMainData(filteredRegionCaseList);
                    binding.setLoading(false);
                    binding.refreshRegion.setRefreshing(false);
        });

        dailyCaseViewModel.getDailyCaseLiveData(false)
                .observe(this, dailyCaseModelList->{

                    if (dailyCaseModelList.size() > 0){
                        regionDailyCaseModelList = dailyCaseModelList;
                        dailyValues.clear();

                        for (RegionDailyCaseModel caseModel : regionDailyCaseModelList){
                            dailyValues.add(new Entry(regionDailyCaseModelList.indexOf(caseModel), (float)caseModel.getTotalCase()));

                            if (regionDailyCaseModelList.indexOf(caseModel) == 0)
                                binding.setStartDate(caseModel.getFormattedDate(caseModel.getDateInString()));
                            else if (regionDailyCaseModelList.indexOf(caseModel) == regionDailyCaseModelList.size() - 1)
                                binding.setEndDate(caseModel.getFormattedDate(caseModel.getDateInString()));

                            if (regionDailyCaseModelList.indexOf(caseModel) == regionDailyCaseModelList.size() - 2)
                                comparatorCaseValue = caseModel.getTotalCase();
                            else if (regionDailyCaseModelList.indexOf(caseModel) == regionDailyCaseModelList.size() - 1)
                                currentCaseValue = caseModel.getTotalCase();
                        }

                        caseGrowthValue = (currentCaseValue - comparatorCaseValue) * 100;
                        binding.setCaseGrowth(caseGrowthValue / comparatorCaseValue);
                        setChart();
                    }
        });
    }

    private void loadMoreRegionData(int lastIndex, int incrementIndex){
        int currentIndex = lastIndex + incrementIndex;
        int insertedItem = 0;

        for (SummaryRegionCaseModel regionCaseModel : regionCaseList){
            if (regionCaseList.indexOf(regionCaseModel) > lastIndex){
                if (regionCaseList.indexOf(regionCaseModel) < currentIndex){
                    filteredRegionCaseList.add(regionCaseModel);
                    insertedItem += 1;
                }
            }
        }

        if (insertedItem > 0){
            this.lastIndex = currentIndex;
            regionAdapter.notifyItemInserted(lastIndex);
        }
        else
            UIHelper.newInstance(this).showErrorToast("Tidak ada data tersedia");

        binding.refreshRegion.setRefreshing(false);
    }

    private void setChart(){

        if (!refreshed)
            UIHelper.newInstance(this).setupLineChart(binding.caseHistory);
        UIHelper.newInstance(this).setLineChartData(dailyValues, binding.caseHistory);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
