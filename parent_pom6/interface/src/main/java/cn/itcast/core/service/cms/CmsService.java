package cn.itcast.core.service.cms;

import java.util.List;

import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.Sku;

public interface CmsService {

	/**
	 * 根据商品id查询商品信息
	 */
	public Product findProductById(Long productId) throws Exception;
	
	
	/**
	 * 根据商品ID插叙库存信息
	 */
	public List<Sku> findSkuListByProductId(Long productId) throws Exception;
}
