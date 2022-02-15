package com.example.adminappsocialmedia;

import android.app.Application;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Collections;

public class PostViewholder extends RecyclerView.ViewHolder {

    ImageView imageViewprofile,iv_post;
    TextView tv_name,tv_desc,tv_likes,tv_comment,tv_time,tv_nameprofile;
    ImageButton likebtn,menuoptions,commentbtn;
    DatabaseReference likesref,commentref,blockref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    int likescount,commentcount;
    CardView cardView;
    LinearLayout linearLayout;




    public PostViewholder(@NonNull View itemView) {
        super(itemView);
    }

    public void SetPost(Application activity, String name, String url, String postUri, String time,
                        String uid, String type, String desc){


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();


        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        reference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       // Toast.makeText(g, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                });

        StorageReference reference2 = FirebaseStorage.getInstance().getReferenceFromUrl(postUri);
        reference2.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       // Toast.makeText(g, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                });


        }


}
