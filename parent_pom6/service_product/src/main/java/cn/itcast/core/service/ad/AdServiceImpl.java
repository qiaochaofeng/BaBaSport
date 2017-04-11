package cn.itcast.core.service.ad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.itcast.core.bean.ad.Ad;
import cn.itcast.core.bean.ad.AdQuery;
import cn.itcast.core.bean.ad.AdQuery.Criteria;
import cn.itcast.core.dao.ad.AdDao;
import cn.itcast.core.dao.ad.PositionDao;
import cn.itcast.json.JsonUtils;
import redis.clients.jedis.Jedis;

@Service(value="adServiceImpl")
@Transactional
public class AdServiceImpl implements AdService {

	@Autowired
	private AdDao adDao;
	
	@Autowired
	private PositionDao positionDao;
	
	@Autowired
	private Jedis jedis;
	
	/**
	 * 广告删除
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@Override
	public void delete(Long id, String positionId) throws Exception {
		
		adDao.deleteByPrimaryKey(id);
		
		//从redis 中删除
		List<Ad> adList = new ArrayList<Ad>();
		String json = jedis.get("ads"+positionId);
		
		ObjectMapper om = new ObjectMapper();
		Ad[] ads = om.readValue(json, Ad[].class);
		for (Ad ad : ads) {
			if(id != ad.getId()){
				adList.add(ad);
			}
		}
		
		String newAds = JsonUtils.objToJsonStr(adList);
		jedis.set("ads"+positionId, newAds);
		
	}
	
	
	/**
	 * 从redis 获取广告信息
	 */
	@Override
	public String findAdsFromRedis(Long positionId) throws Exception {
		
		String ads = jedis.get("ads"+positionId);
		
		if(ads == null){
			AdQuery adQuery = new AdQuery();
			Criteria criteria = adQuery.createCriteria();
			criteria.andPositionIdEqualTo(positionId);
			
			List<Ad> adList = adDao.selectByExample(adQuery);
			
			if(adList != null && adList.size() > 0){
				ads = JsonUtils.objToJsonStr(adList);
				jedis.set("ads"+positionId, ads);
			}
		}
		
		return ads;
	}
	
	/**
	 * 广告添加
	 * @throws Exception 
	 */
	@Override
	public void addAd(Ad ad) throws Exception {
		
		adDao.insertSelective(ad);
		
		//将广告信息放到redis中
		List<Ad> adList = new ArrayList<Ad>();
		String json = jedis.get("ads"+ad.getPositionId());
		
		ObjectMapper om = new ObjectMapper();
		Ad[] ads = om.readValue(json, Ad[].class);
		for (Ad ad2 : ads) {
			adList.add(ad2);
		}
		adList.add(ad);
		
		jedis.set("ads"+ad.getPositionId(), JsonUtils.objToJsonStr(adList));
	}
	
	
	/**
	 * 根据positionId查询广告
	 */
	@Override
	public List<Ad> adList(Long positionId) throws Exception {
		
		AdQuery adQuery = new AdQuery();
		Criteria criteria = adQuery.createCriteria();
		criteria.andPositionIdEqualTo(positionId);
		
		List<Ad> adList = adDao.selectByExample(adQuery);
		
		if(adList != null && adList.size() > 0 ){
			for (Ad ad : adList) {
				ad.setPositionName(positionDao.selectByPrimaryKey(positionId).getName());
			}
		}
		
		return adList;
	}

}
