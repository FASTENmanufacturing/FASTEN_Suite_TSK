
package com.fasten.wp4.iot.kafka.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Filament implements Serializable
{

    @SerializedName("length")
    @Expose
    private Double length;
    @SerializedName("volume")
    @Expose
    private Double volume;
    private final static long serialVersionUID = 2060524536936104438L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Filament() {
    }

    /**
     * 
     * @param volume
     * @param length
     */
    public Filament(Double length, Double volume) {
        super();
        this.length = length;
        this.volume = volume;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

}
