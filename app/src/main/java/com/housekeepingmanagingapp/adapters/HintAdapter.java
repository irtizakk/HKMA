package com.housekeepingmanagingapp.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by hp on 2/21/2017.
 */

// This adapter is used for spinners
// to show last value as default value
public class HintAdapter extends ArrayAdapter<String>
{

    public HintAdapter(Context theContext, int theLayoutResId, List objects)
    {
        super(theContext,theLayoutResId,objects);
    }
    @Override
    public int getCount()
    {
        // don't display last item. It is used as hint.
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }
}
