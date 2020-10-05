package ija.ija2019.homework2.maps;

import ija.ija2019.homework2.maps.Coordinate;

import java.util.ArrayList;
import java.util.List;


public class MyStreet implements Street{
    List<Coordinate> coordinates = new ArrayList<Coordinate>();
    List<Stop> zastavky = new ArrayList<Stop>();
    public String id;

    public MyStreet(String id, Coordinate c1, Coordinate c2, Coordinate c3){
        this.id = id;
        coordinates.add(c1);
        coordinates.add(c2);
        coordinates.add(c3);
    }

    public MyStreet(String id, Coordinate c1, Coordinate c3){
        this.id = id;
        coordinates.add(c1);
        coordinates.add(c3);
    }

    @Override
    public boolean addStop(Stop stop){
        if(coordinates.size() == 2){
            if(areCoordinatesOnStreet(stop, 0, 1) == true){
                zastavky.add(stop);
                stop.setStreet(this);
                return true;
            }
            else{
                return false;
            }
        }
        else if (coordinates.size() == 3){
            if(areCoordinatesOnStreet(stop, 0, 1) == true || areCoordinatesOnStreet(stop, 1, 2) == true){
                zastavky.add(stop);
                stop.setStreet(this);
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public boolean areCoordinatesOnStreet(Stop stop1, int id0, int id1){
        if ((getCoordinates().get(id0)).getX() == stop1.getCoordinate().getX()){
            if(((stop1.getCoordinate().getY() > getCoordinates().get(id1).getY()) || (stop1.getCoordinate().getY() < getCoordinates().get(id0).getY()))&&((stop1.getCoordinate().getY() < getCoordinates().get(id1).getY()) || (stop1.getCoordinate().getY() > getCoordinates().get(id0).getY()))){
                return false;
            }
            return (getCoordinates().get(id1)).getX() == stop1.getCoordinate().getX();
        }
        if ((getCoordinates().get(id0)).getY() == stop1.getCoordinate().getY()){
            if(((stop1.getCoordinate().getX() > getCoordinates().get(id1).getX()) || (stop1.getCoordinate().getX() < getCoordinates().get(id0).getX()))&&((stop1.getCoordinate().getX() < getCoordinates().get(id1).getX()) || (stop1.getCoordinate().getX() > getCoordinates().get(id0).getX()))){
                return false;
            }
            return (getCoordinates().get(id1)).getY() == stop1.getCoordinate().getY();
        }
        return ((getCoordinates().get(id0)).getX() - stop1.getCoordinate().getX())*((getCoordinates().get(id0)).getY() - stop1.getCoordinate().getY()) == (stop1.getCoordinate().getX() - (getCoordinates().get(id1)).getX())*(stop1.getCoordinate().getY() - (getCoordinates().get(id1)).getY());
    }


    @Override
    public Coordinate begin(){
        return coordinates.get(0);
    };

    @Override
    public Coordinate end(){
        if(coordinates.size() == 2){
            return coordinates.get(1);
        }
        else{
            return coordinates.get(2);
        }

    }

    @Override
    public boolean follows(Street s){
        int delkaThis = coordinates.size();
        int delkaS = s.getCoordinates().size();
//        System.out.println("DelkaThis: " + delkaThis);
//        System.out.println("DelkaS: " + delkaS);
        for (int i = 0; i < delkaThis; i++){
            for (int m = 0; m < delkaS; m++){
                if (this.coordinates.get(i).equals(s.getCoordinates().get(m))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<Coordinate> getCoordinates(){
        return coordinates;
    }

    @Override
    public String getId(){
        return this.id;
    }

    @Override
    public List<Stop> getStops(){
        return zastavky;
    }

}
