package fis.dsw.sgc.inmuebles.dto;

public class OpcionComboDTO {
    private final int id;
    private final String nombre;

    public OpcionComboDTO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }

    @Override
    public String toString() {
        return nombre;
    }
}