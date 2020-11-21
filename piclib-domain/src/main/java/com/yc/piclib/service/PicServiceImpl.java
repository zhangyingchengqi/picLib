package com.yc.piclib.service;

import com.yc.piclib.dao.impl.PicMapper;
import com.yc.piclib.domain.PageDomain;
import com.yc.piclib.domain.PicDomain;
import com.yc.piclib.entity.Pic;
import com.yc.piclib.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PicServiceImpl implements PicService {

    @Autowired(required = false)
    private PicMapper picMapper;

    @Transactional(readOnly = true)
    @Override
    public List<PicDomain> list() {
        List<Pic> list = picMapper.selectAll();
        List<PicDomain> r = new ArrayList<PicDomain>();
        for (Pic p : list) {
            PicDomain pd = new PicDomain(p.getId(), p.getPath(), p.getDescription());
            r.add(pd);
        }
        return r;
    }

    @Override
    public PageDomain<PicDomain> listByPage(PicDomain picDomain) {
        Example example = new Example(Pic.class);
        if (CommonUtils.isNotNull(picDomain.getDescription())) {
            example.createCriteria()
                    .andLike("description", "%" + picDomain.getDescription() + "%");
        }
        long total = picMapper.selectCountByExample(example);

        PageDomain<PicDomain> pageDomain = new PageDomain<PicDomain>();
        pageDomain.setTotal(total);
        pageDomain.setPage(picDomain.getPage());
        pageDomain.setPageSize(picDomain.getPageSize());


        List<Pic> list = picMapper.selectByExample(example);
        List<PicDomain> r = new ArrayList<PicDomain>();
        for (Pic p : list) {
            PicDomain pd = new PicDomain(p.getId(), p.getPath(), p.getDescription());
            r.add(pd);
        }
        pageDomain.setData(r);

        return pageDomain;
    }


}
