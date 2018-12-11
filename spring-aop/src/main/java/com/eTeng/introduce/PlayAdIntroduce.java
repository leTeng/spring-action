package com.eTeng.introduce;

import com.eTeng.pojo.interfaces.PlayAd;
import com.eTeng.pojo.DeFaultPlayAd;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;

/**
 * @FileName PalyAdAdvice.java
 * @Author 梁怡腾
 * @Date 2018/12/11 18:19
 * @Description bean引入播放广告
 */
@Aspect
public class PlayAdIntroduce{

    /*
     * 1.+ ：表示匹配Perform接口的所有子类引入新接口。
     * 2.defaultImpl：表示引入功能默认委托目标对象
     * 3.adAdvice 表示引入功能的接口
     */
    @DeclareParents(value = "com.eTeng.point.interfaces.Perform+",
            defaultImpl = DeFaultPlayAd.class)

    public static PlayAd playAd;

}
