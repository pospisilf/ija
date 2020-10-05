/*
 * IJA projekt 2019/202
 * Autoři: xdvora3d, xpospi0f
 * Aplikace pro zobrazení linek hromadné dopravy a sledování jejich pohybu.
 */
package xpospi0f.zdrojaky;


public class Coordinate{
    private double var_x;
    private double var_y;

    /**
     * Setter pro nastavení jedné souřadnice.
     *
     * @param x x-ová souřadnice koordinátu
     * @param y y-ová souřadnice koordinátu
     */
    public Coordinate(double x, double y){
        this.var_x = x;
        this.var_y = y;
    }


    @Override
    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Coordinate cor = (Coordinate) o;
        return var_x == cor.var_x && var_y == cor.var_y;
    }


    @Override
    public int hashCode(){
        int hash = 7;
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.var_x) ^ (Double.doubleToLongBits(this.var_x) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.var_y) ^ (Double.doubleToLongBits(this.var_y) >>> 32));
        return hash;
    }


    /**
     * Vrací rozdíl mezi dvěmi souřadnicemi x.
     *
     * @param c souřadnice x
     * @return rozdíl mezi dvěmi souřadnicemi x.
     */
    public double diffX(Coordinate c){
        return (this.var_x - c.var_x);
    }


    /**
     * Vrací rozdíl mezi dvěmi souřadnicemi y.
     *
     * @param c souřadnice y
     * @return rozdíl mezi dvěmi souřadnicemi y.
     */
    public double diffY(Coordinate c){
        return (this.var_y - c.var_y);
    }


    /**
     * Vrací hodnotu souřadnice x.
     *
     * @return Souřadnice x.
     */
    public double getX(){
        return this.var_x;
    }


    /**
     * Vrací hodnotu souřadnice y.
     *
     * @return Souřadnice y.
     */
    public double getY(){
        return this.var_y;
    }


}






