package ija.ija2019.homework2.maps;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Coordinate{
    Set<Coordinate> set = new HashSet<Coordinate>();
    public int x;
    public int y;

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public static Coordinate create(int x, int y){
        if (x < 0 || y < 0){
            return null;
        }
        else{
            return new Coordinate(x, y);
        }
    }

    public int diffX(Coordinate c){
        return Math.abs(this.getX() - c.getX());
    }

    public int diffY(Coordinate c){
        return Math.abs(this.getY() - c.getY());
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if(!(obj instanceof Coordinate)) return false;

        Coordinate c2 = (Coordinate) obj;
        return Objects.equals(x, c2.getX()) && Objects.equals(y, c2.getY());
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public int hashCode(){
        return Objects.hash(x, y);
    }
}
