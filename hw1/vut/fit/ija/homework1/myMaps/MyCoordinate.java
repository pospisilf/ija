package vut.fit.ija.homework1.myMaps;
import vut.fit.ija.homework1.maps.Coordinate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MyCoordinate implements Coordinate {

    Set<MyCoordinate> set = new HashSet<MyCoordinate>();
    public int x;
    public int y;


    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if(!(obj instanceof MyCoordinate)) return false;

        MyCoordinate c2 = (MyCoordinate) obj;
        return Objects.equals(x, c2.getX()) && Objects.equals(y, c2.getY());
    }

    @Override
    public int hashCode(){
        return Objects.hash(x, y);
    }

    public MyCoordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public static Coordinate create(int x, int y){
        if (x < 0 || y < 0){
            return null;
        }
        else{
            return new MyCoordinate(x, y);
        }
    }


    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }
}
