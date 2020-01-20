package com.example.test.mydb.mydao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test.mydb.myentity.Users;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM Users")
    List<Users> getAll();

    @Query("SELECT * FROM Users WHERE name = :username and password=:password")
    List<Users> getAllbyName(String username,String password);

    @Insert
    void insert(Users users);

    @Delete
    void delete(Users users);

    @Update
    void update(Users users);
}
