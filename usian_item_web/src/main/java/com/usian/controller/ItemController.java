package com.usian.controller;

import com.usian.feign.ItemFeignService;
import com.usian.pojo.TbItem;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("frontend/item")
public class ItemController {

    @Autowired
    private ItemFeignService itemFeignService;

    @RequestMapping("selectItemInfo")
    public Result selectItemInfo(long itemId){
        TbItem tbItem = itemFeignService.selectItemInfo(itemId);
        if(tbItem != null){
            return Result.ok(tbItem);
        }
        return Result.error("没有找到");
    }
}
