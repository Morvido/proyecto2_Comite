package proyecto1.service;

import proyecto1.model.Atleta;
import proyecto1.model.Entrenamiento;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

//Aca vamos a manejar la persistencia de datos guardar y cargar la informacion en sitema de archivos CSV

public class CSVService {

    //El metodo guardar almacenamos los datos en archivo CSV

    public void guardar(String archivo, Map<Atleta, List<Entrenamiento>> registros) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            for (Atleta atleta : registros.keySet()) {
                for (Entrenamiento e : registros.get(atleta)) {
                    //Aca esta la forma en la que se almacenaran los datos en el CSV
                    //nombre,apellido,edad,disciplina,departamento,fecha,tipo_entrenamiento,valor
                    // por ejemplo podría ser: Juan,Pérez,25,Atletismo,Lima,2024-03-15,Velocidad,10.5
                    writer.println(atleta.getNombre() + "," +
                            atleta.getApellido() + "," +
                            atleta.getEdad() + "," +
                            atleta.getDisciplina() + "," +
                            atleta.getDepartamento() + "," +
                            e.getFecha() + "," +
                            e.getTipo() + "," +
                            e.getValor());
                }
            }
        } catch (IOException ex) {
            System.out.println("Error al guardar CSV: " + ex.getMessage());
        }
    }
//Metodo cargar archivos
    //El proceso de carga sera linea por linea del archivo csv
    //Divide cada linea por comas array de strings
    //Verifica que tenga 8 campos para evitar errores
    //Y reconstruye los objetos de atleta y entrenamiento

    public Map<Atleta, List<Entrenamiento>> cargar(String archivo) {
        Map<Atleta, List<Entrenamiento>> registros = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 8) {
                    Atleta atleta = new Atleta(datos[0], datos[1],
                            //el integer.parseint es para convertir string a int para la edad
                            Integer.parseInt(datos[2]), datos[3], datos[4]);
                    Entrenamiento e = new Entrenamiento(
                            //Localdate.parse convierte string a local date que es la fecha
                            LocalDate.parse(datos[5]), datos[6],
                            //El double.parsedouble convierte string a double para el valor
                            Double.parseDouble(datos[7]));
                    //Utilice ptifabstent para evitar los duplicados en los atletas en el map
                    registros.putIfAbsent(atleta, new ArrayList<>());
                    registros.get(atleta).add(e);
                }
            }
        } catch (IOException ex) {
            System.out.println("Error al cargar CSV: " + ex.getMessage());
        }
        return registros;
    }
}
