package proyecto1.service;

import proyecto1.model.Entrenamiento;

import java.util.Comparator;
import java.util.List;

//Acá se realiza el analisis de datos, por ejemplo: calcular promedios, entrenamientos altetlas, mejores marcas y la evolucion

public class AnalisisService {

    //Metodo calcular promedio

    public double calcularPromedio(List<Entrenamiento> entrenamientos) {
        if (entrenamientos == null || entrenamientos.isEmpty()) return 0.0;
        //Verifico que la lista esta vacioa o nula
        double suma = 0;
        for (Entrenamiento e : entrenamientos) suma += e.getValor();
        //suma todos los entrenamientos y divide por los entrenamientos registrados
        return suma / entrenamientos.size();
    }
//Enecuentra el mejor entrenamiento en base a los parametros de la disciplina
    public Entrenamiento mejorMarca(List<Entrenamiento> entrenamientos, String disciplina) {
        if (entrenamientos == null || entrenamientos.isEmpty()) return null;
        String d = disciplina.trim().toLowerCase();

        // Si es una carrrera, Natación o ciclismo el mejor tiempo es el mejor por esto estan aquí y utilizo minutos
        if (d.equals("carrera") || d.equals("natación") || d.equals("natacion") || d.equals("ciclismo")) {
            return entrenamientos.stream().min(Comparator.comparingDouble(Entrenamiento::getValor)).orElse(null);
        }

        // Estos deportes utlizan peso, puntos, distancia, así que son por el mayor valor registrado
        return entrenamientos.stream().max(Comparator.comparingDouble(Entrenamiento::getValor)).orElse(null);
    }

    //metodo evolucion
    //primero ordena los entrenamientos para mostrar la evolucion del atleta
    public List<Entrenamiento> evolucion(List<Entrenamiento> entrenamientos, String disciplina) {
        if (entrenamientos == null || entrenamientos.isEmpty()) return entrenamientos;
        String d = disciplina.trim().toLowerCase();

        if (d.equals("carrera") || d.equals("natación") || d.equals("natacion") || d.equals("ciclismo")) {
            // en carrera/natacion/ciclismo los ordeno de mayor a menor, ya que los primeros tiempos son los peores
            entrenamientos.sort(Comparator.comparingDouble(Entrenamiento::getValor).reversed());
        } else if (d.equals("marcha")) {
            // En marcha se guardanigual de menor a mayor distancia, mostrando el aumento de resistencia
            entrenamientos.sort(Comparator.comparingDouble(Entrenamiento::getValor));
        } else if (d.contains("pesas") || d.equals("levantamiento de pesas") || d.equals("levantamientopesas")) {
            // Para el peso es mientras mas peso mejor, entonces se ordena de menor a mayor
            entrenamientos.sort(Comparator.comparingDouble(Entrenamiento::getValor));
        } else {
            // de la misma forma que el peso se organizan los goles, puntos, rounds etc, aca agregare o añadire segun se agreguen disciplinas
            entrenamientos.sort(Comparator.comparingDouble(Entrenamiento::getValor));
        }
        return entrenamientos;
    }
}
