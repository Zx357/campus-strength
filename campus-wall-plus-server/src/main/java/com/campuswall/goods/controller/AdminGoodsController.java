package com.campuswall.goods.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.common.controller.CrudController;
import com.campuswall.goods.entity.SecondGoods;
import com.campuswall.goods.service.SecondGoodsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/goods")
public class AdminGoodsController extends CrudController<SecondGoods> {
    private final SecondGoodsService service;

    public AdminGoodsController(SecondGoodsService service) {
        this.service = service;
    }

    @Override
    protected IService<SecondGoods> service() {
        return service;
    }
}
