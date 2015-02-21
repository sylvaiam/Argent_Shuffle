package antoku.argenttheconsortiumshuffle;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Random;


public class ConsortiumBoard extends ActionBarActivity {

    private ArrayList<String> consortium;
    private ArrayList<String> possibleVoters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.consortium = new ArrayList<String>();
        this.possibleVoters = new ArrayList<String>();

        Intent intent = getIntent();
        String players = intent.getStringExtra(MainActivity.PLAYER_COLORS);
        String[] playerArray = players.split(",");
        Color[] playerColors = new Color[playerArray.length];
        boolean mancersUsed = intent.getBooleanExtra(MainActivity.INCLUDE_MANCERS, false);
        buildVoterPool(mancersUsed);
        LinearLayout first_row = (LinearLayout)findViewById(R.id.first_row);
        LinearLayout second_row = (LinearLayout)findViewById(R.id.second_row);
        LinearLayout display = (LinearLayout)findViewById(R.id.display_game);

        setContentView(R.layout.activity_consortium_board);
        for(int i = 0; i < Math.max((playerArray.length + 1) / 2, 2); i++) {
            Button playerButton = new Button(this);
            playerButton.setText(convertPlayer(playerArray[i]) + "5");
            playerButton.setBackgroundColor(colorToInt(playerArray[i]));
            first_row.addView(playerButton);
        }
        for(int i = Math.max((playerArray.length+1)/2, 2); i < playerArray.length; i++) {
            Button playerButton = new Button(this);
            playerButton.setText(convertPlayer(playerArray[i]) + "5");
            playerButton.setBackgroundColor(colorToInt(playerArray[i]));
            second_row.addView(playerButton);
        }
    }

    private String convertPlayer(String color) {
        switch (color) {
            case "Red": return "S";
            case "Blue": return "D";
            case "Grey": return "M";
            case "Green": return "N";
            case "Purple": return "P";
            case "Orange": return "T";
            default: return "err";
        }
    }

    private int colorToInt(String color) {
        switch (color) {
            case "Red": return Color.RED;
            case "Blue": return Color.BLUE;
            case "Grey": return Color.GRAY;
            case "Green": return Color.GREEN;
            case "Purple": return 0x9900FF;
            case "Orange": return 0xFF6600;
            default: return 0x000000;
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

    private void buildConsortium() {
        Random r = new Random();
        this.consortium.add("Most Influence");
        this.consortium.add("Most Supporters");
        for(int i = 0; i < 10; i++) {
            int voter = r.nextInt(this.possibleVoters.size());
            this.consortium.add(this.possibleVoters.get(voter));
            this.possibleVoters.remove(voter);
        }
    }

    private void buildVoterPool(boolean mancersUsed) {
        this.possibleVoters.add("2nd-Most Influence");
        this.possibleVoters.add("2nd-Most Supporters");
        this.possibleVoters.add("Most Marks");
        this.possibleVoters.add("Most Divinity");
        this.possibleVoters.add("Most Planar Studies");
        this.possibleVoters.add("Most Natural Magick");
        this.possibleVoters.add("Most Mysticism");
        this.possibleVoters.add("Most Sorcery");
        this.possibleVoters.add("Most Consumables");
        this.possibleVoters.add("Most Treasures");
        this.possibleVoters.add("Most Intelligence");
        this.possibleVoters.add("Most Wisdom");
        this.possibleVoters.add("Most Gold");
        this.possibleVoters.add("Most Mana");
        this.possibleVoters.add("Most Research");
        this.possibleVoters.add("Most Diversity");
        this.possibleVoters.add("Largest Collection (most spell, vault, and supporter cards)");
        if (mancersUsed) {
            this.possibleVoters.add("Most Technomancy");
            this.possibleVoters.add("Owner of the Archmage's Staff");
        }
    }
}
