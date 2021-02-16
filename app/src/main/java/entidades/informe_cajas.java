package entidades;

import java.io.Serializable;

/**
 * Created by CHENAO on 7/05/2017.
 */

public class informe_cajas implements  Serializable{

    private int id;
    private int rojo;
    private int amarillo;
    private String sucursal;
    private String repartidor;
    private String tipo_registro;




    public informe_cajas(Integer id, String sucursal, String repartidor,Integer amarillo,Integer rojo ,String tipo_registro) {
        this.id = id;
        this.sucursal = sucursal;
        this.repartidor = repartidor;
        this.amarillo = amarillo;
        this.rojo = rojo;
        this.tipo_registro = tipo_registro;

    }

    public informe_cajas(){

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



    public Integer getamarillo() {
        return amarillo;
    }

    public void setamarillo(Integer amarillo) {
        this.amarillo = amarillo;
    }



    public Integer getrojo() {
        return rojo;
    }

    public void setrojo(Integer rojo) {
        this.rojo = rojo;
    }



    public String gettipo_registro() {
        return tipo_registro;
    }

    public void settipo_registro(String tipo_registro) {
        this.tipo_registro = tipo_registro;
    }


}
