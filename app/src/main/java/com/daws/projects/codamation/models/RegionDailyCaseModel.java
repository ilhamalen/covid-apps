package com.daws.projects.codamation.models;

import androidx.annotation.Nullable;

import com.daws.projects.codamation.helpers.DateHelper;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegionDailyCaseModel {

    private Integer caseId;
    private Integer dayCount;
    private Long dateInMillis;
    private Integer newDailyCase;
    private Integer totalCase;
    private Integer activeCase;
    private Integer recoveredCase;
    private Integer deathCase;
    private Double activeCasePercentage;
    private Double recoveryCasePercentage;
    private Double deathCasePercentage;
    private Integer activeCaseDaily;
    private Integer recoveryCaseDaily;
    private Integer deathCaseDaily;
    private String dateInString;

    public RegionDailyCaseModel(){}

    public RegionDailyCaseModel(JSONObject responseObject){
        try {
            if (responseObject.has("fid"))
                setCaseId(responseObject.getInt("fid"));
            if (responseObject.has("harike"))
                setDayCount(responseObject.getInt("harike"));
            if (responseObject.has("tanggal"))
                setDateInMillis(responseObject.getLong("tanggal"));
            if (responseObject.has("jumlahKasusBaruperHari"))
                setNewDailyCase(responseObject.getInt("jumlahKasusBaruperHari"));
            if (responseObject.has("jumlahKasusKumulatif"))
                setTotalCase(responseObject.getInt("jumlahKasusKumulatif"));
            if (responseObject.has("jumlahpasiendalamperawatan"))
                setActiveCase(responseObject.getInt("jumlahpasiendalamperawatan"));
            if (responseObject.has("jumlahPasienSembuh"))
                setRecoveredCase(responseObject.getInt("jumlahPasienSembuh"));
            if (responseObject.has("jumlahPasienMeninggal"))
                setDeathCase(responseObject.getInt("jumlahPasienMeninggal"));
            if (responseObject.has("persentasePasiendalamPerawatan"))
                setActiveCasePercentage(responseObject.getDouble("persentasePasiendalamPerawatan"));
            if (responseObject.has("persentasePasienSembuh"))
                setRecoveryCasePercentage(responseObject.getDouble("persentasePasienSembuh"));
            if (responseObject.has("persentasePasienMeninggal"))
                setDeathCasePercentage(responseObject.getDouble("persentasePasienMeninggal"));
            if (responseObject.has("jumlahKasusDirawatperHari"))
                setActiveCaseDaily(responseObject.getInt("jumlahKasusDirawatperHari"));
            if (responseObject.has("jumlahKasusSembuhperHari"))
                setRecoveryCaseDaily(responseObject.getInt("jumlahKasusSembuhperHari"));
            if (responseObject.has("jumlahKasusMeninggalperHari"))
                setDeathCaseDaily(responseObject.getInt("jumlahKasusMeninggalperHari"));
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getFormattedDate(){
        return DateHelper.getInstance().convertMilisToLongDateWithMonthName(getDateInMillis());
    }

    public String getFormattedDate(String originalDateString){
        return DateHelper.getInstance().convertDateToFormattedDate(
                DateHelper.getInstance().convertStringToDate(originalDateString, DateHelper.getInstance().getShortDateWithDash()),
                DateHelper.getInstance().getLongDateWithMonthNameFormat()
        );
    }

    public JSONObject getAsJSONObject(){
        JSONObject modelJSON = new JSONObject();

        try {
            modelJSON.put("fid", getCaseId());
            modelJSON.put("harike", getDayCount());
            modelJSON.put("tanggal", getDateInMillis());
            modelJSON.put("dateInString", getDateInString());
            modelJSON.put("jumlahKasusBaruperHari", getNewDailyCase());
            modelJSON.put("jumlahKasusKumulatif", getTotalCase());
            modelJSON.put("jumlahpasiendalamperawatan", getActiveCase());
            modelJSON.put("jumlahPasienSembuh", getRecoveredCase());
            modelJSON.put("jumlahPasienMeninggal", getDeathCase());
            modelJSON.put("persentasePasiendalamPerawatan", getActiveCasePercentage());
            modelJSON.put("persentasePasienSembuh", getRecoveryCasePercentage());
            modelJSON.put("persentasePasienMeninggal", getDeathCasePercentage());
            modelJSON.put("jumlahKasusDirawatperHari", getActiveCaseDaily());
            modelJSON.put("jumlahKasusSembuhperHari", getRecoveryCaseDaily());
            modelJSON.put("jumlahKasusMeninggalperHari", getDeathCaseDaily());
        } catch (JSONException e){
            e.printStackTrace();
        }

        return modelJSON;
    }

    @Override
    public boolean equals(@Nullable Object comparatorObj) {
        if (comparatorObj instanceof RegionDailyCaseModel)
            return ((RegionDailyCaseModel) comparatorObj).caseId == getCaseId();

        return false;
    }
}
