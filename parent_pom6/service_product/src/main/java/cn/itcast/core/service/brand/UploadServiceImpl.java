package cn.itcast.core.service.brand;

import org.springframework.stereotype.Service;

import cn.itcast.upload.FastDFSUtils;

@Service(value="uploadServiceImpl")
public class UploadServiceImpl implements UploadSerivce {

	//图片上传
	@Override
	public String uploadPic(byte[] pic, String name, Long size) throws Exception {
		String path = FastDFSUtils.uploadPic(pic, name, size);
		return path;
	}

}
