package org.miaohong.fishrpc.core.rpc.chain;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.miaohong.fishrpc.core.extension.ExtensionLoader;
import org.miaohong.fishrpc.core.util.CommonUtils;

import java.util.Collections;
import java.util.List;

public abstract class FilterChain implements Invoker {

    protected List<Filter> filters = Lists.newArrayList();

    public FilterChain() {
        buildChain();
    }

    private void checkFilter(List<Filter> filters) {
        if (CommonUtils.isEmpty(filters)) {
            return;
        }

        filters.forEach((f) -> Preconditions.checkNotNull(f.getFilterOrder()));
    }

    private void buildChain() {
        filters = ExtensionLoader.getExtensionLoader(Filter.class).getAllExtension();
        checkFilter(filters);
        Collections.sort(filters);
    }


}
