package com.lostparticles.dev.metronome;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;



public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.Myholder> {

    private LayoutInflater inflater;

    private Context act;

    private SharedPreferences themes;

    private ArrayList<Long> al;


    private MediaMetadataRetriever mmr=new MediaMetadataRetriever();

    public AlbumAdapter(Context cnt)
    {

        inflater=LayoutInflater.from(cnt);
        act=cnt;

        themes=act.getSharedPreferences("themes",MODE_PRIVATE);

        al=new ArrayList<Long>();
        for(int i=0;i<(int)MusicService.numberofalbums;i++)
        {
            al.add((long)i+1);
        }

    }

    public void search(String element)
    {
        String ele=element.toLowerCase();
        al.clear();
        if (element.length() == 0) {
            for(int i=0;i<(int)MusicService.numberofalbums;i++)
            {
                al.add((long)i+1);
            }

        }
        else
        {

            for(int i=0;i<MusicService.allsongs.size();i++)
            {
                if(MusicService.allsongs.get(i).toLowerCase().contains(ele))
                {
                    long l=MusicService.albumids.get(i);
                    if(!al.contains(l))
                    {
                        al.add(l);

                    }

                }


            }


        }
        notifyDataSetChanged();

       /* Toast.makeText(act, al.size()+"",Toast.LENGTH_LONG).show();

        Toast.makeText(act,"musicservice size " +MusicService.allsongs.size()+"",Toast.LENGTH_LONG).show();

        Toast.makeText(act,"musicservice albumids size " +MusicService.albumids.size()+"",Toast.LENGTH_LONG).show();*/



    }

    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view=inflater.inflate(R.layout.album_item,parent,false);

        Myholder holder=new Myholder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(Myholder holder, int position)
    {

        int pos=MusicService.getalbumposition(al.get(position));


        String arr []=MusicService.allsongs.get(pos).split("@@@");

        holder.artistname.setText(arr[2]);
        holder.albumname.setText(arr[5]);


        mmr.setDataSource(arr[4]);


        byte[] art = mmr.getEmbeddedPicture();


        if(art!=null) {

            Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);

            Bitmap scaledBitmap =MusicControl.scaleDown(songImage,200, true);

            int color=MusicControl.getDominantColor(songImage);
            songImage.recycle();


//

//            String str=String.format("#%06x", color.getRGB() & 0x00FFFFFF);


            holder.albumimage.setImageBitmap(scaledBitmap);
//            scaledBitmap.recycle();




            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);

            //  Toast.makeText(MusicControl.this,a+":"+r+":"+g+":"+b,Toast.LENGTH_LONG).show();

            if(r>230&&g>230&&b>230) {

                holder.ll.setBackgroundResource(R.color.materialgrey);

            }
            else
            {
                holder.ll.setBackgroundColor(color);

            }






            /*int dur=mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            sb.setMax(dur);*/


        }
        else
        {
            holder.albumimage.setImageResource(R.drawable.ic_album);
//

            holder.ll.setBackgroundResource(themes.getInt("coloraccent",R.color.colorAccentTeal));

        }




    }

    @Override
    public int getItemCount() {
        return al.size();
    }


public class Myholder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    TextView albumname,artistname;
    ImageView albumimage;
    LinearLayout ll;


   public Myholder(View currentitem)
   {
       super(currentitem);

       albumname=(TextView)currentitem.findViewById(R.id.albumname);
      artistname=(TextView)currentitem.findViewById(R.id.artistname);

       albumimage=(ImageView)currentitem.findViewById(R.id.albumimage);

        ll=(LinearLayout)currentitem.findViewById(R.id.albumpad);

       currentitem.setOnClickListener(this);


   }


    @Override
    public void onClick(View view) {

        int pos=(int)(long)al.get(getAdapterPosition());

      Intent i=new Intent(act,Album_Artist_Details.class);
        i.putExtra("pos",pos);
        i.putExtra("origin","album");



        act.startActivity(i);

    }
}

}
