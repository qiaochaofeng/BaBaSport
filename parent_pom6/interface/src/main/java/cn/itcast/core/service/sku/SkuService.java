package cn.itcast.core.service.sku;

import java.util.List;

import cn.itcast.core.bean.product.Sku;

public interface SkuService {

	/**
	 * 根据skuid查询库存信息
	 */
	public Sku findSkuById(Long skuId) throws Exception;
	
	/**
	 * 库存信息修改
	 */
	public void update(Sku sku);

	/**
	 * 根据商品Id查询库存信息
	 */
	public List<Sku> findSkuListByProductId(Long productId) throws Exception;

}	

