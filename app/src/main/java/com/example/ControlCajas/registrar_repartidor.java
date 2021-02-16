package com.example.ControlCajas;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import Utilidades.Utilidades;

public class registrar_repartidor extends AppCompatActivity {
    EditText nombre;
    ConexionSQLiteHelper conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_repartidor);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
        nombre= (EditText) findViewById(R.id.txt_nombre);
     }

    public void registrar_repar(View view)
    {
        registrar_repartidor();
    }

    private void registrar_repartidor()
    {
        SQLiteDatabase db=conn.getWritableDatabase();
        Integer id=0;
        Integer band=0;
        Cursor cursor = db.rawQuery("select case  when count(*) = 0 then 1 else max(cod_repartidor) +1  end as d from repartidores", null);
        while (cursor.moveToNext())
        {
            id = cursor.getInt(0);
        }

            ContentValues values=new ContentValues();
            values.put("cod_repartidor", id);
            values.put("repartidor",nombre.getText().toString().toUpperCase());
            values.put("estado", "A");

            db.insert("repartidores" ,null,values);

            db.close();
            limpiar_form();

            new AlertDialog.Builder(registrar_repartidor.this)
                .setTitle("ATENCION!!!")
                .setMessage("REGISTRADO CON EXITO!")
                .setNegativeButton("CERRAR", null).show();
    }
    private void limpiar_form()
    {
        nombre.setText("");

     }

    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("VOLVER ATRAS.")
                .setMessage("CONTINUAR?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                        Intent List = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(List);
                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }



}