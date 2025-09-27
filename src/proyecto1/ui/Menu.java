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

        // ACA AJUSTAMOS EL PROYECTO PARA QUE SE CONECTE A MARIADB CONECTANDO EL SERVIDOR CON EL HOST  LOCAL USANDO EL CLIENTE Y CONTRASEÑA
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

            switch (opcion) {
                case 1 -> registrarAtleta();
                case 2 -> registrarEntrenamiento();
                case 3 -> buscarAtletaPorNombre();
                case 4 -> buscarAtletasPorDisciplina();
                case 5 -> estadisticasMenu();
                case 6 -> gestionFinancieraMenu();
                case 7 -> guardarJson();
                case 8 -> cargarJson();
                case 9 -> exportCSV();
                case 10 -> guardarEnDB();
                case 11 -> cargarDesdeDB();
                case 0 -> System.out.println("SALIENDO DEL PROYECTO");
                default -> System.out.println("ERROR OPCION INVALIDA, INGRESE UNA CORRECTA");
            }
        } while (opcion != 0);
    }

    private void mostrarOpciones() {
        System.out.println("\n BIENVENIDO AL COMITE OLÍMPICO DE GUATEMALA");
        System.out.println("1. REGISTRAR ATLETAS");
        System.out.println("2. REGISTRAR ENTRENAMIENTO");
        System.out.println("3. BUSCAR ALTETAS POR NOMBRE Y APELIDO");
        System.out.println("4. BUSCAR ATLETAS POR DISCIPLINA");
        System.out.println("5. ESTADISTICAS (Historial, Promedio, Mejor Tiempo, Evolución e Internacional y Nacional)");
        System.out.println("7. GESTION FINANCIERA (Calcular/Registrar Pagos, Historial)");
        System.out.println("8. GUARDAR ENTRENAMIENTOS Y ATLETAS (ARCHIVO JSON)");
        System.out.println("9. CARGAR ATLETAS Y ENTRENAMIENTOS (ARCHIVO JSON)");
        System.out.println("10. EXPORTAR REPORTES (CSV)");
        System.out.println("11. GUARDAR TODA LA INFO EN MariaDB");
        System.out.println("12. CARGAR DATOS DESDE MARIADB");
        System.out.println("0. SALIR");
        System.out.print("POR FAVOR INGRESE UNA OPCION: ");
    }

    // Acá se registran los Atletas con Nacionalidad y Fecha de ingreso al comite
    private void registrarAtleta() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("Edad: ");
        int edad = Integer.parseInt(scanner.nextLine());

        // Menú de selección de disciplinas
        System.out.println("Seleccione la Disciplina:");
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
            case 9 -> "Futbol";
            case 10 -> "Baloncesto";
            default -> "Otra";
        };

        System.out.print("Nacionalidad: ");
        String nacionalidad = scanner.nextLine();
        System.out.println("Departamento: ");
        String departamento = scanner.nextLine();
        System.out.print("Fecha de ingreso al comité (YYYY-MM-DD): ");
        LocalDate fechaIngreso = LocalDate.parse(scanner.nextLine());

        Atleta atleta = new Atleta(nombre, apellido, edad, disciplina,nacionalidad, departamento, fechaIngreso);
        registroService.registrarAtleta(atleta);
        System.out.println("Su atleta ha sido registrado: " + atleta);
    }

    // REGISTRAR EL ENTRENAMIENTO
    private void registrarEntrenamiento() {
        System.out.print("Nombre del atleta: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido del atleta: ");
        String apellido = scanner.nextLine();

        Atleta atleta = registroService.buscarAtletaPorNombre(nombre, apellido);
        if (atleta == null) {
            System.out.println("ERROR ATLETA NO REGISTRADO.");
            return;
        }

        System.out.print("Fecha (YYYY-MM-DD): ");
        LocalDate fecha = LocalDate.parse(scanner.nextLine());

        // La Ubicacion del atleta
        System.out.print(" En donde se encuentra el entrenamiento:  (1=Nacional, 2=Internacional): ");
        int ubiOpt = Integer.parseInt(scanner.nextLine());
        boolean internacional = ubiOpt == 2;
        String pais = "";
        if (internacional) {
            System.out.print("En que país se encuentra: ");
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
        System.out.println("SU ENTRENAMIENTO HA SIDO REGISTRADO.");
    }

    // BUSCAR ATLETAS
    private void buscarAtletaPorNombre() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        Atleta a = registroService.buscarAtletaPorNombre(nombre, apellido);
        System.out.println(a == null ? "NO ENCONTRADO" : a);
    }

    // Buscar atleta por disciplina
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

    // NUEVA SECCION DE ESTADISTICAS
    private void estadisticasMenu() {
        System.out.println("Estadísticas:");
        System.out.println("1. Ver historial de entrenamientos");
        System.out.println("2. Calcular promedio");
        System.out.println("3. Ver mejor marca");
        System.out.println("4. Ver evolución en el tiempo");
        System.out.println("5. Calcular promedio internacional y nacional")
        System.out.print("Opción: ");
        int op = Integer.parseInt(scanner.nextLine());
        switch (op) {
            case 1 -> verHistorial();
            case 2 -> calcularPromedio();
            case 3 -> verMejorMarca();
            case 4 -> verEvolucion();
            case 5 -> compararNacionalVsInternacional();
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

    private void compararNacionalVsInternacional() {
        Atleta atleta = pedirAtleta();
        if (atleta == null) return;
        List<Entrenamiento> todos = registroService.obtenerEntrenamientos(atleta);
        if (todos.isEmpty()) { System.out.println("No hay entrenamientos"); return; }

        double avgNac = analisisService.calcularPromedio(todos.stream().filter(e -> !e.isInternacional()).toList());
        double avgInt = analisisService.calcularPromedio(todos.stream().filter(Entrenamiento::isInternacional).toList());

        System.out.println("Promedio nacional: " + avgNac);
        System.out.println("Promedio internacional: " + avgInt);
        if (avgInt == 0 && avgNac == 0) System.out.println("No hay datos comparables");
        else if (avgInt > avgNac) System.out.println("Mejor rendimiento en internacional");
        else if (avgNac > avgInt) System.out.println("Mejor rendimiento en nacional");
        else System.out.println("Rendimiento similar");
    }

    //GESTION FINANCIERA NUEVOOO
    private void gestionFinancieraMenu() {
        System.out.println("Gestión financiera:");
        System.out.println("1. Calcular pago mensual de un atleta");
        System.out.println("2. Registrar pago ");
        System.out.println("3. Ver historial de pagos");
        System.out.println("4. Exportar historial de pagos (CSV)");
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

    // JSON GURADAR DATOS
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

    //GUARDAR CSV

    private void exportCSV() {
        System.out.print("Nombre archivo CSV: ");
        String archivo = scanner.nextLine();
        csvService.guardar(archivo, registroService.getRegistros());
        System.out.println("Exportado a " + archivo);
    }

    //CARGAR Y GUARDAR EN MARIA DB

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


   // PEDIR ATLETA RESPALDO PARA REGISTRO
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
