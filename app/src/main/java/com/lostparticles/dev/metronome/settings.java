package com.lostparticles.dev.metronome;


import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class settings extends AppCompatActivity {

    LinearLayout llprimarycolor;

    SharedPreferences themes;

    SharedPreferences.Editor th;

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bitmap bm = BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher);

        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(getResources().getString(R.string.app_name),bm, ContextCompat.getColor(this,R.color.black));
        (this).setTaskDescription(taskDescription);


        themes=getSharedPreferences("themes",MODE_PRIVATE);

        th=themes.edit();

        setTheme(themes.getInt("curtheme",R.style.AppTheme));


        setContentView(R.layout.activity_settings);




      llprimarycolor=(LinearLayout)findViewById(R.id.llprimarycolor);

        tv=(TextView)findViewById(R.id.primarycolor);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setBackgroundResource(themes.getInt("colorprimary",R.color.colorPrimaryTeal));


        setprimarycolor();







        llprimarycolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog.Builder ab=new AlertDialog.Builder(settings.this);


                ab.setTitle("COLORS");

                LayoutInflater inflator=settings.this.getLayoutInflater();

                View v=inflator.inflate(R.layout.coloritem,null);

                ImageView color1=(ImageView)v.findViewById(R.id.color1);
                ImageView color2=(ImageView)v.findViewById(R.id.color2);

                ImageView color3=(ImageView)v.findViewById(R.id.color3);

                ImageView color4=(ImageView)v.findViewById(R.id.color4);
                ImageView color5=(ImageView)v.findViewById(R.id.color5);

                ImageView color6=(ImageView)v.findViewById(R.id.color6);

                color1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        th.putInt("curtheme",R.style.BlueGrey);
                        th.putBoolean("themechanged",true);

                        th.putInt("colorprimary",R.color.colorPrimary);
                        th.putInt("colorprimarydark",R.color.colorPrimaryDark);
                        th.putInt("coloraccent",R.color.colorAccent);
                        th.putInt("backcolor",R.color.backcolor);

                        th.commit();

                        finish();
                        startActivity(new Intent(settings.this,settings.class));


                    }
                });


                color2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        th.putInt("curtheme",R.style.Lime);
                        th.putBoolean("themechanged",true);


                        th.putInt("colorprimary",R.color.colorPrimaryLime);
                        th.putInt("colorprimarydark",R.color.colorPrimaryDarkLime);
                        th.putInt("coloraccent",R.color.colorAccentLime);
                        th.putInt("backcolor",R.color.backcolorLime);




                        th.commit();

                        finish();
                        startActivity(new Intent(settings.this,settings.class));


                    }
                });


                color3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        th.putInt("curtheme",R.style.Red);
                        th.putBoolean("themechanged",true);

                        th.putInt("colorprimary",R.color.colorPrimaryRed);
                        th.putInt("colorprimarydark",R.color.colorPrimaryDarkRed);
                        th.putInt("coloraccent",R.color.colorAccentRed);
                        th.putInt("backcolor",R.color.backcolorRed);


                        th.commit();

                        finish();
                        startActivity(new Intent(settings.this,settings.class));


                    }
                });

                color4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        th.putInt("curtheme",R.style.Pink);
                        th.putBoolean("themechanged",true);

                        th.putInt("colorprimary",R.color.colorPrimaryPink);
                        th.putInt("colorprimarydark",R.color.colorPrimaryDarkPink);
                        th.putInt("coloraccent",R.color.colorAccentPink);
                        th.putInt("backcolor",R.color.backcolorPink);


                        th.commit();

                        finish();
                        startActivity(new Intent(settings.this,settings.class));

                    }
                });


                color5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        th.putInt("curtheme",R.style.Purple);
                        th.putBoolean("themechanged",true);

                        th.putInt("colorprimary",R.color.colorPrimaryPurple);
                        th.putInt("colorprimarydark",R.color.colorPrimaryDarkPurple);
                        th.putInt("coloraccent",R.color.colorAccentPurple);
                        th.putInt("backcolor",R.color.backcolorPurple);


                        th.commit();

                        finish();
                        startActivity(new Intent(settings.this,settings.class));

                    }
                });


                color6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        th.putInt("curtheme",R.style.AppTheme);
                        th.putBoolean("themechanged",true);

                        th.putInt("colorprimary",R.color.colorPrimaryTeal);
                        th.putInt("colorprimarydark",R.color.colorPrimaryDarkTeal);
                        th.putInt("coloraccent",R.color.colorAccentTeal);
                        th.putInt("backcolor",R.color.backcolorTeal);


                        th.commit();


                        finish();
                        startActivity(new Intent(settings.this,settings.class));


                    }
                });



                ab.setView(v);



                ab.show();





            }
        });


    }

    private void setprimarycolor()
    {

        int i=themes.getInt("curtheme",R.style.AppTheme);

        if(i==R.style.BlueGrey)
        {
            tv.setText("Blue Grey");

        }
        else if(i==R.style.AppTheme)
        {
           tv.setText("Teal");

        }
        else if(i==R.style.Purple)
        {
            tv.setText("Purple");

        }
        else if(i==R.style.Pink)
        {
            tv.setText("Pink");

        }
        else if(i==R.style.Red)
        {

            tv.setText("Orange");
        }
        else if(i==R.style.Lime)
        {
            tv.setText("Lime");

        }



    }

}
