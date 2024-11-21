package org.example.core;

import java.util.Date;

public class Aktivitas
{
    private String nama;
    private boolean selesai;
    private Date waktuDitambahkan;
    private Date deadline;
    private Date waktuSelesai;

    public Aktivitas(String nama) {
        this.nama = nama;
        this.selesai = false;
        this.waktuDitambahkan = new Date();
        this.deadline = null;
        this.waktuSelesai = null;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public boolean isSelesai() {
        return selesai;
    }

    public void setSelesai(boolean selesai) {
        this.selesai = selesai;
    }

    public Date getWaktuDitambahkan() {
        return waktuDitambahkan;
    }

    public void setWaktuDitambahkan(Date waktuDitambahkan) {
        this.waktuDitambahkan = waktuDitambahkan;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getWaktuSelesai() {
        return waktuSelesai;
    }

    public void setWaktuSelesai(Date waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
    }
}
