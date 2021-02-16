package com.example.ControlCajas;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

 import entidades.informe_maples;
import entidades.Usuario;

public class informe_registro_maples extends AppCompatActivity {
    TextView txt_fecha;
    Button btn_fecha;
    DatePickerDialog picker;
    Connection connect;
    ArrayList<informe_maples> lista_maples;
    private ArrayList<String> lista;
    private  ListView ListView;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informe_registro_maples);
        txt_fecha=(TextView)findViewById(R.id.txt_fecha_maples);
        btn_fecha=(Button) findViewById(R.id.btn_fecha);
        ListView=(ListView)findViewById(R.id.listViewDet_maples);
        btn_fecha.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(informe_registro_maples.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                DecimalFormat df = new DecimalFormat("00");
                                SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyy7");

                                cldr.set(year, monthOfYear, dayOfMonth);
                                String strDate = format.format(cldr.getTime());
                                txt_fecha.setText(df.format((dayOfMonth))+ "/" + df.format((monthOfYear + 1))  +"/"+ year  );



                            }
                        }, year, month, day);
                picker.show();


            }
        });
    txt_fecha.setEnabled(false);


        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                String  informacion="NRO DE REGISTRO: "+lista_maples.get(pos).getId()+"\n";
                informacion+="CLIENTE: "+lista_maples.get(pos).getSucursal()+"\n";
                informacion+="REPARTIDOR: "+lista_maples.get(pos).getRepartidor()+"\n";
                informacion+="MAPLES AZUL: "+lista_maples.get(pos).getazul()+"\n";
                informacion+="MAPLES VERDE: "+lista_maples.get(pos).getverde()+"\n";
                informacion+="TIPO DE REGISTRO: "+lista_maples.get(pos).gettipo_registro()+"\n";

                new AlertDialog.Builder(informe_registro_maples.this)
                        .setTitle("DETALLE")
                        .setMessage(informacion)
                        .setNegativeButton("CERRAR", null).show();
            }
        });
    }



    public void buscar(View view) {

        progress = ProgressDialog.show(this, "CONSULTANDO DATOS",
                "ESPERE...", true);

        new informe_registro_maples.hilo_consulta().start();
    }


    private void consultarListaregistro() {

        try {

            ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
            connect = conexion.Connections();
            String query =  "exec pa_select_maples_registrados @fecha='"+txt_fecha.getText().toString()+"'" ;
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            informe_maples informe_maples= null;
            lista_maples=new ArrayList<informe_maples>();

            while ( rs.next())
            {
                informe_maples=new informe_maples();
                informe_maples.setId(rs.getInt("nro_movimiento"));
                informe_maples.setRepartidor(rs.getString("repartidor"));
                informe_maples.setSucursal(rs.getString("sucursal"));
                informe_maples.setazul(rs.getInt("tipo_azul"));
                informe_maples.setverde(rs.getInt("tipo_verde"));
                informe_maples.settipo_registro(rs.getString("tipo_registro"));

                lista_maples.add(informe_maples);
            }

        }

        catch (Exception e){

        }
    }


    class hilo_consulta extends Thread {
        @Override
        public void run() {
            try {
                consultarListaregistro();
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        progress.dismiss();
                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_grilla, R.id.txt_rojo, lista_maples) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                TextView redo = (TextView) view.findViewById(R.id.txt_rojo);
                                TextView amarillo = (TextView) view.findViewById(R.id.txt_amarillo);
                                TextView nro = (TextView) view.findViewById(R.id.txt_nro);
                                TextView sucursal = (TextView) view.findViewById(R.id.txt_sucur);
                                TextView repartidor = (TextView) view.findViewById(R.id.txt_repar);
                                TextView tipo = (TextView) view.findViewById(R.id.txt_tipo_envio_grilla);
                                nro.setText(""+lista_maples.get(position).getId());
                                sucursal.setText(""+lista_maples.get(position).getSucursal());
                                repartidor.setText(""+lista_maples.get(position).getRepartidor());
                                redo.setText(""+lista_maples.get(position).getazul());
                                amarillo.setText(""+lista_maples.get(position).getverde());
                                tipo.setText( lista_maples.get(position).gettipo_registro());
                                redo.setBackgroundColor(0xff292ede);
                                amarillo.setBackgroundColor(0xff00C400);
                                 return view;
                            }
                        };
                        ListView.setAdapter(adapter);
                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }
}