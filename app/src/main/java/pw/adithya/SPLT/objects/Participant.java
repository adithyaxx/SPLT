package pw.adithya.SPLT.objects;

import android.provider.Telephony;

/**
 * Created by Adithya on 3/22/2018.
 */

public class Participant {
    public String name;
    public double contrib = 0.0, price = 0.0;

    public Participant(String name)
    {
        this.name = name;
    }
}
