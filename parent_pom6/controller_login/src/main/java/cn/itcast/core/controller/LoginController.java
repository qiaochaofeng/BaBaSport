package cn.itcast.core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.itcast.EncodePasswordUtils;
import cn.itcast.SigleSignUtils;
import cn.itcast.core.bean.user.Buyer;
import cn.itcast.core.service.buyer.BuyerService;

@Controller
public class LoginController {

	@Autowired
	private BuyerService buyerService;
	
	/**
	 * 判断登入状态
	 * @throws Exception 
	 */
	@RequestMapping(value="isLogin")
	@ResponseBody
	public MappingJacksonValue isLogin(String callback, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//从cookie中获取token
		String token = SigleSignUtils.getToken(request, response);
		
		//从redis获取token
		String username = buyerService.getTokenFromRedis(token);
		
		int tag = 0;
		MappingJacksonValue mjv = new MappingJacksonValue(tag);
		
		if(username != null){
			tag = 1;
			mjv = new MappingJacksonValue(tag);
		}
		mjv.setJsonpFunction(callback);
		
		
		return mjv;
	}
	
	/**
	 * 登入验证
	 * @throws Exception 
	 */
	@RequestMapping(value="login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, HttpServletResponse response, String returnUrl, 
			String username, String password, Model model) throws Exception{
		
		//用户名
		if(username != null && !"".equals(username)){
			if(password != null && !"".equals(password)){
				Buyer buyer = buyerService.findUserName(username);
				if(buyer != null){
					if(buyer.getPassword().equals(EncodePasswordUtils.encodePassword(password))){
						//获取token 令牌
						String token = SigleSignUtils.getToken(request, response);
						//将token 放到redis中
						buyerService.setTokenToRedis(token, username);
						return "redirect:"+returnUrl;
					}else{
						model.addAttribute("error", "密码错误");
					}
				}else{
					model.addAttribute("error", "该用户名不存在");
				}
			}else{
				model.addAttribute("error", "密码不能为空");
			}
		}else{
			model.addAttribute("error", "用户名不能为空");
		}
		
		return "login";
	}
	
	/**
	 * 到登入页面
	 */
	@RequestMapping(value="login", method = RequestMethod.GET)
	public String toLogin(String returnUrl, Model model){
		model.addAttribute("returnUrl", returnUrl);
		return "login";
	}
}
