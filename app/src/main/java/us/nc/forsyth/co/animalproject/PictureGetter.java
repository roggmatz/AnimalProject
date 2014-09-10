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

/**
 * Created by matamora on 9/9/2014.
 */
public class PictureGetter extends AsyncTask<String, Void, Bitmap> {

    private ProgressDialog dialog;
    private Context currentContext;

    public PictureGetter(Context context) {
        currentContext = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(currentContext, currentContext.getString(R.string.progress_dialog_title),
                currentContext.getString(R.string.progress_dialog_details), true);
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        InputStream is;
        Bitmap picture;
        URL url;
        HttpURLConnection httpURLConnection;
        try{
            url = new URL(urls[0]);
        }
        catch (MalformedURLException m) {
            Log.i("PictureGetter.class", "doInBackground() - Bad URL:\n" + m);
            return null;
        }
        try{
            httpURLConnection = (HttpURLConnection) url.openConnection();
        }
        catch (IOException i) {
            Log.i("PictureGetter.class", "doInBackground() - Connection Error:\n" + i);
            return null;
        }
        httpURLConnection.setReadTimeout(10000);
        httpURLConnection.setConnectTimeout(15000);
        try{
            httpURLConnection.setRequestMethod("GET");
        }
        catch (ProtocolException p) {
            Log.i("PictureGetter.class", "doInBackground() - Something went wrong with Protocol:\n"
                + p);
        }
        httpURLConnection.setDoInput(true);
        try {
            httpURLConnection.connect();
            is = httpURLConnection.getInputStream();
            picture = BitmapFactory.decodeStream(is);
            is.close();
            return picture;
        }
        catch (IOException k) {
            Log.i("PictureGetter.class", "doInBackground() - Error hooking up inputStream to HTTP Conn\n"
                + k);
        }
        Log.i("PictureGetter.java", "Returning a null Bitmap");
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap image) {
        dialog.dismiss();
    }
}
