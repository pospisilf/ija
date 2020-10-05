package ija.ija2019.homework2.maps;
import ija.ija2019.homework2.maps.MyStreet;

import java.util.Collection;
import java.util.List;

public interface Street{

    boolean addStop(Stop stop);

    public Coordinate begin();

    static Street defaultStreet(String id, Coordinate c1, Coordinate c2, Coordinate c3){
        if(isItOK(c1, c2, c3)) {
            return new MyStreet(id, c1, c2, c3);
        }
        else{
            return null;
        }
    }

    static Street defaultStreet(String id, Coordinate c1, Coordinate c3){
        return new MyStreet(id, c1, c3);
    }

    static boolean isItOK(Coordinate c1, Coordinate c2, Coordinate c3){
        return (c3.getX() - c2.getX()) * (c2.getX() - c1.getX()) + (c3.getY() - c2.getY()) * (c2.getY() - c1.getY()) == 0;
    }

    public Coordinate end();

    public boolean follows(Street s);

    List<Coordinate> getCoordinates();

    public String getId();

    public List<Stop> getStops();


}
