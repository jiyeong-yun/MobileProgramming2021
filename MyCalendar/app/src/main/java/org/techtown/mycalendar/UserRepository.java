package org.techtown.mycalendar;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserRepository {
    @Query("SELECT * FROM data")
    List<Data> findAll();

    @Query("SELECT * FROM data where uid=:uid")
    Data findById(int uid);

    @Insert
    void insert(Data data);

    @Query("DELETE FROM data WHERE uid=:uid")
    void delete(int uid);

    /*@Delete
    void delete(Data data); //내부에 값을 넣어서 삭제 가능(오버로딩)*/
}
