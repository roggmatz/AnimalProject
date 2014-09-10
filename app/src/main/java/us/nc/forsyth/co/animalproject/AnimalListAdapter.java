package us.nc.forsyth.co.animalproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AnimalListAdapter extends BaseAdapter {
    private static ArrayList<Animal> animalData;
    private LayoutInflater inflater;

    public AnimalListAdapter(Context context, ArrayList<Animal> convertedJsonArray) {
        animalData = convertedJsonArray;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        if(animalData == null) {
            return 0;
        }
        else {
            return animalData.size();
        }
    }

    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        try {
            return animalData.get(position);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_row_appearance, null);
            holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.list_item_name);
            holder.breed = (TextView) convertView.findViewById(R.id.list_item_breed);
            holder.size = (TextView) convertView.findViewById(R.id.list_item_size);
            holder.dateAdded = (TextView) convertView.findViewById(R.id.list_item_date);
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.list_item_thumbnail);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(animalData.get(position).name);
        holder.breed.setText(animalData.get(position).primaryBreed);
        holder.size.setText(animalData.get(position).size);
        holder.dateAdded.setText(animalData.get(position).dateAdded);
        holder.thumbnail.setImageBitmap(animalData.get(position).image);
        Log.i("AnimalListAdapter.class", "Added Row:\n" + animalData.get(position).name
                + ", " + animalData.get(position).primaryBreed
                + ", " + animalData.get(position).size
                + ", " + animalData.get(position).dateAdded);
        return convertView;
    }

    static class ViewHolder {
        public TextView name;
        public TextView breed;
        public TextView size;
        public TextView dateAdded;
        public ImageView thumbnail;

        public ViewHolder() {

        }
    }
}
