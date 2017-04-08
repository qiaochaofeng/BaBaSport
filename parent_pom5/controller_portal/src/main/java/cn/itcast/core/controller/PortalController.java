package cn.itcast.core.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.ad.Ad;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.service.ad.AdService;
import cn.itcast.core.service.color.ColorService;
import cn.itcast.core.service.product.ProductService;
import cn.itcast.core.service.sku.SkuService;
import cn.itcast.core.service.solr.SolrService;
import cn.itcast.json.JsonUtils;

@Controller
public class PortalController {

	@Autowired
	private SolrService solrService;
	
	//注入ProductService
	@Autowired
	private ProductService productService;
	
	//注入 SkuService
	@Autowired
	private SkuService skuService;
	
	//注入ColorService
	@Autowired
	private ColorService colorService;
	
	//注入AdService
	@Autowired
	private AdService adService;
	
	/**
	 * 商品详情
	 */
	@RequestMapping(value="/product/detail")
	public String productDetail(Long id, Model model) throws Exception{
		
		Product product = productService.findProductById(id);
		model.addAttribute("product", product);
		
		//颜色结果集(去重)
		Set<Long> colorIds = new HashSet<Long>();
		Set<Color> colors = new HashSet<Color>();
		
		List<Sku> skuList = skuService.findSkuListByProductId(id);
		
		if(null != skuList && skuList.size() > 0){
			for (Sku sku : skuList) {
				//商品价格
				product.setPrice(sku.getPrice());
				//商品颜色
				colorIds.add(sku.getColorId());
			}
		}
		
		for (Long colorId: colorIds) {
			Color color = colorService.findColorById(colorId);
			colors.add(color);
		}
		
		//颜色 
		model.addAttribute("colors", colors);
		
		model.addAttribute("skuList", skuList);
		
		return "product";
	}
	
	/**
	 * 前台搜索
	 */
	@RequestMapping(value="seachProduct")
	public String seachProduct(Long brandId, String price, Integer pageNo, String keyword, Model model) throws Exception{
		
		Pagination pagination = solrService.seachrProduct(brandId, price, pageNo, keyword);
		
		//从 redis 中获取品牌信息
		List<Brand> brandlist = solrService.getBrandFromRedis();
		model.addAttribute("brands", brandlist);
		
		model.addAttribute("pagination", pagination);
		model.addAttribute("keyword", keyword);
		model.addAttribute("brandId", brandId);
		model.addAttribute("price", price);

		//搜索结果回显
		Map<String,String> map = new HashMap<String,String>();
		
		if(brandlist != null && brandlist.size() > 0){
			for (Brand brand : brandlist) {
				if(brand.getId() == brandId){
					map.put("品牌是:", brand.getName());
				}
			}
		}
		
		if(price != null && !"".equals(price)){
			map.put("价格是:", price);
		}
		model.addAttribute("map", map);
		
		return "search";
	}
	
	/**
	 * 跳转到前台首页
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/")
	public String toIndexPage(Model model) throws Exception{
		
		//获取大广告信息
//		List<Ad> adList = adService.adList(89L);
		
		//将广告集合转换成要求的Json格式
//		String jsonStr = JsonUtils.objToJsonStr(adList);
		
		String jsonStr = adService.findAdsFromRedis(89L);
		
		model.addAttribute("ads", jsonStr);
		
		return "index";
	}
}
