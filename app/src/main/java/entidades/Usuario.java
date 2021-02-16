package entidades;

import java.io.Serializable;

/**
 * Created by CHENAO on 7/05/2017.
 */

public class Usuario implements  Serializable{

    private String id;
    private String nombre;
    private String potrero;
    private String estancia;
    private String cantidad_animales;
    private String fecha;


    public Usuario(String id, String nombre ) {
        this.id = id;
        this.nombre = nombre;

    }

    public Usuario(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPotrero() {
        return potrero;
    }

    public void setPotrero(String potrero) {
        this.potrero = potrero;
    }



    public String getEstancia() {
        return estancia;
    }

    public void setEstancia(String estancia) {
        this.estancia = estancia;
    }
    public String getCantidad_animales() {
        return cantidad_animales;
    }

    public void setCantidad_animales(String cantidad_animales) {
        this.cantidad_animales = cantidad_animales;
    }


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

}
