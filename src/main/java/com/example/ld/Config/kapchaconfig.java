package com.example.ld.Config;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

import java.util.Properties;

/**
 * @ClassName kapchaconfig
 * @Description TODO
 * @Date 2022/9/19 14:59
 */
@Configuration
public class kapchaconfig {
    @Bean
    public Producer kaptchaProducer() {
        DefaultKaptcha captchaProducer = new DefaultKaptcha();
        Properties properties = new Properties();
        //字体颜色
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
        //验证码宽 110px
        properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, "110");
        //验证码高 40px
        properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, "40");
        //字体大小
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "30");
        //生成验证字符的长度
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        //字体
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, "宋体,楷体,微软雅黑");
        //验证字符选项 0-9及A-Z的组合 【可根据具体的情况，进行设置，汉字也同样可以作为验证符选项】
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING, "0123456789ABCDEFGHIGKLMNOPQRSTUVWSYZ");
        Config config = new Config(properties);
        captchaProducer.setConfig(config);
        return captchaProducer;
    }
}


