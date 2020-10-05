package ija.ija2019.homework2.maps;

import java.util.AbstractMap;
import java.util.List;

public interface Line{
    boolean addStop(Stop stop);

    boolean addStreet(Street street);

    static Line defaultLine(String id){
        return new MyLine(id);
    }

    List<AbstractMap.SimpleImmutableEntry<Street, Stop>> getRoute();
}
