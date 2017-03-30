package cn.itcast.core.service.brand;

import java.util.List;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.test.Brand;

public interface BrandService {

	/**
	 *  修改品牌信息
	 */
	public void update(Brand brand);
	
	/**
	 * 根据id 查询品牌信息
	 */
	public Brand findBrandById(Integer id);
	
	/**
	 * 查询品牌列表
	 */
	public Pagination brandListQuery(Integer pageNo, String name,Integer isDisplay);
	
	/**
	 * 查询品牌列表
	 */
	public List<Brand> brandList();
	

}
