package com.company;

import java.util.HashMap;
import java.util.List;

public class PointCounter {
    private int sashimiNum;
    private int tempuraNum;
    private int dumplingNum;
    private int nigiriPointNum;
    private int pointTotal;

    public PointCounter(int sashimiCount, int tempuraCount, int dumplingCount,
                             int nigiriPointCount){
        sashimiNum = sashimiCount;
        tempuraNum = tempuraCount;
        dumplingNum = dumplingCount;
        nigiriPointNum = nigiriPointCount;
    }

    public Integer calculate(){
        pointTotal = 0;
        pointTotal += (sashimiNum / 3) * 10;
        pointTotal += (tempuraNum / 2) * 5;
        switch(dumplingNum){
            case 1:
                pointTotal += 1;
                break;
            case 2:
                pointTotal += 3;
                break;
            case 3:
                pointTotal += 6;
                break;
            case 4:
                pointTotal += 10;
                break;
            case 5:
                pointTotal += 15;
            default:
                if(dumplingNum > 5){
                pointTotal += 15;
                }
        }
        pointTotal += nigiriPointNum;
        return pointTotal;
    }
}
