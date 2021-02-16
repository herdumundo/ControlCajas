package com.example.ControlCajas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class informe_menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informe_menu);

    }



    @Override
    public void onBackPressed() {
        finish();
        Intent List = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(List);

    }



    public void ir_informe_maples(View view) {
         Intent i=new Intent(this, informe_registro_maples.class);
        startActivity(i);
    }

    public void ir_informe_cajas(View view) {
         Intent i=new Intent(this, informe_registro_cajas.class);
        startActivity(i);
    }







}