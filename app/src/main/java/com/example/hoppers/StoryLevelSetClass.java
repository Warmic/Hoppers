package com.example.hoppers;

/**
 * Created by Peter on 01.05.2017.
 */

public class StoryLevelSetClass  {
    private int amountoffrogs;
    private int totallevels;

    StoryLevelSetClass(int amountoffrogs,int totallevels){
        this.amountoffrogs = amountoffrogs;
        this.totallevels = totallevels;
    }

    public int getAmountoffrogs() {
        return amountoffrogs;
    }

    public int getTotallevels() {
        return totallevels;
    }
}
