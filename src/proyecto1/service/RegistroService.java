package proyecto1.service;

//Este es el nucleo de mi programa

import proyecto1.model.Atleta;
import proyecto1.model.Entrenamiento;

import java.util.*;

public class RegistroService {
    private final Map<Atleta, List<Entrenamiento>> registros;

    public RegistroService() {
        this.registros = new HashMap<>();
    }
//aca se registra al nuevo atleta
    //utilice el putifabsent para evitar dulicados
    public void registrarAtleta(Atleta atleta) {
        registros.putIfAbsent(atleta, new ArrayList<>());
    }
//Se busca al atleta por el nombre solo sería nombre y apellido
    public Atleta buscarAtletaPorNombre(String nombre, String apellido) {
        for (Atleta a : registros.keySet()) {
            if (a.getNombre().equalsIgnoreCase(nombre)
                    && a.getApellido().equalsIgnoreCase(apellido)) {
                return a;
            }
        }
        return null;
    }
//Buscar ateltas por disciplina los busca por la discilina
    public List<Atleta> buscarAtletasPorDisciplina(String disciplina) {
        List<Atleta> resultado = new ArrayList<>();
        for (Atleta a : registros.keySet()) {
            if (a.getDisciplina().equalsIgnoreCase(disciplina)) {
                resultado.add(a);
            }
        }
        return resultado;
    }
//añade el entrenamiento de un atleta existente, pero primero verifica que si exista
    public void registrarEntrenamiento(Atleta atleta, Entrenamiento entrenamiento) {
        if (registros.containsKey(atleta)) {
            registros.get(atleta).add(entrenamiento);
        }
    }

    // Obtiene todos los entrenamientos de un atleta ordenados por fecha por eso utilice getorbefault
    public List<Entrenamiento> obtenerEntrenamientos(Atleta atleta) {
        List<Entrenamiento> lista = registros.getOrDefault(atleta, new ArrayList<>());
        lista.sort(Comparator.comparing(Entrenamiento::getFecha));
        return lista;
    }

    //Porporciona el acceso a todos los datos
    public Map<Atleta, List<Entrenamiento>> getRegistros() {
        return registros;
    }
}
