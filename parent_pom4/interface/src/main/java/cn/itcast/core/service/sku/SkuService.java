package cn.itcast.core.service.sku;

import java.util.List;

import cn.itcast.core.bean.product.Sku;

public interface SkuService {

	/**
	 * 库存信息修改
	 */
	void update(Sku sku);

	/**
	 * 根据商品Id查询库存信息
	 */
	List<Sku> findSkuListByProductId(Long productId) throws Exception;

}	

