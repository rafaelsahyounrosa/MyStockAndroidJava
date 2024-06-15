package com.example.mystock.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mystock.model.Produto;

import java.util.List;

@Dao
public interface ProdutoDAO {

    @Insert
    long insert(Produto produto);

    @Delete
    int delete(Produto produto);

    @Update
    int update(Produto produto);

    @Query("SELECT * FROM produto ORDER BY nome ASC")
    List<Produto> queryAllAsc();

    @Query("SELECT * FROM produto ORDER BY nome DESC")
    List<Produto> queryAllDesc();

    @Query("SELECT * FROM produto WHERE id = :id")
    Produto queryForID(long id);
}
