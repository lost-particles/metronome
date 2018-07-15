package com.lostparticles.dev.metronome;



import android.app.ActivityManager;
import android.content.Intent;

import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;

import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;



import com.google.firebase.crash.FirebaseCrash;

import java.util.Random;


public class MusicControl extends AppCompatActivity {
    @Override
    protected void onPause() {
        super.onPause();

        MusicService.isvisible=0;
    }

    @Override
    protected void onResume() {
        super.onResume();

        MusicService.isvisible=1;

        set();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mmr.release();

        handler.removeCallbacks(run);



    }
    MediaMetadataRetriever mmr;


    final Handler handler=new Handler();

    Runnable run;


    private ImageView imgalbum,imgshuffle,imgprevious,imgnext,imgrepeat;
    private static ImageView imgplaypause;
    private TextView timer,totaltime,tvsong,tvalbum;
    private LinearLayout ll;

    SharedPreferences themes;
    SharedPreferences.Editor th;


    private  SeekBar sb;


    SharedPreferences sp;
    SharedPreferences.Editor e;

    SharedPreferences initiated;
    SharedPreferences.Editor in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bitmap bm = BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher);

        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(getResources().getString(R.string.app_name),bm, ContextCompat.getColor(this,R.color.black));
        (this).setTaskDescription(taskDescription);


        themes=getSharedPreferences("themes",MODE_PRIVATE);

        th=themes.edit();

        setTheme(themes.getInt("curtheme",R.style.AppTheme));



        setContentView(R.layout.activity_music_control);

        imgalbum=(ImageView)findViewById(R.id.album);
        imgshuffle=(ImageView)findViewById(R.id.shuffle);
        imgprevious=(ImageView)findViewById(R.id.previous);
        imgplaypause=(ImageView)findViewById(R.id.playpause);
        imgnext=(ImageView)findViewById(R.id.next);
        imgrepeat=(ImageView)findViewById(R.id.repeat);
        sb=(SeekBar)findViewById(R.id.seekswipe);

        ll=(LinearLayout)findViewById(R.id.activity_music_control);

        tvsong=(TextView)findViewById(R.id.tvsongname);
        tvalbum=(TextView)findViewById(R.id.tvalbumname);
        timer=(TextView)findViewById(R.id.curtime);
        totaltime=(TextView)findViewById(R.id.totaltime);


        mmr = new MediaMetadataRetriever();


        run=new Runnable() {
            @Override
            public void run() {
                int curpos= seekproceed();
                sb.setProgress(curpos);
                timer.setText(durconvert(curpos));

                handler.postDelayed(this,1000);
            }
        };


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setBackgroundResource(themes.getInt("colorprimary",R.color.colorPrimaryTeal));


        sp=getSharedPreferences("repeatstate",MODE_PRIVATE);


        e=sp.edit();

        initiated=getSharedPreferences("initiated",MODE_PRIVATE);

        in=initiated.edit();

        set();

//        sb.setMax(MusicService.mp.getDuration());

      //  Toast.makeText(MusicControl.this,sp.getString("repeat","NULL"),Toast.LENGTH_LONG).show();


        imgplaypause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(MusicService.mp.isPlaying())
                {
                    MusicService.mp.pause();
                    MusicService.am.abandonAudioFocus(MusicService.focuschangelistener);
                    imgplaypause.setImageResource(R.drawable.ic_play);

                    MusicService.sendnotification(MusicControl.this);

                }
                else
                {

                    int result=MusicService.am.requestAudioFocus(MusicService.focuschangelistener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

                    if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {


                        MusicService.mp.start();

                        imgplaypause.setImageResource(R.drawable.ic_pause);

                        MusicService.sendnotification(MusicControl.this);


                    }





                }




            }
        });


        imgnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(initiated.getString("origin","song").equals("song"))
                {

                    if(!sp.getBoolean("shuffle",false))

                    {

                        MusicService.positionofcurrentsong++;

                        if (MusicService.positionofcurrentsong > MusicService.allsongs.size() - 1) {


                            if (sp.getString("repeat", "NONE").equals("ALL") || sp.getString("repeat", "NONE").equals("ONE")) {
                                MusicService.positionofcurrentsong = 0;

                            } else {

                                MusicService.positionofcurrentsong--;

                            }


                        }


                    }
                    else
                    {
                        Random r = new Random();
                        MusicService.positionofcurrentsong = r.nextInt(MusicService.allsongs.size());


                    }

                }
                else if(initiated.getString("origin","song").equals("artist")||initiated.getString("origin","song").equals("album"))
                {

                    int counter=initiated.getInt("positioninlist",MusicService.positionofcurrentsong);

                    if(!sp.getBoolean("shuffle",false))

                    {

                        counter++;

                    }
                    else
                    {

                        counter=new Random().nextInt(MusicService.currentalbumartistdetails.size());

                    }
                    if(counter>MusicService.currentalbumartistdetails.size()-1)
                    {
                       if(sp.getString("repeat", "NONE").equals("ALL")||sp.getString("repeat", "NONE").equals("ONE"))
                       {
                           counter=0;

                       }
                       else
                       {

                           counter--;

                       }



                    }

                    in.putInt("positioninlist",counter);
                    in.commit();

                   MusicService.positionofcurrentsong=MusicService.currentalbumartistdetails.get(counter);


                }





                startService(new Intent(MusicControl.this,MusicService.class));

                set();

            }
        });

        imgprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(initiated.getString("origin","song").equals("song"))
                {

                    if(!sp.getBoolean("shuffle",false))

                    {

                        MusicService.positionofcurrentsong--;


                        if (MusicService.positionofcurrentsong < 0) {


                            if (sp.getString("repeat", "NONE").equals("ALL") || sp.getString("repeat", "NONE").equals("ONE")) {
                                MusicService.positionofcurrentsong = MusicService.allsongs.size() - 1;

                            } else {

                                MusicService.positionofcurrentsong++;

                            }


                        }

                    }
                    else
                    {
                        MusicService.positionofcurrentsong = new Random().nextInt(MusicService.allsongs.size());



                    }




                }
                else if(initiated.getString("origin","song").equals("artist")||initiated.getString("origin","song").equals("album"))
                {

                    int counter=initiated.getInt("positioninlist",MusicService.positionofcurrentsong);



                    if(!sp.getBoolean("shuffle",false))

                    {

                        counter--;

                    }
                    else
                    {

                        counter=new Random().nextInt(MusicService.currentalbumartistdetails.size());

                    }




                    if(counter<0)
                    {
                        if(sp.getString("repeat", "NONE").equals("ALL")||sp.getString("repeat", "NONE").equals("ONE"))
                        {
                            counter=MusicService.currentalbumartistdetails.size()-1;

                        }
                        else
                        {

                            counter++;

                        }



                    }

                    in.putInt("positioninlist",counter);
                    in.commit();

                    MusicService.positionofcurrentsong=MusicService.currentalbumartistdetails.get(counter);


                }





                startService(new Intent(MusicControl.this,MusicService.class));
                set();


            }
        });



        imgrepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {

                    if (sp.getString("repeat", "NONE").equals("NONE")) {
                        e.putString("repeat", "ONE");
                        e.commit();
                        imgrepeat.setImageResource(R.drawable.ic_repeatone);

                        MusicService.mp.setLooping(true);


                        Snackbar snack=Snackbar.make(findViewById(android.R.id.content),"REPEAT ONE",Snackbar.LENGTH_LONG);
                        snack.show();


                    } else if (sp.getString("repeat", "NONE").equals("ONE")) {

                        e.putString("repeat", "ALL");
                        e.commit();

                        imgrepeat.setImageResource(R.drawable.ic_repeatall);

                        MusicService.mp.setLooping(false);



                        Snackbar snack=Snackbar.make(findViewById(android.R.id.content),"REPEAT ALL",Snackbar.LENGTH_LONG);
                        snack.show();


                    } else if (sp.getString("repeat", "NONE").equals("ALL")) {

                        e.putString("repeat", "NONE");
                        e.commit();

                        imgrepeat.setImageResource(R.drawable.ic_repeat);



                        Snackbar snack=Snackbar.make(findViewById(android.R.id.content),"REPEAT NONE",Snackbar.LENGTH_LONG);
                        snack.show();


                    }


                }
                catch (Exception ex)
                {
                  //  Toast.makeText(MusicControl.this,"ERROR :"+ex,Toast.LENGTH_LONG).show();

                    FirebaseCrash.report(new Exception("Exception : "+e));
                }



            }
        });





        imgshuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!sp.getBoolean("shuffle",false))
                {
                    e.putBoolean("shuffle",true);
                    e.commit();

                    imgshuffle.setImageResource(R.drawable.ic_shuflletrue);

                    Snackbar snack=Snackbar.make(findViewById(android.R.id.content),"SHUFFLE ON",Snackbar.LENGTH_LONG);
                    snack.show();

                }
                else
                {

                    e.putBoolean("shuffle",false);
                    e.commit();

                    imgshuffle.setImageResource(R.drawable.ic_shuflle);


                    Snackbar snack=Snackbar.make(findViewById(android.R.id.content),"SHUFFLE OFF",Snackbar.LENGTH_LONG);
                    snack.show();
                }



            }
        });



        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {





            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {



            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                MusicService.mp.seekTo(seekBar.getProgress()*1000);

            }
        });



    }



    public void set()
    {

    handler.removeCallbacks(run);

        String arr[]=MusicService.allsongs.get(MusicService.positionofcurrentsong).split("@@@");

      tvsong.setText(arr[1]);

       mmr.setDataSource(arr[4]);

        tvalbum.setText(arr[5]);

        byte[] art = mmr.getEmbeddedPicture();


        if(art!=null) {

            Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);

            Bitmap scaledBitmap =scaleDown(songImage,400, true);

            int color=getDominantColor(songImage);
            songImage.recycle();

        //   Toast.makeText(MusicControl.this,color+"",Toast.LENGTH_LONG).show();
//

//            String str=String.format("#%06x", color.getRGB() & 0x00FFFFFF);


            imgalbum.setImageBitmap(scaledBitmap);
//            scaledBitmap.recycle();



           // int a = Color.alpha(color);
            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);

          //  Toast.makeText(MusicControl.this,a+":"+r+":"+g+":"+b,Toast.LENGTH_LONG).show();

            if(r>230&&g>230&&b>230) {

               ll.setBackgroundResource(R.color.materialgrey);

            }
            else
            {
                ll.setBackgroundColor(color);

            }

            /*int dur=mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            sb.setMax(dur);*/


        }
        else
        {
            imgalbum.setImageResource(R.drawable.ic_album);
//

            ll.setBackgroundResource(themes.getInt("backcolor",R.color.backcolorTeal));

        }



        /*Cursor cursorAlbum = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID + "=" + MainActivity.image.get(MusicService.positionofcurrentsong), null, null);

        if(cursorAlbum != null  && cursorAlbum.moveToFirst())
        {

            String uri = cursorAlbum.getString(cursorAlbum.getColumnIndex("album_art"));
            cursorAlbum.close();
            if(uri != null )
            {
                imgalbum.setImageURI(Uri.parse(uri));

            }

        }
*/

        /*sb.setProgress(MusicService.mp.getCurrentPosition());
        sb.setEnabled(true);
*/



        if(sp.getString("repeat","NONE").equals("NONE"))
        {
            imgrepeat.setImageResource(R.drawable.ic_repeat);

        }
        else if(sp.getString("repeat","NONE").equals("ONE"))
        {
            imgrepeat.setImageResource(R.drawable.ic_repeatone);

        }
        else if(sp.getString("repeat","NONE").equals("ALL"))
        {
            imgrepeat.setImageResource(R.drawable.ic_repeatall);

        }


        if(!sp.getBoolean("shuffle",false))
        {

            imgshuffle.setImageResource(R.drawable.ic_shuflle);

        }
        else
        {
            imgshuffle.setImageResource(R.drawable.ic_shuflletrue);

        }


        setplaypausestate();


        int duration=Integer.parseInt(arr[3])/1000;

        sb.setMax(duration);

        runOnUiThread(run);


        totaltime.setText(durconvert(duration));


    }



    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }


    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    private int seekproceed()
    {


            int curpos=MusicService.mp.getCurrentPosition()/1000;
            return curpos;




    }



    private String durconvert(int duration)
    {
        String sec;

        if(duration%60<10)
        {

            sec="0"+duration%60;
        }
        else
        {
            sec=duration%60+"";

        }

        return (duration/60+":"+sec);
    }

    public static void setplaypausestate()
    {
        if(MusicService.mp.isPlaying())
        {
            imgplaypause.setImageResource(R.drawable.ic_pause);

        }
        else
        {
            imgplaypause.setImageResource(R.drawable.ic_play);

        }


    }


}
