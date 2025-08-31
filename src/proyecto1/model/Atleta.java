package proyecto1.model;

import java.util.Objects;

public class Atleta {
    private final String nombre;
    private final String apellido;
    private final int    edad;
    private final String disciplina;   // En este caso se guardara lo que se agrega para una nueva disciplina
    private final String departamento;

    public Atleta(String nombre, String apellido, int edad, String disciplina, String departamento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.disciplina = disciplina;
        this.departamento = departamento;
    }

    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public int getEdad() { return edad; }
    public String getDisciplina() { return disciplina; }
    public String getDepartamento() { return departamento; }

    @Override
    public String toString() {
        return nombre + " " + apellido + " - " + disciplina + " (" + departamento + ")";
    }

    // Aca use un Hashmap, porque usando el metodo equals elimno l aposiblidad de 2 atletas iguales
    // comparo nombre,apellido y disciplina, ignorando las mayusculas y minusculas
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Atleta atleta)) return false;
        return nombre.equalsIgnoreCase(atleta.nombre)
                && apellido.equalsIgnoreCase(atleta.apellido)
                && disciplina.equalsIgnoreCase(atleta.disciplina);
    }
//El hashcode solo genera un codigo numerico y unico para cada objeto registrado
    @Override
    public int hashCode() {
        return Objects.hash(nombre.toLowerCase(), apellido.toLowerCase(), disciplina.toLowerCase());
    }
}
