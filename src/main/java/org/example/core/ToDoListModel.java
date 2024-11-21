package org.example.core;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ToDoListModel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASS = "";
    private Connection connection;

    public ToDoListModel() {
        try {
            this.connection = getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public void simpanAktivitas(Aktivitas aktivitas) throws SQLException {
        String sql = "INSERT INTO aktivitas (nama, selesai, waktu_ditambahkan, deadline, waktu_selesai) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setString(1, aktivitas.getNama());
            ps.setBoolean(2, aktivitas.isSelesai());
            ps.setTimestamp(3, new Timestamp(aktivitas.getWaktuDitambahkan().getTime()));

            if (aktivitas.getDeadline() != null) {
                ps.setTimestamp(4, new Timestamp(aktivitas.getDeadline().getTime()));
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }

            if (aktivitas.getWaktuSelesai() != null) {
                ps.setTimestamp(5, new Timestamp(aktivitas.getWaktuSelesai().getTime()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }

            ps.executeUpdate();
        }
    }

    public List<Aktivitas> getAllAktivitas() throws SQLException {
        List<Aktivitas> aktivitasList = new ArrayList<>();
        String sql = "SELECT * FROM aktivitas ORDER BY waktu_ditambahkan ASC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Aktivitas aktivitas = new Aktivitas(rs.getString("nama"));
                aktivitas.setSelesai(rs.getBoolean("selesai"));
                aktivitas.setWaktuDitambahkan(rs.getTimestamp("waktu_ditambahkan"));

                Timestamp deadline = rs.getTimestamp("deadline");
                if (!rs.wasNull()) {
                    aktivitas.setDeadline(deadline);
                }

                Timestamp waktuSelesai = rs.getTimestamp("waktu_selesai");
                if (!rs.wasNull()) {
                    aktivitas.setWaktuSelesai(waktuSelesai);
                }

                aktivitasList.add(aktivitas);
            }
        }
        return aktivitasList;
    }

    public void updateAktivitasStatus(String nama, boolean selesai) throws SQLException {
        String sql = "UPDATE aktivitas SET selesai = ?, waktu_selesai = ? WHERE nama = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, selesai);
            if (selesai) {
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            } else {
                ps.setNull(2, Types.DATE);
            }
            ps.setString(3, nama);
            ps.executeUpdate();
        }
    }

    public void deleteAktivitas(String nama) throws SQLException {
        String sql = "DELETE FROM aktivitas WHERE nama = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nama);
            ps.executeUpdate();
        }
    }
}