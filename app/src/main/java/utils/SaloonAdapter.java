package utils;

/**
 * Created by Aisha on 6/14/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.admin.saloon.craftystudio.saloonadmin.R;


public class SaloonAdapter extends RecyclerView.Adapter<SaloonAdapter.SaloonViewHolder> {

    private ArrayList<Saloon> saloonArrayList;


    public SaloonAdapter(ArrayList<Saloon> saloonArrayList) {
        this.saloonArrayList = saloonArrayList;
    }


    @Override
    public SaloonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saloon_adapter_row, parent, false);
        return new SaloonViewHolder(view);

    }

    @Override
    public void onBindViewHolder(SaloonViewHolder holder, int position) {
        Saloon saloon = saloonArrayList.get(position);
        holder.saloonNameTextView.setText(saloon.getSaloonName());
        holder.salooncontactTextView.setText(saloon.getSaloonPhoneNumber());
        holder.saloonPointTextView.setText(saloon.getSaloonPoint() + "");
        holder.saloonAddressTextView.setText(saloon.getSaloonAddress());
        holder.saloonRatingTextView.setText(saloon.getSaloonRating()+"");

    }




    @Override
    public int getItemCount() {
        return saloonArrayList.size();
    }


    public class SaloonViewHolder extends RecyclerView.ViewHolder {
        public TextView saloonNameTextView, salooncontactTextView, saloonPointTextView, saloonAddressTextView, saloonRatingTextView;

        public SaloonViewHolder(View view) {
            super(view);
            saloonNameTextView = (TextView) view.findViewById(R.id.saloonadapterrow_saloonName_textview);
            salooncontactTextView = (TextView) view.findViewById(R.id.saloonadapterrow_contact_textview);
            saloonPointTextView = (TextView) view.findViewById(R.id.saloonadapterrow_saloonPoints_textview);
            saloonAddressTextView = (TextView) view.findViewById(R.id.saloonadapterrow_saloonaadress_textview);
            saloonRatingTextView = (TextView) view.findViewById(R.id.saloonAdapterRow_saloonRating_textview);


        }
    }


}