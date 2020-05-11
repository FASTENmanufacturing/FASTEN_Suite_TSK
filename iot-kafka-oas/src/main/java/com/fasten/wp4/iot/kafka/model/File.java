
package com.fasten.wp4.iot.kafka.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class File implements Serializable
{

    @SerializedName("date")
    @Expose
    private Long date;
    @SerializedName("display")
    @Expose
    private String display;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("origin")
    @Expose
    private String origin;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("size")
    @Expose
    private Long size;
    private final static long serialVersionUID = -3047215062286012449L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public File() {
    }

    /**
     * 
     * @param date
     * @param path
     * @param size
     * @param display
     * @param origin
     * @param name
     */
    public File(Long date, String display, String name, String origin, String path, Long size) {
        super();
        this.date = date;
        this.display = display;
        this.name = name;
        this.origin = origin;
        this.path = path;
        this.size = size;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

}
