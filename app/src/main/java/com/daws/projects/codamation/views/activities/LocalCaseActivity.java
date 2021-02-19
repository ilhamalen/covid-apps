package com.daws.projects.codamation.views.activities;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.daws.projects.codamation.GlobalVariable;
import com.daws.projects.codamation.R;
import com.daws.projects.codamation.adapters.SimpleRecyclerAdapter;
import com.daws.projects.codamation.databinding.ActivityLocalCaseBinding;
import com.daws.projects.codamation.databinding.ItemCaseBinding;
import com.daws.projects.codamation.helpers.UIHelper;
import com.daws.projects.codamation.models.RegionDailyCaseModel;
import com.daws.projects.codamation.models.SummaryRegionCaseModel;
import com.daws.projects.codamation.viewmodels.DailyCaseViewModel;
import com.daws.projects.codamation.viewmodels.SummaryRegionCaseViewModel;
import com.github.mikephil.charting.data.Entry;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class LocalCaseActivity extends BaseActivity<ActivityLocalCaseBinding> {

    private DailyCaseViewModel dailyCaseViewModel;
    private SummaryRegionCaseViewModel localRegionCaseViewModel;

    private ArrayList<Entry> dailyValues;
    private List<RegionDailyCaseModel> regionDailyCaseModelList;
    private List<SummaryRegionCaseModel> summaryRegionCaseModelList;
    private SimpleRecyclerAdapter<SummaryRegionCaseModel> localSummaryCaseAdapter;

    private double caseGrowthValue;
    private int currentCaseValue;
    private int comparatorCaseValue;
    private boolean refreshed = false;

    @Override
    protected int attachLayout() {
        return R.layout.activity_local_case;
    }

    @Override
    protected void initData() {
        super.initData();

        dailyCaseViewModel = ViewModelProviders.of(this).get(DailyCaseViewModel.class);
        localRegionCaseViewModel = ViewModelProviders.of(this).get(SummaryRegionCaseViewModel.class);

        dailyValues = new ArrayList<>();
        regionDailyCaseModelList = new ArrayList<>();
        summaryRegionCaseModelList = new ArrayList<>();
        localSummaryCaseAdapter = new SimpleRecyclerAdapter<>(
                R.layout.item_case,
                ((holder, item) -> {
                    ItemCaseBinding binding = (ItemCaseBinding) holder.getLayoutBinding();
                    binding.setRegionalCaseModel(item);
                })
        );
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        getNetworkData();

        binding.setCaseAdapter(localSummaryCaseAdapter);

        binding.refresh.setOnRefreshListener(direction -> {
            if (direction == SwipyRefreshLayoutDirection.TOP){
                binding.refresh.setRefreshing(true);
                refreshed = true;

                getNetworkData();
            }
        });

        binding.back.setOnClickListener(v -> onBackPressed());

        binding.detailCase.setOnClickListener(v -> startActivityForResult(new Intent(LocalCaseActivity.this, LocalCasePatientActivity.class), GlobalVariable.REQUEST_REFRESH));

        binding.caseHistoryDetail.setOnClickListener(v -> {
            JSONArray dailyCaseModelArray = new JSONArray();
            for (RegionDailyCaseModel dailyCaseModel : regionDailyCaseModelList){
                dailyCaseModelArray.put(dailyCaseModel.getAsJSONObject());
            }

            Intent historyIntent = new Intent(LocalCaseActivity.this, LocalCaseHistoryAcitivity.class);
            historyIntent.putExtra(GlobalVariable.CODE_LOCAL_CASE_HISTORY, dailyCaseModelArray.toString());
            startActivityForResult(historyIntent, GlobalVariable.REQUEST_REFRESH);
        });
    }

    private void getNetworkData(){
        binding.setLoading(true);
        dailyCaseViewModel.getLatestCaseLiveData()
                .observe(this, latestCaseModel -> binding.setLatestCase(latestCaseModel));

        dailyCaseViewModel.getDailyCaseLiveData(true)
                .observe(this, dailyCaseModel -> {

                    if (dailyCaseModel.size() > 0){
                        regionDailyCaseModelList = dailyCaseModel;
                        dailyValues.clear();

                        for (RegionDailyCaseModel caseModel : regionDailyCaseModelList){
                            dailyValues.add(new Entry(regionDailyCaseModelList.indexOf(caseModel), (float)caseModel.getTotalCase()));

                            if (regionDailyCaseModelList.indexOf(caseModel) == 0)
                                binding.setStartDate(caseModel.getFormattedDate());
                            else if (regionDailyCaseModelList.indexOf(caseModel) == regionDailyCaseModelList.size() - 1)
                                binding.setEndDate(caseModel.getFormattedDate());

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

        localRegionCaseViewModel.getSummaryRegionCaseLiveData(true)
                .observe(this, localRegionCaseList -> {
                    summaryRegionCaseModelList = localRegionCaseList;
                    localSummaryCaseAdapter.setMainData(summaryRegionCaseModelList);
                    binding.setLoading(false);
                    binding.refresh.setRefreshing(false);
                });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == GlobalVariable.REQUEST_REFRESH){
//            if (resultCode == Activity.RESULT_OK)
//                getNetworkData();
//        }
    }
}
