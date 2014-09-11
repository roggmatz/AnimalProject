package us.nc.forsyth.co.animalproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class DetailsActivity extends Activity {

    final int ID = 0;
    final int NAME = 1;
    final int TYPE = 2;
    final int TAG_TYPE = 3;
    final int KENNEL_NO = 4;
    final int SEX = 5;
    final int AGE = 6;
    final int COLOR_GROUP = 7;
    final int PRIMARY_COLOR = 8;
    final int SECONDARY_COLOR = 9;
    final int BREED_GROUP = 10;
    final int PRIMARY_BREED = 11;
    final int SECONDARY_BREED = 12;
    final int MARKINGS = 13;
    final int SIZE = 14;
    final int DATE_ADDED = 15;
    final int IMAGE = 16;

    String[] selectedAnimal = new String[16];
    final String URL_START = "http://forsyth.cc/controls/DisplayImage.ashx?ID=";
    final String URL_END = "&resolution=Detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_view);
        Intent intent = getIntent();
        Bitmap img;
        selectedAnimal = intent.getStringArrayExtra(AnimalList.EXTRA_MESSAGE);
        format();
        populateFields(selectedAnimal);
        ImageView imageView = (ImageView) findViewById(R.id.details_view_image);
        PictureGetter pictureGetter = new PictureGetter(this);
        pictureGetter.execute(URL_START + selectedAnimal[ID] + URL_END);
        try {
            img = pictureGetter.get();
            imageView.setImageBitmap(img);
            Log.i("DetailImage Height", Integer.toString(imageView.getHeight()));
        }
        catch (Exception e) {
            Log.i("DetailsActivity.class", e.toString());
        }
    }

    private void populateFields(String[] s) {
        //Displays the data in selectedAnimals[] through the TextViews statically-declared
        //on details_view.xml
        TextView var = (TextView) findViewById(R.id.details_view_name);
        var.setText(s[NAME]);
        var = (TextView) findViewById(R.id.details_view_id);
        var.setText(s[ID]);
        var = (TextView) findViewById(R.id.details_view_dateAdd);
        var.setText(s[DATE_ADDED]);
        var = (TextView) findViewById(R.id.details_view_primColor);
        var.setText(s[PRIMARY_COLOR]);
        var = (TextView) findViewById(R.id.details_view_secColor);
        var.setText(s[SECONDARY_COLOR]);
        var = (TextView) findViewById(R.id.details_view_breed);
        var.setText(s[BREED_GROUP]);
        var = (TextView) findViewById(R.id.details_view_secBreed);
        var.setText(s[SECONDARY_BREED]);
    }

    private void format() {
        //Function takes the strings stored in selectedAnimal[] and converts them from ABC to Abc
        String initial;
        String rest;
        for(int i = 0; i < selectedAnimal.length; i++) {
            if(selectedAnimal[i].charAt(0) > 64 && selectedAnimal[i].charAt(0) < 91) {
                initial = selectedAnimal[i].substring(0, 1);
                rest = selectedAnimal[i].substring(1);
                rest = rest.toLowerCase();
                selectedAnimal[i] = initial + rest;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details, menu);
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
}
