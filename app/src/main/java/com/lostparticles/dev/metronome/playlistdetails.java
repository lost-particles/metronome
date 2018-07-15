package com.lostparticles.dev.metronome;

import android.app.Activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;

public class playlistdetails extends AppCompatActivity {

    private static ListView lv;

    private static ArrayList<String> playlistdetails;

    SQLiteDatabase sb;

    public static String table;

    public static SongsAdapter aa;

    private ArrayList<Long> albumids;

    private ArrayList<Integer> posn;

    SharedPreferences initiated;
    SharedPreferences.Editor in;

    SharedPreferences themes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        Bitmap bm = BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher);

        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(getResources().getString(R.string.app_name),bm, ContextCompat.getColor(this,R.color.black));
        (this).setTaskDescription(taskDescription);

        themes=getSharedPreferences("themes",MODE_PRIVATE);


        setTheme(themes.getInt("curtheme",R.style.AppTheme));

        setContentView(R.layout.fragment_songs_listview);

        lv=(ListView)findViewById(R.id.listview);


        table=getIntent().getExtras().getString("table");

        playlistdetails=new ArrayList<String>();

        albumids=new ArrayList<Long>();
        posn=new ArrayList<Integer>();


        initiated=getSharedPreferences("initiated",MODE_PRIVATE);

        in=initiated.edit();



        sb=openOrCreateDatabase("DATABASE",MODE_PRIVATE,null);


        try {


            String sql = "select * from "+table;
            Cursor cr = sb.rawQuery(sql, null);
            cr.moveToFirst();
            while (cr.isAfterLast() == false) {
                long id = cr.getLong(0);

                int pos=MusicService.getcurrentposition(id);

                posn.add(pos);

                albumids.add(MusicService.albumids.get(pos));





                playlistdetails.add(MusicService.allsongs.get(pos));



                cr.moveToNext();
            }
            cr.close();

            //Toast.makeText(this,albumids+"\n",Toast.LENGTH_LONG).show();

        }
        catch (Exception e)
        {

           // Toast.makeText(playlistdetails.this,"ERROR :"+e,Toast.LENGTH_LONG).show();

            FirebaseCrash.report(new Exception("Exception : "+e));

        }




        visibilty(this);






        aa=new SongsAdapter(this,playlistdetails,1,albumids);
        lv.setAdapter(aa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                MusicService.currentalbumartistdetails =posn;


                try {

                    MusicService.positionofcurrentsong = MusicService.currentalbumartistdetails.get(i);

                    in.putString("origin","artist");
                    in.commit();

                    Intent in = new Intent(playlistdetails.this, MusicService.class);

                    startService(in);




                    MainActivity.barset(playlistdetails.this); //maybe a memory leak situation,instead use weak refrences
                    MainActivity.barlayout.setVisibility(View.VISIBLE);



                } catch (Exception ex) {


                    FirebaseCrash.report(new Exception("Exception : "+ex));

                }

                in.putInt("positioninlist",i);
                in.commit();






            }
        });



        sb.close();

    }

    public static void listupdate(int pos,Activity act)
    {
        playlistdetails.remove(pos);

        visibilty(act);


        aa.notifyDataSetChanged();


    }
    private static void visibilty(Activity cnt)
    {
        FrameLayout fm=(FrameLayout) cnt.findViewById(R.id.frame);

        if(playlistdetails.size()==0)
        {


            lv.setVisibility(View.GONE);

            TextView tv=new TextView(cnt);
            tv.setText("NO SONGS FOUND");
            tv.setTextSize(50);

            FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            lp.setMargins(100,100,100,100);
            fm.addView(tv,lp);








        }

        else{
            lv.setVisibility(View.VISIBLE);

        }





    }
}
