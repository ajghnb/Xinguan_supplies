package com.example.model.po.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author 18237
 */
@Data
@Alias("ImageAttachment")
@TableName("tb_image")
public class ImageAttachmentPo {

    @Id
    private Long id;

    private String path;

    private Long size;

    private String mediaType;

    private String suffix;

    private Integer height;

    private Integer width;

    private Date createTime;

}
