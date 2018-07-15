package com.lostparticles.dev.metronome;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        TextView pp=(TextView)findViewById(R.id.privacypolicy);

        TextView tou=(TextView)findViewById(R.id.termsofuse);


        pp.setPaintFlags(pp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tou.setPaintFlags(pp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("https://lostparticlesdevs.wixsite.com/metronome");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);



            }
        });

        tou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Uri uri = Uri.parse("https://lostparticlesdevs.wixsite.com/metronome/terms-of-use");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);


            }
        });


    }
}
