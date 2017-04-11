package cn.itcast.core.service.ad;

import java.util.List;

import cn.itcast.core.bean.ad.Ad;

public interface AdService {

	/**
	 * 广告删除
	 */
	public void delete(Long id, String positionId) throws Exception;
	
	/**
	 * 从redis 获取广告信息
	 */
	public String findAdsFromRedis(Long positionId) throws Exception;
	
	/**
	 * 广告添加
	 */
	public void addAd(Ad ad) throws Exception;
	
	/**
	 * 根据positionId查询广告
	 */
	public List<Ad> adList(Long positionId) throws Exception;

}
