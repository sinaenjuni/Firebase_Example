package com.example.y2k43.firebaseexample.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.y2k43.firebaseexample.R;
import com.example.y2k43.firebaseexample.model.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PeopleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_people, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.people_fragment_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new PeoepleFragmentRecyclerViewAdqpter());

        return view;
    }

    class PeoepleFragmentRecyclerViewAdqpter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        List<UserModel> usermodels;

        public PeoepleFragmentRecyclerViewAdqpter(){
            usermodels = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //서버에서 넘어오는 데이터

                    usermodels.clear(); //누적 제거거
                   for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        usermodels.add(snapshot.getValue(UserModel.class));
                    }
                    notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_friend, viewGroup, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            Glide.with
                    (viewHolder.itemView.getContext())
                    .load(usermodels.get(i).userProfileImageUrl)
                    .apply(new RequestOptions().circleCrop())
                    .into(((CustomViewHolder)viewHolder).imageView);

            ((CustomViewHolder)viewHolder).textView.setText(usermodels.get(i).userName);

        }

        @Override
        public int getItemCount() {
            return usermodels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView textView;

            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.item_imageview);
                textView = (TextView) view.findViewById(R.id.item_textview_id);

            }
        }
    }
}
