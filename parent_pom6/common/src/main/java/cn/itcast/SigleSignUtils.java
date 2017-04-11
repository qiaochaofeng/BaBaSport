package cn.itcast;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.constant.Constants;

public class SigleSignUtils {
	
	public static String getToken(HttpServletRequest request, HttpServletResponse response){
		
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0){
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals(Constants.TOKEN)){
					return cookie.getValue();
				}
			}
		}
		
		String token = UUID.randomUUID().toString().replaceAll("-", "");
		Cookie cookie = new Cookie(Constants.TOKEN, token);
		cookie.setMaxAge(-1);
		cookie.setPath("/");
		response.addCookie(cookie);
		
		return token;
	}
}
