package cn.itcast.core.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.itcast.constant.Constants;
import cn.itcast.core.service.brand.UploadSerivce;

@Controller
@RequestMapping(value="upload")
public class UploadController {

	@Autowired
	private UploadSerivce uploadService;
	
	/**
	 * 多张图片上传
	 */
	@RequestMapping(value="uploadPics")
	@ResponseBody
	public List<String> uploadPics(@RequestParam MultipartFile[] pics) throws Exception{
		
		List<String> UrlList = new ArrayList<String>();
		if(null != pics){
			for (MultipartFile pic : pics) {
				String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
				String url = Constants.URL_IMG + path; 
				UrlList.add(url);
			}
		}
		
		return UrlList;
	}
	
	/**
	 * 图片异步上传 
	 */
	@RequestMapping(value="updatePic")
	public String updatePic(MultipartFile pic, HttpServletResponse response) throws Exception{
		
		/*//1. 获取图片的完整名称
		String fileName = pic.getOriginalFilename();
		//2. 随机生成字符串+ 源文件扩展名组成新的文件名, 防止图片重名
		String newFileName = UUID.randomUUID().toString().replaceAll("-", "") 
							+ "." +FilenameUtils.getExtension(fileName);
		//3. 将图片保存在文件系统中 
		pic.transferTo(new File("F:\\BaBaSportUploadImg\\" + newFileName));
		
		//4. 将图片路径返回 JSONObject只能转换简单类型, 不能转换list, map, pojo等复杂类型
		JSONObject jo = new JSONObject();
		jo.put("url", "/pic/"+newFileName);*/
		
		//使用 FastDFS 上传图片
		String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
		//group1/M00/00/01/wKjIgFjeR2mAbyuTAAF8TPcgMHA977.jpg
		String url = Constants.URL_IMG; //http://192.168.200.128/
		
		JSONObject jo = new JSONObject();
		jo.put("url", url+path);
		
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(jo.toString());
		
		return null;
	}
}
