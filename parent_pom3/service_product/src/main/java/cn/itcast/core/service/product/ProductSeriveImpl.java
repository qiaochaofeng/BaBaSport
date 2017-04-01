package cn.itcast.core.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.bean.product.ProductQuery.Criteria;
import cn.itcast.core.dao.product.ProductDao;

@Controller(value="productSeriveImpl")
@Transactional
public class ProductSeriveImpl implements ProductService{

	@Autowired
	private ProductDao productDao;
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
