package com.daws.projects.codamation.models;

import androidx.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegionModel {

    private String regionName;
    private int regionId;
    private boolean selected;

    public RegionModel(){
        setSelected(false);
    }

    @Override
    public boolean equals(@Nullable Object comparatorObj) {
        if (comparatorObj instanceof RegionModel)
            return ((RegionModel) comparatorObj).regionId == getRegionId();

        return false;
    }
}
