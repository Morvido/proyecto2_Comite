package proyecto1.service;

import proyecto1.model.Atleta;
import proyecto1.model.Entrenamiento;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

//NUEVA CLASE PARA ALMACENAR DATOS EN TABLA EN MARIADB

public class DBService {
    private final String url;
    private final String user;
    private final String pass;

    public DBService(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    private Connection conn() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    //GUARDAR LOS DATOS DE ENTRENAMIENTO Y REGISTROS EN MARIADB
    public void guardarTodos(Map<Atleta, List<Entrenamiento>> registros) {
        String insertEntreno = "INSERT INTO entrenamientos (atleta_id, fecha, tipo, valor, internacional, pais) VALUES (?,?,?,?,?,?)";
        try (Connection c = conn()) {
            c.setAutoCommit(false);

            for (Atleta a : registros.keySet()) {
                long atletaId = findOrCreateAtleta(c, a);
                for (Entrenamiento e : registros.get(a)) {
                    try (PreparedStatement ps = c.prepareStatement(insertEntreno)) {
                        ps.setLong(1, atletaId);
                        ps.setDate(2, java.sql.Date.valueOf(e.getFecha()));
                        ps.setString(3, e.getTipo());
                        ps.setDouble(4, e.getValor());
                        ps.setBoolean(5, e.isInternacional());
                        ps.setString(6, e.getPais());
                        ps.executeUpdate();
                    }
                }
            }
            c.commit();
        } catch (SQLException ex) {
            System.out.println("Error DB guardar: " + ex.getMessage());
        }
    }

    private long findOrCreateAtleta(Connection c, Atleta a) throws SQLException {
        String find = "SELECT id FROM atletas WHERE nombre=? AND apellido=? AND disciplina=?";
        try (PreparedStatement ps = c.prepareStatement(find)) {
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getApellido());
            ps.setString(3, a.getDisciplina());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong("id");
            }
        }

        String insert = "INSERT INTO atletas (nombre, apellido, edad, disciplina, departamento, nacionalidad, fecha_ingreso) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = c.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getApellido());
            ps.setInt(3, a.getEdad());
            ps.setString(4, a.getDisciplina());
            ps.setString(5, a.getDepartamento());
            ps.setString(6, a.getNacionalidad());
            ps.setDate(7, java.sql.Date.valueOf(a.getFechaIngreso()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
            }
        }
        throw new SQLException("No se pudo insertar atleta");
    }

    //ESTA SECCION ES PARA CARGAR LOS DATOS DE LA TABLA DE MARIADB AL PROYECTO

    public Map<Atleta, List<Entrenamiento>> cargarTodos() {
        Map<Atleta, List<Entrenamiento>> registros = new HashMap<>();

        String sqlAtletas = "SELECT * FROM atletas";
        String sqlEntrenos = "SELECT * FROM entrenamientos WHERE atleta_id=?";

        try (Connection c = conn();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sqlAtletas)) {

            while (rs.next()) {
                Atleta atleta = new Atleta(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getInt("edad"),
                        rs.getString("disciplina"),
                        rs.getString("departamento"),
                        rs.getString("nacionalidad"),
                        rs.getDate("fecha_ingreso").toLocalDate()
                );

                List<Entrenamiento> entrenos = new ArrayList<>();
                try (PreparedStatement ps = c.prepareStatement(sqlEntrenos)) {
                    ps.setLong(1, rs.getLong("id"));
                    try (ResultSet rsE = ps.executeQuery()) {
                        while (rsE.next()) {
                            Entrenamiento e = new Entrenamiento(
                                    rsE.getDate("fecha").toLocalDate(),
                                    rsE.getString("tipo"),
                                    rsE.getDouble("valor"),
                                    rsE.getBoolean("internacional"),
                                    rsE.getString("pais")
                            );
                            entrenos.add(e);
                        }
                    }
                }
                registros.put(atleta, entrenos);
            }

        } catch (SQLException e) {
            System.out.println("Error DB cargar: " + e.getMessage());
        }
        return registros;
    }
}
