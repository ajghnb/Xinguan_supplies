package com.example.controller.business;

import com.example.annotation.ControllerEndpoint;
import com.example.model.ActiveUser;
import com.example.model.PageData;
import com.example.model.R;
import com.example.model.param.business.HealthParam;
import com.example.model.po.business.HealthPo;
import com.example.model.vo.base.PageQueryParam;
import com.example.model.vo.business.HealthVo;
import com.example.service.business.HealthService;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 18237
 */
@Api(tags = "业务模块-健康上报相关接口")
@RestController
@RequestMapping("/business/health")
public class HealthController {

    @Autowired
    private HealthService healthService;

    /**
     * 今日是否已报备
     *
     * @return
     */
    @ApiOperation(value = "是否报备", notes = "今日是否已报备")
    @GetMapping("/isReport")
    public R<HealthVo> isReport() {
        //获取当前用户信息
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();

        HealthPo healthReport = healthService.isReport(activeUser.getUser().getId());
        HealthVo healthVo = HealthVo.fromHealthPo(healthReport);

        return R.ofSuccess(healthVo);
    }

    /**
     * 健康上报
     *
     * @param health
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "健康上报失败", operation = "健康上报")
    @ApiOperation(value = "健康上报", notes = "用户健康上报")
    @RequiresPermissions({"health:report"})
    @PostMapping("/report")
    public R<Void> addReport(@RequestBody @Validated HealthParam health) {
        healthService.report(health);

        return R.ofSuccess();
    }

    /**
     * 签到记录
     *
     * @return
     */
    @ApiOperation(value = "健康记录", notes = "用户健康上报历史记录")
    @GetMapping("/record")
    public R<PageData<HealthVo>> HealthRecordPage(PageQueryParam page) {
        //获取当前用户
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();

        Long userId = activeUser.getUser().getId();
        Page<HealthPo> healths = healthService.historyRecord(userId, page);

        return R.ofSuccess(new PageData<>(healths)
                .convert(HealthVo::fromHealthPo));
    }
}
