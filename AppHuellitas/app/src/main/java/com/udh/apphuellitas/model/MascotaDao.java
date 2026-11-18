package com.udh.apphuellitas.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MascotaDao {
    @Insert
    void insertar(Mascota mascota);

    @Update
    int actualizar(Mascota mascota);

    @Delete
    int eliminar(Mascota mascota);

    @Query("SELECT * FROM mascotas")
    List<Mascota> listar();

    @Query("SELECT * FROM mascotas WHERE idMascota = :id")
    Mascota buscarPorId(String id);

}
