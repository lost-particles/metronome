package com.lostparticles.dev.metronome;

import android.app.Activity;


import android.content.DialogInterface;
import android.database.Cursor;


import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;


public class SongsAdapter extends ArrayAdapter <String>{


   private SQLiteDatabase sb;

    private ArrayList<String>playlistname;








    @Override
    public int getCount() {
        return allsongs.size();
    }

    private Activity cnt;
   ArrayList<String> allsongs=new ArrayList<String>();


    private int code;
//    ArrayList<Bitmap>image=new ArrayList<Bitmap>();

    ArrayList<Long> alid=new ArrayList<Long>();

    ArrayList<Long> albumids=new ArrayList<Long>();



    public void search(String element)
    {
        String ele=element.toLowerCase();
        allsongs.clear();
        albumids.clear();
        if (element.length() == 0) {
            allsongs.addAll(MusicService.allsongs);
            albumids.addAll(MusicService.albumids);
        }
        else
        {

            for(int i=0;i<MusicService.allsongs.size();i++)
            {
                if(MusicService.allsongs.get(i).toLowerCase().contains(ele))
                {
                    allsongs.add(MusicService.allsongs.get(i));
                    albumids.add(MusicService.albumids.get(i));

                }


            }


        }
        notifyDataSetChanged();

       /* Toast.makeText(cnt, allsongs.size()+"",Toast.LENGTH_LONG).show();

        Toast.makeText(cnt,"musicservice size " +MusicService.allsongs.size()+"",Toast.LENGTH_LONG).show();*/



    }


    public SongsAdapter(Activity context,int code)
    {
        super(context,0,0);
        cnt=context;

        this.allsongs.addAll(MusicService.allsongs);
        this.code=code;

        albumids.addAll(MusicService.albumids);

        sb = cnt.openOrCreateDatabase("DATABASE", android.content.Context.MODE_PRIVATE, null);



    }



    public SongsAdapter(Activity context, ArrayList allsongs,int code,ArrayList<Long> alid)
    {
        super(context,0,0);
        cnt=context;

        this.allsongs=allsongs;
        this.code=code;

        sb = cnt.openOrCreateDatabase("DATABASE", android.content.Context.MODE_PRIVATE, null);


        this.alid=alid;



    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {



        View currentitem=convertView;

        if(currentitem==null)
        {
            LayoutInflater inflater=cnt.getLayoutInflater();
            currentitem=inflater.inflate(R.layout.song_item,null);



        }

        TextView tvname=(TextView)currentitem.findViewById(R.id.tvname);
        TextView tvalbum=(TextView)currentitem.findViewById(R.id.tvalbum);
        TextView tvtime=(TextView)currentitem.findViewById(R.id.tvtime);
        ImageView imgart=(ImageView)currentitem.findViewById(R.id.art);


        ImageButton v=(ImageButton)currentitem.findViewById(R.id.overflow);

       final String arr[]=allsongs.get(position).split("@@@");




        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(code==0) {

                   // Toast.makeText(cnt, ""+code, Toast.LENGTH_SHORT).show();


                    PopupMenu popup = new PopupMenu(cnt, view);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.popup_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.addtoplaylist:

                                   // Toast.makeText(cnt, "" + position, Toast.LENGTH_LONG).show();

                                    AlertDialog.Builder ab = new AlertDialog.Builder(cnt);

                                    LayoutInflater inflator = cnt.getLayoutInflater();

                                    View v = inflator.inflate(R.layout.fragment_playlist, null);
                                    ListView lv = (ListView) v.findViewById(R.id.playlistlv);

                                    playlistname = new ArrayList<String>();




                                    setdata();


                                    final playlistitemadapter aa = new playlistitemadapter(cnt,playlistname);

                                    lv.setAdapter(aa);


                                    LinearLayout ll = (LinearLayout) v.findViewById(R.id.playlist);

                                    ll.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            AlertDialog.Builder ab = new AlertDialog.Builder(cnt);

                                            LayoutInflater inflator = cnt.getLayoutInflater();

                                            View v = inflator.inflate(R.layout.playlist_et, null);

                                            final TextView tv = (TextView) v.findViewById(R.id.playet);

                                            ab.setView(v);

                                            ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {


                                                    String name = tv.getText().toString();

                                                    try {
                                                        String query = "insert into list values('" + name + "')";
                                                        sb.execSQL(query);

                                                        String sql = "create table if not exists " + name + "(ids long)";

                                                        sb.execSQL(sql);


                                                    } catch (Exception e) {
                                                       // Toast.makeText(cnt, "ERROR :" + e, Toast.LENGTH_LONG).show();

                                                        FirebaseCrash.report(new Exception("Exception : "+e));

                                                    }


                                                    setdata();
                                                    aa.notifyDataSetChanged();

                                                }
                                            });

                                            ab.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            });


                                            ab.show();

                                        }
                                    });


                                    ab.setView(v);


                                    final AlertDialog ad = ab.show();

                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                                            String qr = "insert into " + playlistname.get(i) + " values('" + MusicService.ids.get(position) + "')";
                                            sb.execSQL(qr);

                                            ad.dismiss();

                                            Toast.makeText(cnt, "Added to " + playlistname.get(i), Toast.LENGTH_LONG).show();


                                        }
                                    });

                                    return true;
                                case R.id.details:

                                    AlertDialog.Builder dialogdetails=new AlertDialog.Builder(cnt);
                                    dialogdetails.setTitle("Details");

                                    LayoutInflater inflator2=cnt.getLayoutInflater();
                                    View detailsview =inflator2.inflate(R.layout.details,null);

                                    TextView title=(TextView)detailsview.findViewById(R.id.tv1);
                                    TextView artist=(TextView)detailsview.findViewById(R.id.tv2);
                                    TextView album=(TextView)detailsview.findViewById(R.id.tv3);
                                    TextView duration=(TextView)detailsview.findViewById(R.id.tv4);
                                    TextView path=(TextView)detailsview.findViewById(R.id.tv5);

                                    title.setText(arr[1]);
                                    artist.setText(arr[2]);
                                    album.setText(arr[5]);
                                    int dur=Integer.parseInt(arr[3])/1000;
                                    String sec=dur+" sec";
                                    duration.setText(sec);
                                    path.setText(arr[4]);

                                    dialogdetails.setView(detailsview);



                                    dialogdetails.show();

                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });

                    popup.show();


                }
                else if(code==1)
                {
                   // Toast.makeText(cnt, ""+code, Toast.LENGTH_SHORT).show();

                    PopupMenu popup = new PopupMenu(cnt, view);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.popup_menu_playlist, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.remove:



                                    try
                                    {



                                        String query="delete from "+playlistdetails.table+" where ids='"+arr[0]+"'";
                                        sb.execSQL(query);

                                        //Toast.makeText(cnt,"inside remove",Toast.LENGTH_LONG).show();

                                        playlistdetails.listupdate(position,cnt);



                                    }
                                    catch(Exception e)
                                    {


                                        FirebaseCrash.report(new Exception("Exception : "+e));

                                    }


                                    return  true;

                                case R.id.details:


                                    AlertDialog.Builder dialogdetails=new AlertDialog.Builder(cnt);
                                    dialogdetails.setTitle("Details");

                                    LayoutInflater inflator2=cnt.getLayoutInflater();
                                    View detailsview =inflator2.inflate(R.layout.details,null);

                                    TextView title=(TextView)detailsview.findViewById(R.id.tv1);
                                    TextView artist=(TextView)detailsview.findViewById(R.id.tv2);
                                    TextView album=(TextView)detailsview.findViewById(R.id.tv3);
                                    TextView duration=(TextView)detailsview.findViewById(R.id.tv4);
                                    TextView path=(TextView)detailsview.findViewById(R.id.tv5);

                                    title.setText(arr[1]);
                                    artist.setText(arr[2]);
                                    album.setText(arr[5]);
                                    int dur=Integer.parseInt(arr[3])/1000;

                                    String sec=dur+" sec";
                                    duration.setText(sec);
                                    path.setText(arr[4]);

                                    dialogdetails.setView(detailsview);



                                    dialogdetails.show();


                                    return true;

                                default:return false;


                            }
                        }
                    });


                    popup.show();

                }

            }
        });






        tvname.setText(arr[1]);


        tvalbum.setText(arr[2]);

        int duration=Integer.parseInt(arr[3])/1000;
        String sec;

        if(duration%60<10)
        {

            sec="0"+duration%60;
        }
        else
        {
            sec=duration%60+"";

        }
        tvtime.setText(duration/60+":"+sec);


        Cursor cursorAlbum;


    if(code==0) {

            cursorAlbum = getContext().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID + "=" + albumids.get(position), null, null);


     }
        else
        {





            cursorAlbum = getContext().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID + "=" + alid.get(position), null, null);

        }
        if(cursorAlbum != null  && cursorAlbum.moveToFirst())
        {

            String uri = cursorAlbum.getString(cursorAlbum.getColumnIndex("album_art"));
            cursorAlbum.close();
            if(uri != null )
            {
                imgart.setImageURI(Uri.parse(uri));
            }

            else
            {
                imgart.setImageResource(R.drawable.ic_album);

            }
        }


       /* if(image.get(position)!=null)
        {
            imgart.setImageBitmap(image.get(position));


        }*/


        return currentitem;









    }

    private void setdata ()
    {
        try {
            if(playlistname!=null) {
                playlistname.clear();

            }
            String sql = "select * from list";
            Cursor cr = sb.rawQuery(sql, null);
            cr.moveToFirst();
            while (cr.isAfterLast() == false) {
                String name = cr.getString(0);
                playlistname.add(name);
                cr.moveToNext();
            }
            cr.close();

        }
        catch (Exception e)
        {
           // Toast.makeText(cnt,"ERROR :"+e,Toast.LENGTH_LONG).show();

            FirebaseCrash.report(new Exception("Exception : "+e));


        }


    }

    public int clickedId(int p)
    {
        String arr[]=allsongs.get(p).split("@@@");

        return MusicService.getcurrentposition(Long.parseLong(arr[0]));


    }



}
