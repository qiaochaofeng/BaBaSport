package cn.itcast.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.dao.Test_tbDao;
import cn.itcast.core.test.TestTB;

@Service("Test_tbServiceImpl")
@Transactional
public class Test_tbServiceImpl implements Test_tbService {

	@Autowired
	private Test_tbDao tbDao;
	
	@Override
	public void addTestTB(TestTB tb) {
		tbDao.addTestTB(tb);
//		throw new RuntimeException();
	}

}
