package cn.itcast.core.service.cart;

import java.util.Map;

public interface BuyerCartService {

	/**
	 * 将购物车存到redis
	 */
	public void addBuyerCartToRedis(Long skuId, Integer amount, String username) throws Exception;
	
	
	/**
	 * 从redis中去出购物车
	 */
	public Map<String, String> findBuyerCartFromRedis(String username) throws Exception;
}
