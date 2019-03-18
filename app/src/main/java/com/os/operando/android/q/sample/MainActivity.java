package com.os.operando.android.q.sample;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;
import com.os.operando.android.q.sample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                // Intent panelIntent = new Intent(Settings.Panel.ACTION_VOLUME);
//                Intent panelIntent = new Intent(Settings.Panel.ACTION_NFC);
                startActivityForResult(panelIntent, 1);

                // I/ActivityTaskManager( 1913): START u0 {act=android.settings.panel.action.INTERNET_CONNECTIVITY cmp=com.android.settings/.panel.SettingsPanelActivity} from uid 10105
                // Sliceで組まれてる
                // SettingsSliceProvider

            }
        });

        binding.role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RoleManager roleManager = getSystemService(RoleManager.class);
                if (roleManager.isRoleAvailable(RoleManager.ROLE_MUSIC)) {
                    if (roleManager.isRoleHeld(RoleManager.ROLE_MUSIC)) {
                        Log.d("RoleManager", "OK");
                    } else {
                        // This app isn't the default music player, but the role is available,
                        // so request it.
                        Intent roleRequestIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_MUSIC);
                        startActivityForResult(roleRequestIntent, 2);
//                        I/ActivityTaskManager( 1917): START u0 {act=android.app.role.action.REQUEST_ROLE pkg=com.google.android.permissioncontroller cmp=com.google.android.permissioncontroller/com.android.packageinstaller.role.ui.RequestRoleActivity (has extras)} from uid 10105
//                        03-18 13:59:44.117 W/Role    ( 2169): com.os.operando.android.q.sample not qualified for android.app.role.MUSIC due to missing RequiredComponent{mIntentFilterData=IntentFilterData{mAction='android.intent.action.MAIN', mCategories='[android.intent.category.APP_MUSIC]', mDataScheme='null', mDataType='null'}, mPermission='null', mMetaData=[]}
//                        03-18 13:59:44.117 W/RequestRoleActivity( 2169): Application doesn't qualify for role, role: android.app.role.MUSIC, package: com.os.operando.android.q.sample
//                                03-18 13:59:44.149 D/onActivityResult(11122): resultCode : 0
                    }
                }
            }
        });


        binding.notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fullScreenIntent = new Intent(MainActivity.this, MainActivity.class);
                PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(MainActivity.this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                NotificationChannel channel = new NotificationChannel("test",
                        "test",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.this, "test")
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle("Incoming call")
                        .setContentText("(919) 555-1234")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_CALL)
                        // Use a full-screen intent only for the highest-priority alerts where you
                        // have an associated activity that you would like to launch after the user
                        // interacts with the notification. Also, if your app targets Android Q, you
                        // need to request the USE_FULL_SCREEN_INTENT permission in order for the
                        // platform to invoke this notification.
                        .setFullScreenIntent(fullScreenPendingIntent, true);

                notificationBuilder.build();
                notificationManager.notify("aaa", 1, notificationBuilder.build());
                // 03-18 15:18:06.288 W/NotificationService( 1917): Package com.os.operando.android.q.sample: Use of fullScreenIntent requires the USE_FULL_SCREEN_INTENT permission
            }
        });

        binding.activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(MainActivity.this, StartBackgroundActivityIntentService.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult", "resultCode : " + resultCode);

//        03-18 14:09:41.229 I/RoleUserState( 1917): Wrote roles.xml successfully
//        03-18 14:09:40.982 I/PackageManager( 1917): Replacing preferred activity com.os.operando.android.q.sample/.MainActivity for user 0:
//        03-18 14:09:40.983 I/PackageManager( 1917):   Action: "android.intent.action.MAIN"
//        03-18 14:09:40.983 I/PackageManager( 1917):   Category: "android.intent.category.APP_MUSIC"
//        03-18 14:09:40.983 I/PackageManager( 1917):   Category: "android.intent.category.DEFAULT"
//        03-18 14:09:41.000 D/onActivityResult(11655): resultCode : -1

    }
}
