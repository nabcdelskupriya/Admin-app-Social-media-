package com.example.adminappsocialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportUsers extends AppCompatActivity {


    RecyclerView recyclerView;
    DatabaseReference reportref;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_users);

        database = FirebaseDatabase.getInstance();

        recyclerView = findViewById(R.id.rv_report_users);

        recyclerView.setLayoutManager(new LinearLayoutManager(ReportUsers.this));
        recyclerView.setHasFixedSize(true);


        reportref = database.getReference("Report users");


        FirebaseRecyclerOptions<ReportClass2> options=
                new FirebaseRecyclerOptions.Builder<ReportClass2>()
                        .setQuery(reportref,ReportClass2.class)
                        .build();

        FirebaseRecyclerAdapter<ReportClass2,ReportVh> firebaseRecyclerAdapter1 =
                new FirebaseRecyclerAdapter<ReportClass2, ReportVh>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ReportVh holder, int position, @NonNull ReportClass2 model) {

                        String uid = getItem(position).getUid();
                        String uidofuser = getItem(position).getType();
                        String issue = getItem(position).getIssue();


                        holder.setReportusers(getApplication(),model.getName(),model.getUrl(),model.getUid(),model.getIssue());

                        holder.issue_ru.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ReportUsers.this,ShowProfile.class);
                                intent.putExtra("u",uid);
                                intent.putExtra("t",uidofuser);
                                intent.putExtra("i",issue);
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ReportVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.report_users_item,parent,false);

                        return new ReportVh(view);
                    }
                };

        firebaseRecyclerAdapter1.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter1);

    }


}