package utils;

/**
 * Created by Aisha on 6/14/2017.
 */

import android.view.View;


public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
