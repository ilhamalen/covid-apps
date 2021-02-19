package com.daws.projects.codamation.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.daws.projects.codamation.CodamationConfiguration;
import com.daws.projects.codamation.helpers.ValidationHelper;
import com.daws.projects.codamation.models.RegionDailyCaseModel;
import com.daws.projects.codamation.networks.INetworkAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DailyCaseViewModel extends BaseViewModel {

    private INetworkAPI networkAPI;
    private MutableLiveData<RegionDailyCaseModel> latestCaseLiveData;
    private MutableLiveData<List<RegionDailyCaseModel>> dailyCaseLiveData;

    public DailyCaseViewModel(@NonNull Application application) {
        super(application);

        networkAPI = CodamationConfiguration.getInstance().getNetworkAPI(application);
    }

    public MutableLiveData<RegionDailyCaseModel> getLatestCaseLiveData() {
        if (latestCaseLiveData == null) latestCaseLiveData = new MutableLiveData<>();

        return latestCaseLiveData;
    }

    public MutableLiveData<List<RegionDailyCaseModel>> getDailyCaseLiveData(boolean local) {
        if (dailyCaseLiveData == null) dailyCaseLiveData = new MutableLiveData<>();
        if (local) getLocalDailyCaseData();
        else getGlobalDailyCaseData();

        return dailyCaseLiveData;
    }

    private void getLocalDailyCaseData(){
        networkAPI.getLocalDailyCase(
                new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        displayNetworkResponse(response);

                        if (response.has("data")){
                            List<RegionDailyCaseModel> regionDailyCaseModelList = new ArrayList<>();
                            RegionDailyCaseModel latestCaseModel = new RegionDailyCaseModel();

                            try{
                                JSONArray dailyCaseArray = response.getJSONArray("data");

                                for (int i = 0; i < dailyCaseArray.length(); i++){
                                    JSONObject dailyCaseObject = dailyCaseArray.getJSONObject(i);
                                    Object dailyObject = dailyCaseObject.get("jumlahKasusBaruperHari");
                                    if (ValidationHelper.checkNotNullOrEmpty(dailyObject.toString())){
                                        RegionDailyCaseModel regionDailyCaseModel = new RegionDailyCaseModel(dailyCaseObject);
                                        if (regionDailyCaseModel.getDeathCaseDaily() != null)
                                            regionDailyCaseModelList.add(regionDailyCaseModel);
                                    }
                                }
                            } catch (JSONException e){
                                e.printStackTrace();
                            }

                            if (regionDailyCaseModelList.size() > 0)
                                latestCaseModel = regionDailyCaseModelList.get(regionDailyCaseModelList.size() - 1);

                            dailyCaseLiveData.setValue(regionDailyCaseModelList);
                            latestCaseLiveData.setValue(latestCaseModel);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        displayEror(anError);
                    }
                }
        );
    }

    private void getGlobalDailyCaseData(){
        networkAPI.getGlobalCaseHistory(
                new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        displayNetworkResponse(response);

                        List<RegionDailyCaseModel> dailyCaseModelList = new ArrayList<>();

                        try {
                            for (int i = 0; i < response.length(); i++){
                                JSONObject dailyObject = response.getJSONObject(i);
                                RegionDailyCaseModel dailyModel = new RegionDailyCaseModel();

                                if (dailyObject.has("totalConfirmed"))
                                    dailyModel.setTotalCase(dailyObject.getInt("totalConfirmed"));
                                if (dailyObject.has("reportDate"))
                                    dailyModel.setDateInString(dailyObject.getString("reportDate"));
                                dailyModel.setCaseId(i);

                                dailyCaseModelList.add(dailyModel);
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                        dailyCaseLiveData.setValue(dailyCaseModelList);
                    }

                    @Override
                    public void onError(ANError anError) {
                        displayEror(anError);
                    }
                }
        );
    }
}
