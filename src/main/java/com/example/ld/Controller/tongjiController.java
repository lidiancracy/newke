package com.example.ld.Controller;

import com.example.ld.service.impl.data_statisticsserviceimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName tongjiController
 * @Description TODO
 * @Date 2022/10/16 16:23
 */
@Controller
public class tongjiController {
    @Autowired
    data_statisticsserviceimpl tj;

    /**
     * 显示访客统计页面
     */
    @GetMapping("/data")
    public String getdata(){
        return "site/admin/data";
    }
    @PostMapping("/data")
    public String getdata2(){
        return "site/admin/data";
    }

    /**
     * post 提交 uv 查询
     */
    @PostMapping("/data/uv")
    public String cacu_uv(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start, @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Model model){
        long l = tj.uv_mutil(start,end);
        model.addAttribute("uvStartDate",start);
        model.addAttribute("uvEndDate",end);
        model.addAttribute("uvCount",l);
        return "forward:/data";
    }

    /**
     * post 提交dau查询
     */
    @PostMapping("/data/dau")
    public String cacu_dau(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start, @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Model model){
        long l = tj.dau_mutil(start,end);
        model.addAttribute("dauStartDate",start);
        model.addAttribute("dauEndDate",end);
        model.addAttribute("dauCount",l);
        return "forward:/data";

    }

}
