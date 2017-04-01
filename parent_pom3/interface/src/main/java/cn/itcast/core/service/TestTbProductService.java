package cn.itcast.core.service;

import cn.itcast.core.bean.product.Product;

public interface TestTbProductService {
	
	//根据id查询商品
	public Product findById(Long id);
	
}
