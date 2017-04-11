package cn.itcast.core.service.color;

import java.util.List;

import cn.itcast.core.bean.product.Color;

public interface ColorService {
	
	/**
	 * 根据Id查询颜色信息
	 */
	public Color findColorById(Long id) throws Exception;

	/**
	 * 商品颜色
	 */
	public List<Color> colorList();

}
