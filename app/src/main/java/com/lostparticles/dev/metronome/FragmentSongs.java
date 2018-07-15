package com.lostparticles.dev.metronome;


import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import android.widget.ListView;
import android.widget.TextView;


import com.google.firebase.crash.FirebaseCrash;

import static android.content.Context.MODE_PRIVATE;


public class FragmentSongs extends Fragment
{
    public FragmentSongs()
    {

    }





    //private AutoCompleteTextView act;

    public static SongsAdapter aa;



    private Intent serint;

    SharedPreferences initiated;
    SharedPreferences.Editor in;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootview=inflater.inflate(R.layout.fragment_songs_listview,container,false);

        initiated=getActivity().getSharedPreferences("initiated",MODE_PRIVATE);

        in=initiated.edit();



        ListView lv=(ListView) rootview.findViewById(R.id.listview);

      //  act=(AutoCompleteTextView)rootview.findViewById(R.id.autocompleteword);





        FrameLayout fm=(FrameLayout)rootview.findViewById(R.id.frame);

        if(MusicService.allsongs.size()==0)
        {


            lv.setVisibility(View.GONE);

            TextView tv=new TextView(getActivity());
            tv.setText("NO SONGS FOUND");
            tv.setTextSize(50);

            FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            lp.setMargins(100,100,100,100);
            fm.addView(tv,lp);






        }

        else{
            lv.setVisibility(View.VISIBLE);

        }



         aa=new SongsAdapter(getActivity(),0);
        lv.setAdapter(aa);

     /*  act.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void afterTextChanged(Editable editable) {

               String str=act.getText().toString();
               aa.search(str);

           }
       });*/


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try {

                   // Toast.makeText(getActivity(),"musicservice size " +MusicService.allsongs.size()+"",Toast.LENGTH_LONG).show();

                    int a=aa.clickedId(i);

                   // Toast.makeText(getActivity(),"passed"+i+" clicked"+a,Toast.LENGTH_LONG).show();

                    MusicService.positionofcurrentsong =a ;

                    //Toast.makeText(getActivity(),"pos"+MusicService.positionofcurrentsong,Toast.LENGTH_LONG).show();
                    serint = new Intent(getActivity(), MusicService.class);

                    getActivity().startService(serint);

                    in.putString("origin","song");
                    in.commit();


                    /*MainActivity.barset(getActivity()); //maybe a memory leak situation,instead use weak refrences
                    MainActivity.barlayout.setVisibility(View.VISIBLE);*/



                } catch (Exception ex) {

                    FirebaseCrash.report(new Exception("Exception : "+ex));

                }


            }
        });









       // Toast.makeText(getActivity(),"inside fragment",Toast.LENGTH_LONG).show();

        return rootview;

    }



}