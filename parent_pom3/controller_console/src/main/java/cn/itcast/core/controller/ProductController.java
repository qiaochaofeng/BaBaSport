package cn.itcast.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.service.brand.BrandService;
import cn.itcast.core.service.color.ColorService;
import cn.itcast.core.service.product.ProductService;
import cn.itcast.core.test.Brand;

@Controller
@RequestMapping(value="product")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	//注入品牌 service
	@Autowired
	private BrandService brandService;
	
	//注入颜色service
	@Autowired
	private ColorService colorService;
	
	/**
	 * 到商品添加 页面
	 */
	@RequestMapping(value="addProduct")
	public String addProduct(Model model){
		
		//商品品牌
		List<Brand> brandList = brandService.brandList();
		model.addAttribute("brandList", brandList);
		
		//颜色
		List<Color> colorList = colorService.colorList();
		
		model.addAttribute("colorList", colorList);
		
		return "/product/add";
	}
	
	/**
	 * 查询所有品牌信息(分页,带条件查询)
	 */
	@RequestMapping(value="productlist")
	public String productList(Integer pageNo, String name, Long brandId, Boolean isShow, Model model) throws Exception{
		
		List<Brand> brandList = brandService.brandList();
		model.addAttribute("brandList", brandList);
		
		Pagination pagination = productService.productList(pageNo, name, brandId, isShow);
		
		model.addAttribute("pagination", pagination);
		model.addAttribute("name", name);
		model.addAttribute("brandId", brandId);
		model.addAttribute("isShow", isShow);
		
		return "product/list";
	}
}
