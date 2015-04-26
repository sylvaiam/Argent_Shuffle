package antoku.argenttheconsortiumshuffle;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.preference.PreferenceManager;


public class MainActivity extends ActionBarActivity {
    public final static String NUM_PLAYERS = "com.antoku.ArgentShuffle.NUM_PLAYERS";
    public final static String SIDES_USED = "com.antoku.ArgentShuffle.SIDES_USED";
    public final static String INCLUDE_MANCERS = "com.antoku.ArgentShuffle.INCLUDE_MANCERS";
    public final static String INCLUDE_RESOURCES = "com.antoku.ArgentShuffle.INCLUDE_RESOURCES";
    public final static String INCLUDE_MAGE = "com.antoku.ArgentShuffle.INCLUDE_MAGE";
    public final static String INCLUDE_SCENARIO = "com.antoku.ArgentShuffle.INCLUDE_SCENARIO";
    public final static String INCLUDE_WHITE = "com.antoku.ArgentShuffle.INCLUDE_WHITE";
    public final static String PLAYER_COLORS = "com.antoku.ArgentShuffle.PLAYER_COLORS";
    public final static String FIRST_PLAYER = "com.antoku.ArgentShuffle.FIRST_PLAYER";
    public final static String INCLUDE_ARCHMAGE = "com.antoku.ArgentShuffle.INCLUDE_ARCHMAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        //set the default selections
        ((Spinner)findViewById(R.id.spinner_players)).setSelection(1);
        ((Spinner)findViewById(R.id.spinner_sides)).setSelection(2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        CheckBox mancers = (CheckBox) findViewById(R.id.checkBox_mancers);
        CheckBox resources = (CheckBox) findViewById(R.id.checkBox_resources);
        CheckBox mages = (CheckBox) findViewById(R.id.checkBox_mages);
        CheckBox scenario = (CheckBox) findViewById(R.id.checkBox_scenario);
        CheckBox white = (CheckBox) findViewById(R.id.checkbox_white);
        CheckBox archmage = (CheckBox) findViewById(R.id.checkbox_archmage);

        mancers.setChecked(prefs.getBoolean("inc_mancers", false));
        resources.setChecked(prefs.getBoolean("inc_resources", false));
        mages.setChecked(prefs.getBoolean("inc_plus_mage", false));
        scenario.setChecked(prefs.getBoolean("inc_scenario", false));
        white.setChecked(prefs.getBoolean("neut_cand", false));
        archmage.setChecked(prefs.getBoolean("inc_archmage", false));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_help:
                openHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openHelp() {
        //for now, just going to do a popup
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Choose how many players are playing, which sides you want to use (Mix for both), " +
                "and other settings.")
                .setPositiveButton("Okay", null).show();
    }

    public void startGame(View view) {
        //all this goes to the selection screen
        Intent intent = new Intent(this, DisplayGameActivity.class);
        Spinner numPlayers = (Spinner) findViewById(R.id.spinner_players);
        Spinner sides = (Spinner) findViewById(R.id.spinner_sides);
        CheckBox mancers = (CheckBox) findViewById(R.id.checkBox_mancers);
        CheckBox resources = (CheckBox) findViewById(R.id.checkBox_resources);
        CheckBox mages = (CheckBox) findViewById(R.id.checkBox_mages);
        CheckBox scenario = (CheckBox) findViewById(R.id.checkBox_scenario);
        CheckBox white = (CheckBox) findViewById(R.id.checkbox_white);
        CheckBox archmage = (CheckBox) findViewById(R.id.checkbox_archmage);

        //pass the values to the next screen
        intent.putExtra(NUM_PLAYERS, numPlayers.getSelectedItem().toString());
        intent.putExtra(SIDES_USED, sides.getSelectedItem().toString());
        intent.putExtra(INCLUDE_MANCERS, mancers.isChecked());
        intent.putExtra(INCLUDE_RESOURCES, resources.isChecked());
        intent.putExtra(INCLUDE_MAGE, mages.isChecked());
        intent.putExtra(INCLUDE_SCENARIO, scenario.isChecked());
        intent.putExtra(INCLUDE_WHITE, white.isChecked());
        intent.putExtra(INCLUDE_ARCHMAGE, archmage.isChecked());

        startActivity(intent);
    }
}
