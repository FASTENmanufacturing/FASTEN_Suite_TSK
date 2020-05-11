
package com.fasten.wp4.iot.kafka.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Job implements Serializable
{

    @SerializedName("averagePrintTime")
    @Expose
    private Double averagePrintTime;
    @SerializedName("estimatedPrintTime")
    @Expose
    private Double estimatedPrintTime;
    @SerializedName("filament")
    @Expose
    private Filament filament;
    @SerializedName("file")
    @Expose
    private File file;
    @SerializedName("lastPrintTime")
    @Expose
    private Double lastPrintTime;
    @SerializedName("user")
    @Expose
    private String user;
    private final static long serialVersionUID = 5367891077643325076L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Job() {
    }

    /**
     * 
     * @param file
     * @param lastPrintTime
     * @param estimatedPrintTime
     * @param averagePrintTime
     * @param filament
     * @param user
     */
    public Job(Double averagePrintTime, Double estimatedPrintTime, Filament filament, File file, Double lastPrintTime, String user) {
        super();
        this.averagePrintTime = averagePrintTime;
        this.estimatedPrintTime = estimatedPrintTime;
        this.filament = filament;
        this.file = file;
        this.lastPrintTime = lastPrintTime;
        this.user = user;
    }

    public Double getAveragePrintTime() {
        return averagePrintTime;
    }

    public void setAveragePrintTime(Double averagePrintTime) {
        this.averagePrintTime = averagePrintTime;
    }

    public Double getEstimatedPrintTime() {
        return estimatedPrintTime;
    }

    public void setEstimatedPrintTime(Double estimatedPrintTime) {
        this.estimatedPrintTime = estimatedPrintTime;
    }

    public Filament getFilament() {
        return filament;
    }

    public void setFilament(Filament filament) {
        this.filament = filament;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Double getLastPrintTime() {
        return lastPrintTime;
    }

    public void setLastPrintTime(Double lastPrintTime) {
        this.lastPrintTime = lastPrintTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
