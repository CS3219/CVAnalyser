package com.parser;

import java.util.ArrayList;

public class WorkExpObject {
    private double duration;
    private ArrayList<String> desc;
    
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
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public void setArea(ArrayList<String> desc) {
        this.desc = desc;
    }
}
