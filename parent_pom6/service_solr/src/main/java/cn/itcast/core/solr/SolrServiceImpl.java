package cn.itcast.core.solr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.common.page.Pagination;
import cn.itcast.constant.Constants;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.product.SkuQuery;
import cn.itcast.core.dao.product.ProductDao;
import cn.itcast.core.dao.product.SkuDao;
import cn.itcast.core.service.solr.SolrService;
import redis.clients.jedis.Jedis;

@Service(value="solrServiceImpl")
public class SolrServiceImpl implements SolrService {

	@Autowired
	private SolrServer solrServer;
	
	//注入jedis
	@Autowired
	private Jedis jedis;
	
	//注入ProductDao
	@Autowired
	private ProductDao productDao;
	
	//注入SkuDao
	@Autowired
	private SkuDao skuDao;
	
	/**
	 * 将商品存入 solr中
	 */
	@Override
	public void insertProductIntoSolr(Long id) throws Exception {
		
		SolrInputDocument docs = new SolrInputDocument();
		
		//商品ID
		docs.setField("id", id.toString() );
		Product product2 = productDao.selectByPrimaryKey(id);
		//商品名称
		docs.setField("name_ik", product2.getName());
		//品牌ID
		docs.setField("brandId", Integer.valueOf(product2.getBrandId().toString()));
		//商品照片
		docs.setField("url", product2.getImgUrl());
		//商品价格
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.setFields("price");
		skuQuery.setOrderByClause("price asc");
		skuQuery.setPageNo(1);
		skuQuery.setPageSize(1);
		
		cn.itcast.core.bean.product.SkuQuery.Criteria criteria = skuQuery.createCriteria();
		criteria.andProductIdEqualTo(id);
		List<Sku> skuList = skuDao.selectByExample(skuQuery);
		Sku sku = skuList.get(0);
		
		docs.addField("price", sku.getPrice());
		
		solrServer.add(docs);
		solrServer.commit();
		
	}
	
	/**
	 * 从redis 中获取品牌信息
	 */
	@Override
	public List<Brand> getBrandFromRedis() throws Exception {
		
		List<Brand> brandList = new ArrayList<Brand>();
		
		Map<String, String> hgetAll = jedis.hgetAll(Constants.REDIS_BRANDIDS);
		Set<Entry<String, String>> entrySet = hgetAll.entrySet();
		
		if(entrySet != null && entrySet.size() > 0){
			for (Entry<String, String> entry : entrySet) {
				String brandId = entry.getKey();
				String brandName = entry.getValue();
				
				Brand brand = new Brand();
				brand.setId(Long.valueOf(brandId));
				brand.setName(brandName);
				brandList.add(brand);
			}
		}
		
		return brandList;
	}
	
	@Override
	public Pagination seachrProduct(Long brandId, String price, Integer pageNo, String keyword) throws Exception {
		
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
		
		//设置高亮
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("name_ik");
		solrQuery.setHighlightSimplePre("<span style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</span>");
		
		//设置排序
		solrQuery.setSort("price", ORDER.desc);
		
		//设置过滤条件
		if(brandId != null && !"".equals(brandId)){
			solrQuery.addFilterQuery("brandId:"+brandId); //品牌
			params.append("&brandId=").append(String.valueOf(brandId));
		}
		
		if(price != null && !"".equals(price)){
			String[] splitPrice = price.split("-");
			if(splitPrice.length == 2){
				solrQuery.addFilterQuery("price:["+splitPrice[0]+" TO "+splitPrice[1]+"]");
				params.append("&price=").append(price);
			}else{
				solrQuery.addFilterQuery("price:["+splitPrice[0]+" TO *]");
				params.append("&price=").append(price);
			}
			
		}
		
		QueryResponse queryResponse = solrServer.query(solrQuery);
		SolrDocumentList results = queryResponse.getResults();
		
		List<Product> productList = new ArrayList<Product>();
		
		long totalCount = 0;
		if(results != null && results.size() > 0){
			//结果总数
			totalCount = results.getNumFound();
			
			for (SolrDocument solrDocument : results) {
				
				//取出高亮
				Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
				
				String name = "";
				if(highlighting != null){
					List<String> list = highlighting.get(solrDocument.get("id")).get("name_ik");
					if(list != null){
						name = list.get(0);
					}else{
						name = solrDocument.get("name_ik").toString();
					}
				}
				
				Product product = new Product();
				product.setId(Long.valueOf(solrDocument.get("id").toString()));
				product.setName(name);
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
