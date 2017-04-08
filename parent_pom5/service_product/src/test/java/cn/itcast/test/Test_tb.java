package cn.itcast.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.core.dao.Test_tbDao;
import cn.itcast.core.service.Test_tbService;
import cn.itcast.core.test.TestTB;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:application-context.xml"})
public class Test_tb {

	@Autowired
	private Test_tbDao tbDao;
	
	@Autowired
	private Test_tbService tbService;
	
	@Test
	public void testTb() throws Exception {
		
		TestTB tb = new TestTB();
		tb.setName("张三");
		tb.setBirthday(new Date());
		
		tbDao.addTestTB(tb);
	}
	@Test
	public void tsteAddTb() throws Exception {
		
		TestTB tb = new TestTB();
		tb.setName("赵六");
		tb.setBirthday(new Date());
		
		tbService.addTestTB(tb);
	}
}
