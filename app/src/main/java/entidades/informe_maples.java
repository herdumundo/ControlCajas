package entidades;

import java.io.Serializable;

/**
 * Created by CHENAO on 7/05/2017.
 */

public class informe_maples implements  Serializable{

    private int id;
    private String sucursal;
    private String repartidor;
    private String tipo_registro;
    private int azul;
    private int verde;




    public informe_maples(Integer id, String sucursal, String repartidor,Integer verde,Integer azul ,String tipo_registro) {
        this.id = id;
        this.sucursal = sucursal;
        this.repartidor = repartidor;
        this.verde = verde;
        this.azul = azul;
        this.tipo_registro = tipo_registro;

    }

    public informe_maples(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }


    public String getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(String repartidor) {
        this.repartidor = repartidor;
    }

    public String toString() {
        return String.valueOf("Nro."+id+"- Repartidor:"+repartidor+" - Sucursal:"+sucursal);
    }



    public Integer getazul() {
        return azul;
    }

    public void setazul(Integer azul) {
        this.azul = azul;
    }



    public Integer getverde() {
        return verde;
    }

    public void setverde(Integer verde) {
        this.verde = verde;
    }



    public String gettipo_registro() {
        return tipo_registro;
    }

    public void settipo_registro(String tipo_registro) {
        this.tipo_registro = tipo_registro;
    }


}
