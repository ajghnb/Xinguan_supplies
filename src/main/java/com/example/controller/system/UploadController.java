package com.example.controller.system;

import com.example.model.PageData;
import com.example.model.R;
import com.example.model.param.system.ImageAttachmentParam;
import com.example.model.po.system.ImageAttachmentPo;
import com.example.model.vo.business.SupplierVo;
import com.example.model.vo.system.ImageAttachmentVo;
import com.example.service.system.UploadService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author 18237
 */
@Slf4j
@Api(tags = "系统模块-文件上传相关接口")
@RestController
@RequestMapping("/system/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    /**
     * 附件列表(图片)
     *
     * @param imageAttachment
     * @return
     */
    @ApiOperation(value = "附件列表", notes = "模糊查询附件列表")
    @GetMapping("/images")
    public R<PageData<ImageAttachmentVo>> imagePage(ImageAttachmentParam imageAttachment) {
        List<ImageAttachmentPo> imageAttachments = uploadService.queryImageList(imageAttachment);
        return R.ofSuccess(new PageData<>(imageAttachments)
                .convert(ImageAttachmentVo::fromImageAttachmentPo));
    }


    /**
     * 上传图片文件
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "上传文件")
    @RequiresPermissions({"upload:image"})
    @PostMapping("/image")
    public R<String> uploadImage(MultipartFile file) throws IOException {
        String realPath = uploadService.uploadImage(file);
        return R.ofSuccess(realPath);
    }


    /**
     * 删除图片
     *
     * @param imageId
     * @return
     */
    @ApiOperation(value = "删除图片", notes = "删除数据库记录,删除图片服务器上的图片")
    @RequiresPermissions("attachment:delete")
    @DeleteMapping("/delete/{id}")
    public R<Void> deleteImageAttachment(@PathVariable("id") Long imageId) {
        uploadService.deleteById(imageId);
        return R.ofSuccess();
    }

}