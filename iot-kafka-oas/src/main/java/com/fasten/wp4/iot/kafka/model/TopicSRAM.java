
package com.fasten.wp4.iot.kafka.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopicSRAM implements Serializable
{

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("enviromentalInfo")
    @Expose
    private EnviromentalInfo enviromentalInfo;
    @SerializedName("processStatus")
    @Expose
    private ProcessStatus processStatus;
    private final static long serialVersionUID = -3062453461487353391L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public TopicSRAM() {
    }

    /**
     * 
     * @param code
     * @param processStatus
     * @param enviromentalInfo
     */
    public TopicSRAM(String code, EnviromentalInfo enviromentalInfo, ProcessStatus processStatus) {
        super();
        this.code = code;
        this.enviromentalInfo = enviromentalInfo;
        this.processStatus = processStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EnviromentalInfo getEnviromentalInfo() {
        return enviromentalInfo;
    }

    public void setEnviromentalInfo(EnviromentalInfo enviromentalInfo) {
        this.enviromentalInfo = enviromentalInfo;
    }

    public ProcessStatus getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
    }

}
