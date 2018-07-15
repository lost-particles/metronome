package com.lostparticles.dev.metronome;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;



public class ArtistAdapter extends ArrayAdapter {

    private ArrayList<String> al;

    @Override
    public int getCount() {


        return al.size();

    }

   private Activity cnt;

    public ArtistAdapter (Activity cnt)
    {
        super(cnt,0,0);

        this.cnt=cnt;

        al=new ArrayList<String>();
        al.addAll(MusicService.uniqueartistname);

    }

    public void search(String element)
    {
        String ele=element.toLowerCase();
        al.clear();
        if (element.length() == 0) {
            al.addAll(MusicService.uniqueartistname);
        }
        else
        {

            for(int i=0;i<MusicService.uniqueartistname.size();i++)
            {
                if(MusicService.uniqueartistname.get(i).toLowerCase().contains(ele))
                {
                    al.add(MusicService.uniqueartistname.get(i));

                }


            }


        }
        notifyDataSetChanged();

       /* Toast.makeText(cnt, al.size()+"",Toast.LENGTH_LONG).show();

        Toast.makeText(cnt,"musicservice size " +MusicService.uniqueartistname.size()+"",Toast.LENGTH_LONG).show();*/



    }
    public int clickedId(int p)
    {
        String str=al.get(p);

        for(int i=0;i<MusicService.uniqueartistname.size();i++)
        {
            if(MusicService.uniqueartistname.get(i).equals(str))
            {
                return i;
            }


        }

        return 0;

    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {




        View currentitem=convertView;

        if(currentitem==null)
        {
            LayoutInflater inflater=cnt.getLayoutInflater();
            currentitem=inflater.inflate(R.layout.artistlistviewitem,null);


        }

        TextView tv=(TextView) currentitem.findViewById(R.id.artistname);



        tv.setText(al.get(position));







        return currentitem;

    }
}
