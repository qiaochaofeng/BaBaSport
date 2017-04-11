package cn.itcast.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.constant.Constants;
import cn.itcast.core.bean.user.Buyer;
import cn.itcast.core.bean.user.BuyerQuery;
import cn.itcast.core.bean.user.BuyerQuery.Criteria;
import cn.itcast.core.dao.user.BuyerDao;
import cn.itcast.core.service.buyer.BuyerService;
import redis.clients.jedis.Jedis;

@Service(value="buyerServiceImpl")
@Transactional
public class BuyerServiceImpl implements BuyerService {

	@Autowired
	private BuyerDao buyerDao;
	
	@Autowired
	private Jedis jedis;
	
	/**
	 * 从redis中获取token
	 */
	@Override
	public String getTokenFromRedis(String token) throws Exception {
		
		String username = jedis.get(Constants.TOKEN+"_"+token);
		jedis.expire(Constants.TOKEN+"_"+token, 60*60);
		
		return username;
	}
	
	
	/**
	 * 将token存到red
	 */
	@Override
	public void setTokenToRedis(String token, String username) throws Exception {
		jedis.set(Constants.TOKEN+"_"+token, username);
		//设置token在redis中的存活时间     1h
		jedis.expire(Constants.TOKEN+"_"+token, 60*60);
		
	}
	
	/**
	 * 用户明验证
	 */
	@Override
	public Buyer findUserName(String username) throws Exception {

		BuyerQuery buyerQuery = new BuyerQuery();
		Criteria criteria = buyerQuery.createCriteria();
		criteria.andUsernameEqualTo(username);
		
		List<Buyer> buyerList = buyerDao.selectByExample(buyerQuery);
		if(buyerList != null && buyerList.size() > 0){
			return buyerList.get(0);
		}
		return null;
	}

}
