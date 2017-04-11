package cn.itcast.core.service.message;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.service.cms.CmsService;
import cn.itcast.core.service.cms.StaticPageService;
import cn.itcast.core.service.color.ColorService;

public class CustomMessageListener implements MessageListener {

	//注入CmsService
	@Autowired
	private CmsService cmsService;
	
	//注入StaticPageService
	@Autowired
	private StaticPageService staticPageService;
	
	@Override
	public void onMessage(Message msg) {
		
		ActiveMQTextMessage atm = (ActiveMQTextMessage)msg;
		
		try {
			String productId = atm.getText();
			
			Product product = cmsService.findProductById(Long.valueOf(productId));
			
			List<Sku> skuList = cmsService.findSkuListByProductId(Long.valueOf(productId));
			
			//颜色结果集(去重)
			Set<Color> colors = new HashSet<Color>();
			
			if(null != skuList && skuList.size() > 0){
				for (Sku sku : skuList) {
					//商品价格
					product.setPrice(sku.getPrice());
					//商品颜色
					colors.add(sku.getColor());
				}
			}
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("product", product);
			map.put("colors", colors);
			map.put("skuList", skuList);
			
			staticPageService.createSaticPage(map, productId);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
