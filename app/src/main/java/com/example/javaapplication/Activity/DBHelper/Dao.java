package com.example.javaapplication.Activity.DBHelper;

import com.example.javaapplication.Activity.Model.Data.UserModel;
import com.example.javaapplication.Activity.UserEntity;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

public interface Dao {

    @Insert
    void insert(UserEntity model);

    @Update
    void update(UserEntity model);

    @Delete
    void delete(UserEntity model);

//    @Query("DELETE FROM course_table")
//    void deleteAllCourses();
//
//    @Query("SELECT * FROM course_table ORDER BY courseName ASC")
//    LiveData<List<CourseModal>> getAllCourses();


}
