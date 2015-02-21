
package antoku.argenttheconsortiumshuffle;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
        //TODO: consortium board. pick 10 random consortium members, be able to mark them per player, keep track of influence, make it visible who's at what influence and who has marked whom
        this.possibleTiles = new ArrayList<Tile>();
        this.manaTiles = new ArrayList<Tile>();
        this.goldTiles = new ArrayList<Tile>();
        this.influenceTiles = new ArrayList<Tile>();
        this.mageTiles = new ArrayList<Tile>();
        super.onCreate(savedInstanceState);

        //get the information from the previous activity
        Intent intent = getIntent();
        int numPlayers = Integer.valueOf(intent.getStringExtra(MainActivity.NUM_PLAYERS));
        String sidesUsed = intent.getStringExtra(MainActivity.SIDES_USED);
        boolean mancersUsed = intent.getBooleanExtra(MainActivity.INCLUDE_MANCERS, false);
        boolean inclRes = intent.getBooleanExtra(MainActivity.INCLUDE_RESOURCES, false);
        this.needsMages = intent.getBooleanExtra(MainActivity.INCLUDE_MAGE, false);
        boolean scenario = intent.getBooleanExtra(MainActivity.INCLUDE_SCENARIO, false);
        boolean white = intent.getBooleanExtra(MainActivity.INCLUDE_WHITE, false);

        //mark each individual resource as needed
        if(inclRes) {
            this.needsMana = true;
            this.needsGold = true;
            this.needsIP = true;
        }


        setContentView(R.layout.activity_display_game);
        LinearLayout display_game = (LinearLayout)findViewById(R.id.display_game);

        //populate possible tiles, depnding on if you're using A/B sides or both
        if(sidesUsed.equals("Mix") || sidesUsed.equals("All A")) {
            this.buildASides(mancersUsed);
        }

        if(sidesUsed.equals("Mix") || sidesUsed.equals("All B")) {
            this.buildBSides(mancersUsed);
        }
        //connect a-sides to their b-sides
        this.connectTiles(sidesUsed);
        if(numPlayers == 2) { //two-player games don't use Great Hall A or Dormitory tiles
            for(int i = this.possibleTiles.size() - 1; i >= 0; i--) {
                Tile t = this.possibleTiles.get(i);
                if (t.name.equals("Great Hall - A")) {
                    this.possibleTiles.remove(t);
                    this.influenceTiles.remove(t);
                }
                else if(t.name.contains("Dormitory")) {
                    this.possibleTiles.remove(t.aSide);
                    this.possibleTiles.remove(t.bSide);
                }
            }
        }

        //select the tiles to be used this game
        ArrayList<Tile> university = this.buildUniversity(numPlayers, sidesUsed);

        //select the players to be used this game. Those without Mancer's won't have the orange character
        if(!mancersUsed) {
            this.characterColors.remove(6);
        } //those who aren't advanced shouldn't use the neutral character
        if(!white) {
            if (this.characterColors.size() > numPlayers) {
                this.characterColors.remove(5);
            }
        }

        Random r = new Random();
        TextView tv;
        int first = r.nextInt(numPlayers); //select which player will be first player

        for(int i = 0; i < numPlayers; i++) { //choose which character each player will play as
            tv = new TextView(this);
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
            this.characterColors.remove(cha); //remove selected character from future consideration
            display_game.addView(tv);
        }

        if(scenario) { //choose a raondom scenario if being used this game
            tv = new TextView(this);
            String text = "Will be playing the \"";
            text += this.scenarios[r.nextInt(this.scenarios.length)];
            text += "\" Scenario";
            tv.setText(text);
            tv.setTextSize(20);
            display_game.addView(tv);
        }

        tv = new TextView(this);
        display_game.addView(tv);

        int size = university.size();
        for(int i=0; i<size; i++) { //shuffle the order of the university tiles
            tv = new TextView(this);
            int tile = r.nextInt(university.size());
            tv.setText(university.get(tile).name);
            tv.setTextSize(20);
            university.remove(tile);
            display_game.addView(tv);
        }
        Button advance = new Button(this);
        advance.setText("To the Game!");
        advance.setTextSize(20);
        final DisplayGameActivity test = this;
        advance.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(test, ConsortiumBoard.class);
            }
        });
        display_game.addView(advance);
    }

    private ArrayList<Tile> buildUniversity(int numPlayers, String sides) {
        int numTiles = this.tilesPerPlayer[numPlayers];
        numTiles -= 3;
        ArrayList<Tile> university = new ArrayList<Tile>();
        Random r = new Random();
        switch(sides){
            case "Mix":
                if (r.nextBoolean()) {
                    university.add(new Tile("Council Chamber - A",false,false,false,false));
                }
                else {
                    university.add(new Tile("Council Chamber - B",false,false,false,false));
                }
                if (r.nextBoolean() || numPlayers == 2) {
                    university.add(new Tile("Infirmary - B",false,false,false,false));
                }
                else {
                    university.add(new Tile("Infirmary - A",false,false,false,false));
                }
                if (r.nextBoolean()) {
                    university.add(new Tile("Library - A",false,false,false,false));
                }
                else {
                    university.add(new Tile("Library - B",false,false,false,false));
                }
                break;
            case "All A":
                university.add(new Tile("Council Chamber - A", false, false, false ,false));
                if (numPlayers == 2) { university.add(new Tile("Infirmary - B", false, false,false, false)); }
                else { university.add(new Tile("Infirmary - A", false, false, false, false)); }
                university.add(new Tile("Library - A", false, false, false, false));
                break;
            default:
                university.add(new Tile("Council Chamber - B", false, false, false, false));
                university.add(new Tile("Infirmary - B", false, false, false, false));
                university.add(new Tile("Library - B", false, false, false, false));
                break;
        }

        if (numPlayers == 2) { //don't require a +mage tile for 2 players
            this.needsMages = false;
        }
        if(this.needsGold) { //add a gold tile
            Tile tile = this.goldTiles.get(r.nextInt(this.goldTiles.size()));
            university.add(tile);
            numTiles -= 1; //needs one fewer tile
            this.possibleTiles.remove(tile.aSide);//don't want the other side showing up
            this.possibleTiles.remove(tile.bSide);
            //if this tile also has another resource, we no longer need to find a tile with that resource
            //otherwise, we don't want this tile showing up again
            if (tile.hasIP) { this.needsIP = false; }
            else { this.influenceTiles.remove(tile.aSide); this.influenceTiles.remove(tile.bSide); }
            if (tile.hasMana) { this.needsMana = false; }
            else { this.manaTiles.remove(tile.aSide); this.manaTiles.remove(tile.bSide); }
            if (tile.addsMages) { this.needsMages = false; }
            else {this.mageTiles.remove(tile.aSide); this.mageTiles.remove(tile.bSide); }
        }
        if(this.needsMana) { //same as gold, but with mana
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
        if(this.needsIP) { //same as gold, but with influence
            Tile tile = this.influenceTiles.get(r.nextInt(this.influenceTiles.size()));
            university.add(tile);
            numTiles -= 1;
            this.possibleTiles.remove(tile.aSide);
            this.possibleTiles.remove(tile.bSide);
            if (tile.addsMages) { this.needsMages = false; }
            else {this.mageTiles.remove(tile.aSide); this.mageTiles.remove(tile.bSide); }
        }
        if(this.needsMages) { //same as gold, but with mages
            Tile tile = this.mageTiles.get(r.nextInt(this.mageTiles.size()));
            university.add(tile);
            numTiles -= 1;
            this.possibleTiles.remove(tile.aSide);
            this.possibleTiles.remove(tile.bSide);
        }

        for (int i = 0; i < numTiles; i++) { //for the rest of the tiles, just pick at random
            Tile tile = this.possibleTiles.get(r.nextInt(this.possibleTiles.size()));
            university.add(tile);
            this.possibleTiles.remove(tile.aSide);
            this.possibleTiles.remove(tile.bSide);
        }
        return university;
    }

    public void buildASides(boolean hasMancers) { //all the a-side tiles
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
        if (hasMancers) { //mancer's tiles
            this.possibleTiles.add(new Tile("Research Archive - A", false, false, false, false));
            this.possibleTiles.add(new Tile("Atelier - A", true, true, false, false));
            this.possibleTiles.add(new Tile("Golem Lab - A", false, false, false, false));
            this.possibleTiles.add(new Tile("Laboratory - A", true, true, false, false));
            this.possibleTiles.add(new Tile("University Tavern - A", false, false, false, false));
            this.possibleTiles.add(new Tile("Synthesis Workshop - A", false, false, false, false));
        }
    }

    public void buildBSides(boolean hasMancers) { //all the b-side tiles
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
        if (hasMancers) { //mancer's tiles
            this.possibleTiles.add(new Tile("Research Archive - B", false, false, false, false));
            this.possibleTiles.add(new Tile("Atelier - B", true, true, true, false));
            this.possibleTiles.add(new Tile("Golem Lab - B", false, false, false, false));
            this.possibleTiles.add(new Tile("Laboratory - B", true, false, false, false));
            this.possibleTiles.add(new Tile("University Tavern - B", false, false, false, false));
            this.possibleTiles.add(new Tile("Synthesis Workshop - B", false, false, false, false));
        }
    }

    public void connectTiles(String mix) { //match up a and b sides so we don't get, eg, Chapel-a and Chapel-b
        if (mix.equals("Mix")) { //joining tiles is only necessary if you're using both sides
            int numTiles = this.possibleTiles.size() / 2;
            for (int i = 0; i < numTiles; i++) {
                this.possibleTiles.get(i).setBSide(this.possibleTiles.get(i + numTiles));
            }
        }
        else { //so if you're not using both sides, just reference itself for code reasons
            for (int i=0; i < this.possibleTiles.size(); i++) {
                this.possibleTiles.get(i).aSide = this.possibleTiles.get(i);
            }
        }

        //for each tile, if it has a resource, add it to the appropriate resource list
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
