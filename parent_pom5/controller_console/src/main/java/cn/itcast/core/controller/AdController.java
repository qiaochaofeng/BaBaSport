package cn.itcast.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.bean.ad.Ad;
import cn.itcast.core.bean.ad.Position;
import cn.itcast.core.service.ad.AdService;
import cn.itcast.core.service.position.PositionService;

@Controller
@RequestMapping(value="ad")
public class AdController {

	@Autowired
	private AdService adService;
	
	@Autowired
	private PositionService positionService;
	
	/**
	 * 广告删除
	 */
	@RequestMapping(value="delete")
	public String delete(Long id, String positionId) throws Exception{
		
		adService.delete(id, positionId);
		
		return "forward:/ad/list.action?root="+positionId;
	}
	
	
	/**
	 * 广告添加
	 * @throws Exception 
	 */
	@RequestMapping(value="add")
	public String add(Ad ad, String positionId) throws Exception{
		
		adService.addAd(ad);
		
		return "forward:/ad/list.action?root="+positionId;
	}
	
	
	/**
	 * 到广告添加页面
	 */
	@RequestMapping(value="toAddPage")
	public String add(Long positionId, Model model){
		
		//根据id查询广告栏信息
		Position position = positionService.findPositionById(positionId);
		model.addAttribute("position", position);
		model.addAttribute("positionId", positionId);
		
		return "ad/add";
	}
	
	/**
	 * 根据positonId查询广告信息
	 * @return
	 * @throws Exception 
	 * @throws  
	 */
	@RequestMapping(value="list")
	public String list(String root, Model model) throws Exception{
		
		List<Ad> adList = adService.adList(Long.parseLong(root));
		
		model.addAttribute("list", adList);
		model.addAttribute("positionId", root);
		
//		return "position/tree";
		return "ad/list";
	}
	
	
	//跳转到广告左侧页面
	@RequestMapping(value="ad_left")
	public String ad_left(){
		return "frame/ad_left";
	}
}
