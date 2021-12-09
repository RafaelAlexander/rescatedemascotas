package ar.edu.utn.frba.dds.dominio.MascotaYDuenio;

import javax.persistence.Embeddable;

@Embeddable
public class CaracteristicaMascota {
    String descripcion;
    String opcionSeleccionada;

    public CaracteristicaMascota(String desc, String opcion){
        this.descripcion = desc;
        this.opcionSeleccionada = opcion;
    }

    public CaracteristicaMascota(){
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getOpcionSeleccionada() {
        return opcionSeleccionada;
    }
}
