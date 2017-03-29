package cn.itcast.core.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.service.Test_tbService;
import cn.itcast.core.test.TestTB;

@Controller
public class TestTb {

	@Autowired
	private Test_tbService tbService;
	
	@RequestMapping(value="list")
	public String testTB(){
		
		TestTB tb = new TestTB();
		tb.setName("chao");
		tb.setBirthday(new Date());
		
		tbService.addTestTB(tb);
		
		return "index2";
	}
}
