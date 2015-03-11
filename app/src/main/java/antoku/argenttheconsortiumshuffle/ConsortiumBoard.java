package antoku.argenttheconsortiumshuffle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class ConsortiumBoard extends ActionBarActivity {

    private ArrayList<String> consortium;
    private ArrayList<String> possibleVoters;
    private Player selectedPlayer;
    private Player[] players;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.consortium = new ArrayList<String>();
        this.possibleVoters = new ArrayList<String>();
        this.toast = Toast.makeText(this, "Press Back again to quit", Toast.LENGTH_SHORT);

        Intent intent = getIntent();
        String[] playerArray = intent.getStringExtra(MainActivity.PLAYER_COLORS).split(",");
        this.players = new Player[playerArray.length];
        boolean mancersUsed = intent.getBooleanExtra(MainActivity.INCLUDE_MANCERS, false);
        boolean hasArchmage = intent.getBooleanExtra(MainActivity.INCLUDE_ARCHMAGE, false);
        int firstPlayer = intent.getIntExtra(MainActivity.FIRST_PLAYER, 0);
        buildVoterPool(mancersUsed, hasArchmage);

        setContentView(R.layout.activity_consortium_board);
        if(getSupportActionBar() != null) {
            //hide the "back" button in the action bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        for (int i = 0; i < playerArray.length; i++) {
            this.players[i] = new Player(playerArray[i]);
        }
        this.selectedPlayer = this.players[0];
        setIPButtonColor();

        Button curBut;
        //all games have at least 2 players
        curBut = (Button)findViewById(R.id.button_player_one);
        setButtonText(curBut, 0, firstPlayer);

        curBut = (Button)findViewById(R.id.button_player_two);
        setButtonText(curBut, 1, firstPlayer);

        //if there's more than 2 players, initialize their buttons
        if(playerArray.length > 2) {
            curBut = (Button)findViewById(R.id.button_player_three);
            setButtonText(curBut, 2, firstPlayer);
            if (playerArray.length > 3) {
                curBut = (Button)findViewById(R.id.button_player_four);
                setButtonText(curBut, 3, firstPlayer);
                if (playerArray.length > 4) {
                    curBut = (Button)findViewById(R.id.button_player_five);
                    setButtonText(curBut, 4, firstPlayer);
                    if (playerArray.length > 5) {
                        curBut = (Button)findViewById(R.id.button_player_six);
                        setButtonText(curBut, 5, firstPlayer);
                    }
                }
            }
        }

        buildConsortium();
    }

    public void setButtonText(Button curBut, int player, int firstPlayer) {
        //set the player's button
        this.players[player].button = curBut;
        String col = this.players[player].color;
        //set the player's rank based on which is the start player
        //start player rank = last; the person to their right = first
        int rank = firstPlayer - player;
        if (player >= firstPlayer) {
            rank += this.players.length;
        }
        this.players[player].rank = rank;
        curBut.setBackgroundColor(colorToInt(col));
        curBut.setTextColor(textColor(col));
        curBut.setVisibility(Button.VISIBLE);
        this.players[player].setButText(convertPlayer(col) + " " + 5);
    }

    public void setIPButtonColor() {
        String col = this.selectedPlayer.color;
        ((Button)findViewById(R.id.ip_up)).setBackgroundColor(colorToInt(col));
        ((Button)findViewById(R.id.ip_up)).setTextColor(textColor(col));
        ((Button)findViewById(R.id.ip_down)).setBackgroundColor(colorToInt(col));
        ((Button)findViewById(R.id.ip_down)).setTextColor(textColor(col));
    }

    public void ipUp(View view) {
        //increase the selected player's influence
        int originalRank = this.selectedPlayer.rank;
        for (Player player : this.players) {
            if (!player.equals(this.selectedPlayer)) {
                //for each player who had the same amount of influence as the selected player, and was
                //higher in rank, the selected player's rank increases
                if (player.influence == this.selectedPlayer.influence && player.rank < originalRank) {
                    this.selectedPlayer.rank--;
                    player.rank++;
                    setInfText(player);
                }
            }
        }
        this.selectedPlayer.influence += 1;
        setInfText(this.selectedPlayer);
        if(this.selectedPlayer.influence % 7 == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You gain a Merit Badge!").setPositiveButton("Okay", null).show();
        }
        //no need to change rank again; if you reach 10 influence after someone else, you're still behind them
    }

    public void ipDown(View view) {
        //decrease the selected player's influence
        int originalRank = this.selectedPlayer.rank;
        for (Player player : this.players) {
            if (!player.equals(this.selectedPlayer)) {
                //for each player who had the same amount of influence as the selected player, and was
                //lower in rank, the selected player's rank decreases
                if (player.influence == this.selectedPlayer.influence && player.rank > originalRank) {
                    this.selectedPlayer.rank++;
                    player.rank--;
                    setInfText(player);
                }
            }
        }
        if(this.selectedPlayer.influence % 7 == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Lose an unused Merit Badge (or a used one, if all used)").setPositiveButton("Okay", null).show();
        }
        this.selectedPlayer.influence -= 1;
        //now, since you're the newcomer on a previous square, your rank is below everyone who made it there first
        for (Player player : this.players) {
            if (!player.equals(this.selectedPlayer)) {
                if (player.influence == this.selectedPlayer.influence) {
                    this.selectedPlayer.rank++;
                    player.rank--;
                    setInfText(player);
                }
            }
        }
        setInfText(this.selectedPlayer);
    }

    public void setInfText(Player p) {
        String text = convertPlayer(p.color) + " " + p.influence;
        p.setButText(text);
    }

    //hidden voter presses. all do basically the same thing
    public void voter_one(View view) {
        askVoter(2, (TextView) findViewById(R.id.three));
    }
    public void voter_two(View view) {
        askVoter(3, (TextView)findViewById(R.id.four));
    }
    public void voter_three(View view) {
        askVoter(4, (TextView)findViewById(R.id.five));
    }
    public void voter_four(View view) {
        askVoter(5, (TextView)findViewById(R.id.six));
    }
    public void voter_five(View view) {
        askVoter(6, (TextView)findViewById(R.id.seven));
    }
    public void voter_six(View view) {
        askVoter(7, (TextView)findViewById(R.id.eight));
    }
    public void voter_seven(View view) {
        askVoter(8, (TextView)findViewById(R.id.nine));
    }
    public void voter_eight(View view) {
        askVoter(9, (TextView)findViewById(R.id.ten));
    }
    public void voter_nine(View view) {
        askVoter(10, (TextView)findViewById(R.id.eleven));
    }
    public void voter_ten(View view) {
        askVoter(11, (TextView)findViewById(R.id.twelve));
    }

    //player change button presses. also do the same thing.
    public void player_one_clicked(View view) {
        this.selectedPlayer = this.players[0];
        setIPButtonColor();
    }
    public void player_two_clicked(View view) {
        this.selectedPlayer = this.players[1];
        setIPButtonColor();
    }
    public void player_three_clicked(View view) {
        this.selectedPlayer = this.players[2];
        setIPButtonColor();
    }
    public void player_four_clicked(View view) {
        this.selectedPlayer = this.players[3];
        setIPButtonColor();
    }
    public void player_five_clicked(View view) {
        this.selectedPlayer = this.players[4];
        setIPButtonColor();
    }
    public void player_six_clicked(View view) {
        this.selectedPlayer = this.players[5];
        setIPButtonColor();
    }

    private void askVoter(int voter, TextView tv) {
        //if the selected player has marked this voter already, just tell them what it is
        if (this.selectedPlayer.marks[voter]) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(this.consortium.get(voter)).setPositiveButton("Okay", null).show();
        }
        //otherwise, verify if they would like to mark this voter (for accidental presses)
        else {
            MyClickListener t = new MyClickListener(this, voter, tv);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Would you like to mark this voter?").setPositiveButton("Yes", t)
                    .setNegativeButton("No", t).show();
        }
    }

    public void markVoter(int voter, TextView tv) {
        this.selectedPlayer.marks[voter] = true;
        boolean allMarked = true;
        String text = "Hidden Voter " + (voter - 1);

        for (Player player : players) {
            if(player.marks[voter]) {
                String col;
                switch(player.color) {
                    case "Orange": col = "#ffa500"; break;
                    case "White": col = "Black"; break;
                    default: col = player.color; break;
                }
                //for each player that has marked this voter, add their department's symbol in their department's color
                text += " <font color='" + col + "'>" + convertPlayer(player.color) + "</font>";
            }
            else {
                allMarked = false;
                text += "  ";
            }
        }
        //if all players have marked this voter, just display it
        if (allMarked) {
            tv.setText(this.consortium.get(voter));
        }
        //otherwise show which have marked it
        else {
            tv.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        }
        //then, display the hidden voter to the player who just marked it
        this.askVoter(voter, tv);
    }

    //convert the color of the player to its department initial
    public static String convertPlayer(String color) {
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

    //convert the color of the player to its Java color
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

    //some colors don't show black text well
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
        for(int i = 0; i < 10; i++) { //randomly select 10 voters from the voter pool
            int voter = r.nextInt(this.possibleVoters.size());
            this.consortium.add(this.possibleVoters.get(voter));
            this.possibleVoters.remove(voter);
        }
    }

    @Override
    public void onBackPressed() {
        //don't allow accidental back presses. this can be a 3-hour game! wouldn't want it ruined by accidental back presses
        if (this.toast.getView().isShown()) {
            super.onBackPressed();
        }
        else {
            toast.show();
        }
    }

    private void buildVoterPool(boolean mancersUsed, boolean hasArchmage) {
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
        }
        if (hasArchmage) {
            this.possibleVoters.add("Owner of the Archmage's Staff");
        }
    }

    private class MyClickListener implements DialogInterface.OnClickListener {
        ConsortiumBoard consortiumBoard;
        int voter;
        TextView tv;
        public MyClickListener(ConsortiumBoard cb, int vot, TextView t) {
            this.consortiumBoard = cb;
            this.voter = vot;
            this.tv = t;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch(which) {
                case DialogInterface.BUTTON_POSITIVE:
                    this.consortiumBoard.markVoter(this.voter, this.tv);
                    break;
            }
        }
    }
}
