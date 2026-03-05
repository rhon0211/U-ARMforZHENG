package ucl.group.talentManageSystem.api.common.annotation;


import ucl.group.talentManageSystem.api.common.enums.BusinessType;
import ucl.group.talentManageSystem.api.common.enums.OperatorType;

import java.lang.annotation.*;


/**
 * 自定义操作日志记录注解
 * @author hejiale
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log
{
    String title() default "";//模块名称

    BusinessType businessType() default BusinessType.OTHER;//功能操作类型

    OperatorType operatorType() default OperatorType.MANAGE;// 操作人类别

    boolean isSaveRequestData() default true;//是否保存请求的参数

    boolean isSaveResponseData() default true;//是否保存响应的参数

    String[] excludeParamNames() default {};//排除指定的请求参数
}
