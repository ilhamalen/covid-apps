package com.daws.projects.codamation.models;

import com.daws.projects.codamation.helpers.DateHelper;
import com.daws.projects.codamation.helpers.ValidationHelper;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummaryRegionCaseModel {

    private int id;
    private int regionCode;
    private String regionName;
    private int casePositive;
    private int caseRecovered;
    private int caseDeath;
    private Long lastUpdate;
    private String lastUpdateString;

    public SummaryRegionCaseModel(){}

    public SummaryRegionCaseModel(JSONObject responseObject){
        try {
            if (responseObject.has("attributes")){
                try {
                    JSONObject attributeObject = responseObject.getJSONObject("attributes");

                    if (attributeObject.has("FID"))
                        setId(attributeObject.getInt("FID"));
                    if (attributeObject.has("Kode_Provi"))
                        setRegionCode(attributeObject.getInt("Kode_Provi"));
                    if (attributeObject.has("Provinsi"))
                        setRegionName(attributeObject.getString("Provinsi"));
                    if (attributeObject.has("Kasus_Posi"))
                        setCasePositive(attributeObject.getInt("Kasus_Posi"));
                    if (attributeObject.has("Kasus_Semb"))
                        setCaseRecovered(attributeObject.getInt("Kasus_Semb"));
                    if (attributeObject.has("Kasus_Meni"))
                        setCaseDeath(attributeObject.getInt("Kasus_Meni"));

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            if (responseObject.has("fid"))
                setId(responseObject.getInt("fid"));
            if (responseObject.has("kodeProvi"))
                setRegionCode(responseObject.getInt("kodeProvi"));
            if (responseObject.has("provinsi"))
                setRegionName(responseObject.getString("provinsi"));
            if (responseObject.has("kasusPosi"))
                setCasePositive(responseObject.getInt("kasusPosi"));
            if (responseObject.has("kasusSemb"))
                setCaseRecovered(responseObject.getInt("kasusSemb"));
            if (responseObject.has("kasusMeni"))
                setCaseDeath(responseObject.getInt("kasusMeni"));

            if (responseObject.has("OBJECTID"))
                setId(responseObject.getInt("OBJECTID"));
            if (responseObject.has("Country_Region"))
                setRegionName(responseObject.getString("Country_Region"));
            if (responseObject.has("Last_Update"))
                setLastUpdate(responseObject.getLong("Last_Update"));
            if (responseObject.has("Confirmed"))
                setCasePositive(responseObject.getInt("Confirmed"));
            if (responseObject.has("Deaths"))
                setCaseDeath(responseObject.getInt("Deaths"));
            if (responseObject.has("Recovered"))
                setCaseRecovered(responseObject.getInt("Recovered"));

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getLastUpdateDate(){
        if (ValidationHelper.checkNotNullOrEmpty(lastUpdate))
            return DateHelper.getInstance().convertMilisToLongDateTimeWithMonthName(lastUpdate);
        else
            return "";
    }
}
