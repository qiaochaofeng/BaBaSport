package cn.itcast.core.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.itcast.SigleSignUtils;
import cn.itcast.constant.Constants;
import cn.itcast.core.bean.cart.BuyerCart;
import cn.itcast.core.bean.cart.BuyerItem;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.service.buyer.BuyerService;
import cn.itcast.core.service.cart.BuyerCartService;
import cn.itcast.core.service.product.ProductService;
import cn.itcast.core.service.sku.SkuService;
import cn.itcast.json.JsonUtils;

@Controller
@RequestMapping(value="/shopping")
public class BuyerCartController {
	
	@Autowired
	private BuyerService buyerService;
	
	@Autowired
	private BuyerCartService buyerCartService;
	
	@Autowired
	private SkuService skuService;
	
	@Autowired
	private ProductService productService;
	
	/**
	 * 去购物车结算
	 * @throws Exception 
	 */
	@RequestMapping(value="account")
	public String buyerCartAccount(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{
		
//		1、从Request当中获取Cookies
		Cookie[] cookies = request.getCookies();
		
		BuyerCart buyerCart = null;
		
		if(cookies != null && cookies.length > 0){
			//2、遍历Cookie
			for (Cookie cookie : cookies) {
				//3.判断是否有购物车, 有进行下面操作, 无则不进行任何操作
				if(Constants.BUYER_CART.equals(cookie.getName())){
					String buyerCartJsonStr = cookie.getValue();
					ObjectMapper om = new ObjectMapper();
					buyerCart = om.readValue(buyerCartJsonStr, BuyerCart.class);
				}
			}
		}   
		
		//4.判断登入状态
		String token = SigleSignUtils.getToken(request, response);
		String username = buyerService.getTokenFromRedis(token);
		
			if(username != null){ //登入状态
				if(buyerCart != null){
					//将购物车添加到Redis中 清空Cookie
					List<BuyerItem> items = buyerCart.getItems();
					if(items != null && items.size() > 0){
						for (BuyerItem buyerItem : items) {
							buyerCartService.addBuyerCartToRedis(buyerItem.getSku().getId(), buyerItem.getAmount(), username);
						}
					}
					Cookie cookie = new Cookie(Constants.BUYER_CART,null);
					cookie.setMaxAge(0);
					cookie.setPath("/");
					
				}else{
					buyerCart = new BuyerCart();
				}
					
				//从Redis中取出所有购物车 
				Map<String, String> buyerCartMap = buyerCartService.findBuyerCartFromRedis(username);
				//4、将购物车中装满
				if(buyerCartMap != null && buyerCartMap.size() > 0){
					Set<Entry<String, String>> entrySet = buyerCartMap.entrySet();
					for (Entry<String, String> entry : entrySet) {
						BuyerItem item = new BuyerItem();
						//查询库存信息
						String skuId = entry.getKey();
						Sku sku = skuService.findSkuById(Long.parseLong(skuId));
						//查询商品信息
						Product product = productService.findProductById(sku.getProductId());
						sku.setProduct(product);
						
						item.setSku(sku);
						
						String amount = entry.getValue();
						item.setAmount(Integer.parseInt(amount));
						
						buyerCart.addItem(item);
					}
				}
			}else{
				List<BuyerItem> items = buyerCart.getItems();
				List<BuyerItem> newItems = new ArrayList<BuyerItem>();
				if(items != null && items.size() > 0){
					for (BuyerItem buyerItem : items) {
						Sku sku = skuService.findSkuById(buyerItem.getSku().getId());
						Product product = productService.findProductById(sku.getProductId());
						sku.setProduct(product);
						
						BuyerItem item = new BuyerItem();
						item.setSku(sku);
						item.setAmount(buyerItem.getAmount());
						newItems.add(item);
					}
				}
				BuyerCart cart = new BuyerCart();
				if(newItems != null && newItems.size() > 0){
					for (BuyerItem buyerItem : newItems) {
						cart.addItem(buyerItem);
					}
				}
				buyerCart = cart;
			}
		//5、回显购物车（model）
		model.addAttribute("buyerCart", buyerCart);
//		6、跳转到购物车页面
		return "cart";
	}	
	/*			if(cookies != null && cookies.length > 0){
			//2、遍历Cookie 找购物车
			for (Cookie cookie1 : cookies) {
			//3、判断是否有购物车, 有进行下面操作, 无则不进行任何操作
				if(Constants.BUYER_CART.equals(cookie1.getName())){
					BuyerCart buyerCart = null;
					String buyerCartJsonStr = cookie1.getValue();
					for (Cookie cookie : cookies) {
						//判断登入状态
						if(Constants.TOKEN.equals(cookie.getName())){
							String username = buyerService.getTokenFromRedis(cookie.getValue());
							Map<String, String> buyerCartMap = null;
							ObjectMapper om = new ObjectMapper();
							buyerCart = om.readValue(buyerCartJsonStr, BuyerCart.class);
							if(username != null){ //登入状态
								//将购物车添加到Redis中 清空Cookie
								if(buyerCart != null){
									List<BuyerItem> items = buyerCart.getItems();
									if(items != null && items.size() > 0){
										for (BuyerItem buyerItem : items) {
											buyerCartService.addBuyerCartToRedis(buyerItem.getSku().getId(), buyerItem.getAmount(), username);
										}
									}
								}
								Cookie cookie2 = new Cookie(Constants.BUYER_CART,null);
								cookie2.setMaxAge(0);
								cookie2.setPath("/");
								//从Redis中取出所有购物车 
								buyerCartMap = buyerCartService.findBuyerCartFromRedis(username);
								//						4、将购物车中装满
								if(buyerCartMap != null && buyerCartMap.size() > 0){
									Set<Entry<String, String>> entrySet = buyerCartMap.entrySet();
									for (Entry<String, String> entry : entrySet) {
										BuyerItem item = new BuyerItem();
										//查询库存信息
										String skuId = entry.getKey();
										Sku sku = skuService.findSkuById(Long.parseLong(skuId));
										//查询商品信息
										Product product = productService.findProductById(sku.getProductId());
										sku.setProduct(product);
										
										item.setSku(sku);
										
										String amount = entry.getValue();
										item.setAmount(Integer.parseInt(amount));
										
										buyerCart.addItem(item);
									}
								}
							}else{
								List<BuyerItem> items = buyerCart.getItems();
								List<BuyerItem> newItems = new ArrayList<BuyerItem>();
								if(items != null && items.size() > 0){
									for (BuyerItem buyerItem : items) {
										Sku sku = skuService.findSkuById(buyerItem.getSku().getId());
										Product product = productService.findProductById(sku.getProductId());
										sku.setProduct(product);
										
										BuyerItem item = new BuyerItem();
										item.setSku(sku);
										item.setAmount(buyerItem.getAmount());
										newItems.add(item);
									}
								}
								BuyerCart cart = new BuyerCart();
								if(newItems != null && newItems.size() > 0){
									for (BuyerItem buyerItem : newItems) {
										cart.addItem(buyerItem);
									}
								}
								buyerCart = cart;
							}
	//						5、回显购物车（model）
							model.addAttribute("buyerCart", buyerCart);
	//						6、跳转到购物车页面
							return "cart";
						}
					}
				}
			}
		}   */ 
		
//		return "cart";
	
	/**
	 * 加入购物车   添加当前款商品
	 * @param request
	 * @param response
	 * @param skuId
	 * @param amount
	 * @param model
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value="buyerCart")
	public String buyerCart(HttpServletRequest request, HttpServletResponse response, Long skuId, Integer amount, Model model) throws Exception{
		
		//加入购物车   添加当前款商品
		
		BuyerCart buyerCart = null;
		
//		1、从Request当中获取Cookies
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0){
//		2、遍历Cookie 找购物车
			for (Cookie cookie : cookies) {
				if(Constants.BUYER_CART.equals(cookie.getName())){
					//4、追加当前款商品
					String buyerCartStr = cookie.getValue();
					ObjectMapper om = new ObjectMapper();
					buyerCart = om.readValue(buyerCartStr, BuyerCart.class);
					
					BuyerItem item = new BuyerItem();
					item.setAmount(amount);
					Sku sku = new Sku();
					sku.setId(skuId);
					item.setSku(sku);
					
					buyerCart.addItem(item);
				}else{ //3、没有 创建购物车 
					buyerCart = new BuyerCart();
					//4、追加当前款商品
					BuyerItem item = new BuyerItem();
					item.setAmount(amount);
					Sku sku = new Sku();
					sku.setId(skuId);
					item.setSku(sku);
					buyerCart.addItem(item);
				}
				
			}
		}
		
		//判断是否登入
		
		String token = SigleSignUtils.getToken(request, response);
		String username = buyerService.getTokenFromRedis(token);
		
		//已登入
		if(username != null){ 
//			5、遍历购物车中购物项追加到Redis中
			List<BuyerItem> items = buyerCart.getItems();
			if(items != null && items.size() > 0){
				for (BuyerItem buyerItem : items) {
					buyerCartService.addBuyerCartToRedis(buyerItem.getSku().getId(), buyerItem.getAmount(), username);
				}
			}
//			6、清空Cookie
			Cookie cookie = new Cookie(Constants.BUYER_CART, null);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
			
//			model.addAttribute("buyerCart", buyerCart);
		}else{ //未登入
//			5、创建Cookie
			String buyerCartJsonStr = JsonUtils.objToJsonStr(buyerCart);
			Cookie cookie = new Cookie(Constants.BUYER_CART, buyerCartJsonStr);
			cookie.setMaxAge(60*60);
			cookie.setPath("/");
//			6、把最新购物车保存到Cookie中,回写到浏览器中
			response.addCookie(cookie);
			
//			model.addAttribute("buyerCart", buyerCart);
		}
		
		
		return "redirect:/shopping/account";
	}
}
