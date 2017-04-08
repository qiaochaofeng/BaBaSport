package cn.itcast.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 将 Object 转换为 json格式的字符串
 * @author qiao
 *
 */

public class JsonUtils {

	public static String objToJsonStr(Object obj) throws Exception{
		
		ObjectMapper om = new ObjectMapper();
		
		om.setSerializationInclusion(Include.NON_NULL);
		
		String str = om.writeValueAsString(obj);
		
		return str;
	}
}
