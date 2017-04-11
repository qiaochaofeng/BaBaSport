package cn.itcast.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.bean.product.ProductQuery.Criteria;
import cn.itcast.core.dao.product.ProductDao;

@Service(value="testTbProductServiceImpl")
@Transactional
public class TestTbProductServiceImpl implements TestTbProductService {

	@Autowired
	private ProductDao productDao;
	
	//根据 Id 查询商品
	@Override
	public Product findById(Long id) {
		
		Product product = productDao.selectByPrimaryKey(id);
		
		return product;
	}

}
