package com.atguigu.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.yygh.cmn.listener.DictListener;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *  @CachePut：使用该注解标志的方法，每次都会执行，并将结果存入指定的缓存中。
 * 其他方法可以直接从响应的缓存中读取缓存数据，而不需要再去查询数据库。一般用在新增方法上。
 * @author wang
 * @create 2022-05-23
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    private DictMapper dictMapper;

    /**
     * 根据id，查子数据列表
     * @param id
     * @return
     */
    //根据方法对其返回结果进行缓存，下次请求时，如果缓存存在，则直接读取缓存数据返回；
    // 如果缓存不存在，则执行方法，并把返回的结果存入缓存中。一般用在查询方法上。
    //value是缓存名，keyGenerator是存的数据key的值，可以自定义组件来生成key
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    @Override
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);

        //之所以baseMapper不需要注入而直接用，因为ServiceImpl已经做好了
        List<Dict> dictList = baseMapper.selectList(wrapper);

        //向list每个dict对象中设置hasChildern值
        for(Dict dict : dictList){
            Long dictId = dict.getId();
            boolean isChild = this.isChildren(dictId);
            dict.setHasChildren(isChild);
        }
        return dictList;
    }

    /**
     * 导出数据字典接口
     * @param response
     */
    @Override
    public void exportDictData(HttpServletResponse response) {
        //设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "dict";
        //设置响应头，Content-disposition代表是下载的意思
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        //查询数据库
        List<Dict> dictList = baseMapper.selectList(null);

        //把list中的Dict转为与excel表对应的实体类DictEeVo
        List<DictEeVo> dictEeVoList = new ArrayList<>(dictList.size());
        for(Dict dict : dictList){
            DictEeVo dictEeVo = new DictEeVo();
            //BeanUtils提供的复制方法
            BeanUtils.copyProperties(dict, dictEeVo);
            dictEeVoList.add(dictEeVo);
        }

        //调用easyExcel的方法进行写操作
        try {
            //sheet代表输出的excel表sheet的名称；输出流，输出实体类
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict").doWrite(dictEeVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    //使用该注解标志的方法，会清空指定的缓存（value是缓存存在的命名空间，allEntries代表是否清除所有缓存）。
    // 一般用在更新或者删除方法上
    @CacheEvict(value = "dict", allEntries=true)
    public void importDictData(MultipartFile file) {
        try {
            //文件输入流，输入的实体类，回调监听器；监听器完成读取工作
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(dictMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isChildren(Long id){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(wrapper);
        return count > 0;
    }

    //查询名称
    @Override
    public String getDictName(String dictCode, String value) {
        //如果dictCode为空,直接根据value查询
        if(StringUtils.isEmpty(dictCode)){
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("value",value);
            Dict dict = dictMapper.selectOne(queryWrapper);
            return dict != null ? dict.getName() : "kong";
        }else{
            //如果dictCode不为空,根据dictCode和value查询
            //根据dictcode查询dict对象，目的是得到dict的id值，在根据此id值查询此id下的子id
            QueryWrapper<Dict> queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("dict_code",dictCode);
            Dict codeDict = dictMapper.selectOne(queryWrapper1);
            Long parent_id = codeDict.getId();
            //根据parentId和value值进行查询
            QueryWrapper<Dict> queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("parent_id",parent_id).eq("value",value);
            Dict finalDict = baseMapper.selectOne(queryWrapper2);
            return finalDict.getName();
        }
    }

    //根据dictcode查询查询子节点
    @Override
    public List<Dict> findByDictCode(String dictCode) {
        //根据dictcode获取对应的id
        QueryWrapper<Dict> queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("dict_code",dictCode);
        Dict codeDict = dictMapper.selectOne(queryWrapper1);
        //根据id获取子节点
        List<Dict> childData = this.findChildData(codeDict.getId());
        return childData;
    }
}
