package antoku.argenttheconsortiumshuffle;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class DisplayGameActivity extends ActionBarActivity {

    private int[] tilesPerPlayer = new int[] {0, 0, 9, 8, 10, 12, 15};
    private String[] scenarios = new String[] { "Assassins", "Key to the University",
            "Political Struggle", "Dimensional Rift", "Talismans of Magic",
            "The Well of Souls", "Summer Break" };
    private ArrayList<String> characterColors = new ArrayList<String>(Arrays.asList(new String[]
            { "Blue", "Grey", "Green", "Red", "Purple", "White", "Orange"}));

    private boolean needsMana;
    private boolean needsGold;
    private boolean needsMages;
    private boolean needsIP;

    private ArrayList<Tile> possibleTiles;
    private ArrayList<Tile> manaTiles;
    private ArrayList<Tile> goldTiles;
    private ArrayList<Tile> influenceTiles;
    private ArrayList<Tile> mageTiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.possibleTiles = new ArrayList<Tile>();
        this.manaTiles = new ArrayList<Tile>();
        this.goldTiles = new ArrayList<Tile>();
        this.influenceTiles = new ArrayList<Tile>();
        this.mageTiles = new ArrayList<Tile>();
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int numPlayers = Integer.valueOf(intent.getStringExtra(MainActivity.NUM_PLAYERS));
        String sidesUsed = intent.getStringExtra(MainActivity.SIDES_USED);
        boolean mancersUsed = intent.getBooleanExtra(MainActivity.INCLUDE_MANCERS, false);
        boolean inclRes = intent.getBooleanExtra(MainActivity.INCLUDE_RESOURCES, false);
        this.needsMages = intent.getBooleanExtra(MainActivity.INCLUDE_MAGE, false);
        boolean scenario = intent.getBooleanExtra(MainActivity.INCLUDE_SCENARIO, false);
        boolean white = intent.getBooleanExtra(MainActivity.INCLUDE_WHITE, false);

        if(inclRes) {
            this.needsMana = true;
            this.needsGold = true;
            this.needsIP = true;
        }


        setContentView(R.layout.activity_display_game);
        LinearLayout display_game = (LinearLayout)findViewById(R.id.display_game);

        if(sidesUsed.equals("Mix") || sidesUsed.equals("All A")) {
            this.buildASides(mancersUsed);
        }

        if(sidesUsed.equals("Mix") || sidesUsed.equals("All B")) {
            this.buildBSides(mancersUsed);
        }

        this.connectTiles(sidesUsed);

        ArrayList<Tile> university = this.buildUniversity(tilesPerPlayer[numPlayers], sidesUsed);

        if(!mancersUsed) {
            this.characterColors.remove(6);
        }
        if(!white) {
            if (this.characterColors.size() > numPlayers) {
                this.characterColors.remove(5);
            }
        }

        Random r = new Random();
        int first = r.nextInt(numPlayers);

        for(int i = 0; i < numPlayers; i++) {
            TextView tv = new TextView(this);
            int cha = r.nextInt(this.characterColors.size());
            String text = "Player " + i + ": " + this.characterColors.get(cha);
            if(sidesUsed.equals("All A")) {
                text += " - A";
            }
            else if(sidesUsed.equals("All B") || r.nextBoolean()) {
                text += " - B";
            }
            else {
                text += " - A";
            }
            if (i == first) {
                text += " Goes First";
            }
            tv.setText(text);
            tv.setTextSize(20);
            this.characterColors.remove(cha);
            display_game.addView(tv);
        }

        if(scenario) {
            TextView tv = new TextView(this);
            String text = "Will be playing the \"";
            text += this.scenarios[r.nextInt(this.scenarios.length)];
            text += "\" Scenario";
            tv.setText(text);
            tv.setTextSize(20);
            display_game.addView(tv);
        }

        int size = university.size();
        for(int i=0; i<size; i++) {
            TextView tv = new TextView(this);
            int tile = r.nextInt(university.size());
            tv.setText(university.get(tile).name);
            tv.setTextSize(20);
            university.remove(tile);
            display_game.addView(tv);
        }
    }

    private ArrayList<Tile> buildUniversity(int numTiles, String sides) {
        numTiles -= 3;
        ArrayList<Tile> university = new ArrayList<Tile>();
        Random r = new Random();
        if (sides.equals("Mix")) {
            if (r.nextBoolean()) {
                university.add(new Tile("Council Chamber - A",false,false,false,false));
            }
            else {
                university.add(new Tile("Council Chamber - B",false,false,false,false));
            }
            if (r.nextBoolean()) {
                university.add(new Tile("Infirmary - A",false,false,false,false));
            }
            else {
                university.add(new Tile("Infirmary - B",false,false,false,false));
            }
            if (r.nextBoolean()) {
                university.add(new Tile("Library - A",false,false,false,false));
            }
            else {
                university.add(new Tile("Library - B",false,false,false,false));
            }
        }
        else if (sides.equals("All A")) {
            university.add(new Tile("Council Chamber - A", false, false, false ,false));
            university.add(new Tile("Infirmary - A", false, false, false, false));
            university.add(new Tile("Library - A", false, false, false, false));
        }
        else {
            university.add(new Tile("Council Chamber - B", false, false, false, false));
            university.add(new Tile("Infirmary - B", false, false, false, false));
            university.add(new Tile("Library - B", false, false, false, false));
        }

        if(this.needsGold) {
            Tile tile = this.goldTiles.get(r.nextInt(this.goldTiles.size()));
            university.add(tile);
            numTiles -= 1;
            this.possibleTiles.remove(tile.aSide);
            this.possibleTiles.remove(tile.bSide);
            if (tile.hasIP) { this.needsIP = false; }
            else { this.influenceTiles.remove(tile.aSide); this.influenceTiles.remove(tile.bSide); }
            if (tile.hasMana) { this.needsMana = false; }
            else { this.manaTiles.remove(tile.aSide); this.manaTiles.remove(tile.bSide); }
            if (tile.addsMages) { this.needsMages = false; }
            else {this.mageTiles.remove(tile.aSide); this.mageTiles.remove(tile.bSide); }
        }
        if(this.needsMana) {
            Tile tile = this.manaTiles.get(r.nextInt(this.manaTiles.size()));
            university.add(tile);
            numTiles -= 1;
            this.possibleTiles.remove(tile.aSide);
            this.possibleTiles.remove(tile.bSide);
            if (tile.hasIP) { this.needsIP = false; }
            else { this.influenceTiles.remove(tile.aSide); this.influenceTiles.remove(tile.bSide); }
            if (tile.addsMages) { this.needsMages = false; }
            else {this.mageTiles.remove(tile.aSide); this.mageTiles.remove(tile.bSide); }
        }
        if(this.needsIP) {
            Tile tile = this.influenceTiles.get(r.nextInt(this.influenceTiles.size()));
            university.add(tile);
            numTiles -= 1;
            this.possibleTiles.remove(tile.aSide);
            this.possibleTiles.remove(tile.bSide);
            if (tile.addsMages) { this.needsMages = false; }
            else {this.mageTiles.remove(tile.aSide); this.mageTiles.remove(tile.bSide); }
        }
        if(this.needsMages) {
            Tile tile = this.mageTiles.get(r.nextInt(this.mageTiles.size()));
            university.add(tile);
            numTiles -= 1;
            this.possibleTiles.remove(tile.aSide);
            this.possibleTiles.remove(tile.bSide);
        }

        for (int i = 0; i < numTiles; i++) {
            Tile tile = this.possibleTiles.get(r.nextInt(this.possibleTiles.size()));
            university.add(tile);
            this.possibleTiles.remove(tile.aSide);
            this.possibleTiles.remove(tile.bSide);
        }
        return university;
    }

    public void buildASides(boolean hasMancers) {
        this.possibleTiles.add(new Tile("Adventuring - A", false, true, true, false));
        this.possibleTiles.add(new Tile("Archmage's Study - A", false, false, true, false));
        this.possibleTiles.add(new Tile("Astronomy Tower - A", true, true, false, false));
        this.possibleTiles.add(new Tile("Catacombs - A", false, false, true, false));
        this.possibleTiles.add(new Tile("Chapel - A", true, true, true, false));
        this.possibleTiles.add(new Tile("Courtyard - A", true, false, false, false));
        this.possibleTiles.add(new Tile("Dormitory - A", false, false, false, true));
        this.possibleTiles.add(new Tile("Great Hall - A", false, false, true, false));
        this.possibleTiles.add(new Tile("Guilds - A", true, true, false, false));
        this.possibleTiles.add(new Tile("Student Stores - A", false, false, false, false));
        this.possibleTiles.add(new Tile("Training Fields - A", false, false, false, false));
        this.possibleTiles.add(new Tile("Vault - A", false, false, false ,false));
        if (hasMancers) {
            this.possibleTiles.add(new Tile("Research Archive - A", false, false, false, false));
            this.possibleTiles.add(new Tile("Atelier - A", true, true, false, false));
            this.possibleTiles.add(new Tile("Golem Lab - A", false, false, false, false));
            this.possibleTiles.add(new Tile("Laboratory - A", true, true, false, false));
            this.possibleTiles.add(new Tile("University Tavern - A", false, false, false, false));
            this.possibleTiles.add(new Tile("Synthesis Workshop - A", false, false, false, false));
        }
    }

    public void buildBSides(boolean hasMancers) {
        this.possibleTiles.add(new Tile("Adventuring - B", false, false, false ,false));
        this.possibleTiles.add(new Tile("Archmage's Study - B", true, false, false, false));
        this.possibleTiles.add(new Tile("Astronomy Tower - B", true, true, false, true));
        this.possibleTiles.add(new Tile("Catacombs - B", false, true, true, false));
        this.possibleTiles.add(new Tile("Chapel - B", false, false, true, false));
        this.possibleTiles.add(new Tile("Courtyard - B", true, false, false, false));
        this.possibleTiles.add(new Tile("Dormitory - B", false, false, false, true));
        this.possibleTiles.add(new Tile("Great Hall - B", true, true, false, false));
        this.possibleTiles.add(new Tile("Guilds - B", true, true, false, false));
        this.possibleTiles.add(new Tile("Student Stores - B", false, false, false, false));
        this.possibleTiles.add(new Tile("Training Fields - B", true, false, false, false));
        this.possibleTiles.add(new Tile("Vault - B", false, true, false, false));
        if (hasMancers) {
            this.possibleTiles.add(new Tile("Research Archive - B", false, false, false, false));
            this.possibleTiles.add(new Tile("Atelier - B", true, true, true, false));
            this.possibleTiles.add(new Tile("Golem Lab - B", false, false, false, false));
            this.possibleTiles.add(new Tile("Laboratory - B", true, false, false, false));
            this.possibleTiles.add(new Tile("University Tavern - B", false, false, false, false));
            this.possibleTiles.add(new Tile("Synthesis Workshop - B", false, false, false, false));
        }
    }

    public void connectTiles(String mix) {
        if (mix.equals("Mix")) {
            int numTiles = this.possibleTiles.size() / 2;
            for (int i = 0; i < numTiles; i++) {
                this.possibleTiles.get(i).setBSide(this.possibleTiles.get(i + numTiles));
            }
        }
        else {
            for (int i=0; i < this.possibleTiles.size(); i++) {
                this.possibleTiles.get(i).aSide = this.possibleTiles.get(i);
            }
        }

        for (Tile t : this.possibleTiles) {
            if (t.hasGold) {
                this.goldTiles.add(t);
            }
            if (t.hasMana) {
                this.manaTiles.add(t);
            }
            if (t.hasIP) {
                this.influenceTiles.add(t);
            }
            if (t.addsMages) {
                this.mageTiles.add(t);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
