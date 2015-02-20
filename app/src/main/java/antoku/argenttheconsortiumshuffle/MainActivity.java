package antoku.argenttheconsortiumshuffle;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.Spinner;


public class MainActivity extends ActionBarActivity {
    public final static String NUM_PLAYERS = "com.antoku.ArgentShuffle.NUM_PLAYERS";
    public final static String SIDES_USED = "com.antoku.ArgentShuffle.SIDES_USED";
    public final static String INCLUDE_MANCERS = "com.antoku.ArgentShuffle.INCLUDE_MANCERS";
    public final static String INCLUDE_RESOURCES = "com.antoku.ArgentShuffle.INCLUDE_RESOURCES";
    public final static String INCLUDE_MAGE = "com.antoku.ArgentShuffle.INCLUDE_MAGE";
    public final static String INCLUDE_SCENARIO = "com.antoku.ArgentShuffle.INCLUDE_SCENARIO";
    public final static String INCLUDE_WHITE = "com.antoku.ArgentShuffle.INCLUDE_WHITE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the default selections
        ((Spinner)findViewById(R.id.spinner_players)).setSelection(1);
        ((Spinner)findViewById(R.id.spinner_sides)).setSelection(2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

        //pass the values to the next screen
        intent.putExtra(NUM_PLAYERS, numPlayers.getSelectedItem().toString());
        intent.putExtra(SIDES_USED, sides.getSelectedItem().toString());
        intent.putExtra(INCLUDE_MANCERS, mancers.isChecked());
        intent.putExtra(INCLUDE_RESOURCES, resources.isChecked());
        intent.putExtra(INCLUDE_MAGE, mages.isChecked());
        intent.putExtra(INCLUDE_SCENARIO, scenario.isChecked());
        intent.putExtra(INCLUDE_WHITE, white.isChecked());

        startActivity(intent);
    }
}
