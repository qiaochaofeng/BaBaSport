package cn.itcast.core.bean.cart;

import java.io.Serializable;

import cn.itcast.core.bean.product.Sku;

/**
 * 购物项
 * @author qiao
 *
 */
public class BuyerItem implements Serializable {
	private static final long serialVersionUID = -6232827842859430169L;
	
	//sku(里面有Product对象)
	private Sku sku;
	
	//是否有货
	private boolean isHave = true;
	
	//购买数量
	private Integer amount = 1;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sku == null) ? 0 : sku.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuyerItem other = (BuyerItem) obj;
		if (sku == null) {
			if (other.sku != null)
				return false;
		} else if (!sku.getId().equals(other.sku.getId()))
			return false;
		return true;
	}

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}

	public boolean isHave() {
		return isHave;
	}

	public void setHave(boolean isHave) {
		this.isHave = isHave;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	} 
	
}
