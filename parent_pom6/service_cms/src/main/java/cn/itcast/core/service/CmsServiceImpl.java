package cn.itcast.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.product.SkuQuery;
import cn.itcast.core.bean.product.SkuQuery.Criteria;
import cn.itcast.core.dao.product.ColorDao;
import cn.itcast.core.dao.product.ProductDao;
import cn.itcast.core.dao.product.SkuDao;
import cn.itcast.core.service.cms.CmsService;
@Service(value="cmsServiceImpl")
@Transactional
public class CmsServiceImpl implements CmsService {

	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private SkuDao skuDao;
	
	@Autowired
	private ColorDao colorDao;
	
	/**
	 * 根据商品id查询商品信息
	 */
	@Override
	public Product findProductById(Long productId) throws Exception {
		return productDao.selectByPrimaryKey(productId);
	}
	

	/**
	 * 根据商品ID插叙库存信息
	 */
	@Override
	public List<Sku> findSkuListByProductId(Long productId) throws Exception {

		SkuQuery skuQuery = new SkuQuery();
		Criteria criteria = skuQuery.createCriteria();
		criteria.andProductIdEqualTo(productId);
		//查询有库存的
		criteria.andStockGreaterThan(0);
		
		List<Sku> skuList = skuDao.selectByExample(skuQuery);
		if(skuList != null && skuList.size() > 0){
			for (Sku sku : skuList) {
				sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
			}
		}
		
		return skuList;
	}

}
