/*
 * Copyright (c) 2014 Nanjing Xiaoyou Information Technology Co.,Ltd. All rights reserved.
 */

package cn.xyspace.xysvr.web.manager.sysprompt;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springside.modules.beanvalidator.BeanValidators;

import cn.xyspace.xysvr.common.sysprompt.entity.SyspromptZhcn;
import cn.xyspace.xysvr.common.sysprompt.form.manager.SyspromptListMgrForm;
import cn.xyspace.xysvr.common.sysprompt.form.manager.SysprompUpdatetMgrForm;
import cn.xyspace.xysvr.common.sysprompt.service.ISyspromptZhcnService;

import com.github.pagehelper.PageInfo;

/**
 * 中文系统提示控制器。
 *
 * @author Tanrongrong(2015年2月7日 下午2:32:20)
 *
 * @since 1.0.0
 *
 * @version 1.0.0
 *
 */
@Controller
@RequestMapping("/sysprompt/zhcn")
public class SyspromptZhcnController {

    @Inject
    private Validator validator;

    @Inject
    private ISyspromptZhcnService syspromptZhcnService;

    /**
     * 获取所有中文系统提示列表。
     *
     * @since 1.0.0
     * @version 1.0.0
     */
    @RequestMapping("/list")
    public String list(@ModelAttribute("pageNumber") String pageNumber, @ModelAttribute("pageSize") String pageSize, @ModelAttribute("encode") String encode, ModelMap model) {
        String enCode = StringUtils.trim(encode);
        Integer code = null;

        SyspromptListMgrForm form = new SyspromptListMgrForm();
        form.setPageNumber(pageNumber);
        form.setPageSize(pageSize);
        // 调用JSR303 Bean Validator进行校验，异常将由RestExceptionHandler统一处理
        BeanValidators.validateWithException(this.validator, form);

        if (StringUtils.isNotBlank(enCode)) {
            form.setCode(enCode);
            // 调用JSR303 Bean Validator进行校验，异常将由RestExceptionHandler统一处理
            BeanValidators.validateWithException(this.validator, form);

            code = Integer.parseInt(enCode);
        }

        PageInfo<SyspromptZhcn> list = this.syspromptZhcnService.mgrFinds(pageNumber, pageSize, code);

        model.addAttribute("pageInfo", list);

        return "sysprompt/zhcn/list";
    }

    /**
     * 单个中文系统提示的详细信息。
     *
     * @since 1.0.0
     * @version 1.0.0
     */
    @RequestMapping("/editUI/{id}")
    public String update(@PathVariable String id, ModelMap model) {
        SyspromptZhcn syspromptZhcn = this.syspromptZhcnService.findById(id);
        model.addAttribute("syspromptZhcn", syspromptZhcn);

        return "sysprompt/zhcn/editUI";
    }

    /**
     * 根据中文系统提示ID更新中文系统提示。
     * 
     * @since 1.0.0
     * @version 1.0.0
     */
    @RequestMapping("/edit/{id}")
    public String updateResult(@PathVariable String id, SysprompUpdatetMgrForm form, ModelMap model) {
        form.setId(id);
        // 调用JSR303 Bean Validator进行校验，异常将由RestExceptionHandler统一处理
        BeanValidators.validateWithException(this.validator, form);
        boolean successed = this.syspromptZhcnService.update(form);

        // 返回操作结果信息map
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("status", "false");
        resultMap.put("message", "更新失败");

        if (successed) {
            resultMap.put("status", "true");
            resultMap.put("message", "更新成功");
            resultMap.put("jumpUrl", "../list");
        }

        model.addAttribute("resultMap", resultMap);

        return "common/info";
    }

}
