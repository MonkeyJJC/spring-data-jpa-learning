package com.monkey.jpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @description: Hibernate提供了Annotation注解方式，使得Hibernate的映射文件变得很方便管理，避免了传统的hbm.xml
 * hibernate注解参见： https://www.cnblogs.com/hoojjack/p/6568920.html
 * @author: JJC
 * @createTime: 2018/11/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 对实体注释,任何Hibernate映射对象都要有这个注释
 */
@Entity
/**
 * @NamedQuery 预定义查询,允许自定义的原生SQL查询
 *  这里的sql表达式里的表名要和当前的Entity一致，
 * 否则会找不到，报错！！！查询参数也要和实体进行对应起来，是firstName而不是first_name
 * ？加数字表示占位符，？1代表在方法参数里的第一个参数，区别于其他的index，这里从1开始
 */
@NamedQuery(name = "Customer.findByFirstName", query = "select c from Customer c where c.firstName = ?1")
public class Customer {
    /**
     * @Id 声明主键
     * @GeneratedValue 指定主键的生成策略，AUTO：根据数据库的不同使用上面三个
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Customer(String firstName) {
        this.firstName = firstName;
    }
}
