package com.example.adminappsocialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class ShowProfile extends AppCompatActivity {


    DocumentReference reference,reference2;
    DatabaseReference alluser,db1,db2,db3,Allstory,userstory,reportref;
    FirebaseFirestore firestore;
    String userid,uidofuser,issue;
    TextView nametv,biotv,proftv,emailtv,webtv,issuetv,removeuser,allowuser;
    ImageView imageView;
    Button btnsp;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        nametv = findViewById(R.id.name_sp);
        biotv = findViewById(R.id.bio_sp);
        proftv = findViewById(R.id.prof_sp);
        emailtv = findViewById(R.id.email_sp);
        webtv = findViewById(R.id.web_sp);
        imageView = findViewById(R.id.iv_sp);
        issuetv = findViewById(R.id.issue_sp);
        btnsp = findViewById(R.id.btn_showreporter);

        removeuser = findViewById(R.id.remove_user);
        allowuser = findViewById(R.id.allow_user);





        Bundle extras = getIntent().getExtras();
        if (extras != null){
            userid = extras.getString("u");
            uidofuser = extras.getString("t");
            issue = extras.getString("i");
           // reference2 = firestore.collection("user").document(uidofuser);

        }else {


        }

        database = FirebaseDatabase.getInstance();

        firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("user").document(userid);

        alluser = database.getReference("All Users");
        db1 = database.getReference("All images").child(userid);
        db2 = database.getReference("All videos").child(userid);
        db3 = database.getReference("All posts");
        Allstory = database.getReference("All story");
        userstory = database.getReference("story").child(userid);

        reportref = database.getReference("Report users");



        issuetv.setText(issue);



        reference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()){

                            String nameResult = task.getResult().getString("name");
                            String bioResult = task.getResult().getString("bio");
                            String emailResult = task.getResult().getString("email");
                            String webResult = task.getResult().getString("web");
                            String url = task.getResult().getString("url");
                            String profResult = task.getResult().getString("prof");

                            Picasso.get().load(url).into(imageView);
                            nametv.setText(nameResult);
                            biotv.setText(bioResult);
                            emailtv.setText(emailResult);
                            webtv.setText(webResult);
                            proftv.setText(profResult);


                        }else {

                        }
                    }
                });


        btnsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showreporter();

            }
        });

        removeuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.delete();
                alluser.child(userid).removeValue();
                db1.removeValue();
                db2.removeValue();
                userstory.removeValue();



               // removePosts();


                Query query1 = db3.orderByChild("uid").equalTo(userid);
                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                            dataSnapshot1.getRef().removeValue();

                            Toast.makeText(ShowProfile.this, "Deleted", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                Query query2 = Allstory.orderByChild("uid").equalTo(userid);
                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                            dataSnapshot1.getRef().removeValue();

                            Toast.makeText(ShowProfile.this, "Deleted", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                Query query3 = reportref.orderByChild("uid").equalTo(userid);
                query3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                            dataSnapshot1.getRef().removeValue();

                            Toast.makeText(ShowProfile.this, "Deleted", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

    }

    private void removePosts() {

        RecyclerView recyclerView = findViewById(R.id.rv_ru);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);


        FirebaseRecyclerOptions<Postmember> options=
                new FirebaseRecyclerOptions.Builder<Postmember>()
                        .setQuery(db1,Postmember.class)
                        .build();

        FirebaseRecyclerAdapter<Postmember,PostViewholder> firebaseRecyclerAdapter1 =
                new FirebaseRecyclerAdapter<Postmember, PostViewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PostViewholder holder, int position, @NonNull Postmember model) {

                        holder.SetPost(getApplication(), model.getName(), model.getUrl(), model.getPostUri(), model.getTime()
                                , model.getUid(), model.getType(), model.getDesc());



                    }

                    @NonNull

                    @Override
                    public PostViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.post_delete,parent,false);

                        return new PostViewholder(view);
                    }
                };

        firebaseRecyclerAdapter1.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter1);



    }


    private void showreporter(){
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_profile);

        ImageView imageView = dialog.findViewById(R.id.iv_sp_bs);
        TextView nametv,biotv,emailtv,webtv,proftv;

        nametv = dialog.findViewById(R.id.name_sp_bs);
        biotv = dialog.findViewById(R.id.bio_sp_bs);
        emailtv = dialog.findViewById(R.id.email_sp_bs);
        webtv = dialog.findViewById(R.id.web_sp_bs);
        proftv = dialog.findViewById(R.id.prof_sp_bs);



        reference2.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()){

                            String nameResult = task.getResult().getString("name");
                            String bioResult = task.getResult().getString("bio");
                            String emailResult = task.getResult().getString("email");
                            String webResult = task.getResult().getString("web");
                            String url = task.getResult().getString("url");
                            String profResult = task.getResult().getString("prof");

                            Picasso.get().load(url).into(imageView);
                            nametv.setText(nameResult);
                            biotv.setText(bioResult);
                            emailtv.setText(emailResult);
                            webtv.setText(webResult);
                            proftv.setText(profResult);


                        }else {

                        }
                    }
                });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Bottomanim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


    }
}