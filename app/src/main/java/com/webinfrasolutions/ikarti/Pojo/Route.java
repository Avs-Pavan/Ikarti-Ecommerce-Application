package com.webinfrasolutions.ikarti.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Route implements Serializable {

    @SerializedName("legs")
    @Expose
    private List<Leg> legs = new ArrayList<Leg>();
    @SerializedName("overview_polyline")
    @Expose
    private OverviewPolyline overviewPolyline;

    /**
     *
     * @return
     * The legs
     */
    public List<Leg> getLegs() {
        return legs;
    }

    /**
     *
     * @param legs
     * The legs
     */
    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    /**
     *
     * @return
     * The overviewPolyline
     */
    public OverviewPolyline getOverviewPolyline() {
        return overviewPolyline;
    }

    /**
     *
     * @param overviewPolyline
     * The overview_polyline
     */
    public void setOverviewPolyline(OverviewPolyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }

}
