CREATE DATABASE todolist_2h;

use todolist_2h;

create table aktivitas (
    nama VARCHAR(255) NOT NULL,
    selesai BOOLEAN DEFAULT FALSE,
    waktu_ditambahkan DATETIME DEFAULT NOW(),
    deadline DATETIME NULL,
    waktu_selesai DATETIME NULL
);