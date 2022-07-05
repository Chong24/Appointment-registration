package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.cmn.client.DictFeignClient;
import com.atguigu.yygh.hosp.repository.HospitalRepository;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 优化点：我们需要远程调用去查省市区、医院等级，我们可以用多线程去做，用一个线程安全的单例线程池
 * @author wang
 * @create 2022-05-24
 */
@Service
public class HospitalSevriceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;

    //获取可用的处理器个数，一般设置这个数位核心线程大小
    private static int corePoolSize = Runtime.getRuntime().availableProcessors();

    //构建线程安全的单例模式（双锁安全模式），来创建线程池ExecutorService，它有多个实现，我们选择常用的ThreadPoolExecutor
    private volatile ExecutorService taskExe;

    //构建对象锁
    private Object object = new Object();

    //构建线程池实例的内部方法
    private ExecutorService getThreadPoolInstance(){
        if (taskExe == null){
            synchronized (object){
                if (taskExe == null){
                    /**
                     * 构建线程池的参数：
                     * 核心线程池数
                     * 最大线程池数
                     * 线程活跃时间
                     * 阻塞队列的大小
                     * 线程工厂
                     * 拒绝策略
                     */
                    taskExe = new ThreadPoolExecutor(corePoolSize, corePoolSize + 1, 10l, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));
                }
            }
        }
        return taskExe;
    }

    /**
     * save：既可以有保存的作用，又可以有修改的作用，关键看数据库是否已经存在了该数据
     * @param parapMap
     */
    @Override
    public void save(Map<String, Object> parapMap) {
        //把参数的map集合转换为对象Hospital，方便操作：方法就是先转为字符串，再转为Hospital对象
        String mapString = JSONObject.toJSONString(parapMap);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

        //判断是否存在相同的数据
        String hoscode = hospital.getHoscode();
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hoscode);
        //如果存在，进行修改
        if (hospitalExist != null){
            //其他的都是传入的数据，只有没传入的数据需要自己设置
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }else{
            //如果不存在，进行添加
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    //医院列表(条件查询分页)
    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        //创建pageable对象
        Pageable pageable = PageRequest.of(page - 1,limit);
        //创建条件匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        //hospitalSetQueryVo转换成hospital对象
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);
        //创建Example对象
        Example<Hospital> example = Example.of(hospital,matcher);
        //调用方法实现查询
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);

        //查询到所有医院集合并遍历，然后获取到医院等级信息
        //采用流的方式
        pages.getContent().stream().forEach(item -> {
            try {
                this.setHospitalHosType(item);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return pages;
    }

    //更新医院的上线状态
    @Override
    public void updateStatus(String id, Integer status) {
        //根据id查询医院信息
        Hospital hospital = hospitalRepository.findById(id).get();
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }

    //查询医院详情
    @Override
    public Map<String, Object> getHospById(String id) throws ExecutionException, InterruptedException {
        Map<String, Object> result = new HashMap<>();
        //根据id查询医院信息并将等级信息也封装进去
        Hospital hospital = this.setHospitalHosType(hospitalRepository.findById(id).get());
        result.put("hospital",hospital);
        //单独处理更直观
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }

    //获取医院名称
    @Override
    public String getHospName(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        if(hospital != null){
            return hospital.getHosname();
        }
        return null;
    }

    //根据医院名称查询
    @Override
    public List<Hospital> findByHosname(String hosname) {
        List<Hospital> list = hospitalRepository.findHospitalByHosnameLike(hosname);
        return list;
    }

    //根据医院编号获取预约挂号详情
    @Override
    public Map<String, Object> item(String hoscode) throws ExecutionException, InterruptedException {
        Map<String, Object> result = new HashMap<>();
        //医院详情
        Hospital hospital = this.setHospitalHosType(this.getByHoscode(hoscode));
        result.put("hospital", hospital);
        //预约规则
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }

    //获取查询到的医院集合，遍历进行医院等级封装
    private Hospital setHospitalHosType(Hospital hospital) throws ExecutionException, InterruptedException {
        ExecutorService taskExe = getThreadPoolInstance();
        //因为我们需要知道任务的返回结果，所以我们用的是Callable接口，如果不需要知道可用Runnable
        FutureTask getHostypeTask = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                //根据dictCode和value获取医院等级名称
                String hostypeString = dictFeignClient.getName("Hostype", hospital.getHostype());
                return hostypeString;
            }
        });

        FutureTask getProvinceTask = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                //查询省
                String provinceString = dictFeignClient.getName(hospital.getProvinceCode());
                return provinceString;
            }
        });

        FutureTask getCityTask = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                //查询市
                String cityString = dictFeignClient.getName(hospital.getCityCode());
                return cityString;
            }
        });

        FutureTask getDistrictTask = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                //查询区
                String districtString = dictFeignClient.getName(hospital.getDistrictCode());
                return districtString;
            }
        });

        //提交任务，Execute只能接收Runnable类型参数，没有返回值；submit可以接收Runnable和callable，有返回值
        taskExe.submit(getHostypeTask);
        taskExe.submit(getProvinceTask);
        taskExe.submit(getCityTask);
        taskExe.submit(getDistrictTask);

        //获取任务执行的结果
        String hostypeString = (String) getHostypeTask.get();
        String provinceString = (String) getProvinceTask.get();
        String cityString = (String) getCityTask.get();
        String districtString = (String) getDistrictTask.get();
        hospital.getParam().put("hostypeString",hostypeString);
        hospital.getParam().put("fullAddress", new StringBuilder().append(provinceString).append(cityString).append(districtString).toString());
        return hospital;
    }
}
