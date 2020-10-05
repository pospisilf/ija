package vut.fit.ija.homework1.myMaps;

import vut.fit.ija.homework1.maps.Coordinate;
import vut.fit.ija.homework1.maps.Stop;
import vut.fit.ija.homework1.maps.Street;

import java.util.ArrayList;
import java.util.List;

public class MyStreet implements Street {
    public String name;
    ArrayList<Stop> stops = new ArrayList<Stop>();
    ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();

    public MyStreet(String name, Coordinate coordinate, Coordinate coordinate1){
        this.name = name;
        coordinates.add(coordinate);
        coordinates.add(coordinate1);
    }

    @Override
    public String getId() {
        return this.name;
    }

    @Override
    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    @Override
    public List<Stop> getStops() {
        return stops;
    }

    @Override
    public void addStop(Stop stop) {
        stops.add(stop);
        stop.setStreet(this);
    }
}