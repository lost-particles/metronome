package com.lostparticles.dev.metronome;




import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;

import android.database.Cursor;


import android.media.AudioManager;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;


import android.view.View;

import android.widget.RemoteViews;





import com.google.firebase.crash.FirebaseCrash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class MusicService extends Service {


    static MediaPlayer mp;

    static AudioManager am;

    public static int isvisible=0;

    private static Boolean paused=false;

    public static TaskStackBuilder tsb;

    private static  int notId = 1057;



   static AudioManager.OnAudioFocusChangeListener focuschangelistener=new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int i) {

            if (i == AudioManager.AUDIOFOCUS_LOSS) {

                try {
                    mp.pause();


                   paused=true;

                    am.abandonAudioFocus(focuschangelistener);



                    sendnotification(App.getContext());

                    if(isvisible==1)
                    {
                        MusicControl.setplaypausestate();

                    }
                    if(ismainvisivble)
                    {

                        MainActivity.barset(App.getContext());

                    }



                } catch (Exception e) {


                    FirebaseCrash.report(new Exception("Exception : "+e));

                }


            } else if (i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ) {


                try {

                    mp.pause();


                    sendnotification(App.getContext());

                    if(isvisible==1)
                    {
                        MusicControl.setplaypausestate();

                    }
                    if(ismainvisivble)
                    {

                        MainActivity.barset(App.getContext());

                    }






                } catch (Exception e) {


                    FirebaseCrash.report(new Exception("Exception : "+e));

                }


            }

            else if(i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)

            {

                try {

                    if(mp!=null)
                    {

                        mp.setVolume(0.3f,0.3f);

                    }



                } catch (Exception e) {

                    FirebaseCrash.report(new Exception("Exception : "+e));

                }



            }
            else if (i == AudioManager.AUDIOFOCUS_GAIN) {

                try {

                    if(paused) {
                        mp.start();

                        paused=false;

                        sendnotification(App.getContext());

                        if(isvisible==1)
                        {
                            MusicControl.setplaypausestate();

                        }
                        if(ismainvisivble)
                        {

                            MainActivity.barset(App.getContext());

                        }


                        Log.v("tag", "onAudioFocusChange: ");


                    }

                    if (mp != null) {

                        mp.setVolume(1.0f, 1.0f);

                    }

                } catch (Exception e) {

                    FirebaseCrash.report(new Exception("Exception : "+e));

                }
            }


        }
    };




    static long numberofalbums=0;

    static ArrayList<Long> ids = new ArrayList<Long>();
    static ArrayList<Long> albumids = new ArrayList<Long>();
    static ArrayList<Integer> currentalbumartistdetails =new ArrayList<Integer>();


    static ArrayList<String> artistname=new ArrayList<String>();

    static ArrayList<String> uniqueartistname=new ArrayList<String>();

    SharedPreferences states;
    SharedPreferences.Editor e2;
    backthread bt;
    public static NotificationManager manager;
    public static Notification myNotication;

//
//    MediaMetadataRetriever mmr;



   /* private ImageView albumimg;
    private TextView songname,artistnametv;*/



    public static Boolean ismainvisivble=false;


    SharedPreferences sp;
    SharedPreferences.Editor e;

    SharedPreferences initiated;
    SharedPreferences.Editor in;


    static ArrayList<String> allsongs = new ArrayList<String>();
    static int positionofcurrentsong;

    @Override
    public void onCreate() {
        super.onCreate();

//        mp = MediaPlayer.create(MusicService.this, Settings.System.DEFAULT_NOTIFICATION_URI);


        mp = new MediaPlayer();
        try {
            mp.setDataSource(MusicService.this, Settings.System.DEFAULT_NOTIFICATION_URI);
        } catch (IOException e1) {
            //e1.printStackTrace();

            FirebaseCrash.report(new Exception("Exception : "+e1));

        }
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mp.prepare();
        } catch (IOException e1) {
           // e1.printStackTrace();

            FirebaseCrash.report(new Exception("Exception : "+e1));

        }




       // Toast.makeText(MusicService.this, "INSIDE ON CREATE", Toast.LENGTH_LONG).show();
        states = getSharedPreferences("states", MODE_PRIVATE);
        e2 = states.edit();

        e2.putBoolean("servicerunning", true);
        e2.commit();

        if (am == null) {
            am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        }

        sp=getSharedPreferences("repeatstate",MODE_PRIVATE);


        e=sp.edit();

        initiated=getSharedPreferences("initiated",MODE_PRIVATE);

        in=initiated.edit();

       /* try {

            LayoutInflater lf = LayoutInflater.from(getApplicationContext());

            View v = lf.inflate(R.layout.nav_header_main, null);

            albumimg = (ImageView) v.findViewById(R.id.albumimg);

            songname = (TextView) v.findViewById(R.id.songname);

            artistnametv = (TextView) v.findViewById(R.id.artistname);

            mmr = new MediaMetadataRetriever();


        }
        catch (Exception e)
        {
            FirebaseCrash.report(new Exception("Exception : "+e));

            Toast.makeText(this,"oncreate : "+e,Toast.LENGTH_LONG).show();

        }*/


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {








        String arrs[] = allsongs.get(positionofcurrentsong).split("@@@");

        Uri uri = Uri.parse(arrs[4]);

        bt = new backthread();




        int result=am.requestAudioFocus(focuschangelistener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // Start playback

            bt.execute(uri);

          /*  try {

                Cursor cursorAlbum = getApplicationContext().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID + "=" + albumids.get(positionofcurrentsong), null, null);


                if (cursorAlbum != null && cursorAlbum.moveToFirst()) {

                    String urii = cursorAlbum.getString(cursorAlbum.getColumnIndex("album_art"));
                    cursorAlbum.close();
                    if (uri != null) {

                        albumimg.setImageURI(Uri.parse(urii));

                    } else {
                        albumimg.setImageResource(R.drawable.ic_album);

                    }
                }


                songname.setText(arrs[1]);

                artistnametv.setText(arrs[2]);

            }
            catch (Exception e)
            {
                FirebaseCrash.report(new Exception("Exception : "+e));

            }
*/


         /* try {

              mmr.setDataSource(arrs[4]);


              byte[] art = mmr.getEmbeddedPicture();


              if (art != null) {

                  Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);

                  Bitmap scaledBitmap = MusicControl.scaleDown(songImage, 200, true);

                  songImage.recycle();


                  albumimg.setImageBitmap(scaledBitmap);


              } else {
                  albumimg.setImageResource(R.drawable.ic_album);

              }

              songname.setText(arrs[1]);

              artistnametv.setText(arrs[2]);


          }
          catch (Exception e)
          {
              FirebaseCrash.report(new Exception("Exception : "+e));

              Toast.makeText(this,"onstartcommand : "+e,Toast.LENGTH_LONG).show();

          }
*/




            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {


                  //  Toast.makeText(MusicService.this, "on completion", Toast.LENGTH_SHORT).show();

                    if(sp.getString("repeat", "NONE").equals("ALL"))
                    {


                        if(initiated.getString("origin","song").equals("song"))
                        {

                            if(!sp.getBoolean("shuffle",false)) {
                                MusicService.positionofcurrentsong++;

                            }
                            else
                            {

                                MusicService.positionofcurrentsong = new Random().nextInt(MusicService.allsongs.size());

                            }
                            if(MusicService.positionofcurrentsong>MusicService.allsongs.size()-1)
                            {


                                if(sp.getString("repeat", "NONE").equals("ALL")||sp.getString("repeat", "NONE").equals("ONE"))
                                {
                                    MusicService.positionofcurrentsong=0;

                                }
                                else
                                {

                                    MusicService.positionofcurrentsong--;

                                }



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
                                counter=new Random().nextInt(currentalbumartistdetails.size());


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




                        startService(new Intent(MusicService.this,MusicService.class));

                        try {
                            if(ismainvisivble) {
                                MainActivity.barset(MusicService.this);

                            }
                            }
                        catch (Exception e)
                        {

                            FirebaseCrash.report(new Exception("Exception : "+e));

                        }


                        if(isvisible==1)
                        {

                            Intent in = new Intent(MusicService.this, MusicControl.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);


                        }



                    }
                    else if(sp.getString("repeat", "NONE").equals("NONE"))
                    {

                       /* MusicService.am.abandonAudioFocus(MusicService.focuschangelistener);
                        if(isvisible==1)
                        {
                            MusicControl.setplaypausestate();

                        }

                        sendnotification(MusicService.this);
                        if(ismainvisivble)
                        {

                            MainActivity.barset(MusicService.this);

                        }*/




                       Boolean end=false;


                        if(initiated.getString("origin","song").equals("song"))
                        {

                            if(!sp.getBoolean("shuffle",false)) {
                                MusicService.positionofcurrentsong++;

                            }
                            else
                            {

                                MusicService.positionofcurrentsong = new Random().nextInt(MusicService.allsongs.size());

                            }
                            if(MusicService.positionofcurrentsong>MusicService.allsongs.size()-1)
                            {


                                end=true;
                                MusicService.positionofcurrentsong--;





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
                                counter=new Random().nextInt(currentalbumartistdetails.size());


                            }
                            if(counter>MusicService.currentalbumartistdetails.size()-1)
                            {


                                end=true;
                                    counter--;





                            }

                            in.putInt("positioninlist",counter);
                            in.commit();

                            MusicService.positionofcurrentsong=MusicService.currentalbumartistdetails.get(counter);


                        }



                        if(end)
                        {

                        }
                        else
                        {

                            startService(new Intent(MusicService.this, MusicService.class));

                            try {
                                if (ismainvisivble) {
                                    MainActivity.barset(MusicService.this);

                                }
                            } catch (Exception e) {

                                FirebaseCrash.report(new Exception("Exception : " + e));

                            }


                            if (isvisible == 1) {

                                Intent in = new Intent(MusicService.this, MusicControl.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);


                            }


                        }



                    }


                }
            });


        }








        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        Toast.makeText(MusicService.this,"on destroy",Toast.LENGTH_LONG).show();

        try {
            e2.putInt("songpos", positionofcurrentsong);
            e2.putBoolean("servicerunning", false);
            e2.commit();
            if(mp!=null) {
                mp.stop();
                mp.release();
            }
                am.abandonAudioFocus(focuschangelistener);

            stopForeground(true);






        } catch (Exception e) {

            FirebaseCrash.report(new Exception("Exception : "+e));

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class backthread extends AsyncTask<Uri, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if(ismainvisivble)
            {

                if(states.getBoolean("firsttime",true))
                {
                    e2.putBoolean("firsttime",false);
                    e2.commit();


                }


                if(!states.getBoolean("firsttime",true)) {



                    MainActivity.barlayout.setVisibility(View.VISIBLE);

                    MainActivity.barset(MusicService.this); //maybe a memory leak situation,instead use weak refrences





                }
            }

            if(isvisible==1)
            {
                MusicControl.setplaypausestate();

            }

            sendnotification(MusicService.this);

            startForeground(notId, myNotication);




        }

        @Override
        protected Void doInBackground(Uri... uris) {


            try {
                mp.stop();
                mp.reset();
              /*  mp = MediaPlayer.create(MusicService.this, uris[0]);
                mp.start();*/


                try {
                    mp.setDataSource(MusicService.this,uris[0]);
                } catch (IOException e1) {
                   // e1.printStackTrace();

                    FirebaseCrash.report(new Exception("Exception : "+e1));

                }
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mp.prepare();
                } catch (IOException e1) {
                    //e1.printStackTrace();

                    FirebaseCrash.report(new Exception("Exception : "+e1));

                }
                mp.start();




            } catch (Exception exc) {

                FirebaseCrash.report(new Exception("Exception : "+exc));

            }
            /*try
            {
                Thread.sleep(mp.getDuration());

            }
            catch (Exception e)
            {

                Toast.makeText(MusicService.this,"ERROR :"+e,Toast.LENGTH_LONG).show();
            }*/

            return null;
        }
    }



    public static void sendnotification(Context srv) {



        String arr[] = allsongs.get(positionofcurrentsong).split("@@@");


        manager = (NotificationManager) srv.getApplicationContext().getSystemService(NOTIFICATION_SERVICE);


        RemoteViews contentView=new RemoteViews(srv.getApplicationContext().getPackageName(), R.layout.notificationsmallcontent);

        RemoteViews contentViewBig=new RemoteViews(srv.getApplicationContext().getPackageName(),R.layout.notification);

        Intent intentmain=new Intent(srv.getApplicationContext(),MainActivity.class);

        Intent intent = new Intent(srv.getApplicationContext(), MusicControl.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        tsb=TaskStackBuilder.create(srv.getApplicationContext());

        tsb.addParentStack(MainActivity.class);

        tsb.addNextIntent(intentmain);

        tsb.addNextIntent(intent);




        Intent previntent=new Intent("handle.notification.click");

        previntent.putExtra("clickevent","prev");

        Intent playpauseintent=new Intent("handle.notification.click");

        playpauseintent.putExtra("clickevent","playpause");

        Intent nextintent=new Intent("handle.notification.click");

        nextintent.putExtra("clickevent","next");

        Intent closeintent=new Intent("handle.notification.click");

        closeintent.putExtra("clickevent","close");


        //   intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);


     //   PendingIntent pendingIntent = PendingIntent.getActivity(MusicService.this, 1, intent,PendingIntent.FLAG_CANCEL_CURRENT);

        PendingIntent pendingIntent = tsb.getPendingIntent(1,PendingIntent.FLAG_CANCEL_CURRENT);

        PendingIntent prevpendingintent=PendingIntent.getBroadcast(srv.getApplicationContext(),2,previntent,PendingIntent.FLAG_UPDATE_CURRENT);


        PendingIntent playpausependingintent = PendingIntent.getBroadcast(srv.getApplicationContext(),3,playpauseintent,PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent nextpendingintent=PendingIntent.getBroadcast(srv.getApplicationContext(),4,nextintent,PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent closependingintent=PendingIntent.getBroadcast(srv.getApplicationContext(),5,closeintent,PendingIntent.FLAG_UPDATE_CURRENT);









        NotificationCompat.Builder builder = new NotificationCompat.Builder(srv.getApplicationContext());

        builder.setAutoCancel(true);
     //   builder.setTicker("MUSIC PLAYING");
      //  builder.setContentTitle(arr[1]);
      //  builder.setContentText(arr[2]);
        builder.setSmallIcon(R.drawable.ic_album);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
     //  builder.setSubText("This is subtext...");
        builder.setPriority(Notification.PRIORITY_MAX);

      //  builder.setWhen(System.currentTimeMillis()*2);


        builder.setCategory(NotificationCompat.CATEGORY_TRANSPORT);
     //  builder.addAction(R.drawable.barpause,"pause",pendingIntent);

      //  builder.setStyle(new NotificationCompat.BigTextStyle().bigText("ALBUM"));


        //API level 16
      // builder.setNumber(100);

        contentView.setOnClickPendingIntent(R.id.notiprev,prevpendingintent);

        contentView.setOnClickPendingIntent(R.id.notiplaypause,playpausependingintent);

        contentView.setOnClickPendingIntent(R.id.notinext,nextpendingintent);



        contentView.setTextViewText(R.id.tvname,arr[1]);

        contentView.setTextViewText(R.id.tvalbum,arr[5]);

        if(mp.isPlaying())
        {

            contentView.setImageViewResource(R.id.notiplaypause,R.drawable.barpause);
        }
        else
        {
            contentView.setImageViewResource(R.id.notiplaypause,R.drawable.barplay);

        }





        contentViewBig.setOnClickPendingIntent(R.id.notiprev,prevpendingintent);

        contentViewBig.setOnClickPendingIntent(R.id.notiplaypause,playpausependingintent);

        contentViewBig.setOnClickPendingIntent(R.id.notinext,nextpendingintent);

        contentViewBig.setOnClickPendingIntent(R.id.close,closependingintent);



        contentViewBig.setTextViewText(R.id.tvname,arr[1]);

        contentViewBig.setTextViewText(R.id.tvalbum,arr[5]);

        if(mp.isPlaying())
        {

            contentViewBig.setImageViewResource(R.id.notiplaypause,R.drawable.barpause);
        }
        else
        {
            contentViewBig.setImageViewResource(R.id.notiplaypause,R.drawable.barplay);

        }






        Cursor cursorAlbum = srv.getApplicationContext().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID + "=" + albumids.get(positionofcurrentsong), null, null);


        if(cursorAlbum != null  && cursorAlbum.moveToFirst())
        {

            String uri = cursorAlbum.getString(cursorAlbum.getColumnIndex("album_art"));
            cursorAlbum.close();
            if(uri != null )
            {
                contentView.setImageViewUri(R.id.curimage,Uri.parse(uri));

                contentViewBig.setImageViewUri(R.id.curimage,Uri.parse(uri));
            }

            else
            {
                contentView.setImageViewResource(R.id.curimage,R.drawable.ic_album);
                contentViewBig.setImageViewResource(R.id.curimage,R.drawable.ic_album);

            }
        }







        builder.setContent(contentView);
       builder.setCustomBigContentView(contentViewBig);


        myNotication = builder.build();



       // myNotication.when=System.currentTimeMillis()*2;






        manager.notify(notId, myNotication);







    }


    public static int getcurrentposition(Long l) {
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i).equals(l)) {
                return i;
            }
        }

        return positionofcurrentsong;


    }

    public static int getalbumposition(Long p) {
        for (int i = 0; i < albumids.size(); i++) {
            if (albumids.get(i).equals(p)) {
                return i;
            }


        }

        return 0;

    }

    public static ArrayList<Integer> collectposition(Long l)
    {

        ArrayList<Integer> arr=new ArrayList<Integer>();
        for (int i = 0; i < albumids.size(); i++)
        {
            if (albumids.get(i).equals(l)) {
                arr.add(i);
            }


        }

        return arr;



    }

    public static ArrayList<Integer> collectposition(String l)
    {

        ArrayList<Integer> arr=new ArrayList<Integer>();
        for (int i = 0; i < artistname.size(); i++)
        {
            if (artistname.get(i).equals(l)) {
                arr.add(i);
            }


        }

        return arr;



    }


}
