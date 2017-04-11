package cn.itcast.core.service.buyer;

import cn.itcast.core.bean.user.Buyer;

public interface BuyerService {
	
	/**
	 * 从redis中获取token
	 */
	public String getTokenFromRedis(String token) throws Exception;

	/**
	 * 将token存到redis
	 */
	public void setTokenToRedis(String token, String username) throws Exception;
	
	/**
	 * 用户明验证
	 */
	public Buyer findUserName(String username) throws Exception;
	
	
}
