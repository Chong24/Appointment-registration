package com.atguigu.yygh.cmn.service;

import com.atguigu.yygh.model.cmn.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author wang
 * @create 2022-05-23
 */
public interface DictService extends IService<Dict>{

    //根据数据id查询子数据列表，即根据这个id，查出数据库中父id为这个id的数据
    List<Dict> findChildData(Long id);

    //导出数据字典接口
    void exportDictData(HttpServletResponse response);

    //导入数据字典
    void importDictData(MultipartFile file);

    //查询名称
    String getDictName(String dictCode, String value);

    //根据dictcode查询查询子节点
    List<Dict> findByDictCode(String dictCode);
}
