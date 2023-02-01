package com.example.service.system.impl;

import com.example.common.utils.FdfsUtil;
import com.example.common.utils.LogUtils;
import com.example.dao.system.ImageAttachmentDao;
import com.example.exception.ApiRuntimeException;
import com.example.exception.asserts.Assert;
import com.example.model.param.system.ImageAttachmentParam;
import com.example.model.po.system.ImageAttachmentPo;
import com.example.service.system.UploadService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * @author 18237
 */
@Service("uploadService")
public class UploadServiceImpl implements UploadService {

    @Autowired
    private FdfsUtil fdfsUtil;

    @Autowired
    private ImageAttachmentDao imageAttachmentDao;

    @Override
    public List<ImageAttachmentPo> queryImageList(ImageAttachmentParam param) {
        LogUtils.LOGGER.debug("系统图片文件列表: 查询参数:{}", param);

        PageHelper.startPage(param.getPageNum(), param.getPageSize());

        List<ImageAttachmentPo> imageAttachments = imageAttachmentDao.queryImageList(param);
        return imageAttachments;
    }

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        LogUtils.LOGGER.debug("开始上传图片文件, file: {}", file);
        if (file.isEmpty()) {
            throw new ApiRuntimeException(Assert.PARAMETER, "上传的文件不能为空");
        }
        InputStream inputStream = file.getInputStream();
        //文件的原名称
        long size = file.getSize();
        String originalFilename = file.getOriginalFilename();
        String fileExtName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String path = fdfsUtil.upfileImage(inputStream, size, fileExtName.toUpperCase(), null);
        //保存图片信息到数据库
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image != null) {
            //如果image=null 表示上传的不是图片格式
            ImageAttachmentPo imageAttachment = new ImageAttachmentPo();
            imageAttachment.setCreateTime(new Date());
            imageAttachment.setHeight(image.getHeight());
            imageAttachment.setWidth(image.getWidth());
            imageAttachment.setMediaType(fileExtName);
            imageAttachment.setMediaType(file.getContentType());
            imageAttachment.setPath(path);
            imageAttachmentDao.insert(imageAttachment);
        }
        // TODO
        return path;
    }

    @Override
    public void deleteById(Long imageId) {
        LogUtils.LOGGER.debug("开始删除图片, imageId: {}", imageId);
        ImageAttachmentPo imageAttachment = checkImageIsExit(imageId);

        Assert.DB_OPERATE.sqlSuccess(imageAttachmentDao.deleteById(imageId)).orThrow(isAssert -> {
            LogUtils.LOGGER.debug("[{}]  删除图片文件失败, imageId: {}", imageAttachment, imageId);
            return new ApiRuntimeException(isAssert);
        });
    }

    public ImageAttachmentPo checkImageIsExit(Long imageId) {
        ImageAttachmentPo image = imageAttachmentDao.selectById(imageId);
        if (image == null) {
            throw new ApiRuntimeException(Assert.PARAMETER, "指定图片不存在");
        }
        return image;
    }

    public void checkUploadImage(MultipartFile file) {

    }
}
