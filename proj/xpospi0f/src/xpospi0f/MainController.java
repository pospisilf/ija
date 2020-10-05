/*
 * IJA projekt 2019/202
 * Autoři: xdvora3d, xpospi0f
 * Aplikace pro zobrazení linek hromadné dopravy a sledování jejich pohybu.
 */
package xpospi0f;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import xpospi0f.zdrojaky.*;


/**
 * FXML Controller class
 *
 * @author xdvora3d
 * @author xpospi0f
 */
public class MainController {
    @FXML
    private Pane content;
    @FXML
    private Label time_label;
    @FXML
    private TextField busline_text;
    @FXML
    private ListView busline_view;
    @FXML
    private TextField time_scale_text;
    @FXML
    private TextField time_set_hh_text;
    @FXML
    private TextField time_set_mm_text;
    @FXML
    private ChoiceBox streets_menu;
    @FXML
    private ChoiceBox traffic_menu;
    
    
    
    private Timer timer;
    private LocalTime time = LocalTime.of(12,30,0);
    
    private List<Drawable> elements = new ArrayList<>();
    private List<Vehicle> movable_objects = new ArrayList<>();
    private List<Coordinate> high_lightedpath = new ArrayList<>();
    private List<Busline> seznam_linek_symlink = new ArrayList<>();
    private List<Street> slowed_streets = new ArrayList<>();
    
    /**
     * Initializes the controller class.
     */
    @FXML
    private void onZoom(ScrollEvent event)
    {
        event.consume();
        double zoom = event.getDeltaY() > 0 ? 1.2 : 0.8;
        
        content.setScaleX(zoom * content.getScaleX());
        content.setScaleY(zoom * content.getScaleY());
    }
    
    
   
    /**
        * Setter, jenž přidá do seznamu položky které se mají vykreslit na mapě.
        * @param elements seznam elementů, které se mají vykreslit
    */
    public void setElements(List<Drawable> elements)
    {
        this.elements = elements;
        
        for(Drawable drawable : elements )
        {
            content.getChildren().addAll(drawable.getGUI());
            
            if(drawable instanceof Vehicle) // pokud je objekt instací třídy Vehicle znamená to, že se bude pohybovat
            {
                movable_objects.add((Vehicle) drawable);
            }
        }
        
        set_elements_to_streets_menu();
        set_elements_to_traffic_menu();
        
    }
    
    @FXML
    private void set_elements_to_streets_menu()
    {
        ObservableList <String> items = FXCollections.observableArrayList();
        for(Street st : Main.get_seznam_ulic())
        {
            items.add(st.getId());
        }
        
        streets_menu.setItems(items);
    }
    
    @FXML
    private void set_elements_to_traffic_menu()
    {
        ObservableList <String> items = FXCollections.observableArrayList();
        
        items.add("Zadne");
        items.add("Lehke");
        items.add("Stredni");
        items.add("Tezke");
        items.add("Velmi tezke");
        
        traffic_menu.setItems(items);
    }
    
    
    
    @FXML
    public void set_traffic()
    {
       if(streets_menu.getValue() == null || traffic_menu.getValue() == null)
       {
           return;
       }
       
       ArrayList<Vehicle> buses = (ArrayList<Vehicle>) Main.get_seznam_busu_symlink();
       String street = streets_menu.getValue().toString();
       String traffic = traffic_menu.getValue().toString();
      
       for(Street st : Main.get_seznam_ulic())
       {
           if(st.getId().equals(street))
           {
               if(!slowed_streets.contains(st))
               {
                   slowed_streets.add(st);
               }
               
               switch (traffic)
               {
                   case "Zadne":
                       st.througput_coeficient = 1;
                       slowed_streets.remove(st);
                       break;
                   case "Lehke":
                       st.througput_coeficient = 0.8;
                       break;
                   case "Stredni":
                       st.througput_coeficient = 0.6;
                       break;
                   case "Tezke":
                       st.througput_coeficient = 0.4;
                       break;
                    case "Velmi tezke":
                       st.througput_coeficient = 0.2;
                       break;
               }
               
               break;
           }
       }
    }
    
    @FXML
    public void clear_traffic()
    {
        slowed_streets.clear();
    }
    
    
    /**
        * Metoda, která smaže zvýraznění cesty linky na mapě
    */
    @FXML
    public void del_highlight()
    {
        
        for(int i = 0; i < (high_lightedpath.size()-1); i++)
        {
            double x1 = high_lightedpath.get(i).getX();
            double y1 = high_lightedpath.get(i).getY();
            double x2 = high_lightedpath.get(i+1).getX();
            double y2 = high_lightedpath.get(i+1).getY();

            Line line = new Line(x1,y1,x2,y2);

            line.setStroke(Color.WHITE);
            line.setStrokeWidth(2.1);
            content.getChildren().add(line);

            line = new Line(x1,y1,x2,y2);

            line.setStroke(Color.BLACK);
            line.setStrokeWidth(2);
            content.getChildren().add(line);
        }
        high_lightedpath.clear();
        
        move_to_front();
        
    }
    
    /**
        * Metoda, zvýrazní cestu linky na mapě
        * @param linka id_linky jejíž cesta má být zvýrazněna
    */
    @FXML
    public void highlight_line(String linka)
    {
        del_highlight();
        
        for(Busline b_l : Main.get_seznam_linek_symlink())
        {
            ArrayList<Two_stop_path> t_p_l = new ArrayList<>();
            ArrayList<Coordinate> high_light = new ArrayList<>();
            t_p_l.addAll(b_l.get_line_path());
            
            for(Two_stop_path t_p : t_p_l)
            {
                high_light.addAll(t_p.get_coordinates());
            }

            for(int i = 0; i < (high_light.size()-1); i++)
            {
                if(linka.equals(b_l.getid()))
                {
                    high_lightedpath.add(high_light.get(i));
                    high_lightedpath.add(high_light.get(i+1));
                    
                    double x1 = high_light.get(i).getX();
                    double y1 = high_light.get(i).getY();
                    double x2 = high_light.get(i+1).getX();
                    double y2 = high_light.get(i+1).getY();
                    Line line = new Line(x1,y1,x2,y2);

                    line.setStroke(Color.YELLOW);
                    line.setStrokeWidth(2);
                    content.getChildren().add(line);
                }
            }
        }
        move_to_front();
    }
    
    @FXML
    private void move_to_front()
    {
        for(int i = 0; i < content.getChildren().size(); i++)
        {
            if(content.getChildren().get(i) instanceof Circle)
            {
                content.getChildren().get(i).toFront();
               
            }
        }
        for(int i = 0; i < content.getChildren().size(); i++)
        {
            if(content.getChildren().get(i) instanceof Circle)
            {
                content.getChildren().get(i).toFront();
               
            }
        }
        
    }
    
    
    
    


    /**
        * Metoda, která vypíše jízdní řád linky (id_linky se bere z textového pole)
    */
    @FXML
    public void print_busline()
    {
        String linka = busline_text.getText().toString();

        highlight_line(linka);
        
        List<String> linky = Main.vypis_linek();
        List<String> zastavky = Main.vypis_zastavek();

        for (int i = 0; i < linky.size(); i++) {
            if (linka.equals(linky.get(i))) {
                ObservableList <String> items = FXCollections.observableArrayList();
                String data = zastavky.get(i);
                String[] arrOfStr = data.split(":");
                System.out.println(arrOfStr[1]);

                for (int b = 0; b < arrOfStr.length; b++){
                    items.add(arrOfStr[b]);
                }

                busline_view.setItems(items);
            }
        }


    }

    /**
        * Metoda, která vypíše aktuální čas do gui
    */
    @FXML
    public void set_label_time()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.GERMANY);
        LocalTime time = this.time;
        String f = formatter.format(time);
        
        Platform.runLater(new Runnable() 
        {
            @Override 
            public void run() 
            {
                 time_label.setText(f);
            }
        });
    }
    
    
    /**
        * Metoda, která nastaví požadovaný čas
    */
    @FXML
    public void set_time()
    {
        String str_time = time_set_hh_text.getText() + ":" + time_set_mm_text.getText() + ":00";
        try
        {
            LocalTime s_time = LocalTime.parse(str_time);
            this.time = s_time;
        }
        catch (DateTimeParseException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Zadany cas neodpovida formatu HH:MM");
            alert.showAndWait();
        }
    }
    
    
    
    /**
        * Metoda, která zrychlí / zastaví čas
    */
    @FXML
    public void scale_time()
    {
        try
        {
            float time_c = Float.parseFloat(time_scale_text.getText());
            if(time_c >= 0)
            {
                timer.cancel();
                start(time_c);
            }
            else
            {
                throw new NumberFormatException();
            }
        }
        catch(NumberFormatException e)
        {
            time_scale_text.setText("Neplatné číslo!");
        }
    }
    
    private void check_if_bus_on_slowed_street(Vehicle bus)
    {
        
        for(Street street : slowed_streets)
        {
            double A = getDistance(street.begin(), bus.actual_position);
            double B = getDistance(street.end(), bus.actual_position);
            double C = getDistance(street.begin(), street.end());
            
            if((A + B < (C + 0.1)) && (A + B > (C - 0.1)))
            {
                if (bus.get_speed() == 0){
                }
                else{
                    bus.set_speed(bus.get_def_speed() * street.througput_coeficient);
                    System.out.println(bus.get_def_speed() * street.througput_coeficient);
                    return;
                }
            }
        }
        if(bus.get_speed() != 0)
            bus.set_default_speed();
        
    }
    
    
    private double getDistance(Coordinate c1, Coordinate c2)
    {
        return Math.sqrt(Math.pow(c1.getX() - c2.getX(), 2) + Math.pow(c1.getY() - c2.getY(), 2));
    }
   
    /**
        * Timer, který podle zadaných parametrů updatuje vozidla na mapě
    */




    public void start(float time_c)
    {
        timer = new Timer(Boolean.FALSE);

        List<String> seznam_busu = Main.get_seznam_busu();
        List<Vehicle> seznam_busu_symlink = Main.get_seznam_busu_symlink();
        List<String> SeznamSpoju = Main.get_seznam_spoju();
        List<String> Odjezdy = Main.get_seznam_casu_odjezdu();
        List<String> Linka_K_Odjezdu = Main.get_seznam_linek();
        List<String> Zastavky_linky = Main.get_zastavky_linky();
        List<Integer> Pocet_Ujetych_zasatvek = new ArrayList<>();
        List<String> seznamZastavek = Main.get_seznamZastavek();
        List<Stop> symlinkNaZastavky = Main.get_symlinkNaZastavky();
        for (int pjz = 0; pjz < SeznamSpoju.size(); pjz++){
            Pocet_Ujetych_zasatvek.add(1);
        }

        timer.scheduleAtFixedRate(new TimerTask(){
                                      @Override
                                      public void run(){
                                          time = time.plusSeconds(1);
                                          Date system = null;

                                          for (int i = 0; i < Odjezdy.size(); i++) {
                                              SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                              try {
                                                  if (time.toString().length() == 5) {
                                                      String toParse = time + ":00";
                                                      system = sdf.parse(toParse);
                                                  } else {
                                                      system = sdf.parse(time.toString());
                                                  }
                                              } catch (ParseException e) {
                                                  e.printStackTrace();
                                              }

                                              Date odjezd = null;
                                              try {
                                                  odjezd = sdf.parse(Odjezdy.get(i));
                                              } catch (ParseException e) {
                                                  e.printStackTrace();
                                              }

                                              long elapsed = odjezd.getTime() - system.getTime();
                                              if (elapsed == 0) {
                                                  seznam_busu_symlink.get(i).set_speed(5);             
                                                 
                                              }
                                          }

                                          //odjezdy ze zastávek
                                          for (int k = 0; k < seznam_busu.size(); k++) {
                                              String[] split_spoje = SeznamSpoju.get(k).split(",");
                                              String cas_odjezdu = split_spoje[1];
                                              String linka = split_spoje[0];

                                              SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                              try {
                                                  if (time.toString().length() == 5) {
                                                      String toParse = time + ":00";
                                                      system = sdf.parse(toParse);
                                                  } else {
                                                      system = sdf.parse(time.toString());
                                                  }
                                              } catch (ParseException e) {
                                                  e.printStackTrace();
                                              }

                                              Date odjezd = null;
                                              try {
                                                  odjezd = sdf.parse(cas_odjezdu);
                                              } catch (ParseException e) {
                                                  e.printStackTrace();
                                              }

                                              //Kontrola, zda autobus už náhodou není v depu. Pokud ano, nezajímá mne žádné zastavování na zastávkách - i kdyby měl zpoždění, tak jede co může!
                                              long elapsed = odjezd.getTime() - system.getTime();


                                              //potřeba ověřit, kdy má bus být na konečně zastávce a přišíst k TOMU, kdy mě to nemá zajímat!
                                              for (int j = 0; j < Zastavky_linky.size(); j++) {
                                                  String[] Zastavky_split = Zastavky_linky.get(j).split(":");
                                                  if (linka.equals(Zastavky_split[0])) {
                                                      String[] Jednotlive_zastavky_s_offsetem = Zastavky_split[1].split(",");
                                                      int pozice = ((Jednotlive_zastavky_s_offsetem.length) - 1);
                                                      String[] Posledni_zastavka = Jednotlive_zastavky_s_offsetem[pozice].split("\\+");
                                                      int offset_posledni_zastavky = Integer.parseInt((Posledni_zastavka[1]));

                                                      //Když je podle JŘ bus už v depu, neřeším ho
                                                      if ((elapsed + (offset_posledni_zastavky * 60 * 1000)*3) <= 0) {
                                                          System.out.println("Debug:Bus je zpatky v depu!");
                                                          Pocet_Ujetych_zasatvek.set(k, 1);
                                                      } else {
                                                          String dalsi_zastavka_i_s_offsetem = Jednotlive_zastavky_s_offsetem[Pocet_Ujetych_zasatvek.get(k)];
                                                          System.out.println("dalsi zastavka:");
                                                          System.out.println(Pocet_Ujetych_zasatvek.get(k));
                                                          String[] split_zas_s_off = dalsi_zastavka_i_s_offsetem.split("\\+");
                                                          String zastavka_to_be = split_zas_s_off[0];
                                                          String offset = split_zas_s_off[1];
                                                          int offset_int = Integer.parseInt(offset);
                                                          System.out.println(zastavka_to_be);
                                                          double souradniceX_dalsi_zastavky = 0; //fugenrova for debug purpose only
                                                          double souradniceY_dalsi_zastavky = 0;

                                                          for (int l = 0; l < seznamZastavek.size(); l++) {
                                                              if (zastavka_to_be.equals(seznamZastavek.get(l))) {
                                                                  Coordinate souradnice = symlinkNaZastavky.get(l).getCoordinate();
                                                                  souradniceX_dalsi_zastavky = souradnice.getX();
                                                                  souradniceY_dalsi_zastavky = souradnice.getY();
                                                              }
                                                          }

                                                          double souradniceX = seznam_busu_symlink.get(k).actual_position.getX();
                                                          double souradniceY = seznam_busu_symlink.get(k).actual_position.getY();

                                                          if (souradniceX > souradniceX_dalsi_zastavky){
                                                              if (souradniceY==souradniceY_dalsi_zastavky){
                                                                  if ((souradniceX-souradniceX_dalsi_zastavky) < 5){
                                                                      seznam_busu_symlink.get(k).set_speed(0); // autobus je na zastávce, takže

                                                                      if ((elapsed + offset_int * 60 * 1000) <= 0) {
                                                                          seznam_busu_symlink.get(k).set_speed(5);
                                                                          int aktualne_ujeto = Pocet_Ujetych_zasatvek.get(k);
                                                                          int zvyseno = aktualne_ujeto + 1;
                                                                          Pocet_Ujetych_zasatvek.set(k, zvyseno);
                                                                      }
                                                                  }
                                                              }
                                                          }
                                                          else if (souradniceX_dalsi_zastavky > souradniceX){
                                                              if (souradniceY==souradniceY_dalsi_zastavky){
                                                                  if ((souradniceX_dalsi_zastavky-souradniceX) < 5){
                                                                      seznam_busu_symlink.get(k).set_speed(0); // autobus je na zastávce, takže

                                                                      if ((elapsed + offset_int * 60 * 1000) <= 0) {
                                                                          seznam_busu_symlink.get(k).set_speed(5);
                                                                          int aktualne_ujeto = Pocet_Ujetych_zasatvek.get(k);
                                                                          int zvyseno = aktualne_ujeto + 1;
                                                                          Pocet_Ujetych_zasatvek.set(k, zvyseno);
                                                                      }
                                                                  }
                                                              }
                                                          }
                                                          else if (souradniceY > souradniceY_dalsi_zastavky){
                                                              if (souradniceX==souradniceX_dalsi_zastavky){
                                                                  if ((souradniceY-souradniceY_dalsi_zastavky) < 5){
                                                                      seznam_busu_symlink.get(k).set_speed(0); // autobus je na zastávce, takže

                                                                      if ((elapsed + offset_int * 60 * 1000) <= 0) {
                                                                          seznam_busu_symlink.get(k).set_speed(5);
                                                                          int aktualne_ujeto = Pocet_Ujetych_zasatvek.get(k);
                                                                          int zvyseno = aktualne_ujeto + 1;
                                                                          Pocet_Ujetych_zasatvek.set(k, zvyseno);
                                                                      }
                                                                  }
                                                              }
                                                          }
                                                          else if(souradniceY_dalsi_zastavky > souradniceY){
                                                              if (souradniceX==souradniceX_dalsi_zastavky){
                                                                  if ((souradniceY_dalsi_zastavky-souradniceY) < 5){
                                                                      seznam_busu_symlink.get(k).set_speed(0); // autobus je na zastávce, takže

                                                                      if ((elapsed + offset_int * 60 * 1000) <= 0) {
                                                                          seznam_busu_symlink.get(k).set_speed(5);
                                                                          int aktualne_ujeto = Pocet_Ujetych_zasatvek.get(k);
                                                                          int zvyseno = aktualne_ujeto + 1;
                                                                          Pocet_Ujetych_zasatvek.set(k, zvyseno);
                                                                      }
                                                                  }
                                                              }
                                                          }
                            }
                        }
                    }



                    //zkontroluju jestli uz probehl cas odjezdu, jestli jeste ne tak nedelam nic
                    //kdyz probehl, tak zaroven zkontroluju i posledni offset zastavky, kdyz uz ten cas je mimo, tak taky n euvazuju
                    //a jinak same podminky a nastavovani rychlosti
                }




                set_label_time();

                for(Vehicle m_object : movable_objects)
                {
                    check_if_bus_on_slowed_street(m_object);
                    m_object.update(time);
                }

            }
        }, 1000, (long) (1000 / time_c));

    }

}
