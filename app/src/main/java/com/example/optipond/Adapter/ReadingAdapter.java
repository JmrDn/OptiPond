package com.example.optipond.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.optipond.Model.ReadingModel;
import com.example.optipond.R;

import java.util.ArrayList;

public class ReadingAdapter extends RecyclerView.Adapter<ReadingAdapter.MyViewHolder> {

    Context context;
    ArrayList<ReadingModel> readingModelList;


    public ReadingAdapter(Context context, ArrayList<ReadingModel> readingModelList){
        this.context = context;
        this.readingModelList = readingModelList;
    }
    @NonNull
    @Override
    public ReadingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reading_list_recyclerview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadingAdapter.MyViewHolder holder, int position) {
        holder.number.setText(readingModelList.get(position).getNumber());
        holder.waterLevelPercentage.setText(readingModelList.get(position).getWaterPercentage());
        holder.phLevel.setText(readingModelList.get(position).getPhValue());


    }

    @Override
    public int getItemCount() {
        return readingModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView number, waterLevelPercentage, phLevel;
        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            number = itemView.findViewById(R.id.number_Textview);
            waterLevelPercentage = itemView.findViewById(R.id.waterPercentage_Textview);
            phLevel = itemView.findViewById(R.id.phLevel_Textview);


        }
    }
}
