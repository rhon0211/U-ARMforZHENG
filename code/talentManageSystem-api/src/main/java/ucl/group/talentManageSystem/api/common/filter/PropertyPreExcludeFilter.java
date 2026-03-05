package ucl.group.talentManageSystem.api.common.filter;

import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;

/**排除JSON敏感属性
 * @author hejiale
 */
public class PropertyPreExcludeFilter extends SimplePropertyPreFilter
{
    public PropertyPreExcludeFilter()
    {
    }

    public PropertyPreExcludeFilter addExcludes(String... filters)
    {
        for (String filter : filters) {
            this.getExcludes().add(filter);
        }
        return this;
    }
}
