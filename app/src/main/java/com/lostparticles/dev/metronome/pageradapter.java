package com.lostparticles.dev.metronome;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



public class pageradapter extends FragmentPagerAdapter
{
    @Override
    public CharSequence getPageTitle(int position) {


        switch (position)
        {

            case 0:return "SONGS";

            case 1:return "ALBUMS";

            case 2:return"ARTISTS";

            case 3:return"PLAYLISTS";




        }


        return  null;

    }

    public pageradapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {



        switch (position)
        {

            case 0:return new FragmentSongs();

            case 1:return new FragmentAlbums();

            case 2:return new FragmentArtists();

            case 3:return new playlist();




        }

       return null;



    }




}
