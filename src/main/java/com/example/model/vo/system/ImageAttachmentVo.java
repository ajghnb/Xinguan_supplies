package com.example.model.vo.system;

import com.example.model.po.system.ImageAttachmentPo;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author 18237
 */
@Data
@Builder
public class ImageAttachmentVo {

    private Long id;

    private String path;

    private Long size;

    private String mediaType;

    private String suffix;

    private Integer height;

    private Integer width;

    public static ImageAttachmentVo fromImageAttachmentPo(ImageAttachmentPo imageAttachment){
        ImageAttachmentVo imageAttachmentVo = ImageAttachmentVo.builder()
                .id(imageAttachment.getId())
                .path(imageAttachment.getPath())
                .size(imageAttachment.getSize())
                .mediaType(imageAttachment.getMediaType())
                .suffix(imageAttachment.getSuffix())
                .height(imageAttachment.getHeight())
                .width(imageAttachment.getWidth())
                .build();
        return imageAttachmentVo;
    }

}
