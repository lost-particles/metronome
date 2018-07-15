package com.lostparticles.dev.metronome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;


import com.google.firebase.crash.FirebaseCrash;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by nEW u on 27-07-2017.
 */

public class broadcast_receiver extends BroadcastReceiver {


    SharedPreferences sp;

    SharedPreferences initiated;
    SharedPreferences.Editor in;
    @Override
    public void onReceive(Context act, Intent intent) {

        sp=act.getSharedPreferences("repeatstate",MODE_PRIVATE);


        initiated=act.getSharedPreferences("initiated",MODE_PRIVATE);

        in=initiated.edit();


       String str=intent.getExtras().getString("clickevent");

        if(str.equals("prev"))
        {



            if(initiated.getString("origin","song").equals("song"))
            {

                MusicService.positionofcurrentsong--;


                if(MusicService.positionofcurrentsong<0)
                {


                    if(sp.getString("repeat", "NONE").equals("ALL")||sp.getString("repeat", "NONE").equals("ONE"))
                    {
                        MusicService.positionofcurrentsong=MusicService.allsongs.size()-1;

                    }
                    else
                    {

                        MusicService.positionofcurrentsong++;

                    }



                }




            }
            else if(initiated.getString("origin","song").equals("artist")||initiated.getString("origin","song").equals("album"))
            {

                int counter=initiated.getInt("positioninlist",MusicService.positionofcurrentsong);

                counter--;

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





            act.startService(new Intent(act,MusicService.class));


        }

        else if(str.equals("next"))
        {





            if(initiated.getString("origin","song").equals("song"))
            {

                MusicService.positionofcurrentsong++;

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

                counter++;

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





            act.startService(new Intent(act,MusicService.class));
        }

        else if(str.equals("playpause"))
        {

            if(MusicService.mp.isPlaying())
            {
                MusicService.mp.pause();
                MusicService.am.abandonAudioFocus(MusicService.focuschangelistener);


                MusicService.sendnotification(act);
            }
            else
            {

                int result=MusicService.am.requestAudioFocus(MusicService.focuschangelistener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {


                    MusicService.mp.start();

                    MusicService.sendnotification(act);




                }





            }





        }
        else if(str.equals("close"))
        {

            try {

              /*  if (!MusicService.mp.isPlaying()) {


                }*/
                act.stopService(new Intent(act, MusicService.class));


            }

            catch (Exception e)
            {

                FirebaseCrash.report(new Exception("Exception : "+e));

            }

        }

        if(MusicService.isvisible==1)
        {

            Intent in = new Intent(act, MusicControl.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            act.startActivity(in);


        }

        if(MusicService.ismainvisivble)
        {
           MainActivity.barset(act);

        }



    }
}
