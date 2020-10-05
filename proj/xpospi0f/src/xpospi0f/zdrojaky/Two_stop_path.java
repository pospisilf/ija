/*
 * IJA projekt 2019/202
 * Autoři: xdvora3d, xpospi0f
 * Aplikace pro zobrazení linek hromadné dopravy a sledování jejich pohybu.
 */
package xpospi0f.zdrojaky;

import java.util.ArrayList;
import java.util.List;

public class Two_stop_path{
    private Stop stop1;
    private Stop stop2;

    private List<Coordinate> path = new ArrayList<>();


    /**
     * Konstruktor pro vytvoření spojení mezi dvěmi zastávkami, které leží buď na stejné ulici, nebo na dvou bezprostředně navazujících ulicích.
     *
     * @param s1 Počáteční zastávka.
     * @param s2 Koncová zastávka.
     * @throws java.lang.Exception výjimka pokud na sebe ulice zastávek nenavazují
     */
    public Two_stop_path(Stop s1, Stop s2) throws Exception{
        if (s1.getStreet().equals(s2.getStreet())) {
            this.path.add(s1.getCoordinate());
            this.path.add(s2.getCoordinate());
        } else {
            int pom = s1.getStreet().follows(s2.getStreet());

            switch (pom) {
                case 1:
                case 2:
                    this.path.add(s1.getCoordinate());
                    this.path.add(s1.getStreet().begin());
                    this.path.add(s2.getCoordinate());
                    break;
                case 3:
                case 4:
                    this.path.add(s1.getCoordinate());
                    this.path.add(s1.getStreet().end());
                    this.path.add(s2.getCoordinate());
                    break;
                default:
                    throw new Exception("Ulice zastavek na sebe nenavazuji");
            }
        }


    }


    /**
     * Konstruktor pro vytvoření spojení mezi dvěmi zastávkami, které jsou na ulicích ale dané ulice na sebe nemusí navazovat, je tedy nutné specifikovat trasu.
     *
     * @param s1      Počáteční zastávka.
     * @param s2      Koncová zastávka.
     * @param streets seznam ulic které se mají projet, aby se dalo dostat ze zastávky s1 do zastávky s2.
     * @throws java.lang.Exception výjimka pokud na sebe ulice zastávek nenavazují
     */
    public Two_stop_path(Stop s1, Stop s2, List<Street> streets) throws Exception{
        ArrayList<Street> pom_street = new ArrayList<>();

        pom_street.add(s1.getStreet());

        for (Street str : streets) {
            pom_street.add(str);
        }

        pom_street.add(s2.getStreet());

        this.path.add(s1.getCoordinate());

        for (int i = 0; i < pom_street.size() - 1; i++) {
            int pom = pom_street.get(i).follows(pom_street.get(i + 1));

            switch (pom) {
                case 1:
                case 2:
                    this.path.add(pom_street.get(i).begin());
                    break;
                case 3:
                case 4:
                    this.path.add(pom_street.get(i).end());
                    break;
                default:
                    throw new Exception("Ulice na sebe nenavazuji");
            }
        }

        int pom = pom_street.get(pom_street.size() - 1).follows(s2.getStreet());

        switch (pom) {
            case 1:
            case 2:
                this.path.add(pom_street.get(pom_street.size() - 1).begin());
                break;
            case 3:
            case 4:
                this.path.add(pom_street.get(pom_street.size() - 1).end());
                break;
            default:
                throw new Exception("Ulice na sebe nenavazuji");
        }

        this.path.add(s2.getCoordinate());


    }


    /**
     * Getter vracející seznam souřadnic mezi zastávkami s1 a s2.
     *
     * @return Seznam souřadnic.
     */
    public List<Coordinate> get_coordinates(){
        return this.path;
    }

    /**
     * Metoda vracející vzdálenost mezi zastávkami s1 a s2.
     *
     * @return vzdálenost mezi dvěmi zastávkami.
     */
    public double getDistance_between_two_stops(){
        double length = 0;
        for (int i = 0; i < this.path.size() - 1; i++) {
            Coordinate c1 = this.path.get(i);
            Coordinate c2 = this.path.get(i + 1);

            length = length + getDistance(c1, c2);
        }

        return length;
    }

    private double getDistance(Coordinate c1, Coordinate c2){
        return Math.sqrt(Math.pow(c1.getX() - c2.getX(), 2) + Math.pow(c1.getY() - c2.getY(), 2));
    }

}
