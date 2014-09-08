package us.nc.forsyth.co.animalproject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SpinnerAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class AnimalList extends ListActivity implements ActionBar.OnNavigationListener {
    final String URL_ALL_ANIMALS = "http://forsyth.cc/animalcontrol/adoptableanimals.ashx";
    final String URL_CATS = "http://forsyth.cc/animalcontrol/adoptablecats.ashx";
    final String URL_DOGS = "http://forsyth.cc/animalcontrol/adoptabledogs.ashx";
    final String URL_OTHER = "http://forsyth.cc/animalcontrol/adoptableother.ashx";
    final int ALL = 0;
    final int DOG = 1;
    final int CAT = 2;
    final int OTHER = 3;

    private ArrayList<Animal> animalData;
    ListView listenerListView;

    public final static String EXTRA_MESSAGE = "us.nc.forsyth.co.animalproject.MESSAGE";

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        DetailsViewFragment deetsFrag = new DetailsViewFragment();
        listenerListView = (ListView) findViewById(android.R.id.list);
       /* View listView;
        if(listenerListView == null) {
            LinearLayout emptyList = (LinearLayout) findViewById(R.id.empty_list);
            ViewGroup parent = (ViewGroup) emptyList.getParent();
            parent.removeView(emptyList);
            listView = getLayoutInflater().inflate(R.layout.activity_animal_list, parent, false);
            parent.addView(listView);
        }*/
        GetJsonData listenerImporter = new GetJsonData(this);
        switch (i) {
            case ALL:
                listenerImporter.execute(URL_ALL_ANIMALS);
                break;
            case DOG:
                listenerImporter.execute(URL_DOGS);
                break;
            case CAT:
                listenerImporter.execute(URL_CATS);
                break;
            case OTHER:
                listenerImporter.execute(URL_OTHER);
                break;
            default:
                break;
        }
        try {
            animalData = listenerImporter.get();
        } catch (Exception e) {
            Log.i("AnimalList.class", "Error in getting asyncTask results. \n" + e);
        }
/*        if(animalData.isEmpty()) {
            emptyListHandler(listenerListView);
        }*/
        AnimalListAdapter animalListAdapter = new AnimalListAdapter(this, animalData);
/* ------------- TODO
        if(listenerListView != null) {
            listenerListView.setAdapter(animalListAdapter);
        }
*/
        listenerListView.setAdapter(animalListAdapter);
        //emptyListHandler(listenerListView);
        //FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.replace(R.id.animal_details, deetsFrag, categoriesList[i]);
        //ft.commit();
        return true;
    }

    private void emptyListHandler(View currentView) {
        ViewGroup parent = (ViewGroup) currentView.getParent();
        parent.removeView(currentView);
        LinearLayout emptyList = (LinearLayout) findViewById(R.id.empty_list);
        if(emptyList == null) {
            View view = getLayoutInflater().inflate(R.layout.empty_list, parent, false);
            parent.addView(view);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_list);
        configureActionBar();
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Bundle itemClicked = new Bundle();
        Animal tempAnimal = animalData.get(position);
        itemClicked.putString("0", tempAnimal.id);
        itemClicked.putString("1", tempAnimal.name);
        itemClicked.putString("2", tempAnimal.type);
        itemClicked.putString("3", tempAnimal.tagType);
        itemClicked.putString("4", tempAnimal.kennelNo);
        itemClicked.putString("5", tempAnimal.sex);
        itemClicked.putString("6", tempAnimal.age);
        itemClicked.putString("7", tempAnimal.colorGroup);
        itemClicked.putString("8", tempAnimal.primaryColor);
        itemClicked.putString("9", tempAnimal.secondaryColor);
        itemClicked.putString("10", tempAnimal.breedGroup);
        itemClicked.putString("11", tempAnimal.primaryBreed);
        itemClicked.putString("12", tempAnimal.secondaryBreed);
        itemClicked.putString("13", tempAnimal.markings);
        itemClicked.putString("14", tempAnimal.size);
        itemClicked.putString("15", tempAnimal.dateAdded);
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, itemClicked);
        startActivity(intent);
    }
}
