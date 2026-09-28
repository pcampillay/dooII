package model;

public class Repartidor {
    private Integer id;
    private final String nombre;

    public Repartidor(String nombre) {
        this(null, nombre);
    }

    public Repartidor(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
