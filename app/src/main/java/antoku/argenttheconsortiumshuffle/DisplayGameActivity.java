package antoku.argenttheconsortiumshuffle;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class DisplayGameActivity extends ActionBarActivity {

    private int[] tilesPerPlayer = new int[] {0, 0, 9, 8, 10, 12, 15};
    private boolean needsMana;
    private boolean needsGold;
    private boolean needsMages;
    private boolean needsIP;

    private ArrayList<Tile> possibleTiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.possibleTiles = new ArrayList<Tile>();
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int numPlayers = Integer.valueOf(intent.getStringExtra(MainActivity.NUM_PLAYERS));
        String sidesUsed = intent.getStringExtra(MainActivity.SIDES_USED);
        Boolean mancersUsed = intent.getBooleanExtra(MainActivity.INCLUDE_MANCERS, false);


        setContentView(R.layout.activity_display_game);
        LinearLayout display_game = (LinearLayout)findViewById(R.id.display_game);

        if(sidesUsed.equals("Mix") || sidesUsed.equals("All A")) {
            this.buildASides(mancersUsed);
        }

        if(sidesUsed.equals("Mix") || sidesUsed.equals("All B")) {
            this.buildBSides(mancersUsed);
        }

        if (sidesUsed.equals("Mix")) {
            this.connectTiles();
        }

        ArrayList<Tile> university = this.buildUniversity(tilesPerPlayer[numPlayers], sidesUsed);
        Random r = new Random();

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
        this.possibleTiles.add(new Tile("Astronomy Tower - B", true, true, false, true));
        this.possibleTiles.add(new Tile("Archmage's Study - B", true, false, false, false));
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

    public void connectTiles() {
        int numTiles = this.possibleTiles.size()/2;
        for (int i = 0; i < numTiles; i++) {
            this.possibleTiles.get(i).setBSide(this.possibleTiles.get(i+numTiles));
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
