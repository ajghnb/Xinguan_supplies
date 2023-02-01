package com.example.model.param.system;

import com.example.model.vo.base.PageQueryParam;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * @author 18237
 */
@Data
@Alias("ImageAttachmentParam")
public class ImageAttachmentParam extends PageQueryParam {

    private String mediaType;

    private String suffix;

    private String path;
}
