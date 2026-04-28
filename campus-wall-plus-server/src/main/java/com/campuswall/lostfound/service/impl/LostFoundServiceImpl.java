package com.campuswall.lostfound.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campuswall.lostfound.entity.LostFound;
import com.campuswall.lostfound.mapper.LostFoundMapper;
import com.campuswall.lostfound.service.LostFoundService;
import org.springframework.stereotype.Service;

@Service
public class LostFoundServiceImpl extends ServiceImpl<LostFoundMapper, LostFound> implements LostFoundService {
}
