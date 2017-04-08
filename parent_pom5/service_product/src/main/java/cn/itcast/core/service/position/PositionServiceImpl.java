package cn.itcast.core.service.position;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.bean.ad.Position;
import cn.itcast.core.bean.ad.PositionQuery;
import cn.itcast.core.bean.ad.PositionQuery.Criteria;
import cn.itcast.core.dao.ad.PositionDao;

@Service(value="positionServiceImpl")
@Transactional
public class PositionServiceImpl implements PositionService {

	@Autowired
	private PositionDao positionDao;

	/**
	 * 根据id查询广告栏信息
	 */
	@Override
	public Position findPositionById(Long positionId) {
		return positionDao.selectByPrimaryKey(positionId);
	}
	
	
	/**
	 * 广告栏位集合
	 */
	@Override
	public List<Position> findPositionByParentId(Long root) {
		
		PositionQuery positionQuery = new PositionQuery();
		Criteria criteria = positionQuery.createCriteria();
		
		criteria.andParentIdEqualTo(root);
		criteria.andIsEnableEqualTo(true);
		
		List<Position> positionList = positionDao.selectByExample(positionQuery);
		
		return positionList;
	}

}
