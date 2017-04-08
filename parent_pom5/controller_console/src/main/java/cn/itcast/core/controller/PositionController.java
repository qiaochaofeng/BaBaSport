package cn.itcast.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.bean.ad.Position;
import cn.itcast.core.service.position.PositionService;

@Controller
@RequestMapping(value="position")
public class PositionController {
	
	@Autowired
	private PositionService positionService;
	
	/**
	 * 广告栏位
	 * @return
	 * @throws Exception 
	 */
	
	@RequestMapping(value="list")
	public String list(String root, Model model){
		
		List<Position> list = positionService.findPositionByParentId(Long.parseLong(root));
		model.addAttribute("list", list);
		
//		return "position/tree";
		return "position/list";
	}
	
	@RequestMapping(value="tree")
	public String positionTree(String root, Model model) throws Exception{
		
		List<Position> list;
		if("source".equals(root)){
			list = positionService.findPositionByParentId(0L);
		}else{
			list = positionService.findPositionByParentId(Long.parseLong(root));
		}
		
		model.addAttribute("list", list);
		
		return "position/tree";
	}
}
