package org.autumn.commons.web.response.wrapper.page;

import org.autumn.commons.web.response.AutumnResponse;
import org.autumn.commons.web.response.wrapper.ResultWrapper;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 分页包装器<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public class PageResultWrapper implements ResultWrapper {

    private SpringDataWebProperties springDataWebProperties;

    public void setSpringDataWebProperties(SpringDataWebProperties springDataWebProperties) {
        this.springDataWebProperties = springDataWebProperties;
    }

    @Override
    public boolean supports(Object result, MethodParameter returnType) {
        return result instanceof Page;
    }

    @Override
    public void wrap(AutumnResponse<Object> response, Object result, MethodParameter returnType) {
        Object page = wrapPageObject((Page<?>) result);
        response.setData(page);
    }

    private Object wrapPageObject(Page<?> page) {
        PageResponse response = new PageResponse();
        response.setTotal(page.getTotalElements());
        response.setSize(page.getSize());
        int pageNumber = page.getPageable().getPageNumber();
        if (null != springDataWebProperties && springDataWebProperties.getPageable().isOneIndexedParameters()) {
            pageNumber++;
        }
        response.setPage(pageNumber);
        response.setFirst(page.isFirst());
        response.setLast(page.isLast());
        response.setList(page.getContent());
        return response;
    }
}
