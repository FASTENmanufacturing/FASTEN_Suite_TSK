
package com.fasten.wp4.iot.kafka.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Progress implements Serializable
{

    @SerializedName("completion")
    @Expose
    private Double completion;
    @SerializedName("filepos")
    @Expose
    private Double filepos;
    @SerializedName("printTime")
    @Expose
    private Double printTime;
    @SerializedName("printTimeLeft")
    @Expose
    private Double printTimeLeft;
    @SerializedName("printTimeLeftOrigin")
    @Expose
    private String printTimeLeftOrigin;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Progress() {
    }

    /**
     * 
     * @param printTimeLeft
     * @param printTimeLeftOrigin
     * @param completion
     * @param filepos
     * @param printTime
     */
    public Progress(Double completion, Double filepos, Double printTime, Double printTimeLeft, String printTimeLeftOrigin) {
        super();
        this.completion = completion;
        this.filepos = filepos;
        this.printTime = printTime;
        this.printTimeLeft = printTimeLeft;
        this.printTimeLeftOrigin = printTimeLeftOrigin;
    }

    public Double getCompletion() {
        return completion;
    }

    public void setCompletion(Double completion) {
        this.completion = completion;
    }

    public Double getFilepos() {
        return filepos;
    }

    public void setFilepos(Double filepos) {
        this.filepos = filepos;
    }

    public Double getPrintTime() {
        return printTime;
    }

    public void setPrintTime(Double printTime) {
        this.printTime = printTime;
    }

    public Double getPrintTimeLeft() {
        return printTimeLeft;
    }

    public void setPrintTimeLeft(Double printTimeLeft) {
        this.printTimeLeft = printTimeLeft;
    }

    public String getPrintTimeLeftOrigin() {
        return printTimeLeftOrigin;
    }

    public void setPrintTimeLeftOrigin(String printTimeLeftOrigin) {
        this.printTimeLeftOrigin = printTimeLeftOrigin;
    }

}
