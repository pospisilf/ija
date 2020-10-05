package ija.ija2019.homework2.maps;

public class MyStop implements Stop{
    public String name;
    public Coordinate souradnice;
    public Street ulice = null;

    public MyStop(String name, Coordinate c1){
        this.name = name;
        this.souradnice = c1;
    }

    public Coordinate getCoordinate(){
        return this.souradnice;
    }

    @Override
    public String getId(){
        return this.name;
    }

    @Override
    public void setStreet(Street street){
        this.ulice = street;
    }

    @Override
    public Street getStreet(){
        return this.ulice;
    }

    @Override
    public String toString() {
        return String.format("stop("+name+")");
    }

}
