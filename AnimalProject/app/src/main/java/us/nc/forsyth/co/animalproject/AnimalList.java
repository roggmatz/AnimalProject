package us.nc.forsyth.co.animalproject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;


public class AnimalList extends Activity implements ActionBar.OnNavigationListener {
    final String URL_ALL_ANIMALS = "http://forsyth.cc/animalcontrol/adoptableanimals.ashx";
    final String URL_CATS = "http://forsyth.cc/animalcontrol/adoptablecats.ashx";
    final String URL_DOGS = "http://forsyth.cc/animalcontrol/adoptabledogs.ashx";
    final String URL_OTHER = "http://forsyth.cc/animalcontrol/adoptableother.ashx";

    private ArrayList<Animal> animalData;

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        String[] categoriesList = getResources().getStringArray(R.array.categories_list);
        DetailsViewFragment deetsFrag = new DetailsViewFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.animal_details, deetsFrag, categoriesList[i]);
        ft.commit();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_list);
        configureActionBar();
        ListView listview = (ListView) findViewById(R.id.animal_list);
        GetJsonData importer = new GetJsonData(this);
        importer.execute(URL_ALL_ANIMALS);
        try {
            animalData = importer.get();
        } catch (Exception e) {
            Log.i("AnimalList.class", "Error in getting asyncTask results. \n" + e);
        }
        AnimalListAdapter animalListAdapter = new AnimalListAdapter(this, animalData);
        listview.setAdapter(animalListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.animal_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void configureActionBar() {
        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.categories_list, android.R.layout.simple_spinner_dropdown_item);
        ActionBar ab = getActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ab.setListNavigationCallbacks(spinnerAdapter, this);
        ab.setDisplayShowTitleEnabled(false);
    }
}
