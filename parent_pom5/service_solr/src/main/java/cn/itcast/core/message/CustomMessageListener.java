package cn.itcast.core.message;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.core.service.solr.SolrService;

/**
 * 自定义消息处理类
 * @author qiao
 *
 */

public class CustomMessageListener implements MessageListener {

	//注入SolrService
	@Autowired
	private SolrService solrService;
	
	
	@Override
	public void onMessage(Message msg) {
		
		ActiveMQTextMessage atm = (ActiveMQTextMessage)msg;
		try {
			String id = atm.getText();
			solrService.insertProductIntoSolr(Long.valueOf(id));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
