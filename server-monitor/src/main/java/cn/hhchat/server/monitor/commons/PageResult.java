package cn.hhchat.server.monitor.commons;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResult<T> {

    private List<T> elements;

    private Integer number;

    private PageInfo pageInfo;

    public PageResult() {

    }

    public PageResult(Page<T> pageResult) {
        this.elements = pageResult.getContent();
        this.number = elements.size();
        this.pageInfo = new PageInfo(pageResult);
    }

    public PageResult(List<T> elements, Page pageResult) {
        this.elements = elements;
        this.number = elements.size();
        this.pageInfo = new PageInfo(pageResult);
    }

    public PageResult(List<T> elements, Integer number, Integer page, Integer size, Integer totalPage, Long totalSize) {
        this.elements = elements;
        this.number = number;
        this.pageInfo = new PageInfo(page, size, totalPage, totalSize);
    }

    public PageResult setElements(List<T> elements) {
        this.elements = elements;
        return this;
    }

    public PageResult setNumber(Integer number) {
        this.number = number;
        return this;
    }

    public Integer getNumber() {
        return number;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public PageResult setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
        return this;
    }

}
