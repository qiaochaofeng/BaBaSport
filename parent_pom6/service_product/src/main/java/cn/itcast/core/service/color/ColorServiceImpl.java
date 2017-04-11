package cn.itcast.core.service.color;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.ColorQuery;
import cn.itcast.core.bean.product.ColorQuery.Criteria;
import cn.itcast.core.dao.product.ColorDao;

@Service(value="colorServiceImpl")
@Transactional
public class ColorServiceImpl implements ColorService {

	@Autowired
	private ColorDao colorDao;
	
	/**
	 * 根据Id查询颜色信息
	 */
	@Override
	public Color findColorById(Long id) throws Exception {
		return colorDao.selectByPrimaryKey(id);
	}
	
	
	/**
	 * 商品颜色
	 */
	@Override
	public List<Color> colorList() {
		
		ColorQuery colorQuery = new ColorQuery();
		Criteria criteria = colorQuery.createCriteria();
		criteria.andParentIdNotEqualTo(0L);
		List<Color> colorList = colorDao.selectByExample(colorQuery);
		
		//自己编写的笨方法
		/*ColorQuery colorQuery1 = new ColorQuery();
		
		//查询所有id
		colorQuery1.setFields("id");
		List<Color> colorList1 = colorDao.selectByExample(colorQuery1);
		List<Long> ids = new ArrayList<Long>();
		for (Color color : colorList1) {
			ids.add(color.getId());
		}
		
		ColorQuery colorQuery2 = new ColorQuery();
		colorQuery2.setDistinct(true); //去重复
		
		Criteria criteria2 = colorQuery2.createCriteria();
		criteria2.andParentIdIn(ids);
		
		List<Color> colorList2 = colorDao.selectByExample(colorQuery2);*/
		
		return colorList;
	}

}
