package com.daws.projects.codamation.views.activities;

import android.os.Bundle;

import com.daws.projects.codamation.GlobalVariable;
import com.daws.projects.codamation.R;
import com.daws.projects.codamation.adapters.SimpleRecyclerAdapter;
import com.daws.projects.codamation.databinding.ActivityLocalCaseHistoryBinding;
import com.daws.projects.codamation.databinding.ItemDailyHistoryBinding;
import com.daws.projects.codamation.helpers.UIHelper;
import com.daws.projects.codamation.models.RegionDailyCaseModel;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalCaseHistoryAcitivity extends BaseActivity<ActivityLocalCaseHistoryBinding> {

    private ArrayList<Entry> newCaseDaily;
    private ArrayList<Entry> activeCaseDaily;
    private ArrayList<Entry> recoverCaseDaily;
    private ArrayList<Entry> deathCaseDaily;
    private List<RegionDailyCaseModel> regionDailyCaseModelList;
    private List<Integer> newCaseList;
    private List<Integer> activeCaseList;
    private List<Integer> recoverCaseList;
    private List<Integer> deathCaseList;
    private JSONArray localHistoryArray;
    private SimpleRecyclerAdapter<RegionDailyCaseModel> dailyCaseAdapter;

    @Override
    protected int attachLayout() {
        return R.layout.activity_local_case_history;
    }

    @Override
    protected void initData() {
        super.initData();
        newCaseDaily = new ArrayList<>();
        newCaseList = new ArrayList<>();
        activeCaseDaily = new ArrayList<>();
        activeCaseList = new ArrayList<>();
        recoverCaseDaily = new ArrayList<>();
        recoverCaseList = new ArrayList<>();
        deathCaseDaily = new ArrayList<>();
        deathCaseList = new ArrayList<>();
        regionDailyCaseModelList = new ArrayList<>();
        localHistoryArray = new JSONArray();

        Bundle originBundle = getIntent().getExtras();
        if (originBundle.containsKey(GlobalVariable.CODE_LOCAL_CASE_HISTORY)){
            try {
                localHistoryArray = new JSONArray(originBundle.getString(GlobalVariable.CODE_LOCAL_CASE_HISTORY));
                for (int i = 0 ; i < localHistoryArray.length(); i++){
                    JSONObject localHistoryObject = localHistoryArray.getJSONObject(i);
                    RegionDailyCaseModel localHistoryModel = new RegionDailyCaseModel(localHistoryObject);

                    if (localHistoryModel.getNewDailyCase() > 0)
                        regionDailyCaseModelList.add(localHistoryModel);

                    newCaseList.add(localHistoryModel.getNewDailyCase());
                    activeCaseList.add(localHistoryModel.getActiveCaseDaily());
                    recoverCaseList.add(localHistoryModel.getRecoveryCaseDaily());
                    deathCaseList.add(localHistoryModel.getDeathCaseDaily());

                    newCaseDaily.add(new Entry(i, (float)localHistoryModel.getNewDailyCase()));
                    activeCaseDaily.add(new Entry(i, (float)localHistoryModel.getActiveCaseDaily()));
                    recoverCaseDaily.add(new Entry(i, (float)localHistoryModel.getRecoveryCaseDaily()));
                    deathCaseDaily.add(new Entry(i, (float)localHistoryModel.getDeathCaseDaily()));
                }

                Collections.reverse(regionDailyCaseModelList);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }

        dailyCaseAdapter = new SimpleRecyclerAdapter<>(
                regionDailyCaseModelList,
                R.layout.item_daily_history,
                (holder, item) -> {
                    ItemDailyHistoryBinding binding = (ItemDailyHistoryBinding) holder.getLayoutBinding();
                    binding.setDailyHistory(item);

                    binding.setActivePercentage((double) item.getActiveCaseDaily() / (double) item.getNewDailyCase() * 100);
                    binding.setRecoverPercentage((double) item.getRecoveryCaseDaily() / (double) item.getNewDailyCase() * 100);
                    binding.setDeathPercentage((double) item.getDeathCaseDaily() / (double) item.getNewDailyCase() * 100);
                }
        );
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        binding.setHistoryAdapter(dailyCaseAdapter);
        binding.back.setOnClickListener(v -> finish());
        setHistoryChart();
        setActivityChart();
    }

    private void setHistoryChart(){
        UIHelper.newInstance(this).setupLineChart(binding.caseHistory);
        UIHelper.newInstance(this).setLineChartData(newCaseDaily, binding.caseHistory);
        binding.caseHistory.getLegend().setEnabled(false);
        binding.caseHistory.invalidate();
    }

    private void setActivityChart(){
        ArrayList<ArrayList<Entry>> multipleData = new ArrayList<>();
        multipleData.add(activeCaseDaily);
        multipleData.add(recoverCaseDaily);
        multipleData.add(deathCaseDaily);

        UIHelper.newInstance(this).setupLineChart(binding.caseActivity);
        UIHelper.newInstance(this).setMultipleLineChartData(multipleData, binding.caseActivity);
        binding.caseActivity.getLegend().setEnabled(false);
    }
}
