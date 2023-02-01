package com.example.service.system;

import com.example.model.param.system.ImageAttachmentParam;
import com.example.model.po.system.ImageAttachmentPo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author 18237
 */
public interface UploadService {

    /**
     * 图片列表
     *
     * @param param
     * @return
     */
    List<ImageAttachmentPo> queryImageList(ImageAttachmentParam param);

    /**
     * 图片上传
     *
     * @param file
     * @return
     */
    String uploadImage(MultipartFile file) throws IOException;

    /**
     * 删除图片
     *
     * @param imageId
     * @return
     */
    void deleteById(Long imageId);


}
