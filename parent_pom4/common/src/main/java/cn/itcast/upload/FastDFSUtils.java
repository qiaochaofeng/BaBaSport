package cn.itcast.upload;

import org.apache.commons.io.FilenameUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

/**
 * 利用 FastDFS 自定义图片上传工具类
 * @author qiao
 *
 */

public class FastDFSUtils {

	public static String uploadPic(byte[] pic, String name, Long size) throws Exception{
		
		//加载 FastDFS 配置文件
		ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
		ClientGlobal.init(resource.getClassLoader().getResource("fdfs_client.conf").getPath());
		
		//连接 tracker
		TrackerClient tracker = new TrackerClient();
		
		TrackerServer connection = tracker.getConnection();
		
		//连接 storage
		StorageClient1 storage = new StorageClient1(connection, null);
		
		//将图片存入 storage
		String extName = FilenameUtils.getExtension(name);
		
		NameValuePair[] meta_list = new NameValuePair[3];
		
		//存入文件名
		meta_list[0] = new NameValuePair("fileName",name);
		//扩展名
		meta_list[1] = new NameValuePair("fileExtName",extName);
		//文件大小
		meta_list[2] = new NameValuePair("fileSize",String.valueOf(size));
		
		//上传文件,返回地址值
		String path = storage.upload_file1(pic, extName, meta_list);
		
		return path;
	}
}
