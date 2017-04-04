package cn.itcast.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.service.solr.SolrService;

@Controller
public class PortalController {

	@Autowired
	private SolrService solrService;
	
	/**
	 * 前台搜索
	 */
	@RequestMapping(value="seachProduct")
	public String seachProduct(Integer pageNo, String keyword, Model model) throws Exception{
		
		Pagination pagination = solrService.seachrProduct(pageNo, keyword);
		model.addAttribute("pagination", pagination);
		model.addAttribute("keyword", keyword);
		
		return "search";
	}
	
	/**
	 * 跳转到前台首页
	 * @return
	 */
	@RequestMapping(value="/")
	public String toIndexPage(){
		return "index";
	}
}
