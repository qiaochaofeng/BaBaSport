package cn.itcast.core.service.product;


import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Product;

public interface ProductService {

	/**
	 * 商品上架
	 */
	public void isShow(Long[] ids) throws Exception;
	
	/**
	 * 商品添加
	 */
	public void add(Product product);
	
	/**
	 * 商品列表
	 * @return
	 * @throws Exception
	 */
	public Pagination productList(Integer pageNo, String name,Long brandId, Boolean isShow) throws Exception;


	
}
