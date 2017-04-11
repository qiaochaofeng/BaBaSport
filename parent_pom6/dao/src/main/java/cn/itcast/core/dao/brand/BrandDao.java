package cn.itcast.core.dao.brand;

import java.util.List;

import cn.itcast.core.test.Brand;
import cn.itcast.core.test.BrandQuery;

public interface BrandDao {

	/**
	 * 批量删除
	 */
	public void deleteAll(Long[] ids);
	
	/**
	 *  修改品牌信息
	 */
	public void updateBrand(Brand brand);
	
	/**
	 * 根据id 查询品牌信息
	 */
	public Brand findBrandById(Integer id);
	
	/**
	 * 品牌总记录数
	 */
	public Integer brandCount(BrandQuery brandQuery);
	
	/**
	 * 查询品牌列表
	 */
	public List<Brand> brandListQuery(BrandQuery brandQuery);
	
	/**
	 * 品牌列表
	 */
	public List<Brand> brandList();

}
