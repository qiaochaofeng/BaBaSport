package cn.itcast.core;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.constant.Constants;
import cn.itcast.core.service.cart.BuyerCartService;
import redis.clients.jedis.Jedis;

@Service(value="buyerCartServiceImpl")
@Transactional
public class BuyerCartServiceImpl implements BuyerCartService {

	@Autowired
	private Jedis jedis;
	
	/**
	 * 将购物车存到redis
	 */
	@Override
	public void addBuyerCartToRedis(Long skuId, Integer amount, String username) throws Exception {
	/*	Map<String, String> buyCartMap = jedis.hgetAll(Constants.BUYER_CART+"_"+username);
		if(buyCartMap != null && buyCartMap.size() > 0){
			Set<Entry<String, String>> entrySet = buyCartMap.entrySet();
			for (Entry<String, String> entry : entrySet) {
				if(skuId == Long.parseLong(entry.getKey())){
					jedis.hincrBy(Constants.BUYER_CART+"_"+username, String.valueOf(skuId), amount);
				}
			}
		}
			
		jedis.hset(Constants.BUYER_CART+"_"+username, String.valueOf(skuId), String.valueOf(amount));*/
		
		//判断这个用户, 这个skuId在reids中的购物车是否存在
		if(jedis.hexists(Constants.BUYER_CART + "_" + username, String.valueOf(skuId))){
			//如果存在同款商品, 则数量进行追加
			jedis.hincrBy(Constants.BUYER_CART + "_" + username, String.valueOf(skuId), amount);
		} else {
			//不存在同款商品, 则将本商品加入到redis的购物车中
			jedis.hset(Constants.BUYER_CART + "_" + username, String.valueOf(skuId), String.valueOf(amount));
		}
		
	}

	/**
	 * 从redis中取出购物车
	 */
	@Override
	public Map<String, String> findBuyerCartFromRedis(String username) throws Exception {
		Map<String, String> buyCartMap = jedis.hgetAll(Constants.BUYER_CART+"_"+username);
		return buyCartMap;
	}

	
}
