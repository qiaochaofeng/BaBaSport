package cn.itcast.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.bean.product.ProductQuery.Criteria;
import cn.itcast.core.dao.product.ProductDao;
import cn.itcast.core.service.TestTbProductService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:application-context.xml"})
public class TestTbProduct {

	@Autowired
	private TestTbProductService productService;
	
	@Autowired
	private ProductDao productDao;
	
	/**
	 * 通过条件查询 排序,分页,指定字段
	 */
	@Test
	public void testSelectByExample() throws Exception {
		
		ProductQuery query = new ProductQuery();
		Criteria criteria = query.createCriteria();
		
		//where条件
		criteria.andBrandIdEqualTo(2L);
		
		//排序
		query.setOrderByClause("id desc");
		//分页
		query.setPageNo(1);
		query.setPageSize(5);
		//指定字段
		query.setFields("id,name");
		
		List<Product> productList = productDao.selectByExample(query);
		for (Product product : productList) {
			System.out.println("==============="+product);
		}
	}
	
	/**
	 * 测试逆向工程 根据ID查询商品
	 * @throws Exception
	 */
	@Test
	public void testProduct() throws Exception {
		
		Product product = productService.findById(1L);
		System.out.println("===================="+product);
	}
}
