package com.example.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.param.system.ImageAttachmentParam;
import com.example.model.po.system.ImageAttachmentPo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 18237
 */
@Repository
public interface ImageAttachmentDao extends BaseMapper<ImageAttachmentPo> {

    /**
     * 查询图片列表
     *
     * @param  param
     * @return
     */
    List<ImageAttachmentPo> queryImageList(ImageAttachmentParam param);

}