package com.daws.projects.codamation.networks;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;

public class NetworkAPI extends BaseNetworkAPI {

    public static final String BASE_MATHDROID_URL = "https://covid19.mathdro.id/api";
    public static final String BASE_MATHDROID_INDONESIA_URL = "https://indonesia-covid-19.mathdro.id/api";
    public static final String BASE_KAWALCORONA_URL = "https://api.kawalcorona.com";
    public static final String BASE_RAPIDCOVID_URL = "https://covid-193.p.rapidapi.com";
    public static final String BASE_KEMKES_URL = "https://data.kemkes.go.id/data/api/analytics.json?dimension=dx:{indicator}&dimension=pe:{periode}&filter=ou:amZZzlibrMp&skipData=false&skipMeta=false&includeNumDen=false&displayProperty=SHORTNAME";


    public NetworkAPI(Context context) {
        super(context);
    }

    public void getArrayResponseListener(String url, JSONArrayRequestListener responseListener){
        AndroidNetworking.get(url)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(responseListener);
    }

    public void getJsonResponseListener(String url, JSONObjectRequestListener responseListener){
        AndroidNetworking.get(url)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(responseListener);
    }

    @Override
    public void getGlobalCaseHistory(JSONArrayRequestListener responseListener) {
        getArrayResponseListener(BASE_MATHDROID_URL + "/daily", responseListener);
    }

    @Override
    public void getGlobalCase(JSONObjectRequestListener responseListener) {
        getJsonResponseListener(BASE_MATHDROID_URL, responseListener);
    }

    @Override
    public void getGlobalCase(String status, JSONObjectRequestListener responseListener) {
        AndroidNetworking.get(BASE_KAWALCORONA_URL + "/{caseStatus}")
                .addPathParameter("caseStatus", status)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(responseListener);
    }

    @Override
    public void getGlobalRegionCase(JSONObjectRequestListener responseListener) {
        AndroidNetworking.get(BASE_RAPIDCOVID_URL + "/statistics")
                .addHeaders("x-rapidapi-host", "covid-193.p.rapidapi.com")
                .addHeaders("x-rapidapi-key", "0bf6b3b84fmsh36553c24ad442dcp17a9ebjsncfdf474425dd")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(responseListener);
    }

    @Override
    public void getLocalCase(JSONObjectRequestListener responseListener) {
        getJsonResponseListener(BASE_MATHDROID_INDONESIA_URL, responseListener);
    }

    @Override
    public void getLocalProvinceCase(JSONObjectRequestListener responseListener) {
        getJsonResponseListener(BASE_MATHDROID_INDONESIA_URL + "/provinsi", responseListener);
    }

    @Override
    public void getLocalDailyCase(JSONObjectRequestListener responseListener) {
        getJsonResponseListener(BASE_MATHDROID_INDONESIA_URL + "/harian", responseListener);
    }

    @Override
    public void getLocalDailyCaseOfficial(String indicator, String periode, OkHttpResponseListener responseListener) {
        AndroidNetworking.get("https://data.kemkes.go.id/data/api/analytics.json?dimension=dx:U7BaEXUa1Ii;SlMC7W5vVRw;XfDUmCzFLVG;TAqRuO1R1eI;aaG6RlQBZBa;UEBmmFH8OzT;bny3czIlcDZ&dimension=pe:20200302;20200303;20200304;20200305;20200306;20200307;20200308;20200309;20200310;20200311;20200312;20200313;LAST_14_DAYS;TODAY&filter=ou:amZZzlibrMp&skipData=false&skipMeta=false&includeNumDen=false&displayProperty=SHORTNAME")
                .addPathParameter("indicator", indicator)
                .addPathParameter("periode", periode)
                .setOkHttpClient(OkHttpClientTrust.getUnsafeOkHttpClient())
                .setPriority(Priority.LOW)
                .build()
                .getAsOkHttpResponse(responseListener);
    }

    @Override
    public void getPatientCase(JSONObjectRequestListener responseListener) {
        getJsonResponseListener(BASE_MATHDROID_INDONESIA_URL + "/kasus", responseListener);
    }
}
