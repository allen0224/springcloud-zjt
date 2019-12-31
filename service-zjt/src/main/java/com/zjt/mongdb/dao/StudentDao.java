package com.zjt.mongdb.dao;


import com.zjt.mongdb.pojo.Student;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface StudentDao {


    void save(Student student);

    void update(Student student);

    List<Student> findAll();

    void delete(Integer id);

    List<Student> query(String searchkey);

    List<Student> queryByRang(String begin, String end ,String sex) throws ParseException;
}
