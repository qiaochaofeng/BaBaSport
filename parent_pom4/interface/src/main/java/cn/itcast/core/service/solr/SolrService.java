package cn.itcast.core.service.solr;

import cn.itcast.common.page.Pagination;

public interface SolrService {

	/**
	 * 全文搜索
	 */
	public Pagination seachrProduct(Integer pageNo, String keyword) throws Exception;
}
