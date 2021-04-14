package com.usian.controller;

import com.usian.feign.ItemFeignService;
import com.usian.pojo.TbItem;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("backend/item")
public class ItemController {

    @Autowired
    private ItemFeignService itemFeignService;

    @RequestMapping("selectTbItemAllByPage")
    public Result selectTbItemAllByPage(@RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "10") Integer rows){
        PageResult pageResult = itemFeignService.selectTbItemAllByPage(page,rows);
        if(pageResult.getResult() != null && pageResult.getResult().size() > 0){
            return Result.ok(pageResult);
        }
        return Result.error("没有结果");
    }

    @RequestMapping("insertTbItem")
    public Result insertTbItem(TbItem tbItem, String desc, String itemParams){
        Integer insertTbItemNum = itemFeignService.insertTbItem(tbItem,desc,itemParams);
        System.out.println(insertTbItemNum);
        if(insertTbItemNum == 3){
            return Result.ok();
        }
        return Result.error("添加失败");
    }

    @RequestMapping("preUpdateItem")
    public Result preUpdateItem(Long itemId){
        Map<String,Object> map = itemFeignService.preUpdateItem(itemId);
        if(map.size() >0){
            return Result.ok(map);
        }
        return Result.error("查询失败");
    }

    @RequestMapping("updateTbItem")
    public Result updateTbItem(TbItem tbItem,String desc,String itemParams){
        Integer updateTbItemNum = itemFeignService.updateTbItem(tbItem,desc,itemParams);
        if(updateTbItemNum == 3){
            return Result.ok();
        }
        return Result.error("修改失败");
    }

    @RequestMapping("deleteItemById")
    public Result deleteItemById(Long itemId){
        Integer deleteTbItemNum = itemFeignService.deleteItemById(itemId);
        if(deleteTbItemNum == 1){
            return Result.ok();
        }
        return Result.error("删除失败");
    }

}
