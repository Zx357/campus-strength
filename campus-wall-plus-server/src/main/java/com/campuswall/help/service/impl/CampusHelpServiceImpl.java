package com.campuswall.help.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campuswall.help.entity.CampusHelp;
import com.campuswall.help.mapper.CampusHelpMapper;
import com.campuswall.help.service.CampusHelpService;
import org.springframework.stereotype.Service;

@Service
public class CampusHelpServiceImpl extends ServiceImpl<CampusHelpMapper, CampusHelp> implements CampusHelpService {
}
