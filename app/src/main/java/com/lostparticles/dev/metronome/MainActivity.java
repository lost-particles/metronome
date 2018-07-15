package com.lostparticles.dev.metronome;

import android.app.ActivityManager;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.ContentResolver;

import android.content.Context;
import android.content.Intent;


import android.content.SharedPreferences;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;


import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

import java.util.Collections;
import java.util.Date;




public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    @Override
    protected void onPause() {
        super.onPause();

    MusicService.ismainvisivble=false;

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(checkCallingOrSelfPermission("android.permission.READ_EXTERNAL_STORAGE")==getPackageManager().PERMISSION_GRANTED) {

            MusicService.ismainvisivble=true;




        if(themes.getBoolean("themechanged",false))
        {
            th.putBoolean("themechanged",false);
            th.commit();
            setTheme(themes.getInt("curtheme",R.style.AppTheme));

            recreate();


        }


            if(!states.getBoolean("firsttime",true)) {

                  barset(MainActivity.this);

            }
        }
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();

       /* barplaypause=null;

        baralbum=null;
         barlayout=null;

        tablayout=null;
*/


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        switch (requestCode) {
            case 1057: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == getPackageManager().PERMISSION_GRANTED) {

                   // Toast.makeText(MainActivity.this,"YAY",Toast.LENGTH_LONG).show();

                    finish();

                    Intent i=new Intent(MainActivity.this,MainActivity.class);
                    startActivity(i);






                } else {




                       Snackbar snack=Snackbar.make(findViewById(android.R.id.content),getResources().getString(R.string.app_name)+" needs STORAGE PERMISSION to scan for music files.",Snackbar.LENGTH_INDEFINITE);

                        snack.setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                if (Build.VERSION.SDK_INT >= 23) {

                                    if (shouldShowRequestPermissionRationale("android.permission.READ_EXTERNAL_STORAGE")) {

                                        requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1057);

                                    }
                                    else
                                    {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", getPackageName(), null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);

                                    }

                                    } else {

                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", getPackageName(), null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);


                                    }


                            }
                        });



                        snack.show();





                }
            }


        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    private static TextView  tv1,tv2;

    private static ImageView baralbum;
    private static ImageView barplaypause;

    public static LinearLayout barlayout;

    private Intent serint;

    private static SharedPreferences states;
    SharedPreferences.Editor e2;

    static SharedPreferences themes;
    SharedPreferences.Editor th;

   // static  ArrayList<Long>image;

    private FirebaseAnalytics mFirebaseAnalytics;





    private CountDownTimer counttimer;

    public static TabLayout tablayout;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bitmap bm = BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher);

        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(getResources().getString(R.string.app_name),bm,ContextCompat.getColor(this,R.color.black));
        (this).setTaskDescription(taskDescription);

        if(checkCallingOrSelfPermission("android.permission.READ_EXTERNAL_STORAGE")==getPackageManager().PERMISSION_DENIED)
        {

            if (Build.VERSION.SDK_INT >= 23) {

                requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1057);


            }
            else {

                Snackbar snack=Snackbar.make(findViewById(android.R.id.content),getResources().getString(R.string.app_name)+" needs STORAGE PERMISSION to scan for music files.",Snackbar.LENGTH_INDEFINITE);

                snack.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                });

                snack.show();
            }

        }



        if(checkCallingOrSelfPermission("android.permission.READ_EXTERNAL_STORAGE")==getPackageManager().PERMISSION_GRANTED)

        {

            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


            Bundle b=new Bundle();

            long dtMili = System.currentTimeMillis();
            Date dt = new Date(dtMili);


            b.putString(FirebaseAnalytics.Param.START_DATE,dt.toString());

            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN,b);


            themes=getSharedPreferences("themes",MODE_PRIVATE);

            th=themes.edit();


            setTheme(themes.getInt("curtheme",R.style.AppTheme));





            setContentView(R.layout.activity_main);


           // image = new ArrayList<Long>();

            tv1 = (TextView) findViewById(R.id.sngname);

            tv2 = (TextView) findViewById(R.id.sngartist);

            baralbum = (ImageView) findViewById(R.id.baralbum);

            barplaypause = (ImageView) findViewById(R.id.barplaypause);

            barlayout = (LinearLayout) findViewById(R.id.barlayout);

            Toolbar toolbar = (Toolbar) findViewById(R.id.search_edit_frame);
            setSupportActionBar(toolbar);

            toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.timer));

            toolbar.setBackgroundResource(themes.getInt("colorprimary",R.color.colorPrimaryTeal));


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);





            SearchView searchView = (SearchView) findViewById(R.id.search);
// Sets searchable configuration defined in searchable.xml for this SearchView
            SearchManager searchManager =
                    (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


                @Override
                public boolean onQueryTextSubmit(String query) {








                    int tabpos=tablayout.getSelectedTabPosition();

                    if(tabpos==0)
                    {

                        FragmentSongs.aa.search(query);

                    }
                    else if(tabpos==1)
                    {
                        FragmentAlbums.aa.search(query);

                    }
                    else if(tabpos==2)
                    {

                        FragmentArtists.aa.search(query);


                    }
                    else if(tabpos==3)
                    {

                        playlist.aa.search(query);

                    }




                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {



                    int tabpos=tablayout.getSelectedTabPosition();

                    if(tabpos==0)
                    {

                        FragmentSongs.aa.search(newText);

                    }
                    else if(tabpos==1)
                    {
                        FragmentAlbums.aa.search(newText);

                    }
                    else if(tabpos==2)
                    {

                        FragmentArtists.aa.search(newText);


                    }
                    else if(tabpos==3)
                    {

                        playlist.aa.search(newText);

                    }




                    return true;
                }
            });




            states = getSharedPreferences("states", MODE_PRIVATE);
            e2 = states.edit();




            if(MusicService.allsongs.size()==0) {


                scan();

            }

            if(states.getBoolean("firsttime",true))
            {
                barlayout.setVisibility(View.GONE);




                FirebaseCrash.report(new Exception("APP INSTALLED AND PERMISSION GRANTED"));




            }

            else
            {

                try {
                  //  Toast.makeText(MainActivity.this,"inside else",Toast.LENGTH_LONG).show();

                   /* MusicService.positionofcurrentsong=states.getInt("songpos",0);
*/

                   if(!states.getBoolean("servicerunning",false))
                   {
                       MusicService.positionofcurrentsong=states.getInt("songpos",0);

                   }
                    barset(MainActivity.this);

                }
                catch (Exception e)
                {

                   // Toast.makeText(MainActivity.this,"EXCEPTION : "+e,Toast.LENGTH_LONG).show();

                    FirebaseCrash.report(new Exception("Exception : "+e));
                }
            }

/*
            e2.putBoolean("firsttime",true);
            e2.commit();*/


       /* mmr = new MediaMetadataRetriever();
        MusicService.allsongs=fetch(Environment.getExternalStorageDirectory());

        for(int j=0;j<10;j++) {


            mmr.setDataSource(MusicService.allsongs.get(j).toString());


            byte[] art = mmr.getEmbeddedPicture();


            if(art!=null) {

                Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);

                Bitmap scaledBitmap = scaleDown(songImage,200, true);


                songImage.recycle();

                image.add(scaledBitmap);

//                scaledBitmap.recycle();


            }
            else
            {
                image.add(null);

            }


            String albumstr= mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);

            album.add(albumstr);


        }

        mmr.release();*/


            //Toast.makeText(MainActivity.this,"before view pager",Toast.LENGTH_LONG).show();

           ViewPager vp=(ViewPager)findViewById(R.id.container);

            pageradapter pa=new pageradapter(getSupportFragmentManager());

            vp.setAdapter(pa);

            tablayout=(TabLayout)findViewById(R.id.tabs);

            tablayout.setupWithViewPager(vp);



           // Toast.makeText(MainActivity.this,"after view pager",Toast.LENGTH_LONG).show();

          /*  barlayout.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this){


                @Override
                public void onSwipeUp() {
                    super.onSwipeUp();


                    if(!states.getBoolean("servicerunning",false))
                    {
                        serint = new Intent(MainActivity.this, MusicService.class);

                        startService(serint);
                        Toast.makeText(MainActivity.this,"inside intent call to musicservice",Toast.LENGTH_LONG).show();

                        Log.v("intent call","yes");


                    }

                    Intent in = new Intent(MainActivity.this, MusicControl.class);
*//*
                    Pair<View, String> p1 = Pair.create((View) baralbum, "albumpic");
                    Pair<View, String> p2 = Pair.create((View) barplaypause, "playpause");

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,p1,p2);*//*
                    startActivity(in);









                }
            });
*/



            barlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!states.getBoolean("servicerunning",false))
                    {
                        serint = new Intent(MainActivity.this, MusicService.class);

                        startService(serint);
                       // Toast.makeText(MainActivity.this,"inside intent call to musicservice",Toast.LENGTH_LONG).show();

                        Log.v("intent call","yes");


                    }

                    Intent in = new Intent(MainActivity.this, MusicControl.class);

                    Pair<View, String> p1 = Pair.create((View) baralbum, "albumpic");
                    Pair<View, String> p2 = Pair.create((View) barplaypause, "playpause");

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,p1,p2);
                    startActivity(in, options.toBundle());

                }
            });

            barplaypause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(MusicService.mp==null)
                    {

                        Intent intent=new Intent(MainActivity.this,MusicService.class);

                        startService(intent);


                    }
                    else {


                        if (MusicService.mp.isPlaying()) {
                            MusicService.mp.pause();
                            MusicService.am.abandonAudioFocus(MusicService.focuschangelistener);
                            barplaypause.setImageResource(R.drawable.barplay);


                            MusicService.sendnotification(MainActivity.this);

                        } else {

                            int result = MusicService.am.requestAudioFocus(MusicService.focuschangelistener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

                            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {


                                MusicService.mp.start();

                                barplaypause.setImageResource(R.drawable.barpause);

                                if (states.getBoolean("servicerunning", false)) {

                                    MusicService.sendnotification(MainActivity.this);

                                }

                            }


                        }


                    }

                }
            });

        }

    }

   /* private static ArrayList<File> fetch(File root)
    {
        ArrayList<File> songs=new ArrayList<File>();
        File files[]=root.listFiles();

        for(int i=0;i<files.length;i++)
        {
            if(files[i].isDirectory()&& !files[i].isHidden())
            {
               songs.addAll(fetch(files[i]));

            }
            else
            {
                if(files[i].getName().endsWith(".mp3"))
                {
                    songs.add(files[i]);

                }

            }

        }

        return songs;
*/




   public static void barset(Context cnt)
   {

       if(!states.getBoolean("firsttime",true)) {

           barlayout.setBackgroundResource(themes.getInt("coloraccent", R.color.colorAccentTeal));


           String arr[] = MusicService.allsongs.get(MusicService.positionofcurrentsong).split("@@@");


           tv1.setText(arr[1]);


           tv2.setText(arr[2]);


           Cursor cursorAlbum = cnt.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                   new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID + "=" + MusicService.albumids.get(MusicService.positionofcurrentsong), null, null);

           if (cursorAlbum != null && cursorAlbum.moveToFirst()) {

               String uri = cursorAlbum.getString(cursorAlbum.getColumnIndex("album_art"));
               cursorAlbum.close();
               if (uri != null) {
                   baralbum.setImageURI(Uri.parse(uri));
               } else {
                   baralbum.setImageResource(R.drawable.ic_album);

               }

           }


           if (states.getBoolean("servicerunning", false)) {

               if (MusicService.mp != null) {
                   if (MusicService.mp.isPlaying()) {
                       barplaypause.setImageResource(R.drawable.barpause);

                   } else {
                       barplaypause.setImageResource(R.drawable.barplay);

                   }


               }

           } else {
               barplaypause.setImageResource(R.drawable.barplay);

           }


       }


   }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {


            TimePickerDialog tpd=new TimePickerDialog(this,new timelistener(), 3,15,false);
            tpd.setMessage("Music Playback will stop at :");

            tpd.show();




            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.settings)
        {


            Intent i=new Intent(MainActivity.this,settings.class);
            startActivity(i);

        } else if (id == R.id.rescan)
        {

            MusicService.allsongs.clear();
            MusicService.ids.clear();
            MusicService.albumids.clear();
            MusicService.artistname.clear();
            MusicService.uniqueartistname.clear();

            MusicService.numberofalbums=0;

            scan();

            Toast.makeText(MainActivity.this,"Scan Completed",Toast.LENGTH_LONG).show();



        } else if (id == R.id.about)
        {

            Intent newintent=new Intent(MainActivity.this,About.class);

            startActivity(newintent);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private class timelistener implements TimePickerDialog.OnTimeSetListener
    {


        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {




            long dtMili = System.currentTimeMillis();
            Date dt = new Date(dtMili);

            String arr[]=dt.toString().split(" ");
            String mil[]=arr[3].split(":");
            long diff=(i*3600+i1*60)*1000-(Integer.parseInt(mil[0])*3600+Integer.parseInt(mil[1])*60+Integer.parseInt(mil[2]))*1000;




            if(diff<0)
            {


                diff=24*3600*1000+diff;

               // Toast.makeText(MainActivity.this,""+diff,Toast.LENGTH_LONG).show();

            }

          //  Toast.makeText(MainActivity.this,""+diff/(1000*60),Toast.LENGTH_LONG).show();


             Toast.makeText(MainActivity.this,"Music Playback will stop at : "+i+":"+i1,Toast.LENGTH_LONG).show();




            if(counttimer!=null) {

                counttimer.cancel();

            }
            counttimer= new CountDownTimer(diff, 100000)
            {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish()
                {
                    if(MusicService.mp!=null) {

                        MusicService.mp.pause();
                        MusicService.am.abandonAudioFocus(MusicService.focuschangelistener);

                        MusicService.sendnotification(App.getContext());

                        if(MusicService.isvisible==1)
                        {
                            MusicControl.setplaypausestate();

                        }
                        if(MusicService.ismainvisivble)
                        {

                            MainActivity.barset(App.getContext());

                        }


                    }
                }
            };

            counttimer.start();



        }
    }

    private void scan()

    {


        try {

            ContentResolver musicResolver = getContentResolver();
            Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor musicCursor = musicResolver.query(musicUri, null, null, null, MediaStore.Audio.Media.TITLE + " ASC");
            //iterate over results if valid
            if (musicCursor != null && musicCursor.moveToFirst()) {
                //get columns
                int titleColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.TITLE);
                int idColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media._ID);
                int artistColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.ARTIST);
                int albumId = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.ALBUM_ID);
                int data = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                int album = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                int dur = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
                //add songs to list
                do {
                    long thisId = musicCursor.getLong(idColumn);
                    String thisTitle = musicCursor.getString(titleColumn);
                    String thisArtist = musicCursor.getString(artistColumn);
                    long thisalbumId = musicCursor.getLong(albumId);
                    String thisdata = musicCursor.getString(data);
                    String Album = musicCursor.getString(album);
                    String duration = musicCursor.getString(dur);


                    // Log.d("Tit+ID+ART+ALB ID+DATA",thisTitle+"\n"+thisId+"\n"+thisArtist+"\n"+thisalbumId+"\n"+thisdata+"\n"+album+"\n"+duration);

                    MusicService.allsongs.add(thisId + "@@@" + thisTitle + "@@@" + thisArtist + "@@@" + duration + "@@@" + thisdata + "@@@" + Album);
                    //  image.add(thisalbumId);

                    MusicService.ids.add(thisId);
                    MusicService.albumids.add(thisalbumId);

                    MusicService.artistname.add(thisArtist);

                    if (!MusicService.uniqueartistname.contains(thisArtist)) {
                        MusicService.uniqueartistname.add(thisArtist);

                    }

                    Collections.sort(MusicService.uniqueartistname, String.CASE_INSENSITIVE_ORDER);

                    if (thisalbumId > MusicService.numberofalbums) {

                        MusicService.numberofalbums = thisalbumId;

                    }


                }
                while (musicCursor.moveToNext());

            }

            musicCursor.close();

        } catch (Exception ex) {

            FirebaseCrash.report(new Exception("Exception : " + ex));

        }


    }

    }







