package com.example.adminappsocialmedia;

import android.app.Application;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;

import java.util.Collections;

public class ReportVh extends RecyclerView.ViewHolder {

    TextView tvname,timetv,issetv,tvnamer_u,issue_ru;
    ImageView imageView,ivru;
    PlayerView playerView;
    Button btnallow,btndecline;




    public ReportVh(@NonNull View itemView) {
        super(itemView);
    }

    public void setReportusers(Application application,String name,String url, String uid,String issue){


        ivru = itemView.findViewById(R.id.iv_r_u);
        tvnamer_u = itemView.findViewById(R.id.name_r_u);
        issue_ru = itemView.findViewById(R.id.view_r_u);


        tvnamer_u.setText(name);
        Picasso.get().load(url).into(ivru);
        issue_ru.setText("View Profile");


    }
    public void setreport(Application application, String name,String url,
                          String useruri, String uid,String type,String issue,String time){


        tvname = itemView.findViewById(R.id.tvname);
        issetv = itemView.findViewById(R.id.tvissue);
        timetv = itemView.findViewById(R.id.tvtime);
        btnallow  = itemView.findViewById(R.id.btn_allow);
        btndecline = itemView.findViewById(R.id.btn_decline);


        imageView = itemView.findViewById(R.id.iv_post_item);
        playerView = itemView.findViewById(R.id.exoplayer_item_post);


        tvname.setText(name);
        timetv.setText(time);
        issetv.setText(issue);

        if (type.equals("iv")){

            Picasso.get().load(url).into(imageView);
            playerView.setVisibility(View.GONE);

        }else if (type.equals("vv")){

            imageView.setVisibility(View.GONE);

            try {
                SimpleExoPlayer simpleExoPlayer = new SimpleExoPlayer.Builder(application).build();
                playerView.setPlayer(simpleExoPlayer);
                MediaItem mediaItem = MediaItem.fromUri(url);
                simpleExoPlayer.addMediaItems(Collections.singletonList(mediaItem));
                simpleExoPlayer.prepare();
                simpleExoPlayer.setPlayWhenReady(false);

            }catch (Exception e){
              //  Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }

        }

    }

}
