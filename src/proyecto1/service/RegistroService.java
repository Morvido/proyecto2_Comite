package proyecto1.service;

import proyecto1.model.Atleta;
import proyecto1.model.Entrenamiento;

import java.util.*;
import java.util.stream.Collectors;


//ESTOS SON LOS METODOS QUE UTILIZAN LOS ATLETAS

public class RegistroService {
    private final Map<Atleta, List<Entrenamiento>> registros;

    public RegistroService() {
        this.registros = new HashMap<>();
    }

    public void registrarAtleta(Atleta atleta) {
        registros.putIfAbsent(atleta, new ArrayList<>());
    }

    public Atleta buscarAtletaPorNombre(String nombre, String apellido) {
        for (Atleta a : registros.keySet()) {
            if (a.getNombre().equalsIgnoreCase(nombre) && a.getApellido().equalsIgnoreCase(apellido)) {
                return a;
            }
        }
        return null;
    }

    public List<Atleta> buscarAtletasPorDisciplina(String disciplina) {
        return registros.keySet().stream()
                .filter(a -> a.getDisciplina().equalsIgnoreCase(disciplina))
                .collect(Collectors.toList());
    }

    //ESTOS SON LOS METODOS DE LOS ENTRENAMIENTOS

    public void registrarEntrenamiento(Atleta atleta, Entrenamiento entrenamiento) {
        registros.putIfAbsent(atleta, new ArrayList<>());
        registros.get(atleta).add(entrenamiento);
    }

    public List<Entrenamiento> obtenerEntrenamientos(Atleta atleta) {
        List<Entrenamiento> lista = registros.getOrDefault(atleta, new ArrayList<>());
        lista.sort(Comparator.comparing(Entrenamiento::getFecha));
        return lista;
    }

    public Map<Atleta, List<Entrenamiento>> getRegistros() {
        return registros;
    }


    public List<Entrenamiento> obtenerEntrenamientosMes(Atleta atleta, int year, int month) {
        return obtenerEntrenamientos(atleta).stream()
                .filter(e -> e.getFecha().getYear() == year && e.getFecha().getMonthValue() == month)
                .toList();
    }
}
