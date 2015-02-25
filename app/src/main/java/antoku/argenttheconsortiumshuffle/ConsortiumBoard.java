package antoku.argenttheconsortiumshuffle;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class ConsortiumBoard extends ActionBarActivity {

    private ArrayList<String> consortium;
    private ArrayList<String> possibleVoters;
    private Player selectedPlayer;
    private Player[] players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.consortium = new ArrayList<String>();
        this.possibleVoters = new ArrayList<String>();

        Intent intent = getIntent();
        String[] playerArray = intent.getStringExtra(MainActivity.PLAYER_COLORS).split(",");
        this.players = new Player[playerArray.length];
        boolean mancersUsed = intent.getBooleanExtra(MainActivity.INCLUDE_MANCERS, false);
        buildVoterPool(mancersUsed);

        setContentView(R.layout.activity_consortium_board);

        LinearLayout display = (LinearLayout)findViewById(R.id.display_game);
        TextView tv;
        String text="";

        for (int i = 0; i < playerArray.length; i++) {
            String s = playerArray[i];
            tv = new TextView(this);
            tv.setText(s + " " + convertPlayer(s));
            text += "<font color='" + s + "'>player </font>";
            tv.setBackgroundColor(colorToInt(s));
            tv.setTextColor(textColor(s));
            tv.setTextSize(20);
            display.addView(tv);
            Player p = new Player(s);
            this.players[i] = p;
        }
        this.selectedPlayer = this.players[0];

        tv = new TextView(this);
        tv.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        ((TextView)findViewById(R.id.one)).setText(Html.fromHtml(text),TextView.BufferType.SPANNABLE);
        tv.setTextSize(20);
        display.addView(tv);

        Button curBut;
        curBut = (Button)findViewById(R.id.button_player_one);
        setButtonText(curBut, 0);

        curBut = (Button)findViewById(R.id.button_player_two);
        setButtonText(curBut, 1);

        if(playerArray.length > 2) {
            curBut = (Button)findViewById(R.id.button_player_three);
            setButtonText(curBut, 2);
            if (playerArray.length > 3) {
                curBut = (Button)findViewById(R.id.button_player_four);
                setButtonText(curBut, 3);
                if (playerArray.length > 4) {
                    curBut = (Button)findViewById(R.id.button_player_five);
                    setButtonText(curBut, 4);
                    if (playerArray.length > 5) {
                        curBut = (Button)findViewById(R.id.button_player_six);
                        setButtonText(curBut, 5);
                    }
                }
            }
        }

        buildConsortium();
    }

    public void setButtonText(Button curBut, int player) {
        String col = this.players[player].color;
        curBut.setBackgroundColor(colorToInt(col));
        curBut.setTextColor(textColor(col));
        curBut.setVisibility(Button.VISIBLE);
        curBut.setText(convertPlayer(col) + " " + 5);
    }

    public void voter_one(View view) {

    }
    public void voter_two(View view) {

    }
    public void voter_three(View view) {

    }
    public void voter_four(View view) {

    }
    public void voter_five(View view) {

    }
    public void voter_six(View view) {

    }
    public void voter_seven(View view) {

    }
    public void voter_eight(View view) {

    }
    public void voter_nine(View view) {

    }
    public void voter_ten(View view) {

    }

    private String convertPlayer(String color) {
        switch (color) {
            case "Red": return "S";
            case "Blue": return "D";
            case "Grey": return "M";
            case "Green": return "N";
            case "Purple": return "P";
            case "Orange": return "T";
            case "White": return "0";
            default: return "err";
        }
    }

    private int colorToInt(String color) {
        switch (color) {
            case "Red": return Color.RED;
            case "Blue": return Color.BLUE;
            case "Grey": return Color.GRAY;
            case "Green": return Color.GREEN;
            case "Purple": return Color.parseColor("#9900FF");
            case "Orange": return Color.parseColor("#FF6600");
            case "White": return Color.parseColor("#FFFFFF");
            default: return Color.parseColor("#000000");
        }
    }

    private int textColor(String color) {
        switch (color) {
            case "Blue": return Color.parseColor("#FFFFFF");
            default: return Color.parseColor("#000000");
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
        ((TextView)findViewById(R.id.one)).setText(this.consortium.get(0));
        this.consortium.add("Most Supporters");
        ((TextView)findViewById(R.id.two)).setText(this.consortium.get(1));
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
