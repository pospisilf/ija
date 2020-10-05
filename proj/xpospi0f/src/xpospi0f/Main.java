/*
 * IJA projekt 2019/202
 * Autoři: xdvora3d, xpospi0f
 * Aplikace pro zobrazení linek hromadné dopravy a sledování jejich pohybu.
 */
package xpospi0f;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import xpospi0f.zdrojaky.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author xpospi0f
 * @author xdvora3d
 */
public class Main extends Application{
    private static List<String> cislaLinek_vypis = new ArrayList<>();
    private static List<String> zastavky_vypis = new ArrayList<>();
    private static List<String> seznam_busu = new ArrayList<>();
    private static List<Vehicle> seznam_busu_symlink = new ArrayList<>();
    private static List<String> zastavky_linky = new ArrayList<>();
    private static List<String> seznam_spoju = new ArrayList<>();
    private static List<String> seznam_linek = new ArrayList<>();
    private static List<Busline> seznam_linek_symlink = new ArrayList<>();
    private static List<String> seznam_casu_odjezdu = new ArrayList<>();
    public static List<Drawable> elem = new ArrayList<>();
    public static List<String> seznamZastavek = new ArrayList<>();
    public static List<Stop> symlinkNaZastavky = new ArrayList();
    public static List<Street> seznam_ulic = new ArrayList<>();

    public static List<String> vypis_zastavek(){
        return zastavky_vypis;
    }

    public static List<String> vypis_linek(){
        return cislaLinek_vypis;
    }

    public static List<String> get_seznam_busu(){
        return seznam_busu;
    }

    public static List<Vehicle> get_seznam_busu_symlink(){
        return seznam_busu_symlink;
    }

    public static List<String> get_zastavky_linky(){
        return zastavky_linky;
    }

    public static List<String> get_seznam_spoju(){
        return seznam_spoju;
    }

    public static List<String> get_seznam_linek(){
        return seznam_linek;
    }

    public static List<String> get_seznam_casu_odjezdu(){
        return seznam_casu_odjezdu;
    }

    public static List<String> get_seznamZastavek(){
        return seznamZastavek;
    }

    public static List<Stop> get_symlinkNaZastavky(){
        return symlinkNaZastavky;
    }

    public static List<Street> get_seznam_ulic(){
        return seznam_ulic;
    }

    public static List<Busline> get_seznam_linek_symlink(){
        return seznam_linek_symlink;
    }

    public static List<Drawable> get_elements(){
        return elem;
    }


    @Override
    public void start(Stage primaryStage) throws Exception{


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout.fxml"));
        BorderPane root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        MainController controller = loader.getController();
        List<String> seznamUlic = new ArrayList<>();
        List<Street> symlinkNaUlice = new ArrayList();
        List<String> nazevCest = new ArrayList<>();

        List<Two_stop_path> symlinkNaNazvy = new ArrayList<>();

        java.nio.file.Path path = Paths.get("");

        
        path = path.toAbsolutePath();


        String s = path.toString();


        //Načítání vstupního souboru s ulicemi.
        try {
            File myObj = new File(s + "/data/ulice_jednoduche.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String str = data.toString();
                String[] arrOfStr = str.split(":");

                String ulice = arrOfStr[0];
                String souradnice_pocatek = arrOfStr[1];
                String[] split_zacatek = souradnice_pocatek.split(",");
                String souradnice_konec = arrOfStr[2];
                String[] split_konec = souradnice_konec.split(",");

                seznamUlic.add(ulice);
                Street ulice1 = new Street(ulice, new Coordinate(Double.valueOf(split_zacatek[0]), Double.valueOf(split_zacatek[1])), new Coordinate(Double.valueOf(split_konec[0]), Double.valueOf(split_konec[1])));
                seznam_ulic.add(ulice1);
                symlinkNaUlice.add(ulice1);
                elem.add(ulice1);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Chyba pri otevirani souboru s mapami!");
            e.printStackTrace();
        }

        //Načítání vstupního souboru se zastávkami.
        try {
            File myObj = new File(s + "/data/zastavky.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String str = data.toString();
                String[] arrOfStr = str.split(":");

                String zastavka = arrOfStr[0];
                String souradnice_zastavky = arrOfStr[1];
                String[] split_souradnice = souradnice_zastavky.split(",");
                String naUlici = arrOfStr[2];

                int poradi = 0;
                while (poradi < seznamUlic.size()) {
                    if (seznamUlic.get(poradi).toString().equals(naUlici.toString())) {
                        seznamZastavek.add(zastavka);
                        Stop zastavka1 = new Stop(zastavka, new Coordinate(Double.valueOf(split_souradnice[0]), Double.valueOf(split_souradnice[1])), symlinkNaUlice.get(poradi));
                        symlinkNaZastavky.add(zastavka1);
                        elem.add(zastavka1);
                    }
                    poradi = poradi + 1;
                }

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Chyba pri otevirani souboru se zastavkami!");
            e.printStackTrace();
        }

        //Načitání vstupních souborů pro propojení zastávek.
        try {
            File myObj = new File(s + "/data/propojeni_zastavek.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] arrOfStr = data.split(":");
                String nazev = arrOfStr[0] + arrOfStr[1];
                String pocatek = arrOfStr[0];
                String konec = arrOfStr[1];
                Stop pocatekStop = null;
                Stop konecStop = null;
                for (int i = 0; i < seznamZastavek.size(); i++) {
                    if (seznamZastavek.get(i).toString().equals(pocatek.toString())) {
                        pocatekStop = symlinkNaZastavky.get(i);
                    } else if (seznamZastavek.get(i).toString().equals(konec.toString())) {
                        konecStop = symlinkNaZastavky.get(i);
                    }
                }
                nazevCest.add(nazev);
                Two_stop_path path1 = new Two_stop_path(pocatekStop, konecStop);
                symlinkNaNazvy.add(path1);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Chyba pri otevirani souboru se propojenim zastavek!");
            e.printStackTrace();
        }

        try {
            File myObj = new File(s +"/data/autobusy_se_zastavkami.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] arrOfStr = data.split(":");
                String cislo_linky = arrOfStr[0];
                String[] souradnice = arrOfStr[1].split(",");
                String rychlost = arrOfStr[2];
                String[] cesta = arrOfStr[3].split(",");
                String[] zastavky = arrOfStr[4].split(",");
                Busline linka1 = new Busline(arrOfStr[0]);
                String zastavky_linka = arrOfStr[0] + ":" + arrOfStr[4];
                zastavky_linky.add(zastavky_linka);
                seznam_linek.add(cislo_linky);
                seznam_linek_symlink.add(linka1);
                for (int i = 0; i < cesta.length; i++) {
                    for (int n = 0; n < nazevCest.size(); n++) {
                        if (nazevCest.get(n).toString().equals(cesta[i].toString())) {
                            linka1.addstop_conection(symlinkNaNazvy.get(n));
                        }
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Chyba pri otevirani souboru s autobusy!");
            e.printStackTrace();
        }

        //Načítání vstupních souborů pro výpis linek.
        try {
            File myObj = new File(s + "/data/autobusy_se_zastavkami.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] arrOfStr = data.split(":");
                String vystup = "";

                cislaLinek_vypis.add(arrOfStr[0]);
                String[] zastavky_naout = arrOfStr[4].split(",");
                for (int i = 0; i < zastavky_naout.length; i++) {
                    if (i == 0) {
                        vystup = vystup + zastavky_naout[i];
                    } else {
                        vystup = vystup + ":" + zastavky_naout[i];
                    }
                }
                zastavky_vypis.add(vystup);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Chyba pri otevirani souboru s autobusy!");
            e.printStackTrace();
        }

        //Načítání vstupích souborů se spoji.
        try {
            File myObj = new File(s + "/data/spoje.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                seznam_spoju.add(data);
                String[] arrOfStr = data.split(",");
                String linka = arrOfStr[0];
                String cas = arrOfStr[1];
                seznam_busu.add(linka);
                Vehicle bus1 = new Vehicle(new Coordinate(Double.valueOf(0), Double.valueOf(0)), Double.valueOf(0));
                seznam_busu_symlink.add(bus1);
                seznam_casu_odjezdu.add(cas);
                for (int i = 0; i < seznam_linek.size(); i++) {
                    if (linka.equals(seznam_linek.get(i))) {
                        bus1.set_vehicle_busline(seznam_linek_symlink.get(i));
                        bus1.set_vehicle_path();
                        elem.add(bus1);
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Chyba pri otevirani souboru se spoji!");
            e.printStackTrace();
        }


        controller.setElements(elem);

        controller.start(1);
    }
}

