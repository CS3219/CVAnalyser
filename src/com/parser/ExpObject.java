package com.parser;

import java.util.ArrayList;

public class ExpObject {
    private double duration;
    private ArrayList<String> desc;
    
    public ExpObject() {
        desc = new ArrayList<String>();
        duration = 0;
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
