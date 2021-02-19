package com.daws.projects.codamation.networks;

import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;

public interface INetworkAPI {
    void getGlobalCaseHistory(JSONArrayRequestListener responseListener);

    void getGlobalCase(JSONObjectRequestListener responseListener);

    void getGlobalCase(String status, JSONObjectRequestListener responseListener);

    void getGlobalRegionCase(JSONObjectRequestListener responseListener);

    void getLocalCase(JSONObjectRequestListener responseListener);

    void getLocalProvinceCase(JSONObjectRequestListener responseListener);

    void getLocalDailyCase(JSONObjectRequestListener responseListener);

    void getLocalDailyCaseOfficial(String indicator, String periode, OkHttpResponseListener responseListener);

    void getPatientCase(JSONObjectRequestListener responseListener);
}
