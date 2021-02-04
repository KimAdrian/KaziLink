package com.kimadrian.kazilink;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<User> userArrayList;
    Context context;

    //Create a constructor.
    public MyAdapter(Context context, ArrayList<User> userArrayList){
        this.context = context;
        this.userArrayList = userArrayList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent , false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = userArrayList.get(position);
        holder.userNameTextView.setText(user.getUserName());
        holder.emailTextView.setText(user.getEmail());
        holder.professionTextView.setText(user.getProfession());
        holder.phoneNumberTextView.setText(user.getPhoneNumber());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Calling : " + holder.userNameTextView.getText().toString(), Toast.LENGTH_LONG ).show();
                Intent callPerson = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + holder.phoneNumberTextView.getText().toString()));
                context.startActivity(callPerson);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView userNameTextView, emailTextView, phoneNumberTextView, professionTextView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTextView = itemView.findViewById(R.id.textViewDisplayUsername);
            emailTextView = itemView.findViewById(R.id.textViewDisplayEmail);
            phoneNumberTextView = itemView.findViewById(R.id.textViewDisplayPhoneNumber);
            professionTextView = itemView.findViewById(R.id.textViewDisplayProfession);
        }
    }
}
