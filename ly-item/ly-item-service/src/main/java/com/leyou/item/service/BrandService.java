package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryByBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page,rows);//分页助手会用到mybatis的拦截器，对brandMapper.selectAll();执行的sql进行拦截，然后自动添加limit语句
        //过滤(查询 SELECT * FROM tb_brand WHERE name LIKE"%X%" OR letter=='X' ORDER BY id DESC;)
         Example example=new Example(Brand.class);
        if(StringUtils.isNotBlank(key)){
            example.createCriteria().andLike("name","%"+key+"%").orEqualTo("letter",key.toUpperCase());
        }
        //排序
        if(StringUtils.isNotBlank(sortBy)){
            String sortByClause=sortBy+" "+(desc?"DESC":"ASC");//排序用的子句“ORDER BY id DESC”
            example.setOrderByClause(sortByClause);
        }
        //查询
        List<Brand> list = brandMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        //解析分页结果
        PageInfo<Brand> pageInfo = new PageInfo<>(list);
        PageResult<Brand> result = new PageResult<>(pageInfo.getTotal(),pageInfo.getPages(),list);
        return result;
    }

    @Transactional//添加事务标签
    public void saveBrand(Brand brand, List<Long> cids) {
        brand.setId(null);
        //新增品牌
        int count = brandMapper.insert(brand);
            if(count!=1){
                throw new LyException(ExceptionEnum.BRAND_SAVE_ERROR);
            }
            //新增中间表
        for (Long cid : cids) {
            count = brandMapper.insertCategoryBrand(cid,brand.getId());
            if(count!=1){
                throw new LyException(ExceptionEnum.BRAND_SAVE_ERROR);
            }
        }

    }

    /***
     * 删除brand，并且维护中间表
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)//添加事务标签
    public void deleteBrand(Long id) {
        //删除品牌信息
        brandMapper.deleteByPrimaryKey(id);

        //删除中间表信息
        brandMapper.deleteCategoryBrandByBrandId(id);
    }
}
