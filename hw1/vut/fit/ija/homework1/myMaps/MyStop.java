package vut.fit.ija.homework1.myMaps;

import vut.fit.ija.homework1.maps.Coordinate;
import vut.fit.ija.homework1.maps.Stop;
import vut.fit.ija.homework1.maps.Street;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MyStop implements Stop {
    Set<MyStop> set = new HashSet<MyStop>();
    public Street street;
    public String name;
    public Coordinate coordinate;

    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if(!(obj instanceof MyStop)) return false;

        MyStop s2 = (MyStop) obj;
        return name.equals(s2.getId());
    }

    @Override
    public int hashCode(){
       return Objects.hash(name);
    }

    public MyStop(String name, Coordinate c1){
        set.add(this);
        this.name = name;
        this.coordinate = c1;
    }

    public MyStop(String name){
        set.add(this);
        this.name = name;
        this.coordinate = null;
    }

    @Override
    public String getId() {
        return this.name;
    }

    @Override
    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public void setStreet(Street s) {
        this.street = s;
    }

    @Override
    public Street getStreet() {
        return this.street;
    }
}
