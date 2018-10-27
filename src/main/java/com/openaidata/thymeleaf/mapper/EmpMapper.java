package com.openaidata.thymeleaf.mapper;

import com.github.pagehelper.Page;
import com.openaidata.thymeleaf.entity.Dept;
import com.openaidata.thymeleaf.entity.Emp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {
	
	public Emp findById(Integer empno);

    public List<Emp> findEmpAll();

    public List<Dept> findDeptAll();
	
	public List<LinkedHashMap> findDepts(Map param);
	
	public void create(Emp emp);
	
	public void update(Emp emp);
	
	public void delete(Integer empno);

	public Dept findDeptNo(String dname);

    @Select("SELECT * FROM EMP")
	public List<Emp> findAllUser();
	

}
