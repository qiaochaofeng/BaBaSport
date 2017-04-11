package cn.itcast.core.service.product;

import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.bean.product.ProductQuery.Criteria;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.product.SkuQuery;
import cn.itcast.core.dao.product.ProductDao;
import cn.itcast.core.dao.product.SkuDao;
import redis.clients.jedis.Jedis;

@Controller(value="productSeriveImpl")
@Transactional
public class ProductSeriveImpl implements ProductService{

	@Autowired
	private ProductDao productDao;
	
	//注入库存dao
	@Autowired
	private SkuDao skuDao;
	
	//注入jedis
	@Autowired
	private Jedis jedis;
	
	//注入solr服务器
	@Autowired
	private SolrServer solrServer;
	
	//注入 jmsTemplate
	@Autowired
	private JmsTemplate jmsTemplate;
	
	/**
	 * 根据商品ID 查询商品信息
	 */
	@Override
	public Product findProductById(Long id) throws Exception {
		return productDao.selectByPrimaryKey(id);
	}
	
	
	/**
	 * 商品上架
	 */
	@Override
	public void isShow(Long[] ids) throws Exception {
		
		if(ids != null && ids.length > 0){
			Product product = new Product();
			product.setIsShow(true);
			for (final Long id : ids) {
				product.setId(id);
				productDao.updateByPrimaryKeySelective(product);
				
				//发送消息
				jmsTemplate.send(new MessageCreator(){

					@Override
					public Message createMessage(Session session) throws JMSException {
						TextMessage textMessage = session.createTextMessage(String.valueOf(id));
						return textMessage;
					}
					
				});
				
				
				//将商品存入 solr中 (将此段代码使用ActiveMQ消息中间插件完成)
				
			}
		}
		
	}
	
	/**
	 * 商品添加
	 */
	@Override
	public void add(Product product) {
		
		//使用jedis将商品ID设置为全国唯一     事先在redis中设置productId, 值初始值为500
		Long id = jedis.incr("productId");
		//设置商品id
		product.setId(id);
		
		//保存到商品表中
		product.setCreateTime(new Date());
		product.setIsDel(true);
		product.setIsShow(false);
		productDao.insertSelective(product);
		
		//将有关信息保存到库存表中
		String colors = product.getColors();//所选颜色
		String sizes = product.getSizes(); //所选尺寸
		String[] splitColors = colors.split(",");
		String[] splitSizes = sizes.split(",");
		for (String color : splitColors) {
			for (String size : splitSizes) {
				Sku sku = new Sku();
				sku.setProductId(product.getId());
				sku.setColorId(Long.valueOf(color));
				sku.setSize(size);
				sku.setDeliveFee(10f);
				sku.setMarketPrice(0f);
				sku.setPrice(0f);
				sku.setStock(0);
				sku.setUpperLimit(0);
				sku.setCreateTime(new Date());
				
				skuDao.insertSelective(sku);
			}
		}
		
		
		
	}
	
	
	/**
	 * 商品列表
	 * 上下架:0否 1是
	 */
	@Override
	public Pagination productList(Integer pageNo, String name,Long brandId, Boolean isShow) throws Exception {
		
		StringBuilder params = new StringBuilder();
		
		ProductQuery productQuery = new ProductQuery();
		Criteria criteria = productQuery.createCriteria();
		
		if(null != name){
			criteria.andNameLike("%"+name+"%");
			params.append("name=").append(name);
		}
		if(null != brandId){
			criteria.andBrandIdEqualTo(brandId);
			params.append("&brandId=").append(String.valueOf(brandId));
		}
		if(null != isShow){
			criteria.andIsShowEqualTo(isShow);
			params.append("&isShow=").append(isShow);
		}else{
			criteria.andIsShowEqualTo(true);
			params.append("&isShow=").append(true);
		}
		
		int totalCount = productDao.countByExample(productQuery);
		
		productQuery.setPageNo(Pagination.cpn(pageNo));
		productQuery.setPageSize(5);
		
		
		List<Product> productList = productDao.selectByExample(productQuery);
		
		Pagination pagination = new Pagination(
				productQuery.getPageNo(), 
				productQuery.getPageSize(), 
				totalCount, 
				productList);
		
		String url = "/product/productlist.action";
		pagination.pageView(url, params.toString());
		
		
		return pagination;
	}

}
