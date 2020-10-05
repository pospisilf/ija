/*
 * IJA projekt 2019/202
 * Autoři: xdvora3d, xpospi0f
 * Aplikace pro zobrazení linek hromadné dopravy a sledování jejich pohybu.
 */
package xpospi0f.zdrojaky;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


public class Stop implements Drawable{
    private String id_s;
    private Coordinate c;
    private Street street;
    private List<Shape> gui = new ArrayList<>();

    /**
     * Setter pro nastavení jedné zastávky.
     *
     * @param id   Jméno zastávky.
     * @param coor Souřadnice zastávky.
     * @param s    Ulice zastávky.
     */
    public Stop(java.lang.String id, Coordinate coor, Street s){
        this.id_s = id;
        this.c = coor;
        this.street = s;
        gui.add(new Rectangle(c.getX(), c.getY(), 10, 10));
        Text nazev = new Text(id);
        nazev.setFill(Color.RED);
        nazev.relocate(c.getX() + 15, c.getY());
        gui.add(nazev);
    }

    /**
     * Vrací souřadnice zastávky.
     *
     * @return souřadnice zastávky.
     */
    public Coordinate getCoordinate(){
        return this.c;
    }

    /**
     * Vrací jméno zastávky.
     *
     * @return jméno zastávky.
     */
    public String getId(){
        return this.id_s;
    }

    @Override
    public String toString(){
        return "stop(" + id_s + ")";
    }

    /**
     * Vrací ulici zastávky.
     *
     * @return ulice zastávky.
     */
    public Street getStreet(){
        return this.street;
    }

    @Override
    public List<Shape> getGUI(){
        return this.gui;
    }


}

