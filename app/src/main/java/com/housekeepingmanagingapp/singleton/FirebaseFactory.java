package com.housekeepingmanagingapp.singleton;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by NGI Muzammil on 4/4/2017.
 */

public class FirebaseFactory
{


    private DatabaseReference databaseRef = null;
    private static FirebaseFactory firebaseFactory = null;

    public static FirebaseFactory getFirebaseFactory()
    {
        if (firebaseFactory == null)
        {
             firebaseFactory =new FirebaseFactory();

        }
        return firebaseFactory;
    }

    public DatabaseReference getDatabaseRef()
    {
        if(databaseRef == null)
        {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            databaseRef = database.getReference();
        }
        return databaseRef;
    }



    public static String getCurrentUserId()
    {
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        else
            return "";
    }
}
