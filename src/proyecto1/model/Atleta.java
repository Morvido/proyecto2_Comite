package proyecto1.model;

import java.time.LocalDate;
import java.util.Objects;

public class Atleta {
    private final String nombre;
    private final String apellido;
    private final int edad;
    private final String disciplina;
    private final String departamento;
    private final String nacionalidad; //ESTE ES EL NUEVO CAMPO DE NACIONALIDAD
    private final LocalDate fechaIngreso; // NUEVO CAMPO DE FECHA DE INGRESO

    public Atleta(String nombre, String apellido, int edad, String disciplina, String departamento,
                  String nacionalidad, LocalDate fechaIngreso) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.disciplina = disciplina;
        this.departamento = departamento;
        this.nacionalidad = nacionalidad;
        this.fechaIngreso = fechaIngreso;
    }


    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public int getEdad() { return edad; }
    public String getDisciplina() { return disciplina; }
    public String getDepartamento() { return departamento; }
    public String getNacionalidad() { return nacionalidad; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }

    @Override
    public String toString() {
        return nombre + " " + apellido + " - " + disciplina + " (" + departamento + ") - " + nacionalidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Atleta atleta)) return false;
        return nombre.equalsIgnoreCase(atleta.nombre)
                && apellido.equalsIgnoreCase(atleta.apellido)
                && disciplina.equalsIgnoreCase(atleta.disciplina);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre.toLowerCase(), apellido.toLowerCase(), disciplina.toLowerCase());
    }
}
