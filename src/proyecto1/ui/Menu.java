package proyecto1.ui;

import proyecto1.model.Atleta;
import proyecto1.model.Entrenamiento;
import proyecto1.model.Pago;
import proyecto1.service.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private final RegistroService registroService;
    private final AnalisisService analisisService;
    private final CSVService csvService;
    private final JSONService jsonService;
    private final GestionFinanciera gestionFinanciera;
    private final DBService dbService;
    private final Scanner scanner;

    public Menu() {
        this.registroService = new RegistroService();
        this.analisisService = new AnalisisService();
        this.csvService = new CSVService();
        this.jsonService = new JSONService();
        this.gestionFinanciera = new GestionFinanciera(registroService);

        // EN EL MENU SE CONECTA A MI LOCAL HOST DE MARIADB

        this.dbService = new DBService(
                "jdbc:mariadb://127.0.0.1:3306/proyecto1db",
                "root",
                "root"
        );

        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        int opcion;
        do {
            mostrarOpciones();
            opcion = Integer.parseInt(scanner.nextLine());
// SE AGREGARON LAS NUEVAS FUNCIONES AL MENU
            switch (opcion) {
                case 1 -> registrarAtleta();
                case 2 -> registrarEntrenamiento();
                case 3 -> buscarAtletaPorNombre();
                case 4 -> buscarAtletasPorDisciplina();
                case 5 -> estadisticasMenu();
                case 6 -> compararNacionalVsInternacional();
                case 7 -> gestionFinancieraMenu();
                case 8 -> guardarJson();
                case 9 -> cargarJson();
                case 10 -> exportCSV();
                case 11 -> guardarEnDB();
                case 12 -> cargarDesdeDB();
                case 0 -> System.out.println("Saliendo...");
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
        System.out.println("5. Estadísticas (historial, promedio, mejor, evolución)");
        System.out.println("6. Comparar rendimiento nacional vs internacional");
        System.out.println("7. Gestión financiera (calcular/registrar pagos, historial)");
        System.out.println("8. Guardar atletas + entrenamientos (JSON)");
        System.out.println("9. Cargar atletas + entrenamientos (JSON)");
        System.out.println("10. Exportar reportes (CSV)");
        System.out.println("11. Guardar todo en MariaDB");
        System.out.println("12. Cargar datos desde MariaDB");
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
        System.out.print("Nacionalidad: ");
        String nacionalidad = scanner.nextLine();
        System.out.print("Fecha de ingreso al comité (YYYY-MM-DD): ");
        LocalDate fechaIngreso = LocalDate.parse(scanner.nextLine());

        Atleta atleta = new Atleta(nombre, apellido, edad, disciplina, departamento, nacionalidad, fechaIngreso);
        registroService.registrarAtleta(atleta);
        System.out.println("Atleta registrado: " + atleta);
    }


    private void registrarEntrenamiento() {
        System.out.print("Nombre del atleta: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido del atleta: ");
        String apellido = scanner.nextLine();

        Atleta atleta = registroService.buscarAtletaPorNombre(nombre, apellido);
        if (atleta == null) {
            System.out.println("Atleta no encontrado.");
            return;
        }

        System.out.print("Fecha (YYYY-MM-DD): ");
        LocalDate fecha = LocalDate.parse(scanner.nextLine());

        // ubicación
        System.out.print("Ubicación (1=Nacional, 2=Internacional): ");
        int ubiOpt = Integer.parseInt(scanner.nextLine());
        boolean internacional = ubiOpt == 2;
        String pais = "";
        if (internacional) {
            System.out.print("País: ");
            pais = scanner.nextLine();
        }

        String disciplina = atleta.getDisciplina().toLowerCase();
        String tipo;
        double valor;

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

        Entrenamiento entrenamiento = new Entrenamiento(fecha, tipo, valor, internacional, pais);
        registroService.registrarEntrenamiento(atleta, entrenamiento);
        System.out.println("Entrenamiento registrado.");
    }


    private void buscarAtletaPorNombre() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        Atleta a = registroService.buscarAtletaPorNombre(nombre, apellido);
        System.out.println(a == null ? "No encontrado" : a);
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
            default -> "";
        };
        if (disciplina.isEmpty()) { System.out.println("Opción inválida"); return; }
        List<Atleta> encontrados = registroService.buscarAtletasPorDisciplina(disciplina);
        if (encontrados.isEmpty()) System.out.println("No se encontraron atletas en " + disciplina);
        else encontrados.forEach(System.out::println);
    }
//CREE EL SUB MENU DE ESTADISTICAS PARA VERIRICAR EL PROYECTO Y ACORTAR EL MENU

    private void estadisticasMenu() {
        System.out.println("Estadísticas:");
        System.out.println("1. Ver historial de entrenamientos");
        System.out.println("2. Calcular promedio");
        System.out.println("3. Ver mejor marca");
        System.out.println("4. Ver evolución en el tiempo");
        System.out.print("Opción: ");
        int op = Integer.parseInt(scanner.nextLine());
        switch (op) {
            case 1 -> verHistorial();
            case 2 -> calcularPromedio();
            case 3 -> verMejorMarca();
            case 4 -> verEvolucion();
            default -> System.out.println("Opción inválida");
        }
    }

    private void verHistorial() {
        Atleta atleta = pedirAtleta();
        if (atleta == null) return;
        List<Entrenamiento> historial = registroService.obtenerEntrenamientos(atleta);
        if (historial.isEmpty()) System.out.println("No hay entrenamientos registrados.");
        else historial.forEach(System.out::println);
    }

    private void calcularPromedio() {
        Atleta atleta = pedirAtleta();
        if (atleta == null) return;
        double promedio = analisisService.calcularPromedio(registroService.obtenerEntrenamientos(atleta));
        System.out.println("Promedio: " + promedio);
    }

    private void verMejorMarca() {
        Atleta atleta = pedirAtleta();
        if (atleta == null) return;
        Entrenamiento mejor = analisisService.mejorMarca(registroService.obtenerEntrenamientos(atleta), atleta.getDisciplina());
        System.out.println(mejor == null ? "No hay registros" : "Mejor: " + mejor);
    }

    private void verEvolucion() {
        Atleta atleta = pedirAtleta();
        if (atleta == null) return;
        List<Entrenamiento> evo = analisisService.evolucion(registroService.obtenerEntrenamientos(atleta), atleta.getDisciplina());
        evo.forEach(System.out::println);
    }

    //ESTO LO REQUERIA EL PROYECTO
    //COMPARAR INTERNACIONAL CON NACIONAL PARA VER LA MEJOR MARCA

    private void compararNacionalVsInternacional() {
        Atleta atleta = pedirAtleta();
        if (atleta == null) return;
        List<Entrenamiento> todos = registroService.obtenerEntrenamientos(atleta);
        if (todos.isEmpty()) { System.out.println("No hay entrenamientos"); return; }

        double avgNac = analisisService.calcularPromedio(todos.stream().filter(e -> !e.isInternacional()).toList());
        double avgInt = analisisService.calcularPromedio(todos.stream().filter(Entrenamiento::isInternacional).toList());
//METODO PRA HACER EL PROMEDIO
        System.out.println("Promedio nacional: " + avgNac);
        System.out.println("Promedio internacional: " + avgInt);
        if (avgInt == 0 && avgNac == 0) System.out.println("No hay datos comparables");
        else if (avgInt > avgNac) System.out.println("Mejor rendimiento en internacional");
        else if (avgNac > avgInt) System.out.println("Mejor rendimiento en nacional");
        else System.out.println("Rendimiento similar");
    }

// NUEVO MENÚ PARA REALIZAR LOS PAGOS Y GUARDAR DATOS EN CSV
    private void gestionFinancieraMenu() {
        System.out.println("Gestión financiera:");
        System.out.println("1. Calcular pago mensual de un atleta");
        System.out.println("2. Registrar pago (guardar historial)");
        System.out.println("3. Ver historial de pagos");
        System.out.println("4. Exportar historial de pagos a CSV");
        System.out.print("Opción: ");
        int op = Integer.parseInt(scanner.nextLine());
        switch (op) {
            case 1 -> calcularPagoMensual();
            case 2 -> registrarPagoManual();
            case 3 -> verHistorialPagos();
            case 4 -> exportarHistorialPagosCSV();
            default -> System.out.println("Opción inválida");
        }
    }

    private void calcularPagoMensual() {
        Atleta atleta = pedirAtleta();
        if (atleta == null) return;
        System.out.print("Año (YYYY): ");
        int y = Integer.parseInt(scanner.nextLine());
        System.out.print("Mes (1-12): ");
        int m = Integer.parseInt(scanner.nextLine());
        double pago = gestionFinanciera.calcularPagoMensual(atleta, y, m);
        System.out.println("Pago para " + YearMonth.of(y, m) + ": Q" + pago);
    }

    private void registrarPagoManual() {
        Atleta atleta = pedirAtleta();
        if (atleta == null) return;
        System.out.print("Año (YYYY): ");
        int y = Integer.parseInt(scanner.nextLine());
        System.out.print("Mes (1-12): ");
        int m = Integer.parseInt(scanner.nextLine());
        YearMonth ym = YearMonth.of(y, m);
        double monto = gestionFinanciera.calcularPagoMensual(atleta, y, m);
        System.out.print("¿Registrar pago por Q" + monto + " ? (s/n): ");
        String r = scanner.nextLine();
        if (r.equalsIgnoreCase("s")) {
            Pago p = gestionFinanciera.registrarPago(atleta, ym, monto, "Pago calculado automatico");
            System.out.println("Pago registrado: " + p);
        } else System.out.println("No registrado");
    }

    private void verHistorialPagos() {
        List<Pago> h = gestionFinanciera.getHistorialPagos();
        if (h.isEmpty()) System.out.println("No hay pagos registrados");
        else h.forEach(System.out::println);
    }

    private void exportarHistorialPagosCSV() {
        System.out.print("Nombre archivo CSV: ");
        String archivo = scanner.nextLine();
        gestionFinanciera.exportHistorialCSV(archivo, csvService);
        System.out.println("Exportado a " + archivo);
    }

// ACA ESTA LOS METODOS PARA GUARDAR EN JSON

    private void guardarJson() {
        System.out.print("Nombre archivo JSON: ");
        String archivo = scanner.nextLine();
        jsonService.guardar(archivo, registroService.getRegistros());
        System.out.println("Guardado en " + archivo);
    }

    private void cargarJson() {
        System.out.print("Nombre archivo JSON: ");
        String archivo = scanner.nextLine();
        Map<Atleta, List<Entrenamiento>> datos = jsonService.cargar(archivo);
        for (Atleta a : datos.keySet()) {
            registroService.registrarAtleta(a);
            for (Entrenamiento e : datos.get(a)) registroService.registrarEntrenamiento(a, e);
        }
        System.out.println("Cargado desde " + archivo);
    }

    //EXPORTACIONES A CSV
    private void exportCSV() {
        System.out.print("Nombre archivo CSV: ");
        String archivo = scanner.nextLine();
        csvService.guardar(archivo, registroService.getRegistros());
        System.out.println("Exportado a " + archivo);
    }

    //GUARDAR Y CARGAR EN MARIADB
    private void guardarEnDB() {
        System.out.println("Guardando en MariaDB (asegúrate de configurar URL/usuario/contraseña en Menu.java)...");
        try {
            dbService.guardarTodos(registroService.getRegistros());
            System.out.println("Guardado en DB");
        } catch (Exception e) {
            System.out.println("Error guardando en DB: " + e.getMessage());
        }
    }
    private void cargarDesdeDB() {
        System.out.println("Cargando datos desde MariaDB...");
        Map<Atleta, List<Entrenamiento>> datos = dbService.cargarTodos();
        for (Atleta a : datos.keySet()) {
            registroService.registrarAtleta(a);
            for (Entrenamiento e : datos.get(a)) {
                registroService.registrarEntrenamiento(a, e);
            }
        }
        System.out.println("Datos cargados desde la base de datos.");
    }


    private Atleta pedirAtleta() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        Atleta atleta = registroService.buscarAtletaPorNombre(nombre, apellido);
        if (atleta == null) System.out.println("Atleta no encontrado");
        return atleta;
    }
}
