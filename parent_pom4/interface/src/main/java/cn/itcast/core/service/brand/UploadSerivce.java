package cn.itcast.core.service.brand;

public interface UploadSerivce {

	//图片上传
	public String uploadPic(byte[] pic, String name, Long size) throws Exception;
}
