package proyecto1.model;

import java.time.LocalDate;
import java.util.Objects;

public class Atleta {
    private final String nombre;
    private final String apellido;
    private final int edad;
    private final String disciplina;
    private final String nacionalidad;  //AGREGUE NUEVOS REQUERIMENTOS
    private final String departamento;
    private final LocalDate fechaIngreso;

    public Atleta(String nombre, String apellido, int edad, String disciplina, String nacionalidad,
                  String departamento, LocalDate fechaIngreso) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.disciplina = disciplina;
        this.nacionalidad = nacionalidad;
        this.departamento = departamento;
        this.fechaIngreso = fechaIngreso;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public int getEdad() { return edad; }
    public String getDisciplina() { return disciplina; }
    public String getNacionalidad() { return nacionalidad; }
    public String getDepartamento() { return departamento; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }

    //SE MUESTRA DATOS AL USUARIO
    @Override
    public String toString() {
        return nombre + " " + apellido + " - " + disciplina + nacionalidad + departamento ;
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
