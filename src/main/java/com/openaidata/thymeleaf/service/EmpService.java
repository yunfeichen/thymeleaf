package com.openaidata.thymeleaf.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.openaidata.thymeleaf.entity.Dept;
import com.openaidata.thymeleaf.entity.Emp;
import com.openaidata.thymeleaf.mapper.EmpMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.exit;

@Service
public class EmpService {
	@Resource
	private EmpMapper empMapper;

	
	public Emp findById(Integer empno) {
		return empMapper.findById(empno);
		
	}

	public List<Emp> findEmpAll() {
		return empMapper.findEmpAll();
	}

    public List<Dept> findDetpAll() {
        return empMapper.findDeptAll();
    }

	public List<LinkedHashMap> findDepts(String dname, Float sal) {
		Map param = new HashMap();
		param.put("pdname", dname);
		param.put("psal", sal);

		System.out.println(param);
		exit(0);
		return empMapper.findDepts(param);

	}
	
	public void create(Emp emp) {
		empMapper.create(emp);
	}
	
	public void update(Emp emp) {
		empMapper.update(emp);
	}
	
	public void delete(Integer empno) {
		empMapper.delete(empno);
	}

	public Dept findDetpNo(String dname) {
		return empMapper.findDeptNo(dname);
	}


	public PageInfo<Emp> findAllUser(Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
        List<Emp> userList= empMapper.findAllUser();
        PageInfo<Emp> pages = new PageInfo<>(userList);
        return pages;
	}

}
