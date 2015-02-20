package antoku.argenttheconsortiumshuffle;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;


public class ConsortiumBoard extends ActionBarActivity {

    private ArrayList<String> consortium;
    private ArrayList<String> possibleVoters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.consortium = new ArrayList<String>();
        this.possibleVoters = new ArrayList<String>();

        Intent intent = getIntent();
        boolean mancersUsed = intent.getBooleanExtra(MainActivity.INCLUDE_MANCERS, false);
        buildVoterPool(mancersUsed);

        setContentView(R.layout.activity_consortium_board);
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
