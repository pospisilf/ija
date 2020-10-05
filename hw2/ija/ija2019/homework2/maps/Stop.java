package ija.ija2019.homework2.maps;
import ija.ija2019.homework2.maps.MyStop;

public interface Stop{

    static Stop defaultStop(String name, Coordinate c){
        return new MyStop(name, c);
    }

    Coordinate getCoordinate();

    String getId();

    Street getStreet();

    void setStreet(Street street);

}
