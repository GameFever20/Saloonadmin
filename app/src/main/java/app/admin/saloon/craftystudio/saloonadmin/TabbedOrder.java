package app.admin.saloon.craftystudio.saloonadmin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Aisha on 6/16/2017.
 */

public class TabbedOrder extends Fragment {

    TextView mfragmentOrderStatus, mfragmentOrderTime, mfragmentOrderBookingTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabbed_order, container, false);
        View tempView = rootView;

        mfragmentOrderStatus = (TextView) tempView.findViewById(R.id.fragment_order_status_textview);
        mfragmentOrderTime = (TextView) tempView.findViewById(R.id.fragment_order_time_textview);
        mfragmentOrderBookingTime = (TextView) tempView.findViewById(R.id.fragment_order_bookingTime_textview);


        mfragmentOrderStatus.setText(mfragmentOrderStatus.getText().toString() + "  " + FullDetailActivity.ORDER.resolveOrderStatus());
        mfragmentOrderTime.setText(mfragmentOrderTime.getText().toString() + "  " + FullDetailActivity.ORDER.resolveOrderDate());
        mfragmentOrderBookingTime.setText(mfragmentOrderBookingTime.getText().toString() + "  " + FullDetailActivity.ORDER.resolveOrderBookingTime());


        return tempView;
    }
}
