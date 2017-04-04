package cn.itcast.core.solr;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.service.solr.SolrService;

@Service(value="solrServiceImpl")
public class SolrServiceImpl implements SolrService {

	@Autowired
	private SolrServer solrServer;
	
	@Override
	public Pagination seachrProduct(Integer pageNo, String keyword) throws Exception {
		
		StringBuilder params = new StringBuilder();
		
		ProductQuery productQuery = new ProductQuery();
		productQuery.setPageNo(Pagination.cpn(pageNo));
		productQuery.setPageSize(8);
		
		SolrQuery solrQuery = new SolrQuery();
		
		//查询
		if(keyword != null && !"".equals(keyword)){
			solrQuery.setQuery("name_ik:"+keyword);
			params.append("keyword=").append(keyword);
		}else{
			solrQuery.setQuery("*:*");
		}
		
		//分页
		solrQuery.setStart(productQuery.getStartRow());
		solrQuery.setRows(productQuery.getPageSize());
		
		QueryResponse queryResponse = solrServer.query(solrQuery);
		SolrDocumentList results = queryResponse.getResults();
		
		List<Product> productList = new ArrayList<Product>();
		
		long totalCount = 0;
		if(results != null && results.size() > 0){
			//结果总数
			totalCount = results.getNumFound();
			
			for (SolrDocument solrDocument : results) {
				Product product = new Product();
				product.setId(Long.valueOf(solrDocument.get("id").toString()));
				product.setName(solrDocument.get("name_ik").toString());
				product.setBrandId(Long.valueOf(solrDocument.get("brandId").toString()));
				product.setImgUrl(solrDocument.get("url").toString());
				product.setPrice(Float.valueOf(solrDocument.get("price").toString()));
				
				productList.add(product);
			}
		}
		
		Pagination pagination = new Pagination(
				productQuery.getPageNo(), 
				productQuery.getPageSize(), 
				(int)totalCount, 
				productList);
		
		String url = "/seachProduct.action";
		pagination.pageView(url, params.toString());
		
		return pagination;
	}

}
