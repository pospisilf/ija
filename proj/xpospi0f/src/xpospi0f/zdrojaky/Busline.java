/*
 * IJA projekt 2019/202
 * Autoři: xdvora3d, xpospi0f
 * Aplikace pro zobrazení linek hromadné dopravy a sledování jejich pohybu.
 */
package xpospi0f.zdrojaky;


import java.util.ArrayList;
import java.util.List;

public class Busline{
    private java.lang.String id_l;
    private List<Two_stop_path> line_path = new ArrayList<>();

    /**
     * Setter pro nastavení jména autobusové linky.
     *
     * @param id název linky
     */
    public Busline(String id){
        this.id_l = id;
    }

    /**
     * Vrací název autobusové linky.
     *
     * @return název autobusové linky.
     */
    public String getid(){
        return this.id_l;
    }

    /**
     * Přidá spojení mezi dvěma zastávekami do seznamu zastávek.
     *
     * @param path spojení mezi devěmi zastávkami
     */
    public void addstop_conection(Two_stop_path path){
        this.line_path.add(path);

    }

    /**
     * Vrací seznam, jehož každá položka reprezentuje spojení mezi dvěmi zastávkami.
     *
     * @return výše popsaný seznam.
     */
    public List<Two_stop_path> get_line_path(){
        return this.line_path;
    }

}
