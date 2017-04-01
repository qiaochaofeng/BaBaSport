package cn.itcast.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="sku")
public class SkuController {

	/**
	 * 倒库存页面
	 * @return 
	 */
	@RequestMapping(value="skuList")
	public String skuList(){
		
		return "sku/list";
	}
}
