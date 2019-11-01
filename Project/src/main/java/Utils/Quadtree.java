package Utils;

import java.awt.*;
import java.util.ArrayList;

public class Quadtree {
    private Rectangle rect;
    private Quadtree northWest, northEast, southEast, southWest;
    private int subWidth;
    private int subHeight;
    private int capacity = 4;
    private ArrayList<Point> points;
    private boolean divided = false;
    public Quadtree(Rectangle rect){

        this.rect = rect;
        subWidth = (int)rect.getWidth()/2;
        subHeight = (int)rect.getHeight()/2;
        points = new ArrayList<>();
    }
    public void divide(){
        this.northEast = new Quadtree(new Rectangle((int)rect.getCenterX(),(int)rect.getY() , subWidth, subHeight));
        this.northWest = new Quadtree(new Rectangle((int)rect.getX(),(int)rect.getY(),subWidth , subHeight));
        this.southEast = new Quadtree(new Rectangle((int)rect.getCenterX(),(int)rect.getCenterY(), subWidth, subHeight));
        this.southWest = new Quadtree(new Rectangle( (int)rect.getX(),(int)rect.getCenterY(),subWidth,subHeight));
        this.divided = true;

    }
    public void insert(Point p){
        if(!rect.contains(p)){
            return;
        }

        if(this.points.size() < this.capacity){
            this.points.add(p);
        }else{
            if(!this.divided){
                this.divide();
            }
            this.northWest.insert(p);
            this.northWest.insert(p);
            this.southWest.insert(p);
            this.southEast.insert(p);
        }
    }
}
