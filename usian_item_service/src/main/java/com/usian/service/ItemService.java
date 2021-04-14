package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.*;
import com.usian.pojo.*;
import com.usian.utils.IDUtils;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.util.IdUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    /*
    * 查询商品并分页
    * */
    public PageResult selectTbItemAllByPage(Integer page,Integer rows){
        PageHelper.startPage(page,rows);
        //查询状态是1 并且  按修改时间逆序排列
        TbItemExample tbItemExample = new TbItemExample();
        tbItemExample.setOrderByClause("updated DESC");
        TbItemExample.Criteria criteria = tbItemExample.createCriteria();
        criteria.andStatusEqualTo((byte)1);
        List<TbItem> tbItemList = tbItemMapper.selectByExample(tbItemExample);
        for (int i = 0; i < tbItemList.size(); i++) {
            TbItem tbItem =  tbItemList.get(i);
            tbItem.setPrice(tbItem.getPrice()/100);
        }
        PageInfo<TbItem> pageinfo = new PageInfo<>(tbItemList);
        //返回pageresult
        PageResult pageResult = new PageResult();
        pageResult.setResult(pageinfo.getList());
        pageResult.setTotalPage(Long.valueOf(pageinfo.getPages()));
        pageResult.setPageIndex(pageinfo.getPageNum());
        return pageResult;
    }


    public Integer insertTbItem(TbItem tbItem, String desc, String itemParams) {
        //添加TbItem数据
        Long itemId = IDUtils.genItemId();
        Date date = new Date();
        tbItem.setId(itemId);
        tbItem.setStatus((byte)1);
        tbItem.setUpdated(date);
        tbItem.setCreated(date);
        Integer tbItemNum = this.tbItemMapper.insertSelective(tbItem);

        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        Integer tbItemDescNum = this.tbItemDescMapper.insertSelective(tbItemDesc);

        //补齐商品规格参数
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setItemId(itemId);
        tbItemParamItem.setParamData(itemParams);
        tbItemParamItem.setUpdated(date);
        tbItemParamItem.setCreated(date);
        Integer tbItemParamItemNum = tbItemParamItemMapper.insertSelective(tbItemParamItem);
        return tbItemNum+tbItemDescNum+tbItemParamItemNum;
    }


    public Map<String, Object> preUpdateItem(Long itemId) {
//        {“itemCat”:”xxxx”,”item”:{xxxx},”itemDesc”:”xxxx”,”itemParamItem”:”xxxxx”}
        Map<String,Object> map = new HashMap<>();
        //根据商品id查询商品
        TbItem tbItem = this.tbItemMapper.selectByPrimaryKey(itemId);
        map.put("item",tbItem);
        //根据商品id查询商品描述
        TbItemDesc tbItemDesc = this.tbItemDescMapper.selectByPrimaryKey(itemId);
        map.put("itemDesc",tbItemDesc);
        //根据商品id查询商品类目
        TbItemCat tbItemCat = this.tbItemCatMapper.selectByPrimaryKey(tbItem.getCid());
        map.put("itemCat",tbItemCat.getName());
        //根据商品id查询商品规格信息
        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemParamItem> list = this.tbItemParamItemMapper.selectByExampleWithBLOBs(example);
        if(list != null && list.size() > 0){
            map.put("itemParamItem",list.get(0).getParamData());
        }
        return map;
    }

    public Integer updateTbItem(TbItem tbItem, String desc, String itemParams) {
        //修改TbItem数据
        Long itemId = tbItem.getId();
        Date date = new Date();
        tbItem.setUpdated(date);
        Integer tbItemNum = this.tbItemMapper.updateByPrimaryKeySelective(tbItem);

        //修改商品描述
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        Integer tbItemDescNum = this.tbItemDescMapper.updateByPrimaryKeySelective(tbItemDesc);

        //修改商品规格参数
        TbItemParamItem tbItemParamItem = tbItemParamItemMapper.selectByItemId(itemId);
        tbItemParamItem.setUpdated(date);
        tbItemParamItem.setParamData(itemParams);
        Integer tbItemParamItemNum = tbItemParamItemMapper.updateByPrimaryKeySelective(tbItemParamItem);
        return tbItemNum+tbItemDescNum+tbItemParamItemNum;
    }

    public Integer deleteItemById(long itemId) {
        //根据id查询商品
        TbItem tbItem  = tbItemMapper.selectByPrimaryKey(itemId);
        //修改商品状态码0.
        tbItem.setStatus((byte)3);
        Integer deleteItemNum = tbItemMapper.updateByPrimaryKeySelective(tbItem);
        return deleteItemNum;
    }
}
