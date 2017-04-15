package com.example.hoppers;

/**
 * Created by Peter on 15.04.2017.
 */

public class Move {
    public int startx;
    public int starty;
    public int finalx;
    public int finaly;
    public int deletedx;
    public int deletedy;
    public boolean is_redfrog;

    Move(int startx,int starty,int finalx,int finaly,int deletedx,int deletedy,boolean is_redfrog){
        this.startx = startx;
        this.starty = starty;
        this.finaly = finaly;
        this.finalx = finalx;
        this.deletedx = deletedx;
        this.deletedy = deletedy;
        this.is_redfrog = is_redfrog;
    }
}
