package com.daws.projects.codamation.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientModel {
    private int patientId;
    private int caseId;
    private String regionName;
    private int regionId;
    private int age;
    private String gender;
    private String status;
    private String nationality;

    public PatientModel(JSONObject responseObject){
        try {
            if (responseObject.has("id_pasien"))
                setPatientId(responseObject.getInt("id_pasien"));
            if (responseObject.has("kasus"))
                setCaseId(responseObject.getInt("kasus"));
            if (responseObject.has("klaster"))
                setRegionName(responseObject.getString("klaster"));
            if (responseObject.has("provinsi"))
                setRegionId(responseObject.getInt("provinsi"));
            if (responseObject.has("umur"))
                setAge(responseObject.getInt("umur"));
            if (responseObject.has("jenis_kelamin")){
                int genderCode = responseObject.getInt("jenis_kelamin");
                setGender(genderCode == 0 ? "Perempuan" : "Laki-laki");
            }
            if (responseObject.has("id_status")){
                int statusCode = responseObject.getInt("id_status");
                String status;
                switch (statusCode){
                    case 0 : {
                        status = "Meninggal";
                        break;
                    }
                    case 1 : {
                        status = "Sembuh";
                        break;
                    }

                    case 2 : {
                        status = "Aktif";
                        break;
                    }

                    default: {
                        status = "-";
                        break;
                    }
                }

                setStatus(status);
            }
            if (responseObject.has("wn")){
                int nationalityCode = responseObject.getInt("wn");
                setNationality(nationalityCode == 1 ? "WNI" : "WNA");
            }
        } catch (JSONException e){
            e.printStackTrace();
            Log.v("JSONError", responseObject.toString());
        }
    }

    public JSONObject getAsJSONObject(){
        JSONObject modelAsJSON = new JSONObject();

        try {
            modelAsJSON.put("id", getPatientId());
            modelAsJSON.put("caseId", getCaseId());
            modelAsJSON.put("cluster", getRegionName());
            modelAsJSON.put("clusterId", getRegionId());
            modelAsJSON.put("age", getAge());
            modelAsJSON.put("gender", getGender());
            modelAsJSON.put("status", getStatus());
            modelAsJSON.put("wni", getNationality());
        } catch (JSONException e){
            e.printStackTrace();
        }

        return modelAsJSON;
    }
}
