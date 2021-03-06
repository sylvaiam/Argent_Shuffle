package antoku.argenttheconsortiumshuffle;

import android.widget.Button;

/**
 * Created by antoku on 2/22/2015.
 */
public class Player {
    public int influence;
    public int rank;
    public boolean[] marks = new boolean[] { true, true, false, false, false, false, false, false, false, false, false, false };
    public String color;
    public Button button;

    public Player(String col) {
        this.influence = 5;
        this.color = col;
    }

    public String rankString() {
        switch(this.rank) {
            case 1: return "1st";
            case 2: return "2nd";
            case 3: return "3rd";
            default: return this.rank + "th";
        }
    }

    public void setButText(String text) {
        this.button.setText(text + " (" + this.rankString() + ")");
    }
}
