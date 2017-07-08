package utils;

/**
 * Created by Aisha on 6/14/2017.
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.admin.saloon.craftystudio.saloonadmin.R;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private ArrayList<Order> orderArrayList;

    Context context;


    public OrderAdapter(ArrayList<Order> orderArrayList, Context context) {
        this.orderArrayList = orderArrayList;
        this.context = context;
    }


    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_adapter_row, parent, false);
        return new OrderViewHolder(view);

    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = orderArrayList.get(position);
        holder.saloonNameTextView.setText(order.getSaloonName());
        holder.orderDateTextView.setText(order.resolveOrderDate());
        holder.orderPriceTextView.setText(order.getOrderPrice() + "");
        holder.orderStatusTextView.setText(order.resolveOrderStatus());
        holder.serviceNameTextView.setText(order.getOrderServiceName());

        if (order.getOrderStatus() == 2) {

            holder.orderStatusTextView.setTextColor(ContextCompat.getColorStateList(context, R.color.blue));
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.blue));

            //red rejected green comleted blue accepted
        } else if (order.getOrderStatus() == 3) {
            holder.orderStatusTextView.setTextColor(ContextCompat.getColorStateList(context, R.color.green));
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green));

        } else if (order.getOrderStatus() == -1) {
            holder.orderStatusTextView.setTextColor(ContextCompat.getColorStateList(context, R.color.red));
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red));

        }

    }


    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }


    public class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView saloonNameTextView, orderDateTextView, orderStatusTextView, orderPriceTextView, serviceNameTextView;

        public CardView cardView;


        public OrderViewHolder(View view) {
            super(view);
            saloonNameTextView = (TextView) view.findViewById(R.id.orderAdapterRow_saloonName_textview);
            orderDateTextView = (TextView) view.findViewById(R.id.orderAdapterRow_orderDate_textview);
            orderStatusTextView = (TextView) view.findViewById(R.id.orderAdapterRow_orderStatus_textview);
            orderPriceTextView = (TextView) view.findViewById(R.id.orderAdapterRow_orderPrice_textview);
            serviceNameTextView = (TextView) view.findViewById(R.id.orderAdapterRow_serviceName_textview);
            cardView = (CardView) view.findViewById(R.id.orderAdapterRow_colorcard_cardview);

        }
    }


}