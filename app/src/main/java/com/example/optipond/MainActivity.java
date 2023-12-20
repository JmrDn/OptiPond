package com.example.optipond;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.widget.Toast;

import com.example.optipond.Adapter.ReadingAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.reading_Recyclerview);
        list = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new ReadingAdapter(getApplicationContext(), list);
        recyclerView.setAdapter(myAdapter);



        setUpRecyclerview();
    }

    private void setUpRecyclerview() {


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
}