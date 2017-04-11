package cn.itcast.test;

import static org.junit.Assert.*;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context.xml")
public class TestSolr {

	@Autowired
	private SolrServer solrServer;
	
	/**
	 * 删除索引
	 * @throws Exception
	 */
	@Test
	public void testDeleteSolr() throws Exception {
		SolrInputDocument doc = new SolrInputDocument();
		
		solrServer.deleteById("01");
		solrServer.deleteById("02");
		solrServer.deleteById("03");
		solrServer.commit();
	}
	
	@Test
	public void testSolr() throws Exception {
		
		String baseURL = "http://192.168.200.128:8080/solr";
		SolrServer server = new HttpSolrServer(baseURL); 
		
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "02");
		doc.addField("title", "未来");
		server.add(doc);
		server.commit();
	}
	
	@Test
	public void testSolr2() throws Exception {
		
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "03");
		doc.addField("title", "美好");
		solrServer.add(doc);
		solrServer.commit();
	}
}
