package com.leyou.item.web;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.BrandService;
import com.netflix.ribbon.proxy.annotation.Http;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;
    /****
     *分页查询品牌
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    @RequestMapping("page")
    public ResponseEntity<PageResult<Brand>> queryByBrandByPage(
            //页面请求参数page=1&rows=5&sortBy=id&desc=false&key=
            @RequestParam(value = "page",defaultValue ="1")Integer page,
            @RequestParam(value = "rows",defaultValue ="5")Integer rows,
            @RequestParam(value = "sortBy",required = false)String sortBy,
            @RequestParam(value = "desc",defaultValue = "false")Boolean desc,
            @RequestParam(value = "key",required = false)String key
    ){
        PageResult<Brand> result = brandService.queryByBrandByPage(page,rows,sortBy,desc,key);
        return ResponseEntity.ok(result);
    }
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand,@RequestParam("cids") List<Long> cids){
            brandService.saveBrand(brand,cids);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //删除品牌
    @DeleteMapping("bid/{bid}")
    public ResponseEntity<Void> deleteBrand(@PathVariable("bid")String bid){
        String separator="-";
        if(bid.contains(separator)){
            String ids[]=bid.split(separator);
            for (String id : ids) {
                brandService.deleteBrand(Long.parseLong(id));
            }
        }else {
            brandService.deleteBrand(Long.parseLong(bid));
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
