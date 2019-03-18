package com.os.operando.android.q.sample;

import android.app.IntentService;
import android.content.Intent;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class StartBackgroundActivityIntentService extends IntentService {

    public StartBackgroundActivityIntentService() {
        super("StartBackgroundActivityIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        i.setFlags(FLAG_ACTIVITY_NEW_TASK);
        getBaseContext().startActivity(i);

        // 03-18 15:35:35.044 W/ActivityTaskManager( 1917): Background activity start [callingPackage: com.os.operando.android.q.sample; callingUid: 10105;
        // isCallingUidForeground: false; isCallingUidPersistentSystemProcess: false; realCallingUid: 10105;
        // isRealCallingUidForeground: false; isRealCallingUidPersistentSystemProcess: false; originatingPendingIntent: null;
        // isBgStartWhitelisted: false; intent: Intent { flg=0x10000000 cmp=com.os.operando.android.q.sample/.MainActivity };
        // callerApp: ProcessRecord{df6d148 15087:com.os.operando.android.q.sample/u0a105}]
    }
}
