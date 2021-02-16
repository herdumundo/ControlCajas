package com.example.ControlCajas;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import entidades.contenedor_tipo_envio;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import entidades.Clientes;
import entidades.Repartidores;

public class recepcion_maples extends AppCompatActivity
{
    TextView txt_tipo_verde,txt_tipo_azul,txt_input_azul,txt_input_verde;
    ConexionSQLiteHelper conn;
    private ProgressDialog progress;
      DatePickerDialog picker;
    EditText txt_fecha;
    SearchableSpinner spinner_cliente,spinner_repartidor,spinner_ayudante;
    int id_cliente=-1;
    int id_repartidor=0;
    int id_ayudante=0;
    String sucursal="";
    private ArrayList<String> cajas;
    int variable_cantidad_azul=0;
    int variable_cantidad_verde=0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Bloquea el giro de pantalla
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recepcion_maples);
        txt_fecha=(EditText) findViewById(R.id.txt_fecha_maples);
        getSupportActionBar().setTitle("REGISTRO: "+contenedor_tipo_envio.nombre_envio);
        txt_fecha.setInputType(InputType.TYPE_NULL);
        spinner_cliente=(SearchableSpinner)findViewById(R.id.spinner_cliente);
        spinner_repartidor=(SearchableSpinner)findViewById(R.id.spinner_repartidor);
        spinner_ayudante=(SearchableSpinner)findViewById(R.id.spinner_ayudante);
         cajas=new ArrayList<String>();
        txt_tipo_azul=(TextView) findViewById(R.id.txt_tipo_azul);
        txt_tipo_verde=(TextView) findViewById(R.id.txt_tipo_verde);
        txt_input_azul=(TextView) findViewById(R.id.txt_input_azul);
        txt_input_verde=(TextView) findViewById(R.id.txt_input_verde);
        txt_fecha.setEnabled(false);
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
        cargar_cliente();
        cargar_repartidor();
        spinner_cliente.setTitle("SELECCIONAR CLIENTE");
        spinner_cliente.setPositiveButton("CERRAR");
        spinner_repartidor.setTitle("SELECCIONAR REPARTIDOR");
        spinner_repartidor.setPositiveButton("CERRAR");
        spinner_ayudante.setTitle("SELECCIONAR AYUDANTE");
        spinner_ayudante.setPositiveButton("CERRAR");
        SQLiteDatabase db=conn.getReadableDatabase();
        Cursor cursor1=db.rawQuery("SELECT date('now') as fecha",null);
        while (cursor1.moveToNext())
        {
            txt_fecha.setText(cursor1.getString(0));
        }

        spinner_cliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Clientes cliente_combo= (Clientes)parent.getSelectedItem();
                id_cliente=cliente_combo.getCod_cliente();
                sucursal=cliente_combo.getRazon_social();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        spinner_repartidor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Repartidores repartidores_combo= (Repartidores)parent.getSelectedItem();
                id_repartidor=repartidores_combo.getCod_repartidor();
               // Toast.makeText(recepcion_maples.this, String.valueOf(id_repartidor), Toast.LENGTH_SHORT).show();

            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        spinner_ayudante.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Repartidores repartidores_combo= (Repartidores)parent.getSelectedItem();
                id_ayudante=repartidores_combo.getCod_repartidor();
             //   Toast.makeText(recepcion_maples.this, String.valueOf(id_ayudante), Toast.LENGTH_SHORT).show();

            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });









        txt_input_verde.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int s, int b, int c) {

                if( txt_input_verde.getText().length()==0)
                {
                    variable_cantidad_verde= 0;
                }
                else {
                    variable_cantidad_verde= Integer.parseInt(txt_input_verde.getText().toString());
                }
                 if (txt_input_azul.getText().length()==0)
                {
                    variable_cantidad_azul=0;
                }
                 else {

                     variable_cantidad_azul= Integer.parseInt(txt_input_azul.getText().toString());

                 }

                calculo_multiplo();
            }

            public void afterTextChanged(Editable editable) { }
            public void beforeTextChanged(CharSequence cs, int i, int j, int
                    k) { }
        });

        txt_input_azul.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int s, int b, int c) {

                if( txt_input_verde.getText().length()==0)
                {
                    variable_cantidad_verde= 0;
                }
                else {
                    variable_cantidad_verde= Integer.parseInt(txt_input_verde.getText().toString());
                }
                if (txt_input_azul.getText().length()==0)
                {
                    variable_cantidad_azul=0;
                }
                else {

                    variable_cantidad_azul= Integer.parseInt(txt_input_azul.getText().toString());

                }

                calculo_multiplo();
            }

            public void afterTextChanged(Editable editable) { }
            public void beforeTextChanged(CharSequence cs, int i, int j, int
                    k) { }
        });

 }








    private  void cargar_cliente()
    {
        List<Clientes> lista_combo = new ArrayList<>();
        SQLiteDatabase db=conn.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM clientes "   ,null);
        while (cursor.moveToNext())
        {
            Clientes p= new Clientes();
            p.setCod_cliente(cursor.getInt(0));
            p.setRazon_social(cursor.getString(1));

            lista_combo.add(p);
        }
        cursor.close();
         ArrayAdapter<CharSequence> adaptador_clientes=new ArrayAdapter (this,R.layout.spinner_item,lista_combo) ;
        spinner_cliente.setAdapter(adaptador_clientes);
    }

    private  void cargar_repartidor()
    {
        List<Repartidores> lista_combo_repartidor = new ArrayList<>();
        SQLiteDatabase db=conn.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM repartidores "   ,null);
        while (cursor.moveToNext())
        {
            Repartidores p= new Repartidores();
            p.setCod_repartidor(cursor.getInt(0));
            p.setRepartidor(cursor.getString(1));

            lista_combo_repartidor.add(p);
        }
        cursor.close();
        ArrayAdapter<CharSequence> adaptador_repartidor=new ArrayAdapter (this,R.layout.spinner_item,lista_combo_repartidor) ;
        spinner_repartidor.setAdapter(adaptador_repartidor);
        spinner_ayudante.setAdapter(adaptador_repartidor);
    }



    ////////////////////////////////////////////////////////





    private  void registrar_maples(){
         ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
        SQLiteDatabase db1=conn.getReadableDatabase();
        ContentValues valor_cab=new ContentValues();
        try {

            Cursor cursor1=db1.rawQuery("SELECT  case  when count(*) = 0 then 1 else max(nro_movimiento) +1  end as d FROM mov_maples   "  ,null);
            String id= "";
            while (cursor1.moveToNext())
            {
                id=cursor1.getString(0);
            }
            valor_cab.put("nro_movimiento",id);
            valor_cab.put("fecha",txt_fecha.getText().toString());
            valor_cab.put("repartidor",id_repartidor);
            valor_cab.put("tipo_mov",contenedor_tipo_envio.tipo_envio);
            valor_cab.put("cod_cliente",id_cliente);
            valor_cab.put("sucursal",sucursal);
            valor_cab.put("ayudante",id_ayudante);
            valor_cab.put("estado","P");
            db1.insert("mov_maples","nro_movimiento",valor_cab);
            db1.close();


            if(Integer.parseInt(txt_tipo_azul.getText().toString())>0 ){
                SQLiteDatabase db=conn.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put("nro_movimiento",id);
                values.put("tipo_m",1);
                values.put("cantidad",txt_tipo_azul.getText().toString());
                values.put("estado","P");
                db.insert("det_mov_maples",null,values);
                db.close();
            }

            if(Integer.parseInt(txt_tipo_verde.getText().toString())>0 ){
                SQLiteDatabase db=conn.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put("nro_movimiento",id);
                values.put("tipo_m",2);
                values.put("cantidad",txt_tipo_verde.getText().toString());
                values.put("estado","P");
                db.insert("det_mov_maples",null,values);
                db.close();
            }
        }

        catch (Exception e){

            String mensaje=e.toString();

        }

    }

    private void calculo_multiplo(){

        try {


            if(contenedor_tipo_envio.tipo_envio==2){
                txt_tipo_verde.setText(String.valueOf(variable_cantidad_verde) );
                txt_tipo_azul.setText(String.valueOf(variable_cantidad_azul));
            }
        else {
                txt_tipo_verde.setText(String.valueOf(variable_cantidad_verde*8) );
                txt_tipo_azul.setText(String.valueOf(variable_cantidad_azul*8));

            }

            }
            catch (Exception e){

            System.out.println(e.toString());
            }


    }

    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("VOLVER ATRAS.")
                .setMessage("SE PERDERAN TODOS LOS DATOS.")
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


    class hilo_registrar extends Thread {
        @Override
        public void run() {
            try {
                registrar_maples();
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        progress.dismiss();
                        new AlertDialog.Builder(recepcion_maples.this)
                                .setTitle("INFORMACION")
                                .setMessage("REGISTRADO CON EXITO")
                                .setNegativeButton("CERRAR", null).show();
                        Intent i=new Intent(recepcion_maples.this,MainActivity.class);
                        startActivity(i);
                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }




    public void registrar(View v) {
        if(txt_fecha.getText().toString().trim().equals("")){

            new AlertDialog.Builder(recepcion_maples.this)
                    .setTitle("ATENCION!!!")
                    .setMessage("ERROR, DEBE INGRESAR FECHA")
                    .setNegativeButton("CERRAR", null).show();
        }


        else if (id_cliente<0){

            new AlertDialog.Builder(recepcion_maples.this)
                    .setTitle("ATENCION!!!")
                    .setMessage("ERROR, DEBE SELECCIONAR CLIENTE")
                    .setNegativeButton("CERRAR", null).show();
        }
        else if (id_repartidor==0){

            new AlertDialog.Builder(recepcion_maples.this)
                    .setTitle("ATENCION!!!")
                    .setMessage("ERROR, DEBE SELECCIONAR REPARTIDOR")
                    .setNegativeButton("CERRAR", null).show();
        }
        else if (id_ayudante==0){

            new AlertDialog.Builder(recepcion_maples.this)
                    .setTitle("ATENCION!!!")
                    .setMessage("ERROR, DEBE SELECCIONAR AYUDANTE")
                    .setNegativeButton("CERRAR", null).show();
        }

        else if(Integer.parseInt(txt_tipo_azul.getText().toString())==0 &&Integer.parseInt(txt_tipo_verde.getText().toString())==0 ){
            new AlertDialog.Builder(recepcion_maples.this)
                    .setTitle("ATENCION!!!")
                    .setMessage("ERROR, DEBE INGRESAR CANTIDAD")
                    .setNegativeButton("CERRAR", null).show();
        }

        else{
            new AlertDialog.Builder(recepcion_maples.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("REGISTRAR.")
                    .setMessage("ESTA SEGURO DE REGISTRAR LOS DATOS INGRESADOS?")
                    .setPositiveButton("SI", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progress = ProgressDialog.show(recepcion_maples.this, "REGISTRANDO DATOS",
                                    "ESPERE...", true);
                            new recepcion_maples.hilo_registrar().start();
                        }
                    })
                    .setNegativeButton("NO", null)
                    .show();
        }



    }



    public void buscar_fecha(View v) {

        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(recepcion_maples.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        DecimalFormat df = new DecimalFormat("00");
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");

                        cldr.set(year, monthOfYear, dayOfMonth);
                        String strDate = format.format(cldr.getTime());
                        txt_fecha.setText(year + "-" + df.format((monthOfYear + 1))  + "-" +df.format((dayOfMonth)));
                    }
                }, year, month, day);
        picker.show();
    }


}
