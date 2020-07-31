package cn.wsw.activiti.util;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.Map;

/**
 * 流程实例util
 * @author wensw
 */
public class ProcessInstanceUtil {

    /**
     * 根据流程定义key运行流程实例
     * @param runtimeService 运行时服务
     * @param key 对应的流程定义的key （key唯一且固定的）
     * @return
     */
    public ProcessInstance startProcessInstanceByKey(RuntimeService runtimeService, String key) {
        return runtimeService.startProcessInstanceByKey(key);
    }

    /**
     * 根据流程定义key ,map数据集合运行流程实例
     * @param runtimeService
     * @param key
     * @param map
     * @return
     */
    public ProcessInstance startProcessInstanceByKeyAndMap(RuntimeService runtimeService, String key, Map<String, Object> map) {
        return runtimeService.startProcessInstanceByKey(key, map);
    }

    /**
     * 根据流程定义id启动流程实例
     * @param id
     * @return
     */
    public ProcessInstance startProcessInstanceById(RuntimeService runtimeService, String id) {
        return runtimeService.startProcessInstanceById(id);
    }


}
