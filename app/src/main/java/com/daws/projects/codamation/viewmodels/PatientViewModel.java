package com.daws.projects.codamation.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.daws.projects.codamation.CodamationConfiguration;
import com.daws.projects.codamation.models.PatientModel;
import com.daws.projects.codamation.networks.INetworkAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PatientViewModel extends BaseViewModel {

    private INetworkAPI networkAPI;

    private List<PatientModel> patientModelList;
    private MutableLiveData<List<PatientModel>> patientModelLiveData;

    public PatientViewModel(@NonNull Application application) {
        super(application);

        networkAPI = CodamationConfiguration.getInstance().getNetworkAPI(application);
        patientModelList = new ArrayList<>();
    }

    public MutableLiveData<List<PatientModel>> getPatientModelLiveData() {
        if (patientModelLiveData == null) patientModelLiveData = new MutableLiveData<>();
        getPatientData();

        return patientModelLiveData;
    }

    private void getPatientData(){
        networkAPI.getPatientCase(
                new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        displayNetworkResponse(response);

                        try {
                            if (response.has("data"))  {
                                JSONArray patientJSONArray = response.getJSONArray("data");

                                for (int i = 0 ; i < patientJSONArray.length(); i++){
                                    JSONObject patientObject = patientJSONArray.getJSONObject(i);
                                    PatientModel patientModel = new PatientModel(patientObject);

                                    patientModelList.add(patientModel);
                                }

                                patientModelLiveData.setValue(patientModelList);
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        displayEror(anError);
                    }
                }
        );
    }
}
