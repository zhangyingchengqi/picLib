package com.yc.piclib.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
        //将实体类转为   domain
        List<PicDomain> r = new ArrayList<PicDomain>();
        for (Pic p : list) {   //  BeanUtils.copyBean(source, destination);
            PicDomain pd = new PicDomain(p.getId(), p.getPath(), p.getDescription());
            r.add(pd);
        }
        return r;
    }

    @Transactional(readOnly = true)
    @Override
    public PageDomain<PicDomain> listByPage(PicDomain picDomain) {
        PageHelper.startPage(picDomain.getPage(), picDomain.getPageSize());
        Example example = new Example(Pic.class);   //条件
        Example.Criteria c = example.createCriteria();
        if (CommonUtils.isNotNull(picDomain.getDescription())) {
            //条件创建

            c.andLike("description", "%" + picDomain.getDescription() + "%");
        }
        long total = picMapper.selectCountByExample(example);

        PageDomain<PicDomain> pageDomain = new PageDomain<PicDomain>();
        pageDomain.setTotal(total);
        pageDomain.setPage(picDomain.getPage());
        pageDomain.setPageSize(picDomain.getPageSize());

        //TODO: 分页条件

        c.andGreaterThanOrEqualTo("id", 1);
        example.setOrderByClause("id desc");

        PageInfo<Pic> pageInfo = new PageInfo<Pic>(picMapper.selectByExample(example));

        //List<Pic> list = picMapper.selectByExample(example);
        List<PicDomain> r = new ArrayList<PicDomain>();
        if (pageInfo.getList() != null) {
            for (Pic p : pageInfo.getList()) {
                PicDomain pd = new PicDomain(p.getId(), p.getPath(), p.getDescription());
                r.add(pd);
            }
        }
        pageDomain.setData(r);

        return pageDomain;
    }


}
