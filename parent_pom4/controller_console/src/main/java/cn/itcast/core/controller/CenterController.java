package cn.itcast.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="center")
public class CenterController {

	@RequestMapping(value="index")
	public String index(){
		return "index";
	}
	@RequestMapping(value="top")
	public String top(){
		return "top";
	}
	@RequestMapping(value="main")
	public String main(){
		return "main";
	}
	@RequestMapping(value="left")
	public String left(){
		return "left";
	}
	@RequestMapping(value="right")
	public String right(){
		return "right";
	}
	
}
