
package com.fasten.wp4.iot.kafka.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EnviromentalInfo implements Serializable
{

    @SerializedName("temperature")
    @Expose
    private Double temperature;
    @SerializedName("humidity")
    @Expose
    private Double humidity;

    /**
     * No args constructor for use in serialization
     * 
     */
    public EnviromentalInfo() {
    }

    /**
     * 
     * @param temperature
     * @param humidity
     */
    public EnviromentalInfo(Double temperature, Double humidity) {
        super();
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

}
