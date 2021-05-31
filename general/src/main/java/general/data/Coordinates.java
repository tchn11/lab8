package general.data;


import java.io.Serializable;

/**
 * X-Y coordinates
 */
public class Coordinates implements Serializable {
    private long x; //\u041C\u0430\u043A\u0441\u0438\u043C\u0430\u043B\u044C\u043D\u043E\u0435 \u0437\u043D\u0430\u0447\u0435\u043D\u0438\u0435 \u043F\u043E\u043B\u044F: 752
    private int y;

    /**
     * Constructor, just set x, y
     * @param X X coordinate
     * @param Y Y coordinate
     */
    public Coordinates(long X, int Y){
        x = X;
        y = Y;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + Long.toString(x) +
                ", y=" + Integer.toString(y) +
                '}';
    }

    public int getY() {
        return y;
    }

    public long getX() {
        return x;
    }
}
