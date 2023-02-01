package com.example.controller.business;

import com.example.annotation.ControllerEndpoint;
import com.example.annotation.valid.Add;
import com.example.annotation.valid.Edit;
import com.example.model.PageData;
import com.example.model.R;
import com.example.model.param.business.ConsumerParam;
import com.example.model.po.business.ConsumerPo;
import com.example.model.vo.business.ConsumerVo;
import com.example.service.business.ConsumerService;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 18237
 */
@Api(tags = "业务模块-物资去向相关接口")
@RestController
@RequestMapping("/business/consumer")
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;


    /**
     * 去向列表
     * @param consumer
     *
     * @return
     */
    @ApiOperation(value = "去向列表", notes = "去向列表,根据去向名模糊查询")
    @GetMapping("/page")
    public R<PageData<ConsumerVo>> consumerPage(ConsumerParam consumer) {
        Page<ConsumerPo> consumers = consumerService.queryConsumerList(consumer);

        return R.ofSuccess(new PageData<>(consumers)
                .convert(ConsumerVo::fromConsumerPo));
    }


    /**
     * 所有去向
     *
     * @return
     */
    @ApiOperation(value = "所有去向", notes = "所有去向列表")
    @GetMapping("/all")
    public R<List<ConsumerVo>> consumerList() {
        List<ConsumerVo> consumerVos = consumerService.findAll()
                .stream()
                .map(ConsumerVo::fromConsumerPo)
                .collect(Collectors.toList());
        return R.ofSuccess(consumerVos);
    }

    /**
     * 添加去向
     * @param consumerParam
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "物资去向添加失败", operation = "物资去向添加")
    @RequiresPermissions({"consumer:add"})
    @ApiOperation(value = "添加去向")
    @PostMapping("/add")
    public R<Void> addConsumer(@RequestBody @Validated({Add.class, Default.class}) ConsumerParam consumerParam) {
        consumerService.addConsumer(consumerParam);
        return R.ofSuccess();
    }

    /**
     * 更新去向
     * @param consumerId
     * @param consumer
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "物资去向更新失败", operation = "物资去向更新")
    @ApiOperation(value = "更新去向", notes = "更新去向信息")
    @RequiresPermissions({"consumer:update"})
    @PutMapping("/update/{id}")
    public R<Void> updateConsumer(@PathVariable("id") Long consumerId,
                                  @RequestBody @Validated({Edit.class, Default.class}) ConsumerParam consumer) {
        consumerService.updateConsumer(consumerId, consumer);
        return R.ofSuccess();
    }


    /**
     * 编辑去向
     *
     * @param consumerId
     * @return
     */
    @ApiOperation(value = "编辑去向", notes = "编辑去向信息")
    @RequiresPermissions({"consumer:edit"})
    @GetMapping("/edit/{id}")
    public R<ConsumerVo> editConsumer(@PathVariable("id") Long consumerId) {
        ConsumerPo consumerPo = consumerService.editById(consumerId);
        ConsumerVo consumerVo = ConsumerVo.fromConsumerPo(consumerPo);
        return R.ofSuccess(consumerVo);
    }

    /**
     * 删除去向
     *
     * @param consumerId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "物资去向删除失败", operation = "物资去向删除")
    @ApiOperation(value = "删除去向", notes = "删除去向信息")
    @RequiresPermissions({"consumer:delete"})
    @DeleteMapping("/delete/{id}")
    public R<Void> deleteConsumer(@PathVariable("id") Long consumerId) {
        consumerService.deleteById(consumerId);
        return R.ofSuccess();
    }


}
