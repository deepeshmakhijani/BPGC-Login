package net.deepeshmakhijani.bpgclogin;

import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by shubhamk on 24/11/16.
 */

public class BPGC extends android.app.Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
