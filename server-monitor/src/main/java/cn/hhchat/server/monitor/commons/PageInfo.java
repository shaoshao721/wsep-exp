package cn.hhchat.server.monitor.commons;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class PageInfo {

    private Integer page;

    private Integer size;

    private Integer totalPage;

    private Long totalSize;

    public PageInfo() {

    }

    public PageInfo(Page page) {
        this.page = page.getPageable().getPageNumber();
        this.size = page.getPageable().getPageSize();
        this.totalPage = page.getTotalPages();
        this.totalSize = page.getTotalElements();
    }

    public PageInfo(Integer page, Integer size, Integer totalPage, Long totalSize) {
        this.page = page;
        this.size = size;
        this.totalPage = totalPage;
        this.totalSize = totalSize;
    }
}