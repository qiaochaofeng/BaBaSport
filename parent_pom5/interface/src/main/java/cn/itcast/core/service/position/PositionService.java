package cn.itcast.core.service.position;

import java.util.List;

import cn.itcast.core.bean.ad.Position;

public interface PositionService {

	 /**
	  * 根据id查询广告栏信息
	 * @param positionId
	 * @return
	 */
	Position findPositionById(Long positionId);

	/**
	 * 广告栏位集合
	 */
	List<Position> findPositionByParentId(Long root);


}
