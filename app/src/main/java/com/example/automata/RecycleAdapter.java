package com.example.automata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {

    Context context;

    ArrayList<String> d1;
    ArrayList<String> d2;
    ArrayList<String> d3;
    List<String> d4;
    public RecycleAdapter(Context ct, ArrayList<String> s1, ArrayList<String> s2, ArrayList<String> img, List<String> date){
        context = ct;
        d1 = s1;
        d2 = s2;
        d3 = img;
        d4 = date;
    }





    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecycleAdapter.MyViewHolder holder, int position) {

        holder.myText1.setText((d1.get(position)));
        holder.myText2.setText((d2.get(position)));
        holder.myText3.setText(d4.get(position));
        Glide.with(context)
                .load(d3.get(position))
                .into(holder.myImage);

    }

    @Override
    public int getItemCount() {
        return d3.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView myText1, myText2, myText3;
        ImageView myImage;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            myText1 = itemView.findViewById(R.id.usernameTextViewHome);
            myText2 = itemView.findViewById(R.id.memoTextViewHome);
            myText3 = itemView.findViewById(R.id.createdAtTextView);
            myImage = itemView.findViewById(R.id.profilePicImageViewHome);
        }
    }
}
