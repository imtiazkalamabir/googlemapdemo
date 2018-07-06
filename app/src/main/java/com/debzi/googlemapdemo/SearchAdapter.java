package com.debzi.googlemapdemo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mCountries;
    private ArrayList<String> mAddress;
    private LayoutInflater mLayoutInflater;
    private boolean mIsFilterList;

    public SearchAdapter(Context context, ArrayList<String> countries, ArrayList<String> address, boolean isFilterList) {
        this.mContext = context;
        this.mCountries =countries;
        this.mIsFilterList = isFilterList;
        this.mAddress= address;
    }


    public void updateList(ArrayList<String> filterList, ArrayList<String> filterAddressList, boolean isFilterList) {
        this.mCountries = filterList;
        this.mAddress = filterAddressList;
        this.mIsFilterList = isFilterList;
        notifyDataSetChanged ();
    }

    @Override
    public int getCount() {
        return mCountries.size();
    }

    @Override
    public String getItem(int position) {
        return mCountries.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder = null;
        if(v==null){

            holder = new ViewHolder();

            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = mLayoutInflater.inflate(R.layout.list_item_search, parent, false);
            holder.txtCountry = (TextView)v.findViewById(R.id.txt_country);
            holder.txtDetails = (TextView)v.findViewById(R.id.txt_details);
            v.setTag(holder);
        } else{

            holder = (ViewHolder) v.getTag();
        }

        try{
        holder.txtCountry.setText(mCountries.get(position));
        holder.txtDetails.setText(mAddress.get(position));
        }catch (Exception e){

            Toast.makeText(mContext, "Loading data. Please wait a moment", Toast.LENGTH_SHORT).show();
        }

        Drawable searchDrawable,recentDrawable;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            searchDrawable = mContext.getResources().getDrawable(R.drawable.ic_location_marker, null);
            recentDrawable = mContext.getResources().getDrawable(R.drawable.ic_backup_restore_grey600_24dp, null);

        } else {
            searchDrawable = mContext.getResources().getDrawable(R.drawable.ic_location_marker);
            recentDrawable = mContext.getResources().getDrawable(R.drawable.ic_backup_restore_grey600_24dp);
        }
        if(mIsFilterList) {
            holder.txtCountry.setCompoundDrawablesWithIntrinsicBounds(searchDrawable, null, null, null);
        }else {
            holder.txtCountry.setCompoundDrawablesWithIntrinsicBounds(recentDrawable, null, null, null);

        }
        return v;
    }

}

class ViewHolder{
     TextView txtCountry;
     TextView txtDetails;
}





