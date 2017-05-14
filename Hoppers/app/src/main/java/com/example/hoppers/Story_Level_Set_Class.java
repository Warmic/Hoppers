package com.example.hoppers;

/**
 * Created by Peter on 01.05.2017.
 */

public class Story_Level_Set_Class {
    private int amountoffrogs;
    private int levels_completed;
    private int totallevels;

    Story_Level_Set_Class(int amountoffrogs, int totallevels, int levels_completed){
        this.amountoffrogs = amountoffrogs;
        this.totallevels = totallevels;
        this.levels_completed = levels_completed;
    }

    public int getAmountoffrogs() {
        return amountoffrogs;
    }

    public int getTotallevels() {
        return totallevels;
    }

    public int getLevels_completed() {return levels_completed;}

}
