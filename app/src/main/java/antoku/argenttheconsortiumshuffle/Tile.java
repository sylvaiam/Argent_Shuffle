package antoku.argenttheconsortiumshuffle;

/**
 * Created by antoku on 2/19/2015.
 */
public class Tile {
    public String name;
    public boolean hasMana;
    public boolean hasGold;
    public boolean hasIP;
    public boolean addsMages;
    public Tile aSide;
    public Tile bSide;

    public Tile(String name, boolean hasMana, boolean hasGold, boolean hasIP, boolean addsMages) {
        this.name = name;
        this.hasMana = hasMana;
        this.hasGold = hasGold;
        this.hasIP = hasIP;
        this.addsMages = addsMages;
    }

    //connect a b-side tile with its a side
    public void setASide(Tile aSide) {
        this.aSide = aSide;
        this.bSide = this;

        aSide.aSide = aSide;
        aSide.bSide = this;
    }

    //connect an a-side tile with its b side
    public void setBSide(Tile bSide) {
        this.aSide = this;
        this.bSide = bSide;

        bSide.aSide = this;
        bSide.bSide = bSide;
    }
}

