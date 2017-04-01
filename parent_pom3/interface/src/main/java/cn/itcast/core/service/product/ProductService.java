package cn.itcast.core.service.product;


import cn.itcast.common.page.Pagination;

public interface ProductService {

	/**
	 * 商品列表
	 * @return
	 * @throws Exception
	 */
	public Pagination productList(Integer pageNo, String name,Long brandId, Boolean isShow) throws Exception;
}
