package com.example.optipond;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.optipond.Adapter.ReadingAdapter;
import com.example.optipond.FirebaseHandler.FirebaseHandler;
import com.example.optipond.Model.ReadingModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ReadingAdapter myAdapter;
    ArrayList<ReadingModel> list;

    FirebaseHandler firebaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseHandler = new FirebaseHandler();


        //If version is greater than version oreo, notification proceeds

        firebaseHandler.startListeningForNewReadings(new FirebaseHandler.OnNewReadingListener() {
            @Override
            public void onNewReading(String phValue, String waterPercentage, String id) {
                getNotify(phValue, waterPercentage, id);
            }

        });



        setUpRecyclerview();
    }

    private void setUpRecyclerview() {
        recyclerView = findViewById(R.id.reading_Recyclerview);
        list = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new ReadingAdapter(getApplicationContext(), list);
        recyclerView.setAdapter(myAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reading");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    list.clear();
                    int num = 0;
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){


                        if (dataSnapshot.hasChild("waterPercentage") && dataSnapshot.hasChild("phValue")){
                            num++;

                            String waterPercentage = dataSnapshot.child("waterPercentage").getValue().toString() + "%";
                            String phValue = dataSnapshot.child("phValue").getValue().toString();
                            String number = Integer.toString(num);



                            Log.d("TAG", waterPercentage + phValue);

                            list.add(new ReadingModel(waterPercentage, phValue, number));
                        }
                    }


                    if (myAdapter != null){
                        myAdapter.notifyDataSetChanged();
                    }
                }
                else{
                    Log.d("Database", "Snapshot does not exist");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database", "Database error " + error.getMessage());
            }
        });
    }

    public void getNotify(String phValue, String waterPercentage, String id){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("OptiPond new reading",
                    "OptiPond new reading", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100,1000,200,340});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationManager notificationManager =  getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Context context = getApplicationContext();
        if (context != null){
            Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,notificationIntent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "OptiPond new reading");
            builder.setContentTitle("New reading");
            builder.setSmallIcon(R.drawable.ic_launcher_foreground);
            builder.setAutoCancel(true);
            builder.setContentText("Water percentage: " + waterPercentage + "%" + "\nPH Value: " + phValue);
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            builder.setVibrate(new long[] {100,1000,200,340});
            builder.setContentIntent(pendingIntent);


            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            manager.notify(id.hashCode(), builder.build());
        }


    }
}