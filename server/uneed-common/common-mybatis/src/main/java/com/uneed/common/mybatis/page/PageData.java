package com.uneed.common.mybatis.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uneed.common.core.collection.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

import static com.uneed.common.core.lang.ObjectUtil.nullToDefault;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/25
 */
@Data
@ApiModel(value = "PageData", description = "分页数据")
public class PageData<T> {

    /**
     * 分页数据集
     */
    @ApiModelProperty(value = "分页数据集", name = "records", position = 1)
    private List<T> records;

    /**
     * 查询总数
     */
    @ApiModelProperty(value = "查询总数", name = "total", position = 2)
    private int total;

    /**
     * 当前查询页
     */
    @ApiModelProperty(value = "当前查询页", name = "current", position = 3)
    private int current;

    /**
     * 查询页大小
     */
    @ApiModelProperty(value = "查询页大小", name = "size", position = 4)
    private int size;

    /**
     * 获取数据集，默认返回空集合
     *
     * @return List<T>
     */
    public List<T> getRecords() {
        return nullToDefault(records, Lists.newArrayList());
    }

    /**
     * 总记录数，默认为集合的数量
     *
     * @return int
     */
    public int getTotal() {
        return total > 0 ? total : getRecords().size();
    }

    /**
     * 当前查询页，默认为 1
     *
     * @return int
     */
    public int getCurrent() {
        return current > 0 ? current : 1;
    }

    /**
     * 查询页数量，默认为 10
     *
     * @return int
     */
    public int getSize() {
        return size > 0 ? size : getTotal();
    }

    ///////////////////////////// 构造函数 ///////////////////////////////////////////////////////////////////

    /**
     * 只有数据集合的构造函数
     *
     * @param records 数据集合
     */
    public PageData(List<T> records) {
        this(records, 0, 0, 0);
    }

    /**
     * 包含数据集合、总数、当前查询页、查询页数量的构造函数
     *
     * @param records 数据集合
     * @param total   总数
     * @param current 当前查询页
     * @param size    查询页数量
     */
    public PageData(List<T> records, int total, int current, int size) {
        this.records = records;
        this.total = total;
        this.current = current;
        this.size = size;
    }

    /**
     * 包含数据集合、分页信息{@link IPage}的构造函数
     *
     * @param records 数据集合
     * @param page    分页信息
     */
    public PageData(List<T> records, IPage<?> page) {
        this(records, (int) page.getTotal(), (int) page.getCurrent(), (int) page.getSize());
    }
}
