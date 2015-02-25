package antoku.argenttheconsortiumshuffle;

/**
 * Created by antoku on 2/22/2015.
 */
public class Player {
    public int influence;
    public boolean[] marks = new boolean[] { true, true, false, false, false, false, false, false, false, false, false, false };
    public String color;

    public Player(String col) {
        this.influence = 5;
        this.color = col;
    }
}
