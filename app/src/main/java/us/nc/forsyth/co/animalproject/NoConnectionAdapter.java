package us.nc.forsyth.co.animalproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by matamora on 9/10/2014.
 */
public class NoConnectionAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    Context c;
    private ArrayList<String> dummyData;


    public NoConnectionAdapter(Context context, ArrayList<String> d) {
        inflater = LayoutInflater.from(context);
        c = context;
        dummyData = d;
    }

    public int getCount() {
        if(dummyData == null) {
            return 0;
        }
        else {
            return dummyData.size();
        }
    }

    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        try {
            return dummyData.get(position);
        }
        catch (Exception e) {
            Log.i("NoConnectionAdapter.class", "getItemId() error.");
            e.printStackTrace();
            return null;
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        NoConnectionHolder holder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.no_network_access, null);
            holder = new NoConnectionHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.noconnection_image);
            convertView.setTag(holder);
        }
        else {
            holder = (NoConnectionHolder) convertView.getTag();
        }
        return convertView;
    }

    static class NoConnectionHolder {
        public ImageView imageView;
    }
}
