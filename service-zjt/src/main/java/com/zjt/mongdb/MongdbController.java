package com.zjt.mongdb;

import com.zjt.mongdb.dao.StudentDao;
import com.zjt.mongdb.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//支持配置中心属性热加载
@RefreshScope
@RestController
@RequestMapping("/demo/config")
public class MongdbController {
    @Autowired
    private StudentDao studentDao;

    /**
     * 查询所有信息
     */
    @GetMapping("/findAll")
    public void findAll() {
        List<Student> all = studentDao.findAll();
        System.out.println(all.size());
    }

    /**
     * 新增信息
     */
    @GetMapping("/save")
    public void save(@RequestParam("id") Long id,
                     @RequestParam("name") String name,
                     @RequestParam("sex") String sex,@RequestParam("age") String age,
                     @RequestParam("introduce") String introduce,@RequestParam("height") Long height,@RequestParam("creatTime") String creatTime) throws ParseException {
        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        student.setSex(sex);
        student.setIntroduce(introduce);
        student.setHeight(height);
        student.setCreatTime(format.parse(creatTime));
        studentDao.save(student);
    }

    /**
     * 修改信息
     */
    @GetMapping("/update")
    public void update() {
        Student student = new Student();
        student.setId(6l);
        student.setName("吴很帅");
        studentDao.update(student);
    }

    /**
     * 删除信息
     */
    @GetMapping("/delete")
    public void delete() {
        studentDao.delete(3);
    }
    /**
     * 查询所有信息
     */
    @GetMapping("/query")
    public void query(@RequestParam("searchkey") String searchkey ) {
        List<Student> all = studentDao.query(searchkey);
        System.out.println(all.toString());
    }
    /**
     * 范围查询
     */
    @GetMapping("/queryByRang")
    public void queryByRang(@RequestParam("begin") String begin, @RequestParam("end") String end,@RequestParam("sex") String sex) throws ParseException {
        List<Student> all = studentDao.queryByRang(begin,end,sex);
        System.out.println(all.toString());
    }
}