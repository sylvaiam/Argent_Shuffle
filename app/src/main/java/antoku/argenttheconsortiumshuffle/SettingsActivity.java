package antoku.argenttheconsortiumshuffle;

import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                openHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openHelp() {
        //for now, just going to do a popup
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Set defaults for new games, as well as what tiles you want to include.")
                .setPositiveButton("Okay", null).show();
    }
}
