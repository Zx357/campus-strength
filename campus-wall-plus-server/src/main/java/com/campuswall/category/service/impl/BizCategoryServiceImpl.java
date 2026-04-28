package com.campuswall.category.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campuswall.category.entity.BizCategory;
import com.campuswall.category.mapper.BizCategoryMapper;
import com.campuswall.category.service.BizCategoryService;
import org.springframework.stereotype.Service;

@Service
public class BizCategoryServiceImpl extends ServiceImpl<BizCategoryMapper, BizCategory> implements BizCategoryService {
}
