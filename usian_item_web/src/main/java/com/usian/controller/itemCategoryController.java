package com.usian.controller;

import com.usian.feign.ItemFeignService;
import com.usian.pojo.TbItemCat;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("backend/itemCategory")
public class itemCategoryController {

    @Autowired
    private ItemFeignService itemFeignService;

    @RequestMapping("selectItemCategoryByParentId")
    public Result selectItemCategoryByParentId(@RequestParam(defaultValue = "0") Long id){
        List<TbItemCat> TbItemCat = itemFeignService.selectItemCategoryByParentId(id);
        if(TbItemCat != null && TbItemCat.size() > 0){
            return Result.ok(TbItemCat);
        }
        return Result.error("没有查到");
    }
}
