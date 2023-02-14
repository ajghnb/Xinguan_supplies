package com.example.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.common.converter.MenuConverter;
import com.example.common.converter.MenuTreeBuilder;
import com.example.common.utils.LogUtils;
import com.example.dao.system.MenuDao;
import com.example.exception.ApiRuntimeException;
import com.example.exception.asserts.Assert;
import com.example.model.param.system.MenuParam;
import com.example.model.po.system.MenuNodePo;
import com.example.model.po.system.MenuPo;
import com.example.service.system.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 18237
 */
@Service("menuServiceImpl")
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;


    /**
     * 加载菜单树（按钮和菜单）
     *
     * @param
     * @return
     */
    @Override
    public List<MenuNodePo> getMenuTree() {
        LogUtils.LOGGER.debug("开始查询获取菜单树");

        List<MenuPo> menus = menuDao.selectList(null);
        List<MenuNodePo> menuNodes = menus.stream()
                .map(MenuConverter::converterToMenuNodePo)
                .collect(Collectors.toList());
        System.out.println(MenuTreeBuilder.build(menuNodes));
        return MenuTreeBuilder.build(menuNodes);
    }


    /**
     * 获取所有菜单
     *
     * @param
     * @return
     */
    @Override
    public List<MenuPo> findAll() {
        LogUtils.LOGGER.debug("开始查询获取获取所有菜单");

        return menuDao.selectList(null);
    }


    /**
     * 获取展开项
     *
     * @param
     * @return
     */
    @Override
    public List<Long> queryOpenMenuIds() {
        LogUtils.LOGGER.debug("开始查询获取获取所有展开菜单id");

        ArrayList<Long> ids = new ArrayList<>();
        List<MenuPo> menus = menuDao.selectList(null);
        if (!CollectionUtils.isEmpty(menus)) {
            menus.stream().forEach((menu) -> {
                if (menu.getOpen() == 1) {
                    ids.add(menu.getId());
                }
            });

        }
        return ids;
    }


    /**
     * 添加菜单
     *
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuPo addMenu(MenuParam param) {
        LogUtils.LOGGER.debug("开始新增菜单栏: 菜单栏:{}", param);

        MenuPo menu = MenuPo.fromMenuParam(param);
        menu.setAvailable(param.isDisabled() ? 0 : 1);

        Assert.DB_OPERATE.sqlSuccess(menuDao.insert(menu))
                .orThrow(isAssert -> {
                    LogUtils.LOGGER.error("新增菜单栏失败: {}", param);
                    return new ApiRuntimeException(isAssert);
                });
        return menu;
    }


    /**
     * 删除菜单
     *
     * @param menuId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long menuId) {
        LogUtils.LOGGER.debug("开始删菜单, menuId: {}", menuId);

        MenuPo menu = checkMenIsExit(menuId);
        Assert.DB_OPERATE.sqlSuccess(menuDao.deleteById(menuId)).orThrow(isAssert -> {
            LogUtils.LOGGER.debug("[{}]  删除菜单失败, menuId: {}", menu, menuId);
            return new ApiRuntimeException(isAssert);
        });
    }


    /**
     * 编辑菜单
     *
     * @param menuId
     * @return
     */
    @Override
    public MenuPo editById(Long menuId) {
        LogUtils.LOGGER.debug("开始编辑菜单, menuId: {}", menuId);

        MenuPo menu = checkMenIsExit(menuId);
        return menu;
    }


    /**
     * 更新菜单
     *
     * @param menuId
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMenu(Long menuId, MenuParam param) {
        checkMenIsExit(menuId);
        MenuPo menu = MenuPo.fromMenuParam(param);
        menu.setId(menuId);
        menu.setAvailable(!param.isDisabled() ? 1 : 0);
        //更新菜单
        LambdaUpdateWrapper<MenuPo> wrapper = new LambdaUpdateWrapper<MenuPo>()
                .eq(true, MenuPo::getId, menuId);
        Assert.DB_OPERATE.sqlSuccess(menuDao.update(menu, wrapper)).orThrow(iAssert -> {
            LogUtils.LOGGER.error("更新菜单失败, menuId: {}, menuPo: {}", menuId, menu);
            return new ApiRuntimeException(iAssert);
        });
    }


    /**
     * 检查菜单栏是否存在
     *
     * @param menuId
     * @return
     */
    public MenuPo checkMenIsExit(Long menuId) {
        MenuPo menu = menuDao.selectById(menuId);
        if (menu == null) {
            throw new ApiRuntimeException(Assert.IS_EXIST, "删除菜单失败, 该菜单不存在");
        }

        return menu;
    }
}
