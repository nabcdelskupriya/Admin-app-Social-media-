package com.example.adminappsocialmedia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Reportposts extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference reportref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    String emailResult,nameResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportposts);

        recyclerView = findViewById(R.id.rv_report);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Reportposts.this));

        reportref =  FirebaseDatabase.getInstance().getReference("Report Post");


        FirebaseRecyclerOptions<ReportClass> options1 =
                new FirebaseRecyclerOptions.Builder<ReportClass>()
                        .setQuery(reportref,ReportClass.class)
                        .build();

        FirebaseRecyclerAdapter<ReportClass,ReportVh> firebaseRecyclerAdapter1 =
                new FirebaseRecyclerAdapter<ReportClass, ReportVh>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull ReportVh holder, int position, @NonNull ReportClass model) {


                        holder.setreport(getApplication(),model.getName(),model.getUrl(),model.getUseruri(),model.getUid()
                        ,model.getType(),model.getIssue(),model.getTime());

                        String time = getItem(position).getTime();
                        String uid = getItem(position).getUid();
                        String dueto = getItem(position).getIssue();

                        holder.btndecline.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                declinePost(uid,time);
                                sendEmail(uid,dueto);
                            }
                        });


                        holder.btnallow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                allowpost(time,uid);
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public ReportVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_report_post,parent,false);

                        return new ReportVh(view);
                    }
                };

        firebaseRecyclerAdapter1.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter1);


    }

    private void sendEmail(String uid,String dueto) {

        String url = "https://apiapp1.000webhostapp.com/sendemail.php";
        String message = "We have Removed your post from Social media app Due to "+ dueto;

        DocumentReference reference;
        FirebaseFirestore firestore;

        firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("user").document(uid);


        reference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()){

                             nameResult = task.getResult().getString("name");
                            emailResult= task.getResult().getString("email");

                        }else {

                        }
                    }
                });


        Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String,String> param  = new HashMap<String, String>();

                        param.put("name",nameResult);
                        param.put("email",emailResult);
                        param.put("message",message);

                        return param;

                    }
                };


                RequestQueue requestQueue = Volley.newRequestQueue(Reportposts.this);
                requestQueue.add(stringRequest);


            }
        },3000);




    }

    private void declinePost(String uid, String time) {

        DatabaseReference db1,db2,db3,reportRefPost;

        db1 = database.getReference("All images").child(uid);
        db2 = database.getReference("All videos").child(uid);
        db3 = database.getReference("All posts");
        reportRefPost = database.getReference("Report Post");


        Query query = db1.orderByChild("time").equalTo(time);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    dataSnapshot1.getRef().removeValue();

                    Toast.makeText(Reportposts.this, "Deleted", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Query query1 = db2.orderByChild("time").equalTo(time);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    dataSnapshot1.getRef().removeValue();

                    Toast.makeText(Reportposts.this, "Deleted", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query query2 = db3.orderByChild("time").equalTo(time);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    dataSnapshot1.getRef().removeValue();

                    Toast.makeText(Reportposts.this, "Deleted", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Query query3 = reportRefPost.orderByChild("time").equalTo(time);
        query3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    dataSnapshot1.getRef().removeValue();

                    Toast.makeText(Reportposts.this, "Deleted", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void allowpost(String time,String uid){


        DatabaseReference reportRefPost = database.getReference("Report Post");

        Query query1 = reportRefPost.orderByChild("time").equalTo(time);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    dataSnapshot1.getRef().removeValue();

                    Toast.makeText(Reportposts.this, "Deleted", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();



    }
}