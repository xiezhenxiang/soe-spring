package cn.soe.boot.core.bean.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.util.Assert;

/**
 * @author xiezhenxiang 2023/4/24
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageParam {

    /**
     * 当前页，默认1
     */
    private int pageNo = 1;
    /**
     * 每页数，默认10
     */
    private int pageSize = 10;


    public int getPageNo() {
        return pageNo;
    }

    public int getPageIndex() {
        return pageNo - 1;
    }

    public int getSkip() {
        return getOffset();
    }

    public int getOffset() {
        return getPageIndex() * getPageSize();
    }

    public void setPageNo(int pageNo) {
        Assert.state(pageNo > 0,"pageNo must gt 0");
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        Assert.state(pageSize > 0,"pageSize must gt 0");
        this.pageSize = pageSize;
    }
}
