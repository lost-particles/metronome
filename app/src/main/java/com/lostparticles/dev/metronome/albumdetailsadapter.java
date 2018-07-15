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



public class albumdetailsadapter extends ArrayAdapter {
   private Activity act;
    private ArrayList<Integer> pos=new ArrayList<Integer>();

    public albumdetailsadapter (Activity act, ArrayList pos)
    {
        super(act,0,0);

        this.act=act;
        this.pos=pos;

    }

    @Override
    public int getCount() {

        return pos.size();


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {



        View currentitem=convertView;

        if(currentitem==null)
        {
            LayoutInflater inflater=act.getLayoutInflater();
            currentitem=inflater.inflate(R.layout.albumdetailslistviewitem,null);



        }


        TextView tv=(TextView) currentitem.findViewById(R.id.textView);

        TextView tv2=(TextView) currentitem.findViewById(R.id.num);

        tv2.setText(position+1+" .");

        String arr[]=MusicService.allsongs.get(pos.get(position)).split("@@@");

        tv.setText(arr[1]);




        return currentitem;

    }
}
