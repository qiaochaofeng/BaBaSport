package cn.itcast.core.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.service.color.ColorService;
import cn.itcast.core.service.sku.SkuService;

@Controller
@RequestMapping(value="sku")
public class SkuController {

	@Autowired
	private SkuService skuService;
	
	//注入颜色service
	@Autowired
	private ColorService colorService;
	
	/**
	 * 库存信息修改
	 */
	@RequestMapping(value="update")
	public void update(Sku sku){
		skuService.update(sku);
	}
	
	/**
	 * 到库存页面
	 * @return 
	 */
	@RequestMapping(value="skuList")
	public String skuList(Long productId, Model model) throws Exception{
		
		//根据商品Id查询库存信息
		List<Sku> skuList = skuService.findSkuListByProductId(productId);
		
		//颜色集合
		List<Color> colorList = colorService.colorList();
		
		model.addAttribute("colorList", colorList);
		
		model.addAttribute("skuList", skuList);
		
		return "sku/list";
	}
}
