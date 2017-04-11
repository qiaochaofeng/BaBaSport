package cn.itcast.core.bean.cart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.itcast.core.bean.product.Sku;

/**
 * 购物车
 * @author qiao
 *
 */
public class BuyerCart implements Serializable {
	private static final long serialVersionUID = 8392535729870844408L;

	//购物项集合
	private List<BuyerItem> items = new ArrayList<BuyerItem>();
	
	//总价格
	//在对象和json互相转换的时候使用了ObjectMapper对象, 那么这个对象转换的pojo必须是标准的javaBean,
	//标准的javaBean就是必须有属性, 还有get set方法, 否则会报错. 我们这里可以使用@JsonIgnore注解, 忽略这些不标准的方法
	@JsonIgnore
	public Float getTotalPrice() {
		return getFee() + getProductPrice();
	}

	//商品总数
	@JsonIgnore
	public Integer getProductAmount() {
		Integer count = 0;
		for(BuyerItem item : items){
			count += item.getAmount();
		}
		return count;
	}

	//运费
	@JsonIgnore
	public Float getFee() {
		//商品总金额大于60元包邮, 其他收运费10元
		if(getProductPrice() >= 60){
			return 0f;
		} else {
			return 10f;
		}
	}

	//商品总金额
	@JsonIgnore
	public Float getProductPrice() {
		Float countPrice = 0f;
		for(BuyerItem item : items){
			countPrice += item.getAmount() * item.getSku().getPrice();
		}
		return countPrice;
	}
	
	
	//添加购物项
	public void addItem(BuyerItem item) {
		
		//判断是否添加相同的商品
		if(this.items.contains(item)){
			for (BuyerItem buyerItem : items) {
				if(buyerItem.getSku().getId().equals(item.getSku().getId())){
					buyerItem.setAmount(buyerItem.getAmount()+item.getAmount());
				}
			}
		}else{
			this.items.add(item);
		}
		
	}

	public List<BuyerItem> getItems() {
		return items;
	}

}
