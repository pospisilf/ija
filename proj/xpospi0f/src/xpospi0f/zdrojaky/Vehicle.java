/*
 * IJA projekt 2019/202
 * Autoři: xdvora3d, xpospi0f
 * Aplikace pro zobrazení linek hromadné dopravy a sledování jejich pohybu.
 */
package xpospi0f.zdrojaky;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Vehicle implements Drawable, Tupdate{
    private Coordinate start_position;
    public Coordinate actual_position;

    private Busline busline;

    private double distance = 0;
    private Path path;
    private double speed;
    private double original_speed = 5;
    private List<Shape> gui;


    /**
     * Setter pro nastavení jednoho vozidla.
     *
     * @param coor  Počáteční souřadnice vozidla.
     * @param speed Rychlost vozidla.
     */
    public Vehicle(Coordinate coor, double speed){
        this.start_position = coor;
        this.actual_position = coor;

        this.speed = speed;

        double radius = 5;

        gui = new ArrayList<>();
        Circle bus = new Circle(this.start_position.getX(), this.start_position.getY(), radius, Color.BLUE);

        gui.add(bus);
    }


    /**
     * Setter pro nastavení linky jednoho vozidla.
     *
     * @param bus Linka, která bude přiřazena vozidlu.
     */
    public void set_vehicle_busline(Busline bus){
        this.busline = bus;
    }

    public String get_number_line(){
        return this.busline.getid();
    }

    /**
     * Setter pro nastaveni aktualni rychlosti vozidla
     *
     * @param speed_change rychlost
     */
    public void set_speed(double speed_change){
        this.speed = speed_change;
    }

    /**
     * Setter pro nastaveni aktualni rychlosti vozidla na defaultni rychlost
     */
    public void set_default_speed(){
        this.speed = this.original_speed;
    }

    /**
     * Getter pro vraceni defaultni rychlosti vozidla
     *
     * @return defaultní rychlost vozidla
     */
    public double get_def_speed(){
        return this.original_speed;
    }

    /**
     * Getter pro vraceni aktualni rychlosti vozidla
     *
     * @return aktální rychlost vozidla
     */
    public double get_speed(){
        return this.speed;
    }


    /**
     * Metoda která nastaví, po jakých souřadnicích se bude vozidlo pohybovat.
     */
    public void set_vehicle_path(){
        List<Two_stop_path> pom = new ArrayList<>();
        List<Coordinate> pom_c = new ArrayList<>();

        System.out.println(this.busline.getid());
        pom = this.busline.get_line_path();

        for (Two_stop_path way : pom) {
            for (Coordinate coor : way.get_coordinates()) {
                pom_c.add(coor);
            }
        }

        this.path = new Path(pom_c);

        for (Coordinate c : pom_c) {
            System.out.println("---------");
            System.out.println(c.getX());
            System.out.println(c.getY());
            System.out.println("---------");
        }

    }

    /**
     * Metoda invertující aktální trasu vozidla
     */
    public void revert_path(){

        List<Coordinate> pom = new ArrayList<>();
        List<Coordinate> pom2 = new ArrayList<>();

        pom = this.path.return_path();


        for (int i = pom.size() - 1; i >= 0; i--) {
            pom2.add(pom.get(i));
        }


        this.path = new Path(pom2);
    }

    /**
     * Metoda která nastaví další pozici vozidla na mapě
     */
    private void moveVehicle(Coordinate coor){
        for (Shape vehicle : gui) {
            double x = (coor.getX() - this.actual_position.getX()) + vehicle.getTranslateX();
            double y = (coor.getY() - this.actual_position.getY()) + vehicle.getTranslateY();

            vehicle.setTranslateX(x);
            vehicle.setTranslateY(y);


        }
    }

    /**
     * @return seznam položek k vykreslení na mapu
     */
    @Override
    public List<Shape> getGUI(){
        return gui;
    }


    /**
     * Update pohybu vozidla
     */
    @Override
    public void update(LocalTime time){
        distance = distance + speed;

        if (distance > path.getWholeDistance()) {
            this.set_speed(0);
            return;
        }

        Coordinate coor = path.getActualPathCoordinate(distance);

        moveVehicle(coor);
        actual_position = coor;

    }
}
