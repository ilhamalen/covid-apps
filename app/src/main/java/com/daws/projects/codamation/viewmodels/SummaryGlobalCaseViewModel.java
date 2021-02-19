package com.daws.projects.codamation.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.daws.projects.codamation.CodamationConfiguration;
import com.daws.projects.codamation.models.SummaryGlobalCaseModel;
import com.daws.projects.codamation.networks.INetworkAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SummaryGlobalCaseViewModel extends BaseViewModel {

    private INetworkAPI networkAPI;
    private MutableLiveData<SummaryGlobalCaseModel> globalSummaryCaseLiveData;
    private MutableLiveData<SummaryGlobalCaseModel> localSummaryCaseLiveData;

    public SummaryGlobalCaseViewModel(@NonNull Application application) {
        super(application);

        networkAPI = CodamationConfiguration.getInstance().getNetworkAPI(application);
    }

    public MutableLiveData<SummaryGlobalCaseModel> getGlobalSummaryCaseLiveData() {
        if (globalSummaryCaseLiveData == null) globalSummaryCaseLiveData = new MutableLiveData<>();
        getGlobalSummaryCaseData();

        return globalSummaryCaseLiveData;
    }

    public MutableLiveData<SummaryGlobalCaseModel> getLocalSummaryCaseLiveData(){
        if (localSummaryCaseLiveData == null) localSummaryCaseLiveData = new MutableLiveData<>();
        getLocalSummaryCaseData();

        return localSummaryCaseLiveData;
    }

    private void getGlobalSummaryCaseData(){
        networkAPI.getGlobalCase(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                displayNetworkResponse(response);

                SummaryGlobalCaseModel globalSummaryGlobalCaseModel = new SummaryGlobalCaseModel(response);
                globalSummaryCaseLiveData.setValue(globalSummaryGlobalCaseModel);
            }

            @Override
            public void onError(ANError anError) {
                displayEror(anError);
            }
        });
    }

    private void getLocalSummaryCaseData(){
        networkAPI.getLocalCase(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                displayNetworkResponse(response);
                SummaryGlobalCaseModel localSummaryGlobalCaseModel = new SummaryGlobalCaseModel(response);
                localSummaryCaseLiveData.setValue(localSummaryGlobalCaseModel);
            }

            @Override
            public void onError(ANError anError) {
                displayEror(anError);
            }
        });
    }
}
