
package com.udh.apphuellitas.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DietaDao {

    @Insert
    void insertar(Dieta dieta);

    @Update
    int actualizar(Dieta dieta);

    @Delete
    int eliminar(Dieta dieta);

    @Query("SELECT * FROM dietas WHERE idMascota = :idMascota")
    List<Dieta> listarPorMascota(String idMascota);

    @Query("SELECT * FROM dietas WHERE idDieta = :id")
    Dieta buscarPorId(String id);
}
