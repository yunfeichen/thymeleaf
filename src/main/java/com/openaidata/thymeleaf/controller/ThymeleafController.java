package com.openaidata.thymeleaf.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.openaidata.thymeleaf.entity.Dept;
import com.openaidata.thymeleaf.entity.Emp;
import com.openaidata.thymeleaf.service.EmpService;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ThymeleafController {

    @Resource
    private EmpService empService = null;

    List<Emp> emps = new ArrayList<Emp>();
    List<Dept> depts = new ArrayList<Dept>();
    @Value("${app.uploaded.location}")
    private String path = null;
    public ThymeleafController() {
//        emps.add(new Emp(7782, "CLARK","MANAGER", 7839, "1981-06-09", 2450.00,null,10,"ACCOUNTING",null));
//        emps.add(new Emp(7839, "KING","PRESIDENT", null, "1981-11-17",5000.00,null,10,"ACCOUNTING",null));
//        emps.add(new Emp(7934, "MILLER","CLERK", 7839, "1981-06-09", 2450.00,null,10,"ACCOUNTING",null));
//        emps.add(new Emp(7369, "SMITH","CLERK", 7839, "1981-06-09", 2450.00,null,20,"RESEARCH",null));
//        emps.add(new Emp(7566, "JONES","MANAGER", 7839, "1981-06-09", 2450.00,null,20,"RESEARCH",null));
//        emps.add(new Emp(7788, "SCOTT","ANALYST", 7839, "1981-06-09", 2450.00,null,20,"RESEARCH",null));
//        emps.add(new Emp(7876, "ADAMS","CLERK", 7839, "1981-06-09", 2450.00,null,20,"RESEARCH",null));
//        emps.add(new Emp(7902, "FORD","ANALYST", 7839, "1981-06-09", 2450.00,null,20,"RESEARCH",null));
//        emps.add(new Emp(7499, "ALLEN","SALEMAN", 7839, "1981-06-09", 2450.00,null,30,"SALES",null));
//        emps.add(new Emp(7521, "WARD","SALEMAN", 7839, "1981-06-09", 2450.00,null,30,"SALES",null));
//        emps.add(new Emp(7654, "MARTIN","SALEMAN", 7839, "1981-06-09", 2450.00,null,30,"SALES",null));
//        emps.add(new Emp(7698, "BLAKE","MANAGER", 7839, "1981-06-09", 2450.00,null,30,"SALES",null));
//        emps.add(new Emp(7844, "TURNER","SALEMAN", 7839, "1981-06-09", 2450.00,null,30,"SALES",null));
//        emps.add(new Emp(7900, "JAMES","MANAGER", 7839, "1981-06-09", 2450.00,null,30,"SALES",null));

//        List<Emp> emps = empService.findAll();

//        depts.add(new Dept(10,"ACCOUNTING","NEW YORK"));
//        depts.add(new Dept(20,"RESEARCH","DALLAS"));
//        depts.add(new Dept(30,"SALES","CHICAGO"));
//        depts.add(new Dept(40,"OPERATIONS","NEW YORK"));
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(String keyword) {
        ModelAndView mav = new ModelAndView("index");
        List filter = new ArrayList();
        List<Emp> emps = empService.findEmpAll();



        if (keyword == null || keyword.trim().equals("")) {
            filter = emps;
        } else {
            for(Emp emp : emps) {
                if (emp.getEname().toLowerCase().indexOf(keyword.toLowerCase()) != -1) {
                    filter.add(emp);
                }
            }
        }
        System.out.println(filter);
        mav.addObject("emps", filter);
        return mav;

    }

    @GetMapping("/dept")
    @ResponseBody
    public List<Dept> obtainDept() {
        List<Dept> newDepts = new ArrayList();
        List<Dept> depts = empService.findDetpAll();
        newDepts.add(new Dept(-1,"请选择","1970-01-01"));
        newDepts.addAll(depts);
        return newDepts;
    }

    @GetMapping("/deptfind")
    @ResponseBody
    public Dept getDeptNo(String dname) {
        Dept depts = empService.findDetpNo(dname);
        return depts;
    }

    @GetMapping("/job")
    @ResponseBody
    public List<String> obtainJob(String d) {
        List<String> jobs = new ArrayList<String>();
        jobs.add("请选择");
        if(d.equals("RESEARCH")) {
            jobs.add("CTO");
            jobs.add("Programmer");
        }else if(d.equals("SALES")) {
            jobs.add("CSO");
            jobs.add("Saler");
        }else if (d.equals("ACCOUNT")) {
            jobs.add("CFO");
            jobs.add("Cashier");
        }
        return jobs;

    }

    @PostMapping("/create")
    public ModelAndView create(Emp emp, @RequestParam("photo") MultipartFile photo) throws IOException {
        String path = "D:\\电子书籍\\spring-boot\\SourceCode\\thymeleaf\\target\\classes\\static\\images\\";
        String filename = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String suffix = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf("."));
        if(!suffix.equals(".jpg") && !suffix.equals(".png")) {
            System.out.println(suffix);
            throw new RuntimeException("无效的图片格式");
        }
        emp.setPhotoFile("/images/" + filename + suffix);
        empService.create(emp);
        System.out.println(emp.getDname());
        System.out.println(emp.getJob());
        FileCopyUtils.copy(photo.getInputStream(), new FileOutputStream(path + filename + suffix));
        ModelAndView  mav = new ModelAndView("redirect:/");
        return mav;
    }

    @RequestMapping("/emp/update/{empno}")
    public ModelAndView update(@PathVariable("empno") Integer empno) {
        Emp emp = empService.findById(empno);
        emp.setSal(emp.getSal() * 2);
        empService.update(emp);
        ModelAndView  mav = new ModelAndView("redirect:/");

        return mav;
    }

    @RequestMapping("/emp/edit")
    @ResponseBody
    public ModelAndView edit(Integer empno) {
        Emp emp = empService.findById(empno);
        ModelAndView  mav = new ModelAndView("edit");
        mav.addObject(emp);
        return mav;
    }

    @RequestMapping("/emp/delete")
    @ResponseBody
    public void delete(Integer empno) {
        empService.delete(empno);
//        return "Delete success";
    }

    @RequestMapping("/emp/find")
    @ResponseBody
    public Emp findById(Integer empno) {
        return empService.findById(empno);
    }


    @RequestMapping("/all/page")
    @ResponseBody
    public PageInfo<Emp> findAllUser(
            @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize){
        return empService.findAllUser(pageNum,pageSize);

    }

    @RequestMapping(value = "/emp/list", method = RequestMethod.GET)
    @ResponseBody
    public List<Emp> list() {
        List<Emp> emps = empService.findEmpAll();
        return emps;
    }

    @RequestMapping(value = "/emp/imp", method = RequestMethod.GET)
//    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public void imp() {
        for (int i = 0; i < 100; i++) {
            Emp emp = new Emp();
            emp.setEname("chenyunfei" + i);
            emp.setJob("Engineer");
            emp.setMgr(7899);
            emp.setHiredate(new Date());
            emp.setSal(i * 1000f);
            emp.setComm(0f);
            emp.setDeptno(20);
            emp.setDname("RESEARCH");
            emp.setPhotoFile("D:/uploaded/111.jpg");

            empService.create(emp);
        }
    }

    @RequestMapping(value = "/allUsers")
    public String list(Model model, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "") String keyword) {

        //引入分页查询，使用PageHelper分页功能在查询之前传入当前页，然后多少记录
        PageHelper.startPage(pageNum, pageSize);
        //startPage后紧跟的这个查询就是分页查询
        List filter = new ArrayList();
        List<Emp> emps = empService.findEmpAll();

        if (keyword == null || keyword.trim().equals("")) {
            filter = emps;
        } else {
            for(Emp emp : emps) {
                if (emp.getEname().toLowerCase().indexOf(keyword.toLowerCase()) != -1) {
                    filter.add(emp);
                }
            }
        }

        //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
        PageInfo pageInfo = new PageInfo<Emp>(filter, 10);

        model.addAttribute("pageInfo", pageInfo);

        //获得当前页
        model.addAttribute("pageNum", pageInfo.getPageNum());
        //获得一页显示的条数
        model.addAttribute("pageSize", pageInfo.getPageSize());
        //是否是第一页
        model.addAttribute("isFirstPage", pageInfo.isIsFirstPage());
        //获得总页数
        model.addAttribute("totalPages", pageInfo.getPages());
        //是否是最后一页
        model.addAttribute("isLastPage", pageInfo.isIsLastPage());
        //keyword
        model.addAttribute("keyword", keyword);
        return "list";
    }
}
