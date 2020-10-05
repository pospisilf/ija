/*
 * IJA projekt 2019/202
 * Autoři: xdvora3d, xpospi0f
 * Aplikace pro zobrazení linek hromadné dopravy a sledování jejich pohybu.
 */
package xpospi0f.zdrojaky;

import java.util.List;

public class Path{
    private List<Coordinate> path;


    /**
     * Setter pro nastavení cesty podle zadaných souřadnic.
     *
     * @param path seznam souřadnic.
     */
    public Path(List<Coordinate> path){
        this.path = path;
    }


    /**
     * Metoda pro výpočet celkové vzdálenosti trasy
     *
     * @return Celkovou vzdálenost trasy.
     */
    public double getWholeDistance(){
        double length = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Coordinate c1 = path.get(i);
            Coordinate c2 = path.get(i + 1);

            length = length + getDistance(c1, c2);
        }

        return length;
    }

    /**
     * Getter pro vrácení seznamu souřadnic.
     *
     * @return Seznam souřadnic
     */
    public List<Coordinate> return_path(){
        return this.path;
    }


    /**
     * Metoda pro výpočet euklidovské vzdálenosti mezi dvěmi souřadnicemi
     *
     * @param c1 první souřadnice
     * @param c2 druhá souřadnice
     * @return vzdálenost mezi dvěmi souřadnicemi.
     */
    public double getDistance(Coordinate c1, Coordinate c2){
        return Math.sqrt(Math.pow(c1.getX() - c2.getX(), 2) + Math.pow(c1.getY() - c2.getY(), 2));
    }


    /**
     * Metoda pro výpočet souřadnice další pozice vozidla na mapě podle ujeté vzdálenosti vozidla.
     *
     * @param distance Ujetá vzdálenost vozidla
     * @return Souřadnici další pozice vozidla
     */
    public Coordinate getActualPathCoordinate(double distance){
        double lenght = 0;

        Coordinate c1 = null;
        Coordinate c2 = null;

        for (int i = 0; i < path.size() - 1; i++) {
            c1 = path.get(i);
            c2 = path.get(i + 1);


            if (lenght + getDistance(c1, c2) >= distance) {

                break;

            }
            lenght = lenght + getDistance(c1, c2);
        }

        if (c1 == null || c2 == null) {
            return null;
        }

        double driven_distance = (distance - lenght) / getDistance(c1, c2);

        double x = c1.getX() + (c2.getX() - c1.getX()) * driven_distance;
        double y = c1.getY() + (c2.getY() - c1.getY()) * driven_distance;

        return new Coordinate(x, y);
    }

}
