package com.housekeepingmanagingapp.customClasses;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.Hashtable;

/**
 * Created by ramal on 9/23/2015.
 */
public class FontUtils {
    public static Context context;
    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    public static String MontserratBold = "Montserrat-Bold.ttf",
            MontserratRegular = "Montserrat-Regular.ttf";


    public static Typeface getFont(Context c, String name)
    {
        synchronized (cache)
        {
            if (!cache.containsKey(name))
            {
                Typeface t = Typeface.createFromAsset(c.getAssets(),
                        //name);
                        String.format("fonts/%s", name));
                cache.put(name, t);
            }
            return cache.get(name);
        }
    }

    public static Typeface getFont(String name) {
        if (context != null)
            synchronized (cache) {
                if (!cache.containsKey(name)) {

                    Typeface t = Typeface.createFromAsset(context.getAssets(),
//                        name);
                            String.format("fonts/%s", name));
                    cache.put(name, t);
                }
                Log.v("Font men ", "agaya");
                return cache.get(name);
            }
        return null;
    }
}
