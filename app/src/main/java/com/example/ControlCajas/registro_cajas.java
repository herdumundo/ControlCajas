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
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
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
import android.app.ActionBar;
public class registro_cajas extends AppCompatActivity
{
    TextView txt_cod_caja,txt_tipo_4,txt_tipo_5;
    ConexionSQLiteHelper conn;
    private ProgressDialog progress;
    private ListView ListView;
    Button cargar,registrar;
    DatePickerDialog picker;
    EditText txt_fecha;
    SearchableSpinner spinner_cliente,spinner_repartidor,spinner_ayudante;
    int id_cliente=-1;
    int id_repartidor=0;
    int id_ayudante=0;
    String sucursal="";
    private ArrayAdapter<String> adaptador1;
    private ArrayList<String> cajas;





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Bloquea el giro de pantalla
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movimientos);
        txt_fecha=(EditText) findViewById(R.id.txt_fecha_maples);
        getSupportActionBar().setTitle("REGISTRO: "+contenedor_tipo_envio.nombre_envio);

        txt_tipo_4=(TextView) findViewById(R.id.txt_tipo_4);
        txt_tipo_5=(TextView) findViewById(R.id.txt_tipo_5);
        txt_fecha.setInputType(InputType.TYPE_NULL);
        spinner_cliente=(SearchableSpinner)findViewById(R.id.spinner_cliente);
        spinner_repartidor=(SearchableSpinner)findViewById(R.id.spinner_repartidor);
        spinner_ayudante=(SearchableSpinner)findViewById(R.id.spinner_ayudante);
        ListView = (ListView) findViewById(R.id.listView_cajas);
        cajas=new ArrayList<String>();
        adaptador1=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cajas);
        ListView.setAdapter(adaptador1);
        txt_cod_caja=(TextView) findViewById(R.id.txt_cod_caja);
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
        cargar_cliente();
        cargar_repartidor();
        spinner_cliente.setTitle("SELECCIONAR CLIENTE");
        spinner_cliente.setPositiveButton("CERRAR");
        spinner_repartidor.setTitle("SELECCIONAR REPARTIDOR");
        spinner_repartidor.setPositiveButton("CERRAR");
        spinner_ayudante.setTitle("SELECCIONAR AYUDANTE");
        spinner_ayudante.setPositiveButton("CERRAR");
        txt_cod_caja.requestFocus();
        txt_fecha.setEnabled(false);
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
               // Toast.makeText(registro_cajas.this, String.valueOf(id_repartidor), Toast.LENGTH_SHORT).show();

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
             //   Toast.makeText(registro_cajas.this, String.valueOf(id_ayudante), Toast.LENGTH_SHORT).show();

            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        txt_cod_caja.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                     case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_TAB:

                    agregar_fila_grilla();
                            txt_cod_caja.requestFocus();
                    }
                }
                return false;
            }
        });

        ListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int posicion=i;

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(registro_cajas.this);
                dialogo1.setTitle("Importante");
                dialogo1.setMessage("¿ Eliminar esta fila ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        cajas.remove(posicion);
                        adaptador1.notifyDataSetChanged();
                        contar_cartones();


                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialogo1.show();

                return false;
            }
        });

    }



    public void agregar_fila(View v) {
        agregar_fila_grilla();
    }

private void agregar_fila_grilla(){

    String contenido_fila="";
    int registro_duplicado=0;
    int caja_parte= Integer.parseInt( txt_cod_caja.getText().toString().substring(0,2));

    if(txt_cod_caja.getText().length()==0)
    {
        txt_cod_caja.requestFocus();
    }
    else if (caja_parte!=29 && caja_parte!=31){

        txt_cod_caja.setText("");
        txt_cod_caja.requestFocus();
    }

    else {

        for (int i = 0; i < ListView.getCount(); i++)
        {
            contenido_fila=(String) ListView.getItemAtPosition(i);

            if(contenido_fila.equals(txt_cod_caja.getText().toString())){
                registro_duplicado++;
            }
        }

        if(registro_duplicado>0){

            txt_cod_caja.setText("");
            txt_cod_caja.requestFocus();
        }

        else {

            cajas.add(txt_cod_caja.getText().toString());
            adaptador1.notifyDataSetChanged();
            txt_cod_caja.setText("");
            contar_cartones();
        }
    }





}

    private void contar_cartones()
    {
        String contenido_grilla="";
        String tipo_cajas="";
        int tipo_4=0;
        int tipo_5=0;
        txt_tipo_4.setText("0");
        txt_tipo_5.setText("0");

        for (int i = 0; i < ListView.getCount(); i++)
        {
            contenido_grilla = (String) ListView.getItemAtPosition(i);

            if (contenido_grilla.substring(0,2).equals("31")){
                tipo_4++;

                txt_tipo_4.setText(String.valueOf(tipo_4));

             }
            else if(contenido_grilla.substring(0,2).equals("29")){

                tipo_5++;

                txt_tipo_5.setText(String.valueOf(tipo_5));
             }


        }
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


    public void registrar(View v) {
        if(txt_fecha.getText().toString().trim().equals("")){

            new AlertDialog.Builder(registro_cajas.this)
                    .setTitle("ATENCION!!!")
                    .setMessage("ERROR, DEBE INGRESAR FECHA")
                    .setNegativeButton("CERRAR", null).show();
        }
       else if(ListView.getAdapter().getCount()==0){

            new AlertDialog.Builder(registro_cajas.this)
                    .setTitle("ATENCION!!!")
                    .setMessage("ERROR, DEBE INGRESAR CAJAS")
                    .setNegativeButton("CERRAR", null).show();
        }

        else if (id_cliente<0){

            new AlertDialog.Builder(registro_cajas.this)
                    .setTitle("ATENCION!!!")
                    .setMessage("ERROR, DEBE SELECCIONAR CLIENTE")
                    .setNegativeButton("CERRAR", null).show();
        }
        else if (id_repartidor==0){

            new AlertDialog.Builder(registro_cajas.this)
                    .setTitle("ATENCION!!!")
                    .setMessage("ERROR, DEBE SELECCIONAR REPARTIDOR")
                    .setNegativeButton("CERRAR", null).show();
        }
        else if (id_ayudante==0){

            new AlertDialog.Builder(registro_cajas.this)
                    .setTitle("ATENCION!!!")
                    .setMessage("ERROR, DEBE SELECCIONAR AYUDANTE")
                    .setNegativeButton("CERRAR", null).show();
        }

        else{
            new AlertDialog.Builder(registro_cajas.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("REGISTRAR.")
                    .setMessage("ESTA SEGURO DE REGISTRAR LOS DATOS INGRESADOS?")
                    .setPositiveButton("SI", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progress = ProgressDialog.show(registro_cajas.this, "REGISTRANDO DATOS",
                                    "ESPERE...", true);
                            new registro_cajas.hilo_registrar().start();
                        }
                    })
                    .setNegativeButton("NO", null)
                    .show();
        }

    }


    private  void registrar_cajas(){
        String clickedItem="";
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
        SQLiteDatabase db1=conn.getReadableDatabase();
        ContentValues valor_cab=new ContentValues();
        try {

            Cursor cursor1=db1.rawQuery("SELECT  case  when count(*) = 0 then 1 else max(nro_movimiento) +1  end as d FROM movimientos  "  ,null);
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
            db1.insert("movimientos","nro_movimiento",valor_cab);
            db1.close();
            for (int i = 0; i < ListView.getCount(); i++)
            {
                clickedItem = (String) ListView.getItemAtPosition(i);


                SQLiteDatabase db=conn.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put("nro_movimiento",id);
                values.put("cod_caja",clickedItem);
                values.put("estado","P");
                db.insert("det_movimientos",null,values);
                db.close();

            }

        }

        catch (Exception e){

            String mensaje=e.toString();

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
                registrar_cajas();
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        progress.dismiss();

                        new AlertDialog.Builder(registro_cajas.this)
                                .setTitle("INFORMACION")
                                .setCancelable(false)
                                .setMessage("REGISTRADO CON EXITO")
                                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener()

                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(registro_cajas.this,MainActivity.class);
                                startActivity(i);
                            }
                        }).show();


                    }

                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void buscar_fecha_cajas(View v) {

        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(registro_cajas.this,
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
