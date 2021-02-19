package com.daws.projects.codamation.models;

import com.daws.projects.codamation.helpers.DateHelper;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummaryGlobalCaseModel {
    private int confirmed;
    private int active;
    private int recovered;
    private int death;
    private String lastUpdate;

    public SummaryGlobalCaseModel(JSONObject responseObject){
        try {
            if (responseObject.has("confirmed")){
                JSONObject confirmedObject = responseObject.getJSONObject("confirmed");
                setConfirmed(confirmedObject.getInt("value"));
            }
            if (responseObject.has("recovered")){
                JSONObject recoveredObject = responseObject.getJSONObject("recovered");
                setRecovered(recoveredObject.getInt("value"));
            }
            if (responseObject.has("deaths")){
                JSONObject deathObject = responseObject.getJSONObject("deaths");
                setDeath(deathObject.getInt("value"));
            }

            if (responseObject.has("jumlahKasus"))
                setConfirmed(responseObject.getInt("jumlahKasus"));
            if (responseObject.has("perawatan"))
                setActive(responseObject.getInt("perawatan"));
            if (responseObject.has("sembuh"))
                setRecovered(responseObject.getInt("sembuh"));
            if (responseObject.has("meninggal"))
                setDeath(responseObject.getInt("meninggal"));

            if (responseObject.has("lastUpdate")){
                String originalDateString = responseObject.getString("lastUpdate");
                setLastUpdate(DateHelper.getInstance().convertDateToFormattedDate(
                        DateHelper.getInstance().convertStringToDate(originalDateString, DateHelper.getInstance().getShortDateWithDash()),
                        DateHelper.getInstance().getLongDateWithMonthNameFormat()
                ));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public JSONObject getAsJSONObject(){
        JSONObject modelAsJSON = new JSONObject();

        try {
            modelAsJSON.put("confirmed", getConfirmed());
            modelAsJSON.put("recovered", getRecovered());
            modelAsJSON.put("death", getDeath());
        } catch (JSONException e){
            e.printStackTrace();
        }

        return modelAsJSON;
    }
}
