package cn.itcast.core.service.sku;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.product.SkuQuery;
import cn.itcast.core.bean.product.SkuQuery.Criteria;
import cn.itcast.core.dao.product.SkuDao;

@Service(value="skuServiceImpl")
@Transactional
public class SkuServiceImpl implements SkuService {

	@Autowired
	private SkuDao skuDao;
	
	/**
	 * 库存信息修改
	 */
	@Override
	public void update(Sku sku) {
		
		skuDao.updateByPrimaryKeySelective(sku);
		
	}
	
	/**
	 * 根据商品Id查询库存信息
	 */
	@Override
	public List<Sku> findSkuListByProductId(Long productId) throws Exception {
		
		SkuQuery skuQuery = new SkuQuery();
		Criteria criteria = skuQuery.createCriteria();
		
		criteria.andProductIdEqualTo(productId);
		
		List<Sku> skuList = skuDao.selectByExample(skuQuery);
		
		return skuList;
	}

}
