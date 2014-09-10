package us.nc.forsyth.co.animalproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by matamora on 9/10/2014.
 */
public class ThumbnailPictureGetter extends AsyncTask<ArrayList<Animal>, Void, ArrayList<Animal>> {
    private ProgressDialog dialog;
    private Context currentContext;

    public ThumbnailPictureGetter(Context context) {
        currentContext = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(currentContext, currentContext.getString(R.string.progress_dialog_title),
                currentContext.getString(R.string.progress_dialog_details), true);
    }

    @Override
    protected ArrayList<Animal> doInBackground(ArrayList<Animal>... animals) {
        final String URL_START = "http://forsyth.cc/controls/DisplayImage.ashx?ID=";
        final String URL_END = "&resolution=Thumb";
        InputStream is;
        Bitmap picture;
        for(int i = 0; i < animals[0].size(); i++) {
            String tempURL = URL_START + animals[0].get(i).id + URL_END;
            URL url;
            HttpURLConnection httpURLConnection;
            try {
                url = new URL(tempURL);
            } catch (MalformedURLException m) {
                Log.i("PictureGetter.class", "doInBackground() - Bad URL:\n" + m);
                return null;
            }
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException j) {
                Log.i("ThumbnailPictureGetter.class", "doInBackground() - Connection Error:\n" + j);
                return null;
            }
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            try {
                httpURLConnection.setRequestMethod("GET");
            } catch (ProtocolException p) {
                Log.i("ThumbnailPictureGetter.class", "doInBackground() - Something went wrong with Protocol:\n"
                        + p);
            }
            httpURLConnection.setDoInput(true);
            try {
                httpURLConnection.connect();
                is = httpURLConnection.getInputStream();
                animals[0].get(i).image = BitmapFactory.decodeStream(is);
                Log.i("Thumbnail Added for:", animals[0].get(i).id);
                is.close();
            } catch (IOException k) {
                Log.i("ThumbnailPictureGetter.class", "doInBackground() - Error hooking up inputStream to HTTP Conn\n"
                        + k);
            }
        }
        return animals[0];
    }

    @Override
    protected void onPostExecute(ArrayList<Animal> animals) {
        dialog.dismiss();
    }
}
