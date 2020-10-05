/*
 * IJA projekt 2019/202
 * Autoři: xdvora3d, xpospi0f
 * Aplikace pro zobrazení linek hromadné dopravy a sledování jejich pohybu.
 */
package xpospi0f.zdrojaky;


import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Street implements Drawable{
    private String id_s;
    private ArrayList<Stop> stops = new ArrayList<>();
    private Coordinate start;
    private Coordinate end;
    private List<Shape> gui = new ArrayList<>();
    public double througput_coeficient = 1;


    /**
     * Setter pro nastavení ulice podle zadaných souřadnic a jména.
     *
     * @param id Jméno ulice
     * @param c1 počáteční souřadnice ulice.
     * @param c2 koncová souřadnice ulice.
     */
    public Street(java.lang.String id, Coordinate c1, Coordinate c2){

        this.start = c1;
        this.end = c2;

        Line souradnice = new Line(c1.getX(), c1.getY(), c2.getX(), c2.getY());
        souradnice.setStrokeWidth(2);
        gui.add(souradnice);
        Text nazev = new Text(id);
        nazev.relocate((c1.getX() + c2.getX()) / 2, (c1.getY() + c2.getY()) / 2);

        gui.add(nazev);

        this.id_s = id;
    }


    @Override
    public int hashCode(){
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id_s);
        hash = 37 * hash + Objects.hashCode(this.stops);
        hash = 37 * hash + Objects.hashCode(this.start);
        hash = 37 * hash + Objects.hashCode(this.end);
        return hash;
    }


    @Override
    public boolean equals(Object obj){
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Street other = (Street) obj;
        if (!Objects.equals(this.id_s, other.id_s)) {
            return false;
        }
        if (!Objects.equals(this.stops, other.stops)) {
            return false;
        }
        if (!Objects.equals(this.start, other.start)) {
            return false;
        }
        return Objects.equals(this.end, other.end);
    }


    /**
     * Přidá do seznamu zastávek novou zastávku.
     *
     * @param stop Nově přidávaná zastávka.
     */
    public void addStop(Stop stop){
        this.stops.add(stop);
    }


    /**
     * Zjistí jestli ulice navazuje na další ulici.
     *
     * @param s ulice, jejíž návaznost se kontroluje
     * @return Číslo, které určuje jestli ulice na sebe navazují svými začátky nebo konci.
     */
    public int follows(Street s){
        if (this.begin().getX() == s.begin().getX() && this.begin().getY() == s.begin().getY()) {
            return 1;
        } else if (this.begin().getX() == s.end().getX() && this.begin().getY() == s.end().getY()) {
            return 2;
        } else if (this.end().getX() == s.begin().getX() && this.end().getY() == s.begin().getY()) {
            return 3;
        } else if (this.end().getX() == s.end().getX() && this.end().getY() == s.end().getY()) {
            return 4;
        } else {
            return 5;
        }

    }


    /**
     * Getter vracející souřadnici začátku ulice
     *
     * @return Souřadnice začátku ulice
     */
    public Coordinate begin(){
        return this.start;
    }


    /**
     * Getter vracející souřadnici konce ulice
     *
     * @return Souřadnice konce ulice
     */
    public Coordinate end(){
        return this.end;
    }


    /**
     * Getter vracející jméno ulice
     *
     * @return Jméno ulice
     */
    public String getId(){
        return this.id_s;
    }


    /**
     * Vrátí seznam zastávek na ulici.
     *
     * @return Seznam zastávek na ulici. Pokud ulize nemá žádnou zastávku, je seznam prázdný.
     */
    public List<Stop> getStops(){
        return this.stops;
    }


    @Override
    public List<Shape> getGUI(){
        return gui;
    }


}
