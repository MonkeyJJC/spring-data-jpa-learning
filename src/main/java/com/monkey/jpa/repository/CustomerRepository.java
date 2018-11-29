package com.monkey.jpa.repository;

import com.monkey.jpa.dto.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Stream;

/**
 * @description: 操作数据的Repository对象
 * JpaRepository<T, ID>: T是被管理的实体类，ID是该类的主键
 * @author: JJC
 * @createTime: 2018/11/28
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * 预定义查询，允许自定义的原生SQL查询
     * @param firstName
     * @return
     */
    Customer findByFirstName(String firstName);

    /**
     * 根据JPA命名规范创建方法，会自动生成sql
     * @param firstName
     * @return
     */
    List<Customer> findCustomerByFirstNameOrderByLastName(String firstName);

    List<Customer> findCustomerByFirstNameStartingWith(String firstName);

    Customer findCustomerByFirstNameAndLastName(String firstName, String LastName);

    /**
     * 使用命名参数绑定， =：后接的是@Param命名定义的参数
     * 由于进行了@Entity的映射，因此查询时的字段名使用Repository的字段名，而不是数据库表名
     * @param lastname
     * @param firstname
     * @return
     */
    @Query("select u from Customer u where u.firstName = :firstname or u.lastName = :lastname")
    List<Customer> findByLastnameOrFirstname1(@Param("lastname") String lastname,
                                   @Param("firstname") String firstname);

    /**
     * 必须添加nativeQuery = true表示本地查询，就是使用原生的sql语句
     * 由于是本地sql，因此字段名要使用数据库表中的字段名
     * @param lastname
     * @param firstname
     * @return
     */
    @Query(value = "select * from Customer where first_name = ?1 or last_name = ?2", nativeQuery = true)
    List<Customer> findByLastnameOrFirstname2(String firstname, String lastname);

    /**
     * 以上注解都是实现查询功能，实现修改，删除，添加需要额外附加注释
     * (1)实现CrudRepository
     * (2)通过@Modifying注解完成修改操作（不支持新增）
     * 使用 = :时尤其需要注意，:是要紧接参数，不然无法识别，例如 :firstname可以识别，: firstname无法识别
     * 坑：springDataJpa中使用jpql完成 更新/删除操作 时 需要手动添加事务的支持
     */
    @Modifying
    /**
     * springDataJpa中使用jpql完成 更新/删除操作 时 需要手动添加事务的支持
     * readOnly设置是否只读
     */
    @Transactional(rollbackFor = Exception.class, readOnly = false)
    /**
     * 设置是否自动回滚
     */
    @Query(value = "update Customer c set c.lastName = :lastname where c.firstName = :firstname")
    int updateLastNameByFirstName(@Param("firstname") String firstname, @Param("lastname") String lastname);

    @Transactional(rollbackFor = Exception.class, readOnly = false)
    int deleteCustomersByFirstName(String firstName);

    @Query(value = "delete from Customer c where c.firstName = :firstName")
    @Transactional(rollbackFor = Exception.class, readOnly = false)
    /**
     * Whenever you are trying to modify a record in db, you have to mark it @Transactional as well as @Modifying,
     * which instruct Spring that it can modify existing records.
     */
    @Modifying
    int deleteByName(@Param("firstName") String firstName);

    /**
     * ====================查询部分字段==========================
     */
    /**
     * 只选取firstName字段，其他字段返回值为空
     * 坑： Customer类中需要有对应的构造函数，例如此处仅查询firstName字段，需要有Customer(String firstName)构造函数
     * @return
     */
    @Query(value = "select new Customer (c.firstName) from Customer c")
    List<Customer> findFirstNameOnly();

    List<Customer> findDistinctByFirstName(String firstName);

    /**
     * 排序
     * @param lastname
     * @param sort
     * @return
     */

//    @Query("select u.id, LENGTH(u.firstName) as fn_len from Customer u where u.lastName like ?1%")
//    List<Object[]> findByAsArrayAndSort(String lastname, Sort sort);
//    @Query("select LENGTH(u.firstName) as fn_len from Customer u where u.lastName like ?1%")
//    List<Integer> findByAsArrayAndSort(String lastname, Sort sort);
    @Query("select u.firstName from Customer u where u.lastName like ?1%")
    List<String> findByAsArrayAndSort(String lastname, Sort sort);

    @Query("select u from Customer u where u.lastName like ?1%")
    List<Customer> findByAndSort(String lastname, Sort sort);

//    @Query(value = "select DISTINCT(first_name) from customer", nativeQuery = true)
//    List<Customer> findDistinctFirstNameOnly();

    /**
     * 支持java8的流操作
     */
    @Query("select c from Customer c")
    Stream<Customer> findAllByCustomQueryAndStream();

    Stream<Customer> readAllByFirstNameNotNull();


}
