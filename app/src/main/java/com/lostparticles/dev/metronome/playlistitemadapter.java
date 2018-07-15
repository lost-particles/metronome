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



public class playlistitemadapter extends ArrayAdapter<String> {

    private Activity act;

    private ArrayList<String> al=new ArrayList<String>();
    private ArrayList<String> entireplaylist=new ArrayList<String>();

    public playlistitemadapter(Activity act, ArrayList<String> al)
    {

        super(act,0,al);

        this.act=act;
        this.al=al;
        entireplaylist.addAll(al);
    }

    public void search(String element)
    {
        String ele=element.toLowerCase();
        if(ele.contains(" "))
        {

            ele=ele.replace(" ","lost");

        }
        al.clear();
        if (element.length() == 0) {
            al.addAll(entireplaylist);
        }
        else
        {

            for(int i=0;i<entireplaylist.size();i++)
            {
                if(entireplaylist.get(i).toLowerCase().contains(ele))
                {
                    al.add(entireplaylist.get(i));

                }


            }


        }
        notifyDataSetChanged();

        /*Toast.makeText(act, al.size()+"",Toast.LENGTH_LONG).show();

        Toast.makeText(act,"entirelist size " +entireplaylist.size()+"",Toast.LENGTH_LONG).show();*/



    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View currentitem=convertView;

        if(currentitem==null)
        {
            LayoutInflater inflater=act.getLayoutInflater();
            currentitem=inflater.inflate(R.layout.textview,null);

        }


        TextView tv=(TextView)currentitem.findViewById(R.id.text1);

        String st;

        if(al.get(position).contains("lost"))
        {
            st=al.get(position);

            st=st.replace("lost"," ");

        }
        else
        {
            st=al.get(position);

        }


        tv.setText(st);


        return currentitem;

    }
}
