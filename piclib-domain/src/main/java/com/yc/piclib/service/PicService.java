package com.yc.piclib.service;

import com.yc.piclib.domain.PageDomain;
import com.yc.piclib.domain.PicDomain;

import java.util.List;

public interface PicService {

    public List<PicDomain> list();

    /**
     * 分页查找
     *
     * @param PicDomain
     * @return
     */
    public PageDomain<PicDomain> listByPage(PicDomain PicDomain);

    public void save(PicDomain picDomain);

    public void delete(Integer id);

    public PicDomain findOne(Integer id);


}
