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

public class TabbedService extends Fragment {

    TextView mfragmentServiceName, mfragmentServicePrice;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabbed_service, container, false);
        View tempView = rootView;

        mfragmentServiceName = (TextView) tempView.findViewById(R.id.fragment_service_name_textview);
        mfragmentServicePrice = (TextView) tempView.findViewById(R.id.fragment_service_price_textview);


        mfragmentServiceName.setText(mfragmentServiceName.getText().toString() + "  " + FullDetailActivity.ORDER.getOrderServiceName());
        mfragmentServicePrice.setText(mfragmentServicePrice.getText().toString() + "  " + FullDetailActivity.ORDER.getOrderPrice());


        return tempView;
    }
}
