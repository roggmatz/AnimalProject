package us.nc.forsyth.co.animalproject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class AnimalList extends ListActivity implements ActionBar.OnNavigationListener {
    final String URL_ALL_ANIMALS = "http://forsyth.cc/animalcontrol/adoptableanimals.ashx";
    final String URL_CATS = "http://forsyth.cc/animalcontrol/adoptablecats.ashx";
    final String URL_DOGS = "http://forsyth.cc/animalcontrol/adoptabledogs.ashx";
    final String URL_OTHER = "http://forsyth.cc/animalcontrol/adoptableother.ashx";
    final String URL_START = "http://forsyth.cc/controls/DisplayImage.ashx?ID=";
    final String URL_END = "&resolution=Detail";
    final int ALL = 0;
    final int DOG = 1;
    final int CAT = 2;
    final int OTHER = 3;

    private ArrayList<Animal> animalData;
    ListView listenerListView;

    public final static String EXTRA_MESSAGE = "us.nc.forsyth.co.animalproject.MESSAGE";

    public void fetchData(int k) {
        GetJsonData listenerImporter = new GetJsonData(this);
        switch (k) {
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
        ThumbnailPictureGetter thumbnailPictureGetter = new ThumbnailPictureGetter(this);
        thumbnailPictureGetter.execute(animalData);
        try {
            animalData = thumbnailPictureGetter.get();
        }
        catch (Exception f) {
            Log.i("AnimalList.class", "fetchData() - Error with ThumbnailPictureGetter:\n"
                  + f);
        }
    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        listenerListView = (ListView) findViewById(android.R.id.list);
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            fetchData(i);
            AnimalListAdapter animalListAdapter = new AnimalListAdapter(this, animalData);
            listenerListView.setAdapter(animalListAdapter);
        }
        else {
            ArrayList<String> d = new ArrayList<String>();
            d.add("FOO");
            NoConnectionAdapter noConnectionAdapter = new NoConnectionAdapter(this, d);
            listenerListView.setAdapter(noConnectionAdapter);
            getListView().setDividerHeight(0);
            getListView().setDivider(null);
        }
        return true;
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
        String[] animalSelected = new String[16];
        Animal tempAnimal = animalData.get(position);
        animalSelected[0] = tempAnimal.id;
        animalSelected[1] = tempAnimal.name;
        animalSelected[2] = tempAnimal.type;
        animalSelected[3] = tempAnimal.tagType;
        animalSelected[4] = tempAnimal.kennelNo;
        animalSelected[5] = tempAnimal.sex;
        animalSelected[6] = tempAnimal.age;
        animalSelected[7] = tempAnimal.colorGroup;
        animalSelected[8] = tempAnimal.primaryColor;
        animalSelected[9] = tempAnimal.secondaryColor;
        animalSelected[10] = tempAnimal.breedGroup;
        animalSelected[11] = tempAnimal.primaryBreed;
        animalSelected[12] = tempAnimal.secondaryBreed;
        animalSelected[13] = tempAnimal.markings;
        animalSelected[14] = tempAnimal.size;
        animalSelected[15] = tempAnimal.dateAdded;

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, animalSelected);
        startActivity(intent);
    }
}
