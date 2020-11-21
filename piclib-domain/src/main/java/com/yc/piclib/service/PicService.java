package com.yc.piclib.service;

import com.yc.piclib.domain.PageDomain;
import com.yc.piclib.domain.PicDomain;

import java.util.List;

public interface PicService {

    public List<PicDomain> list();

    public PageDomain<PicDomain> listByPage(PicDomain PicDomain);
}
