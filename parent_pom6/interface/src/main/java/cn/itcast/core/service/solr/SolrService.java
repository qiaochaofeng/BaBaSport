package cn.itcast.core.service.solr;

import java.util.List;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;

public interface SolrService {

	/**
	 * 将商品存入 solr中
	 */
	public void insertProductIntoSolr(Long id) throws Exception;
	
	/**
	 * 从redis 中获取品牌信息
	 */
	public List<Brand> getBrandFromRedis() throws Exception;
	
	
	/**
	 * 全文搜索
	 */
	public Pagination seachrProduct(Long brandId, String price,Integer pageNo, String keyword) throws Exception;
}
