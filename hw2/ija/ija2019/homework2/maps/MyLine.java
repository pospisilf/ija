package ija.ija2019.homework2.maps;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyLine implements Line{
    public String name;
    List<Street> ulice = new ArrayList<Street>();
    List<Stop> zastavky = new ArrayList<Stop>();
    List<AbstractMap.SimpleImmutableEntry<Street, Stop>> Linka = new ArrayList<>();
    public MyLine(String id){
        this.name = name;
    }

    @Override
    public boolean addStop(Stop stop){
        if(Linka.isEmpty()){
            zastavky.add(stop);
            ulice.add(stop.getStreet());
            Linka.add(new AbstractMap.SimpleImmutableEntry<Street, Stop>(stop.getStreet(), stop));
            return true;
        }
        else{
            if( stop.getStreet().follows(Linka.get(Linka.size()-1).getKey()) ){
                zastavky.add(stop);
                ulice.add(stop.getStreet());
                Linka.add(new AbstractMap.SimpleImmutableEntry<Street, Stop>(stop.getStreet(), stop));
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean addStreet(Street street){
        if(zastavky.size() == 0){
            //nelze jako první přidat ulici bez zastávky!
            return false;
        }
        else{
            if(street.follows(ulice.get(ulice.size()-1))){
                Linka.add(new AbstractMap.SimpleImmutableEntry<Street, Stop>(street, null));
                return true;
            }
            return false;
        }
    }

    @Override
    public List<AbstractMap.SimpleImmutableEntry<Street, Stop>> getRoute(){
        return Collections.unmodifiableList(this.Linka);
    }
}
