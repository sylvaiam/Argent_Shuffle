
package antoku.argenttheconsortiumshuffle;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
    private String[] possibleColors;
    private int[] newIds;

    private boolean needsMana;
    private boolean needsGold;
    private boolean needsMages;
    private boolean needsIP;
    private boolean hasMancers;
    private boolean hasArchmage;

    private ArrayList<Tile> possibleTiles;
    private ArrayList<Tile> manaTiles;
    private ArrayList<Tile> goldTiles;
    private ArrayList<Tile> influenceTiles;
    private ArrayList<Tile> mageTiles;

    private int firstPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        this.hasMancers = mancersUsed;
        boolean inclRes = intent.getBooleanExtra(MainActivity.INCLUDE_RESOURCES, false);
        this.needsMages = intent.getBooleanExtra(MainActivity.INCLUDE_MAGE, false);
        boolean scenario = intent.getBooleanExtra(MainActivity.INCLUDE_SCENARIO, false);
        boolean white = intent.getBooleanExtra(MainActivity.INCLUDE_WHITE, false);
        this.hasArchmage = intent.getBooleanExtra(MainActivity.INCLUDE_ARCHMAGE, false);

        this.newIds = new int[numPlayers];

        //mark each individual resource as needed
        if(inclRes) {
            this.needsMana = true;
            this.needsGold = true;
            this.needsIP = true;
        }


        setContentView(R.layout.activity_display_game);
        LinearLayout display_game = (LinearLayout)findViewById(R.id.display_game);

        this.addTiles(sidesUsed, mancersUsed, numPlayers, false);

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

        //maintain all the possible colors for indexing purposes
        this.possibleColors = new String[this.characterColors.size()];
        this.possibleColors = this.characterColors.toArray(this.possibleColors);

        Random r = new Random();
        TextView tv;
        Spinner s;
        String text;
        this.firstPlayer = r.nextInt(numPlayers); //select which player will be first player

        //Mage Powers block
        tv = new TextView(this);
        tv.setText("Mage Powers:");
        tv.setTextSize(20);
        display_game.addView(tv);

        //for each mage type, select power A or B
        for (int i = 0; i < this.possibleColors.length; i++) {
            String col; String color = this.possibleColors[i];
            if (color.equals("White")) continue; //neutral mages have no powers
            tv = new TextView(this);
            switch(color) { //orange isn't an HTML color in android
                case "Orange": col = "#ffa500"; break;
                default: col = color; break;
            }

            //add color to the department name
            text = "<font color='" + col + "'>" + colorToDepartment(color) + "</font> -";
            if(sidesUsed.equals("All A")) {
                text += " Side A";
            }
            else if(sidesUsed.equals("All B") || r.nextBoolean()) {
                text += " Side B";
            }
            else {
                text += " Side A";
            }
            tv.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
            tv.setTextSize(20);
            display_game.addView(tv);
        }
        tv = new TextView(this);
        display_game.addView(tv);

        //Player Department block
        tv = new TextView(this);
        tv.setText("Player Colors:");
        tv.setTextSize(20);
        display_game.addView(tv);

        for(int i = 0; i < numPlayers; i++) { //choose which character each player will play as
            s = new Spinner(this);
            tv = new TextView(this);
            int cha = r.nextInt(this.characterColors.size());
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, this.possibleColors);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            s.setAdapter(spinnerAdapter);
            s.setSelection(spinnerAdapter.getPosition(this.characterColors.get(cha)));
            int newId;
            //generate a unique id for each spinner
            newId = i + 1;
            s.setId(newId);
            this.newIds[i] = newId;
            text = "Player " + i + ": ";
            if(sidesUsed.equals("All A")) {
                text += " Side A";
            }
            else if(sidesUsed.equals("All B") || r.nextBoolean()) {
                text += " Side B";
            }
            else {
                text += " Side A";
            }
            if (i == this.firstPlayer) {
                text += " Goes First";
            }
            tv.setText(text);
            tv.setTextSize(20);
            this.characterColors.remove(cha); //remove selected character from future consideration
            display_game.addView(tv);
            display_game.addView(s);
        }

        if(scenario) { //choose a raondom scenario if being used this game
            tv = new TextView(this);
            text = "Will be playing the \"";
            text += this.scenarios[r.nextInt(this.scenarios.length)];
            text += "\" Scenario";
            tv.setText(text);
            tv.setTextSize(20);
            display_game.addView(tv);
        }

        tv = new TextView(this);
        display_game.addView(tv);
        tv = new TextView(this);
        tv.setText("Tiles Chosen:");
        tv.setTextSize(20);
        display_game.addView(tv);

        if(this.hasArchmage) {//choose which side of the staff to use (B side isn't a mana tile, even though you can gain mana from it)
            tv = new TextView(this);
            text = "Archmage's Staff - ";
            if(sidesUsed.equals("All A")) {
                text += " Side A";
            }
            else if(sidesUsed.equals("All B") || r.nextBoolean()) {
                text += " Side B";
            }
            else {
                text += " Side A";
            }
            tv.setText(text);
            tv.setTextSize(20);
            display_game.addView(tv);
        }

        int size = university.size();
        for(int i=0; i<size; i++) { //shuffle the order of the university tiles
            tv = new TextView(this);
            int tile = r.nextInt(university.size());
            tv.setText(university.get(tile).name);
            tv.setTextSize(20);
            university.remove(tile);
            display_game.addView(tv);
        }
    }

    private String colorToDepartment(String color) {
        switch (color) {
            case "Red": return "Sorcery";
            case "Blue": return "Divinity";
            case "Grey": return "Mysticism";
            case "Green": return "Natural Magick";
            case "Purple": return "Planar Studies";
            case "Orange": return "Technomancy";
            case "White": return "Neutral";
            default: return "err";
        }
    }

    public void pickVoters(View view) {
        String colors = "";
        String playerText = "";
        for (int i = 0; i < this.newIds.length; i++) {
            Spinner s = (Spinner)findViewById(this.newIds[i]);
            int selected = s.getSelectedItemPosition();
            if (colors.contains("" + selected)) {//make sure there are no repeat colors
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Player " + i + " has a repeat color with " + colors.indexOf("" + selected) + ".").setPositiveButton("Okay", null).show();
                return;
            }
            colors += selected;
            if (i == 0) {
                playerText += this.possibleColors[selected];
            }
            else {
                playerText += "," + this.possibleColors[selected];
            }
        }
        Intent intent = new Intent(this, ConsortiumBoard.class);
        intent.putExtra(MainActivity.PLAYER_COLORS, playerText);
        intent.putExtra(MainActivity.INCLUDE_MANCERS, this.hasMancers);
        intent.putExtra(MainActivity.FIRST_PLAYER, this.firstPlayer);
        intent.putExtra(MainActivity.INCLUDE_ARCHMAGE, this.hasArchmage);

        startActivity(intent);
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
        if(this.goldTiles.size() == 0) {
            this.needsGold = false;
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
        if(this.manaTiles.size() == 0) {
            this.needsMana = false;
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
        if(this.influenceTiles.size() == 0) {
            this.needsIP = false;
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
        if(this.mageTiles.size() == 0) {
            this.needsMages = false;
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

    public void addTiles(String sidesUsed, boolean mancersUsed, int numPlayers, boolean ignorePrefs) {
        //populate possible tiles, depending on if you're using A/B sides or both
        if(sidesUsed.equals("Mix") || sidesUsed.equals("All A")) {
            this.buildASides(mancersUsed, ignorePrefs);
        }

        if(sidesUsed.equals("Mix") || sidesUsed.equals("All B")) {
            this.buildBSides(mancersUsed, ignorePrefs);
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

        if(this.possibleTiles.size() < this.tilesPerPlayer[numPlayers]) {
            this.possibleTiles.clear();
            this.addTiles(sidesUsed, mancersUsed, numPlayers, true);
        }
    }

    public void buildASides(boolean hasMancers, boolean ignorePrefs) { //all the a-side tiles
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(ignorePrefs || prefs.getBoolean("pref_adventuring_a", true)) {
            this.possibleTiles.add(new Tile("Adventuring - A", false, true, true, false));
        }
        if(ignorePrefs || prefs.getBoolean("pref_archmage_study_a", true)) {
            this.possibleTiles.add(new Tile("Archmage's Study - A", false, false, true, false));
        }
        if(ignorePrefs || prefs.getBoolean("pref_astronomy_tower_a", true)) {
            this.possibleTiles.add(new Tile("Astronomy Tower - A", true, true, false, false));
        }
        if(ignorePrefs || prefs.getBoolean("pref_catacombs_a", true)) {
            this.possibleTiles.add(new Tile("Catacombs - A", false, false, true, false));
        }
        if(ignorePrefs || prefs.getBoolean("pref_chapel_a", true)) {
            this.possibleTiles.add(new Tile("Chapel - A", true, true, true, false));
        }
        if(ignorePrefs || prefs.getBoolean("pref_courtyard_a", true)) {
            this.possibleTiles.add(new Tile("Courtyard - A", true, false, false, false));
        }
        if(ignorePrefs || prefs.getBoolean("pref_dormitory_a", true)) {
            this.possibleTiles.add(new Tile("Dormitory - A", false, false, false, true));
        }
        if(ignorePrefs || prefs.getBoolean("pref_great_hall_a", true)) {
            this.possibleTiles.add(new Tile("Great Hall - A", false, false, true, false));
        }
        if(ignorePrefs || prefs.getBoolean("pref_guild_a", true)) {
            this.possibleTiles.add(new Tile("Guilds - A", true, true, false, false));
        }
        if(ignorePrefs || prefs.getBoolean("pref_stud_store_a", true)) {
            this.possibleTiles.add(new Tile("Student Stores - A", false, false, false, false));
        }
        if(ignorePrefs || prefs.getBoolean("pref_train_field_a", true)) {
            this.possibleTiles.add(new Tile("Training Fields - A", false, false, false, false));
        }
        if(ignorePrefs || prefs.getBoolean("pref_vault_a", true)) {
            this.possibleTiles.add(new Tile("Vault - A", false, false, false, false));
        }
        if (hasMancers) { //mancer's tiles
            if(ignorePrefs || prefs.getBoolean("pref_research_arch_a", true)) {
                this.possibleTiles.add(new Tile("Research Archive - A", false, false, false, false));
            }
            if(ignorePrefs || prefs.getBoolean("pref_atelier_a", true)) {
                this.possibleTiles.add(new Tile("Atelier - A", true, true, false, false));
            }
            if(ignorePrefs || prefs.getBoolean("pref_golem_lab_a", true)) {
                this.possibleTiles.add(new Tile("Golem Lab - A", false, false, false, false));
            }
            if(ignorePrefs || prefs.getBoolean("pref_lab_a", true)) {
                this.possibleTiles.add(new Tile("Laboratory - A", true, true, false, false));
            }
            if(ignorePrefs || prefs.getBoolean("pref_univ_tavern_a", true)) {
                this.possibleTiles.add(new Tile("University Tavern - A", false, false, false, false));
            }
            if(ignorePrefs || prefs.getBoolean("pref_synth_work_a", true)) {
                this.possibleTiles.add(new Tile("Synthesis Workshop - A", false, false, false, false));
            }
        }
    }

    public void buildBSides(boolean hasMancers, boolean ignorePrefs) { //all the b-side tiles
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(ignorePrefs || prefs.getBoolean("pref_adventuring_b", true)) {
            this.possibleTiles.add(new Tile("Adventuring - B", false, false, false, false));
        }
        if(ignorePrefs || prefs.getBoolean("pref_archmage_study_b", true)) {
            this.possibleTiles.add(new Tile("Archmage's Study - B", true, false, false, false));
        }
        if(ignorePrefs || prefs.getBoolean("pref_astronomy_tower_b", true)) {
            this.possibleTiles.add(new Tile("Astronomy Tower - B", true, true, false, true));
        }
        if(ignorePrefs || prefs.getBoolean("pref_catacombs_b", true)) {
            this.possibleTiles.add(new Tile("Catacombs - B", false, true, true, false));
        }
        if(ignorePrefs || prefs.getBoolean("pref_chapel_b", true)) {
            this.possibleTiles.add(new Tile("Chapel - B", false, false, true, false));
        }
        if(ignorePrefs || prefs.getBoolean("pref_courtyard_b", true)) {
            this.possibleTiles.add(new Tile("Courtyard - B", true, false, false, false));
        }
        if(ignorePrefs || prefs.getBoolean("pref_dormitory_b", true)) {
            this.possibleTiles.add(new Tile("Dormitory - B", false, false, false, true));
        }
        if(ignorePrefs || prefs.getBoolean("pref_great_hall_b", true)) {
            this.possibleTiles.add(new Tile("Great Hall - B", true, true, false, false));
        }
        if(ignorePrefs || prefs.getBoolean("pref_guild_b", true)) {
            this.possibleTiles.add(new Tile("Guilds - B", true, true, false, false));
        }
        if(ignorePrefs || prefs.getBoolean("pref_stud_store_b", true)) {
            this.possibleTiles.add(new Tile("Student Stores - B", false, false, false, false));
        }
        if(ignorePrefs || prefs.getBoolean("pref_train_field_b", true)) {
            this.possibleTiles.add(new Tile("Training Fields - B", true, false, false, false));
        }
        if(ignorePrefs || prefs.getBoolean("pref_vault_b", true)) {
            this.possibleTiles.add(new Tile("Vault - B", false, true, false, false));
        }
        if (hasMancers) { //mancer's tiles
            if(ignorePrefs || prefs.getBoolean("pref_research_arch_b", true)) {
                this.possibleTiles.add(new Tile("Research Archive - B", false, false, false, false));
            }
            if(ignorePrefs || prefs.getBoolean("pref_atelier_b", true)) {
                this.possibleTiles.add(new Tile("Atelier - B", true, true, true, false));
            }
            if(ignorePrefs || prefs.getBoolean("pref_golem_lab_b", true)) {
                this.possibleTiles.add(new Tile("Golem Lab - B", false, false, false, false));
            }
            if(ignorePrefs || prefs.getBoolean("pref_lab_b", true)) {
                this.possibleTiles.add(new Tile("Laboratory - B", true, false, false, false));
            }
            if(ignorePrefs || prefs.getBoolean("pref_univ_tavern_b", true)) {
                this.possibleTiles.add(new Tile("University Tavern - B", false, false, false, false));
            }
            if(ignorePrefs || prefs.getBoolean("pref_synth_work_b", true)) {
                this.possibleTiles.add(new Tile("Synthesis Workshop - B", false, false, false, false));
            }
        }
    }

    public void connectingHelper(Tile t) {
        String match = t.name.replace(" - A", " - B");
        boolean hasMatch = false;
        for (Tile tile : this.possibleTiles) {
            if(tile.name.equals(match)) {
                t.setBSide(tile);
                hasMatch = true;
                break;
            }
        }
        if(!hasMatch) {
            t.aSide = t;
        }
    }

    public void connectTiles(String mix) { //match up a and b sides so we don't get, eg, Chapel-a and Chapel-b
        if (mix.equals("Mix")) { //joining tiles is only necessary if you're using both sides
            for (Tile t : this.possibleTiles) {
                if(t.name.contains(" - B")) {
                    if(t.aSide == null)
                    {
                        t.aSide = t;
                    }
                    continue;
                }
                connectingHelper(t);
            }
        }
        else { //so if you're not using both sides, just reference itself for code reasons
            for (Tile t : this.possibleTiles) {
                t.aSide = t;
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
