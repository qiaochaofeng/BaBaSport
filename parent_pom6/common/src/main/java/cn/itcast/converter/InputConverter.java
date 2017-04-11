package cn.itcast.converter;

import org.springframework.core.convert.converter.Converter;

public class InputConverter implements Converter<String, String> {

	/**
	 * 表单输入内容去除空格
	 */
	@Override
	public String convert(String source) {
		
		if(!"".equals(source)){
			return source;
		}
		if(null != source){
			String trim = source.trim();
			return trim;
		}
		
		return null;
	}

}
