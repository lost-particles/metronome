package com.lostparticles.dev.metronome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class FragmentArtists extends Fragment
{

    ListView lv;
    public static ArtistAdapter aa;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        final View rootview=inflater.inflate(R.layout.fragment_fragment_artists,container,false);

        lv=(ListView) rootview.findViewById(R.id.artistlistview);


        aa=new ArtistAdapter(getActivity());

        lv.setAdapter(aa);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent in=new Intent(getActivity(),Album_Artist_Details.class);
                in.putExtra("pos",aa.clickedId(i));
                in.putExtra("origin","artist");


                startActivity(in);




            }
        });

        return rootview;


    }
}