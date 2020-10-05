package vut.fit.ija.homework1.myMaps;

import vut.fit.ija.homework1.maps.Street;
import vut.fit.ija.homework1.maps.StreetMap;

import java.util.ArrayList;
import java.util.List;

public class MyStreetMap implements StreetMap {

    private List<Street> Streets = new ArrayList<Street>();
    private List<String> StreetsString = new ArrayList<String>();

    @Override
    public void addStreet(Street s) {
        Streets.add(s);
        StreetsString.add(s.getId());
    }

    @Override
    public Street getStreet(String id) {
        int index = StreetsString.indexOf(id);
        return Streets.get(index);
    }
}