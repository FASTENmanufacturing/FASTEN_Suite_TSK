
package com.fasten.wp4.iot.kafka.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProcessStatus implements Serializable
{

    @SerializedName("job")
    @Expose
    private Job job;
    @SerializedName("progress")
    @Expose
    private Progress progress;
    @SerializedName("state")
    @Expose
    private String state;
    private final static long serialVersionUID = 8002143045176986339L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ProcessStatus() {
    }

    /**
     * 
     * @param progress
     * @param state
     * @param job
     */
    public ProcessStatus(Job job, Progress progress, String state) {
        super();
        this.job = job;
        this.progress = progress;
        this.state = state;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
