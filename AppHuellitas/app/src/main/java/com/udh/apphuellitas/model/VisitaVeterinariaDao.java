
package com.udh.apphuellitas.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;

import java.util.List;

@Dao
public interface VisitaVeterinariaDao {

    @Insert
    void insertar(VisitaVeterinaria visita);

    @Update
    int actualizar(VisitaVeterinaria visita);

    @Delete
    int eliminar(VisitaVeterinaria visita);

    @Query("SELECT * FROM visitas_veterinarias WHERE idMascota = :idMascota ORDER BY fecha DESC")
    List<VisitaVeterinaria> listarPorMascota(String idMascota);

    @Query("SELECT * FROM visitas_veterinarias WHERE idVisita = :id")
    VisitaVeterinaria buscarPorId(String id);
}
