package cn.soe.boot.core.bean.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * api return page model
 * @author xiezhenxiang 2020/1/8
 **/
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ReturnPage<T> {

    protected Collection<T> content;
    protected long total;
    protected Boolean hasNext;

    private ReturnPage(Collection<T> content, long total) {
        this.content = content;
        this.total = total;
    }

    private ReturnPage(Collection<T> content, boolean hasNext) {
        this.content = content;
        this.hasNext = hasNext;
    }

    public static <T> ReturnPage<T> of(Collection<T> content, long total) {
        return new ReturnPage<>(content, total);
    }

    public static <T> ReturnPage<T> of(Collection<T> content, boolean hasNext) {
        return new ReturnPage<>(content, hasNext);
    }
}
