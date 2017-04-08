package cn.itcast.core.service;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.itcast.core.service.cms.StaticPageService;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class StaticPageServiceImpl implements StaticPageService, ServletContextAware {

	//创建freemarker初始化对象
	private Configuration conf;
	
	//通过这个对象就可以获取当前tomcat部署在硬盘上的绝对路径
	private ServletContext servletContext;
	
	//因为在freemarker.xml中设置了传入这里freeMarkerConfigurer属性, 所以会直接调用setFreeMarkerConfigurer方法
	//在这里我们使用freeMarkerConfigurer对象来初始化Configuration对象
	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer){
		this.conf = freeMarkerConfigurer.getConfiguration();
	}
	
	@Override
	public void createSaticPage(Map<String, Object> map, String productId) throws Exception {
		
		//设置生成文件的目录
		String path = "/html/"+productId+".html";
		
		//获取绝对路径
		String realPath = getRealPath(path);
		System.out.println("================"+realPath);
		
		File f = new File(realPath);
		//获取父级目录对象
		File parentFile = f.getParentFile();
		//判断是否有上级目录
		if(!parentFile.exists()){
			//没有上级目录则创建
			parentFile.mkdirs();
		}
		
		//获取模板对象
		Template template = conf.getTemplate("product.html");
		
		//设置生成的静态页面输出流
		Writer out = new FileWriter(f);
		//生成
		template.process(map, out);
		//关闭流
		out.close();

	}
	
	//根据相对路径, 加上获取的tomcat在硬盘上的绝对路径, 生成文件需要放置的绝对目录位置
	private String getRealPath(String path){
		return servletContext.getRealPath(path);
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
