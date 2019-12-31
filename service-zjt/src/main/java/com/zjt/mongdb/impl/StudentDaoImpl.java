package com.zjt.mongdb.impl;
import com.zjt.mongdb.dao.StudentDao;
import com.zjt.mongdb.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class StudentDaoImpl implements StudentDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 新增信息
     * @param student
     */
    @Override
    public void save(Student student) {
        mongoTemplate.save(student);
    }

    /**
     * 修改信息
     * @param student
     */
    @Override
    public void update(Student student) {
        //修改的条件
        Query query = new Query(Criteria.where("id").is(student.getId()));

        //修改的内容
        Update update = new Update();
        update.set("name",student.getName());

        mongoTemplate.updateFirst(query,update,Student.class);
    }

    /**
     * 查询所有信息
     * @return
     */
    @Override
    public List<Student> findAll() {
        return mongoTemplate.findAll(Student.class);
    }

    /**
     * 根据id查询所有信息
     * @param id
     */
    @Override
    public void delete(Integer id) {
        Student byId = mongoTemplate.findById(1,Student.class);
        mongoTemplate.remove(byId);
    }

    @Override
    public List <Student> query(String searchkey) {
        //用来封装所有条件的对象
        Query query = new Query();
        //用来构建条件
        Criteria criteria = new Criteria();
        //定义一个泛型集合，类型为 Criteria
        List<Criteria> criteriaList = new ArrayList<>();
        //定义一个无长度的数组，类型为 Criteria
        Criteria[] criteriaArray = {};
        //大于方法
        Criteria gt = Criteria.where("id").gt(1);
        //小于方法
        Criteria lt = Criteria.where("id").lt(4);
        if(gt!=null && lt!=null){
            criteriaList.add(gt);
            criteriaList.add(lt);
        }else if(gt!=null){
            query.addCriteria(gt);
        }else if(lt!=null){
            query.addCriteria(lt);
        }
        //是否有条件
        if(criteriaList.size()>0){
            //把无长度的数组实例出来，长度就位集合的个数
            criteriaArray = new Criteria[criteriaList.size()];
            for(int i = 0 ; i < criteriaList.size() ; i++){
                //把集合中的条件对象全部存入数组中
                criteriaArray[i] = criteriaList.get(i);
            }
        }
        query.addCriteria(new Criteria().andOperator(criteriaArray));
        return  mongoTemplate.find(query,Student.class,"student");
    }
    @Override
    public List <Student> queryByRang(String begin,String end ,String sex) throws ParseException {
        //多条件聚合查询
        String alias = "nowNum";
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("name").is("张三").and("sex").is("男")),
//Aggregation.match(Criteria.where("size").is(size)),
                Aggregation.group("age").count().as(alias)
        );
        AggregationResults<Map> results= mongoTemplate.aggregate(aggregation, "student", Map.class);
        List<Map> mappedResults = results.getMappedResults();
        if (mappedResults != null && mappedResults.size() > 0) {
            Integer num = (Integer) mappedResults.get(0).get(alias);
            System.out.println("num = " + num);
        }
        //范围查询
        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beginTime=format.parse(begin);
        Date endTime=format.parse(end);
        // 场景：查询指定时间段内，状态为1的数据
        // 入参条件 ：beginTime ，endTime ，statue
        // mongodb字段：time ， state

        // 存放条件的对象
                Query query= new Query();

        // 判断时间是否为空
        if(beginTime != null && endTime != null){
            // 添加大于开始时间小于结束时间的条件
            query.addCriteria(Criteria.where("creatTime").gte(beginTime).lte(endTime));
        }else{
            // 其中一个为空 分别进行判断
            if(beginTime != null){
                query.addCriteria(Criteria.where("creatTime").gte(beginTime));
            }
            if(endTime != null){
                query.addCriteria(Criteria.where("creatTime").lte(endTime));
            }
        }

        // 添加状态为1条件
        if(sex!=null){
            query.addCriteria(Criteria.where("sex").is(sex));
        }
//        query.skip(1).limit(2).with(Sort.by(Sort.Order.desc("id")));
        query.with(Sort.by(Sort.Order.desc("id")));
        return  mongoTemplate.find(query,Student.class,"student");
    }
}
