package cn.itcast.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.service.brand.BrandService;
import cn.itcast.core.test.Brand;

@Controller
@RequestMapping(value="brand")
public class BrandController {
	
	@Autowired
	private BrandService brandService;
	
	/**
	 * 批量删除
	 */
	@RequestMapping(value="deleteAll")
	public String deleteAll(Long[] ids, String name, Integer isDisplay, Integer pageNo, Model model){
		
		brandService.deleteAll(ids);
		
		return "forward:/brand/brandlist.action";
	}
	
	/**
	 *  修改品牌信息
	 */
	@RequestMapping(value="update")
	public String update(Brand brand){
		
		brandService.update(brand);
		
		return "redirect:/brand/brandlist.action";
	}
	
	/**
	 * 根据id 查询品牌信息
	 */
	@RequestMapping(value="edit")
	public String edit(Integer id, Model model){
		
		Brand brand = brandService.findBrandById(id);
		
		model.addAttribute("brand", brand);
		
		return "brand/edit";
	}
	
	/**
	 * 品牌列表  
	 * @return 
	 */
	@RequestMapping(value="brandlist")
	public String brandlist(Integer pageNo,String name,Integer isDisplay, Model model){
		
//		List<Brand> brandList = brandService.brandList();
//		model.addAttribute("brandList", brandList);
		
		//查询
//		List<Brand> brandListQuery = brandService.brandListQuery(pageNo, name, isDisplay);
//		model.addAttribute("brandList", brandListQuery);
		
		Pagination pagination = brandService.brandListQuery(pageNo, name, isDisplay);
		model.addAttribute("pagination", pagination);
		
		//查询数据回显
		model.addAttribute("name", name );
		model.addAttribute("isDisplay", isDisplay );
		model.addAttribute("pageNo", pagination.getPageNo() );
		
		return "brand/list";
	}
	
	
	@RequestMapping(value="product_main")
	public String product_main(){
		return "frame/product_main";
	}
	@RequestMapping(value="product_left")
	public String product_left(){
		return "frame/product_left";
	}
	
	
}
