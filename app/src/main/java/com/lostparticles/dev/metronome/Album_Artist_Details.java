package com.lostparticles.dev.metronome;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;

public class Album_Artist_Details extends AppCompatActivity {

    ListView lv;
    ArrayList<Integer> pos;

    ImageView img;

    TextView tv;

    MediaMetadataRetriever mmr;

    SharedPreferences themes;

    SharedPreferences initiated;
    SharedPreferences.Editor in;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bitmap bm = BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher);

        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(getResources().getString(R.string.app_name),bm, ContextCompat.getColor(this,R.color.black));
        (this).setTaskDescription(taskDescription);

        themes=getSharedPreferences("themes",MODE_PRIVATE);


        setTheme(themes.getInt("curtheme",R.style.AppTheme));



        setContentView(R.layout.album_artist_details);


        themes=getSharedPreferences("themes",MODE_PRIVATE);



        pos=new ArrayList<Integer>();

        img=(ImageView)findViewById(R.id.albumimg);

        tv=(TextView)findViewById(R.id.albumname);

        int i=getIntent().getExtras().getInt("pos");

        String origin=getIntent().getExtras().getString("origin");


        initiated=getSharedPreferences("initiated",MODE_PRIVATE);

        in=initiated.edit();

        in.putString("origin",origin);
        in.commit();


        if(origin.equals("album")) {

            String arr[] = MusicService.allsongs.get(MusicService.getalbumposition((long) i)).split("@@@");


            mmr = new MediaMetadataRetriever();

            mmr.setDataSource(arr[4]);

            tv.setText(arr[5]);

            byte[] art = mmr.getEmbeddedPicture();


            if (art != null) {

                Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);

                Bitmap scaledBitmap = MusicControl.scaleDown(songImage, 400, true);

                int color = MusicControl.getDominantColor(songImage);
                songImage.recycle();

//

//            String str=String.format("#%06x", color.getRGB() & 0x00FFFFFF);


                img.setImageBitmap(scaledBitmap);
//            scaledBitmap.recycle();



                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);

                //  Toast.makeText(MusicControl.this,a+":"+r+":"+g+":"+b,Toast.LENGTH_LONG).show();

                if(r>230&&g>230&&b>230) {

                    tv.setBackgroundResource(R.color.materialgrey);

                }
                else
                {
                    tv.setBackgroundColor(color);

                }








            /*int dur=mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            sb.setMax(dur);*/


            } else {
                img.setImageResource(R.drawable.ic_album);
//

                tv.setBackgroundResource(themes.getInt("coloraccent",R.color.colorAccentTeal));

            }


            mmr.release();


            pos = MusicService.collectposition((long) i);


        }

        else
        {
            tv.setText(MusicService.uniqueartistname.get(i));

            tv.setBackgroundResource(themes.getInt("coloraccent",R.color.colorAccentTeal));

            img.setImageResource(R.drawable.artist);

            pos=MusicService.collectposition(MusicService.uniqueartistname.get(i));


        }

        lv=(ListView)findViewById(R.id.albumlistview);


        albumdetailsadapter ada=new albumdetailsadapter(this,pos);

        lv.setAdapter(ada);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {

                MusicService.currentalbumartistdetails =pos;


                try {

                    MusicService.positionofcurrentsong = MusicService.currentalbumartistdetails.get(i);

                    Intent in = new Intent(Album_Artist_Details.this, MusicService.class);

                    startService(in);




                    MainActivity.barset(Album_Artist_Details.this); //maybe a memory leak situation,instead use weak refrences
                    MainActivity.barlayout.setVisibility(View.VISIBLE);



                } catch (Exception ex) {


                    FirebaseCrash.report(new Exception("Exception : "+ex));

                }


                in.putInt("positioninlist",i);
                in.commit();




            }
        });




    }
}
