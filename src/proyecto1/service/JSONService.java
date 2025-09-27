package proyecto1.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import proyecto1.model.Atleta;
import proyecto1.model.Entrenamiento;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Guarda y carga Map<Atleta, List<Entrenamiento>> como JSON.
 */
public class JSONService {
    private final Gson gson;

    public JSONService() {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                return LocalDate.parse(json.getAsString());
            }
        });
        gb.registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()));
        this.gson = gb.setPrettyPrinting().create();
    }

    public void guardar(String archivo, Map<Atleta, List<Entrenamiento>> registros) {
        // convertir Map<Atleta,List<Entrenamiento>> a una estructura serializable con claves como objetos
        try (Writer w = new FileWriter(archivo)) {
            Type tipo = new TypeToken<Map<Atleta, List<Entrenamiento>>>() {}.getType();
            gson.toJson(registros, tipo, w);
        } catch (IOException e) {
            System.out.println("Error guardando JSON: " + e.getMessage());
        }
    }

    public Map<Atleta, List<Entrenamiento>> cargar(String archivo) {
        try (Reader r = new FileReader(archivo)) {
            Type tipo = new TypeToken<Map<Atleta, List<Entrenamiento>>>() {}.getType();
            return gson.fromJson(r, tipo);
        } catch (FileNotFoundException fnf) {
            System.out.println("Archivo JSON no encontrado: " + archivo);
        } catch (IOException e) {
            System.out.println("Error leyendo JSON: " + e.getMessage());
        }
        return Map.of();
    }
}
