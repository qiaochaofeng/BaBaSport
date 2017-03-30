package cn.itcast.core.service.brand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.dao.brand.BrandDao;
import cn.itcast.core.test.Brand;
import cn.itcast.core.test.BrandQuery;

@Service("brandServiceImpl")
@Transactional
public class BrandServiceImpl implements BrandService {

	@Autowired
	private BrandDao brandDao;

	/**
	 *  修改品牌信息
	 */
	@Override
	public void update(Brand brand) {
		brandDao.updateBrand(brand);
	}
	
	/**
	 * 根据id 查询品牌信息
	 */
	@Override
	public Brand findBrandById(Integer id) {
		Brand brand = brandDao.findBrandById(id);
		return brand;
	}
	
	/**
	 * 查询品牌列表
	 */
	@Override
	public Pagination brandListQuery(Integer pageNo, String name,Integer isDisplay) {
		
		StringBuilder params = new StringBuilder();
		
		BrandQuery brandQuery = new BrandQuery();
		brandQuery.setPageNo(Pagination.cpn(pageNo)); //这里要调用 BrandQuery的setPageNo或setPageSize,若不调用则不会计算startRow
		
		if(name != null){
			brandQuery.setName(name);
			params.append("name=").append(name);
		}
		if(isDisplay != null){
			brandQuery.setIsDisplay(isDisplay);
			params.append("&isDisplay=").append(isDisplay);
		}else{
			brandQuery.setIsDisplay(1);
			params.append("isDisplay=").append(1);
		}
		
		Integer count = brandDao.brandCount(brandQuery);  //总记录数
		
		List<Brand> brandListQuery = brandDao.brandListQuery(brandQuery); //每页数据集合
		
		//int pageNo, int pageSize, int totalCount, List<?> list
		Pagination pagination = new Pagination(brandQuery.getPageNo(), brandQuery.getPageSize(), count, brandListQuery);
		
		String url = "/brand/brandlist.action";
		pagination.pageView(url, params.toString());
		
//		return brandListQuery;
		return pagination;
	}
	
	/**
	 * 品牌列表
	 */
	@Override
	public List<Brand> brandList() {
		List<Brand> brandList = brandDao.brandList();
		return brandList;
	}

}
