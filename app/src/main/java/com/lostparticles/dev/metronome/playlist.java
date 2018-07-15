package com.lostparticles.dev.metronome;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;


public class playlist extends Fragment {

    LinearLayout ll;
    SQLiteDatabase sb;

    ListView playlistlv;



    ArrayList<String> playlistname=new ArrayList<String>();


    public static playlistitemadapter aa;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootview =inflater.inflate(R.layout.fragment_playlist,container,false);

        ll=(LinearLayout)rootview.findViewById(R.id.playlist);

        playlistlv=(ListView)rootview.findViewById(R.id.playlistlv);


        sb = getActivity().openOrCreateDatabase("DATABASE",android.content.Context.MODE_PRIVATE ,null);

        try {
            String sql = "create table if not exists list(name varchar(40))";

            sb.execSQL(sql);

        }

        catch(Exception e)
        {
          //  Toast.makeText(getActivity(),"ERROR :"+e,Toast.LENGTH_LONG).show();

            FirebaseCrash.report(new Exception("Exception : "+e));

        }


        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder ab=new AlertDialog.Builder(getActivity());

                LayoutInflater inflator=getActivity().getLayoutInflater();

                View v=inflator.inflate(R.layout.playlist_et,null);

               final TextView tv=(TextView)v.findViewById(R.id.playet);

                ab.setView(v);

                ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                        String name=tv.getText().toString();

                        if(name.contains(" "))
                        {

                            name=name.replace(" ","lost");

                        }

                        try
                        {
                            String query = "insert into list values('" + name + "')";
                            sb.execSQL(query);


                            String sql = "create table if not exists " + name + "(ids long)";

                            sb.execSQL(sql);



                        }
                        catch (Exception e)
                        {
                          //  Toast.makeText(getActivity(),"ERROR :"+e,Toast.LENGTH_LONG).show();

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




        setdata();




        aa=new playlistitemadapter(getActivity(),playlistname);

        playlistlv.setAdapter(aa);


        playlistlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent in=new Intent(getActivity(),playlistdetails.class);

                in.putExtra("table",playlistname.get(i));

                startActivity(in);

            }
        });

        playlistlv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int ipos=i;
                AlertDialog.Builder ab=new AlertDialog.Builder(getActivity());
                ab.setTitle("Select Action :");

                CharSequence [] seq=new CharSequence[]{"DELETE"};

                ab.setItems(seq, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                        if(i==0)
                        {

                            try
                            {

                                //Toast.makeText(getActivity(),"inside longclick",Toast.LENGTH_LONG).show();
                                String query="DROP TABLE IF EXISTS "+playlistname.get(ipos);
                                sb.execSQL(query);

                                String qrr="delete from list where name='"+playlistname.get(ipos)+"'";
                                sb.execSQL(qrr);


                                playlistname.remove(ipos);
                                aa.notifyDataSetChanged();

                            }
                            catch(Exception e)
                            {

                                FirebaseCrash.report(new Exception("Exception : "+e));

                            }

                        }


                    }
                });


                ab.show();



                return true;
            }
        });













        return rootview;


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

            FirebaseCrash.report(new Exception("Exception : "+e));

          //  Toast.makeText(getActivity(),"ERROR :"+e,Toast.LENGTH_LONG).show();
        }


    }


}
