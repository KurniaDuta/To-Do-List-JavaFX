package org.example;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.core.Aktivitas;
import org.example.core.ToDoListModel;

import java.sql.SQLException;

public class MainController implements ChangeListener<Aktivitas> {
    @FXML
    private Button btnTambah;
    @FXML
    private TextField txtKegiatan;
    @FXML
    private ListView<String> lstDaftarKegiatan;

    private ToDoListModel model;

    @FXML
    public void initialize() {
        model = new ToDoListModel();
        loadAktivitas();
    }

    private void loadAktivitas() {
        try {
            lstDaftarKegiatan.getItems().clear();
            var aktivitas = model.getAllAktivitas();
            for (int i = 0; i < aktivitas.size(); i++) {
                String prefix = aktivitas.get(i).isSelesai() ? "✓ " : "";
                lstDaftarKegiatan.getItems().add((i + 1) + ". " + prefix + aktivitas.get(i).getNama());
            }
        } catch (SQLException e) {
            showError("Error loading aktivitas: " + e.getMessage());
        }
    }

    public void onBtnTambah_Action(ActionEvent actionEvent) {
        String kegiatan = this.txtKegiatan.getText().trim();

        if (!kegiatan.isEmpty()) {
            try {
                Aktivitas aktivitas = new Aktivitas(kegiatan);
                model.simpanAktivitas(aktivitas);
                loadAktivitas();
                this.txtKegiatan.clear();
            } catch (SQLException e) {
                showError("Error saving aktivitas: " + e.getMessage());
            }
        } else {
            showWarning("Mohon masukkan nama kegiatan!");
        }
    }

    public void onBtnTandaiSelesai_Action(ActionEvent actionEvent) {
        int selectedIdx = lstDaftarKegiatan.getSelectionModel().getSelectedIndex();

        if (selectedIdx != -1) {
            try {
                String selectedItem = lstDaftarKegiatan.getItems().get(selectedIdx);
                String[] parts = selectedItem.split("\\. ", 2);
                if (parts.length == 2) {
                    String kegiatan = parts[1].replace("✓ ", "");
                    model.updateAktivitasStatus(kegiatan, true);
                    loadAktivitas();
                }
            } catch (SQLException e) {
                showError("Error updating aktivitas: " + e.getMessage());
            }
        } else {
            showWarning("Mohon pilih kegiatan yang akan ditandai selesai!");
        }
    }

    public void onBtnTandaiHapus_Action(ActionEvent actionEvent) {
        int selectedIdx = lstDaftarKegiatan.getSelectionModel().getSelectedIndex();

        if (selectedIdx != -1) {
            try {
                String selectedItem = lstDaftarKegiatan.getItems().get(selectedIdx);
                String[] parts = selectedItem.split("\\. ", 2);
                if (parts.length == 2) {
                    String kegiatan = parts[1].replace("✓ ", "");
                    model.deleteAktivitas(kegiatan);
                    loadAktivitas();
                }
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

    @Override
    public void changed(ObservableValue<? extends Aktivitas> observable, Aktivitas oldValue, Aktivitas newValue) {
        if (newValue != null) {
            this.txtKegiatan.setText(newValue.getNama());
        }
    }
}