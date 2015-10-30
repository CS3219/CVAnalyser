package com.parser;

import java.util.ArrayList;

public class WorkExpObject {
    private double duration;
    private ArrayList<String> desc;
    
    public WorkExpObject() {
    }
    
    public WorkExpObject(double duration, ArrayList<String> desc) {
        this.duration = duration;
        this.desc = desc;
    }
    
    public double getDuration() {
        return this.duration;
    }
    
    public ArrayList<String> getDescription() {
        return this.desc;
    }
    
    public void setDuration(double duration) {
        this.duration = duration;
    }
    
    public void setDesc(ArrayList<String> desc) {
        this.desc = desc;
    }
}
