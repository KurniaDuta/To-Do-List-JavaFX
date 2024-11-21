package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.core.Aktivitas;
import org.example.core.ToDoListModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class MainController {
    @FXML
    private Button btnTambah;
    @FXML
    private TextField txtKegiatan;
    @FXML
    private DatePicker dpDeadline;
    @FXML
    private TableView<Aktivitas> tblDaftarKegiatan;
    @FXML
    private TableColumn<Aktivitas, String> colNama;
    @FXML
    private TableColumn<Aktivitas, Date> colWaktuDitambahkan;
    @FXML
    private TableColumn<Aktivitas, Date> colDeadline;
    @FXML
    private TableColumn<Aktivitas, Date> colWaktuSelesai;
    @FXML
    private TableColumn<Aktivitas, Boolean> colStatus;

    private ToDoListModel model;
    private ObservableList<Aktivitas> aktivitasList;

    @FXML
    public void initialize() {
        model = new ToDoListModel();
        aktivitasList = FXCollections.observableArrayList();
        setupTableColumns();
        loadAktivitas();
    }

    private void setupTableColumns() {
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colWaktuDitambahkan.setCellValueFactory(new PropertyValueFactory<>("waktuDitambahkan"));
        colDeadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        colWaktuSelesai.setCellValueFactory(new PropertyValueFactory<>("waktuSelesai"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("selesai"));

        tblDaftarKegiatan.setItems(aktivitasList);
    }

    private void loadAktivitas() {
        try {
            aktivitasList.clear();
            aktivitasList.addAll(model.getAllAktivitas());
        } catch (SQLException e) {
            showError("Error loading aktivitas: " + e.getMessage());
        }
    }

    public void onBtnTambah_Action(ActionEvent actionEvent) {
        String kegiatan = this.txtKegiatan.getText().trim();

        if (!kegiatan.isEmpty()) {
            try {
                Aktivitas aktivitas = new Aktivitas(kegiatan);

                LocalDate deadlineDate = dpDeadline.getValue();
                if (deadlineDate != null) {
                    Date deadline = Date.from(deadlineDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    aktivitas.setDeadline(deadline);
                }

                model.simpanAktivitas(aktivitas);
                loadAktivitas();
                this.txtKegiatan.clear();
                this.dpDeadline.setValue(null);
            } catch (SQLException e) {
                showError("Error saving aktivitas: " + e.getMessage());
            }
        } else {
            showWarning("Mohon masukkan nama kegiatan!");
        }
    }

    public void onBtnTandaiSelesai_Action(ActionEvent actionEvent) {
        Aktivitas selectedAktivitas = tblDaftarKegiatan.getSelectionModel().getSelectedItem();

        if (selectedAktivitas != null) {
            try {
                model.updateAktivitasStatus(selectedAktivitas.getNama(), true);
                loadAktivitas();
            } catch (SQLException e) {
                showError("Error updating aktivitas: " + e.getMessage());
            }
        } else {
            showWarning("Mohon pilih kegiatan yang akan ditandai selesai!");
        }
    }

    public void onBtnTandaiHapus_Action(ActionEvent actionEvent) {
        Aktivitas selectedAktivitas = tblDaftarKegiatan.getSelectionModel().getSelectedItem();

        if (selectedAktivitas != null) {
            try {
                model.deleteAktivitas(selectedAktivitas.getNama());
                loadAktivitas();
            } catch (SQLException e) {
                showError("Error deleting aktivitas: " + e.getMessage());
            }
        } else {
            showWarning("Mohon pilih kegiatan yang ingin dihapus!");
        }
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.show();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
}