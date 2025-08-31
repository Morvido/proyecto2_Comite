package proyecto1.ui;

//importe todos los package que tengo juto con sus clases
//a excepcion de las disciplinas
import proyecto1.model.Atleta;
import proyecto1.model.Entrenamiento;
import proyecto1.service.AnalisisService;
import proyecto1.service.CSVService;
import proyecto1.service.RegistroService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private RegistroService registroService; //Gestion de datos
    private AnalisisService analisisService;  //Analisis y metricas
    private CSVService csvService; //persistencia de archivos
    private Scanner scanner; //Entrada de usuario

    public Menu() {
        this.registroService = new RegistroService();
        this.analisisService = new AnalisisService();
        this.csvService = new CSVService();
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        int opcion;
        do {
            mostrarOpciones();
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1 -> registrarAtleta();
                case 2 -> registrarEntrenamiento();
                case 3 -> buscarAtletaPorNombre();
                case 4 -> buscarAtletasPorDisciplina();
                case 5 -> verHistorial();
                case 6 -> calcularPromedio();
                case 7 -> verMejorMarca();
                case 8 -> verEvolucion();
                case 9 -> guardarCSV();
                case 10 -> cargarCSV();
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción inválida");
            }
        } while (opcion != 0);
    }

    private void mostrarOpciones() {
        System.out.println("\n===== COMITÉ OLÍMPICO GUATEMALTECO =====");
        System.out.println("1. Registrar atleta");
        System.out.println("2. Registrar entrenamiento");
        System.out.println("3. Buscar atleta por nombre y apellido");
        System.out.println("4. Buscar atletas por disciplina");
        System.out.println("5. Ver historial de entrenamientos");
        System.out.println("6. Calcular promedio de rendimiento");
        System.out.println("7. Ver mejor marca");
        System.out.println("8. Ver evolución en el tiempo");
        System.out.println("9. Guardar en archivo CSV");
        System.out.println("10. Cargar desde archivo CSV");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private void registrarAtleta() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("Edad: ");
        int edad = Integer.parseInt(scanner.nextLine());

        // Menú de disciplinas
        System.out.println("Seleccione la disciplina:");
        System.out.println("1. Carrera");
        System.out.println("2. Marcha");
        System.out.println("3. LevantamientoPesas");
        System.out.println("4. Natación");
        System.out.println("5. Ciclismo");
        System.out.println("6. Boxeo");
        System.out.println("7. Gimnasia");
        System.out.println("8. Judo");
        System.out.println("9. Fútbol");
        System.out.println("10. Baloncesto");
        System.out.print("Opción: ");
        int opcionDisciplina = Integer.parseInt(scanner.nextLine());

        //Decidi hacer un pequeño menu para seleccionar las disciplinas
        //Y evitar errores entre los nombres de las disciplinas y la opcion de otra por si se agrego

        String disciplina = switch (opcionDisciplina) {
            case 1 -> "Carrera";
            case 2 -> "Marcha";
            case 3 -> "LevantamientoPesas";
            case 4 -> "Natación";
            case 5 -> "Ciclismo";
            case 6 -> "Boxeo";
            case 7 -> "Gimnasia";
            case 8 -> "Judo";
            case 9 -> "Fútbol";
            case 10 -> "Baloncesto";
            default -> "Otra";
        };

        System.out.print("Departamento: ");
        String departamento = scanner.nextLine();

        Atleta atleta = new Atleta(nombre, apellido, edad, disciplina, departamento);
        registroService.registrarAtleta(atleta);
        System.out.println("Atleta registrado exitosamente: " + atleta);
    }

    private void registrarEntrenamiento() {
        System.out.print("Nombre del atleta: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido del atleta: ");
        String apellido = scanner.nextLine();

        Atleta atleta = registroService.buscarAtletaPorNombre(nombre, apellido);
        if (atleta == null) {
            System.out.println(" Atleta no encontrado.");
            return;
        }

        System.out.print("Fecha (YYYY-MM-DD): ");
        LocalDate fecha = LocalDate.parse(scanner.nextLine());

        String disciplina = atleta.getDisciplina().toLowerCase();
        String tipo;
        double valor;

        // Esto hace que el tipo de datos sea adecuado depende la ddisciplina pidiendo los datos necesarios

        switch (disciplina) {
            case "fútbol", "futbol" -> {
                tipo = "Goles";
                System.out.print("Ingrese los goles anotados: ");
                valor = Double.parseDouble(scanner.nextLine());
            }
            case "baloncesto" -> {
                tipo = "Canastas";
                System.out.print("Ingrese las canastas anotadas: ");
                valor = Double.parseDouble(scanner.nextLine());
            }
            case "carrera" -> {
                tipo = "Tiempo (segundos)";
                System.out.print("Ingrese el tiempo realizado (segundos): ");
                valor = Double.parseDouble(scanner.nextLine());
            }
            case "marcha" -> {
                tipo = "Distancia (km)";
                System.out.print("Ingrese los kilómetros recorridos: ");
                valor = Double.parseDouble(scanner.nextLine());
            }
            case "natación", "natacion" -> {
                tipo = "Tiempo (segundos)";
                System.out.print("Ingrese el tiempo realizado (segundos): ");
                valor = Double.parseDouble(scanner.nextLine());
            }
            case "boxeo" -> {
                tipo = "Victorias";
                System.out.print("Ingrese la cantidad de victorias: ");
                valor = Double.parseDouble(scanner.nextLine());
            }
            case "gimnasia" -> {
                tipo = "Puntuación";
                System.out.print("Ingrese la puntuación obtenida (0-10): ");
                valor = Double.parseDouble(scanner.nextLine());
            }
            case "judo" -> {
                tipo = "Victorias";
                System.out.print("Ingrese la cantidad de victorias: ");
                valor = Double.parseDouble(scanner.nextLine());
            }
            case "levantamientopesas", "levantamiento de pesas" -> {
                tipo = "Peso (kg)";
                System.out.print("Ingrese el peso levantado (kg): ");
                valor = Double.parseDouble(scanner.nextLine());
            }
            case "ciclismo" -> {
                tipo = "Tiempo (segundos)";
                System.out.print("Ingrese el tiempo realizado (segundos): ");
                valor = Double.parseDouble(scanner.nextLine());
            }
            default -> {
                tipo = "Rendimiento";
                System.out.print("Ingrese el valor del entrenamiento: ");
                valor = Double.parseDouble(scanner.nextLine());
            }
        }

        Entrenamiento entrenamiento = new Entrenamiento(fecha, tipo, valor);
        registroService.registrarEntrenamiento(atleta, entrenamiento);
        System.out.println(" Entrenamiento registrado exitosamente para " + atleta.getNombre() + " " + atleta.getApellido());
    }



    private void buscarAtletaPorNombre() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();

        Atleta atleta = registroService.buscarAtletaPorNombre(nombre, apellido);
        if (atleta == null) {
            System.out.println("No se encontró al atleta.");
        } else {
            System.out.println("Atleta encontrado: " + atleta);
        }
    }

    private void buscarAtletasPorDisciplina() {
        System.out.println("Seleccione la disciplina a buscar:");
        System.out.println("1. Carrera");
        System.out.println("2. Marcha");
        System.out.println("3. LevantamientoPesas");
        System.out.println("4. Natación");
        System.out.println("5. Ciclismo");
        System.out.println("6. Boxeo");
        System.out.println("7. Gimnasia");
        System.out.println("8. Judo");
        System.out.println("9. Fútbol");
        System.out.println("10. Baloncesto");
        System.out.print("Opción: ");

        int opcion = Integer.parseInt(scanner.nextLine());

        String disciplina = switch (opcion) {
            case 1 -> "Carrera";
            case 2 -> "Marcha";
            case 3 -> "LevantamientoPesas";
            case 4 -> "Natación";
            case 5 -> "Ciclismo";
            case 6 -> "Boxeo";
            case 7 -> "Gimnasia";
            case 8 -> "Judo";
            case 9 -> "Fútbol";
            case 10 -> "Baloncesto";
            default -> {
                System.out.println("Opción inválida.");
                yield "";
            }
        };

        if (disciplina.isEmpty()) return;

        List<Atleta> encontrados = registroService.buscarAtletasPorDisciplina(disciplina);
        if (encontrados.isEmpty()) {
            System.out.println("No se encontraron atletas en " + disciplina + ".");
        } else {
            System.out.println("Atletas en " + disciplina + ":");
            encontrados.forEach(System.out::println);
        }
    }


    private void verHistorial() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();

// aca verriico que este en los lugares necesarios
        // mensajes claros de errro y retuns para evitar la anidacion

        Atleta atleta = registroService.buscarAtletaPorNombre(nombre, apellido);
        if (atleta == null) {
            System.out.println("No se encontro al atleta.");
            return;
        }
//Aca esta para calcular el promedio donde voy tomando las anteriores clases creadas y reutilizo el codigo
        //con metodos ya existentes
        List<Entrenamiento> historial = registroService.obtenerEntrenamientos(atleta);
        if (historial.isEmpty()) {
            System.out.println("No hay entrenamientos registrados.");
        } else {
            historial.forEach(System.out::println);
        }
    }

    private void calcularPromedio() {
        Atleta atleta = pedirAtleta();
        if (atleta == null) return;

        List<Entrenamiento> entrenamientos = registroService.obtenerEntrenamientos(atleta);
        double promedio = analisisService.calcularPromedio(entrenamientos);
        System.out.println("Promedio de rendimiento: " + promedio);
    }

    private void verMejorMarca() {
        Atleta atleta = pedirAtleta();
        if (atleta == null) return;

        List<Entrenamiento> entrenamientos = registroService.obtenerEntrenamientos(atleta);
        if (entrenamientos.isEmpty()) {
            System.out.println("No hay entrenamientos registrados.");
            return;
        }
        Entrenamiento mejor = analisisService.mejorMarca(entrenamientos, atleta.getDisciplina());
        System.out.println("Mejor marca: " + mejor);
    }

    private void verEvolucion() {
        Atleta atleta = pedirAtleta();
        if (atleta == null) return;

        List<Entrenamiento> entrenamientos = registroService.obtenerEntrenamientos(atleta);
        if (entrenamientos.isEmpty()) {
            System.out.println("No hay entrenamientos registrados.");
            return;
        }
        List<Entrenamiento> evolucion = analisisService.evolucion(entrenamientos, atleta.getDisciplina());
        System.out.println("Evolución del rendimiento:");
        evolucion.forEach(System.out::println);
    }

    private void guardarCSV() {
        System.out.print("Nombre del archivo CSV a guardar: ");
        String archivo = scanner.nextLine();
        csvService.guardar(archivo, registroService.getRegistros());
        System.out.println("Datos guardados en " + archivo);
    }

    private void cargarCSV() {
        System.out.print("Nombre del archivo CSV a cargar: ");
        String archivo = scanner.nextLine();
        Map<Atleta, List<Entrenamiento>> datos = csvService.cargar(archivo);
        for (Atleta atleta : datos.keySet()) {
            registroService.registrarAtleta(atleta);
            for (Entrenamiento e : datos.get(atleta)) {
                registroService.registrarEntrenamiento(atleta, e);
            }
        }
        System.out.println("Datos cargados desde " + archivo);
    }

    private Atleta pedirAtleta() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        return registroService.buscarAtletaPorNombre(nombre, apellido);
    }
}
