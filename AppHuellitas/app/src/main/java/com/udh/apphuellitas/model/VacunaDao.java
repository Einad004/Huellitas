package com.udh.apphuellitas.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VacunaDao {

    @Insert
    void insertar(Vacuna vacuna);

    @Query("SELECT * FROM vacunas WHERE idMascota = :idMascota")
    List<Vacuna> listarPorMascota(String idMascota);

    @Query("SELECT * FROM vacunas WHERE idVacuna = :id")
    Vacuna buscarPorId(String id);

    @Update
    int actualizar(Vacuna vacuna);

    @Delete
    int eliminar(Vacuna vacuna);

}
