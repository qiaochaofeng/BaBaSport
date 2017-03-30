package cn.itcast.core.controller;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value="upload")
public class UploadController {

	/**
	 * 图片异步上传
	 */
	@RequestMapping(value="updatePic")
	public String updatePic(MultipartFile pic, HttpServletResponse response) throws Exception{
		
		//1. 获取图片的完整名称
		String fileName = pic.getOriginalFilename();
		//2. 随机生成字符串+ 源文件扩展名组成新的文件名, 防止图片重名
		String newFileName = UUID.randomUUID().toString().replaceAll("-", "") 
							+ "." +FilenameUtils.getExtension(fileName);
		//3. 将图片保存在文件系统中 
		pic.transferTo(new File("F:\\BaBaSportUploadImg\\" + newFileName));
		
		//4. 将图片路径返回 JSONObject只能转换简单类型, 不能转换list, map, pojo等复杂类型
		JSONObject jo = new JSONObject();
		jo.put("url", "/pic/"+newFileName);
		
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(jo.toString());
		
		return null;
	}
}
