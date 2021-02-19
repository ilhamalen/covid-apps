package com.daws.projects.codamation.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.daws.projects.codamation.CodamationConfiguration;
import com.daws.projects.codamation.models.SummaryRegionCaseModel;
import com.daws.projects.codamation.networks.INetworkAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SummaryRegionCaseViewModel extends BaseViewModel {

    private INetworkAPI networkAPI;
    private MutableLiveData<List<SummaryRegionCaseModel>> summaryRegionCaseLiveData;

    public SummaryRegionCaseViewModel(@NonNull Application application) {
        super(application);
        networkAPI = CodamationConfiguration.getInstance().getNetworkAPI(application);
    }

    public MutableLiveData<List<SummaryRegionCaseModel>> getSummaryRegionCaseLiveData(boolean isLocal) {
        if (summaryRegionCaseLiveData == null) summaryRegionCaseLiveData = new MutableLiveData<>();
        if (isLocal) getLocalRegionCaseData();
        else getGlobalRegionCaseData();

        return summaryRegionCaseLiveData;
    }

    private void getLocalRegionCaseData(){
        networkAPI.getLocalProvinceCase(
                new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        displayNetworkResponse(response);

                        List<SummaryRegionCaseModel> summaryLocalRegionCaseModelList = new ArrayList<>();

                        try{
                            JSONArray provinceArray = response.getJSONArray("data");

                            for (int i = 0 ; i < provinceArray.length() ; i ++){
                                JSONObject summaryLocalRegionCaseObject = provinceArray.getJSONObject(i);
                                SummaryRegionCaseModel summaryLocalRegionCaseModel = new SummaryRegionCaseModel(summaryLocalRegionCaseObject);
                                summaryLocalRegionCaseModelList.add(summaryLocalRegionCaseModel);
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                        summaryRegionCaseLiveData.setValue(summaryLocalRegionCaseModelList);
                    }

                    @Override
                    public void onError(ANError anError) {
                        displayEror(anError);
                    }
                }
        );
    }

    private void getGlobalRegionCaseData(){
        networkAPI.getGlobalRegionCase(
                new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        displayNetworkResponse(response);

                        List<SummaryRegionCaseModel> countryModelList = new ArrayList<>();
                        if (response.has("response")){
                            try {
                                JSONArray countryArray = response.getJSONArray("response");
                                for (int i = 0; i < countryArray.length(); i++){
                                    JSONObject countryObject = countryArray.getJSONObject(i);
                                    SummaryRegionCaseModel countryModel = new SummaryRegionCaseModel();

                                    if (countryObject.has("country"))
                                        countryModel.setRegionName(countryObject.getString("country"));
                                    if (countryObject.has("cases")){
                                        JSONObject caseObject = countryObject.getJSONObject("cases");
                                        if (caseObject.has("total"))
                                            countryModel.setCasePositive(caseObject.getInt("total"));
                                        if (caseObject.has("recovered"))
                                            countryModel.setCaseRecovered(caseObject.getInt("recovered"));
                                    }
                                    if (countryObject.has("deaths")){
                                        JSONObject deathObject = countryObject.getJSONObject("deaths");
                                        if (deathObject.has("total"))
                                            countryModel.setCaseDeath(deathObject.getInt("total"));
                                    }
                                    if (countryObject.has("day"))
                                        countryModel.setLastUpdateString(countryObject.getString("day"));

                                    if (!countryModel.getRegionName().equalsIgnoreCase("All")
                                            && !countryModel.getRegionName().equalsIgnoreCase("World"))
                                        countryModelList.add(countryModel);
                                }
                            } catch (JSONException e){
                                e.printStackTrace();
                            }
                        }

                        summaryRegionCaseLiveData.setValue(countryModelList);
                    }

                    @Override
                    public void onError(ANError anError) {
                        displayEror(anError);
                    }
                }
        );
    }
}
