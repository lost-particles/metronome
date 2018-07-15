package com.lostparticles.dev.metronome;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentAlbums extends Fragment {

    RecyclerView rv;

    public  static  AlbumAdapter aa;

 @Nullable
 @Override
 public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




     View rootview=inflater.inflate(R.layout.fragment_fragment_albums,container,false);

     rv=(RecyclerView)rootview.findViewById(R.id.recyclerview);

     aa=new AlbumAdapter(getActivity().getApplicationContext());

     rv.setAdapter(aa);

     GridLayoutManager gm=new GridLayoutManager(getActivity(),2,LinearLayoutManager.VERTICAL,false);

     rv.setLayoutManager(gm);


     return rootview;









 }

 public FragmentAlbums()
 {

 }







}