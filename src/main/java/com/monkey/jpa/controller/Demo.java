package com.monkey.jpa.controller;

import com.monkey.jpa.dto.Customer;
import com.monkey.jpa.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: JJC
 * @createTime: 2018/11/28
 */
@RestController
public class Demo {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("hello")
    public String hello() {
        return "hello jpa";
    }

    @GetMapping("init")
    /**
     * 设置超时时间10秒
     */
    @Transactional(rollbackFor = Exception.class, timeout = 10)
    public void init() {
        customerRepository.save(new Customer("Jack1", "Jones1"));
        customerRepository.save(new Customer("Jack1", "Jones11"));
        customerRepository.save(new Customer("Jack2", "Jones2"));
        customerRepository.save(new Customer("Jack3", "Jones3"));
        customerRepository.save(new Customer("Jack4", "Jones4"));
        customerRepository.save(new Customer("Jack5", "Jones5"));
        customerRepository.save(new Customer("Jack6", "Jones6"));
    }

    @GetMapping("findAll")
    public void findAll() {
        List<Customer> customerIterator = customerRepository.findAll();
        customerIterator.forEach(s -> System.out.println(s));
    }

    @GetMapping("findOne")
    public void findOne() {
        /**
         * Optional类：Java8新特性
         */
        Optional<Customer> customer = customerRepository.findById(1L);
        System.out.println(customer.orElseGet(null));
    }

    @GetMapping("findByFirstName")
    public void findByFirstName() {
        /**
         * Optional类：Java8新特性
         */
        List<Customer> customer = customerRepository.findCustomerByFirstNameOrderByLastName("Jack1");
        customer.forEach(s -> System.out.println(s));
    }

    @GetMapping("findByFirstNameOrLastName")
    public void findByFirstNameOrLastName() {
        List<Customer> customer = customerRepository.findByLastnameOrFirstname2("Jack1", "Jones1");
        customer.forEach(s -> System.out.println(s));
    }

    @GetMapping("update")
    public void updateFirstNameByLastName() {
        customerRepository.updateLastNameByFirstName("Jack1", "Jones3");
        List<Customer> customer = customerRepository.findCustomerByFirstNameOrderByLastName("Jack1");
        customer.forEach(s -> System.out.println(s));
    }

    @GetMapping("delete")
    public void deleteALLByFirstName() {
        System.out.println(customerRepository.deleteCustomersByFirstName("Jack1"));
    }

    @GetMapping("delete2")
    public void deleteALLByFirstName2() {
        System.out.println(customerRepository.deleteByName("Jack2"));
    }

    @GetMapping("findFirstNameOnly")
    public void findFirstNameOnly() {
        customerRepository.findFirstNameOnly().forEach(e -> System.out.println(e));
    }

    @GetMapping("findDistinctByFirstName")
    public void findDistinctByFirstName() {
        customerRepository.findDistinctByFirstName("Jack1").forEach(e -> System.out.println(e));
    }

//    @GetMapping("findDistinctFirstNameOnly")
//    public void findDistinctFirstNameOnly() {
//        customerRepository.findDistinctFirstNameOnly().forEach(e -> System.out.println(e));
//    }

    @GetMapping("findByAsArrayAndSort")
    public void findByAsArrayAndSort() {
        System.out.println(
//                customerRepository.findByAsArrayAndSort("Jon", new Sort("fn_len")).toString()
                customerRepository.findByAsArrayAndSort("Jon", JpaSort.unsafe("LENGTH(firstName)")).toString()
        );
    }

    @GetMapping("findByAndSort")
    public void findByAndSort() {
        //new Sort("firstName")
        customerRepository.findByAndSort("Jon", JpaSort.unsafe("LENGTH(firstName)")).forEach(e -> System.out.println(e));
    }

}
