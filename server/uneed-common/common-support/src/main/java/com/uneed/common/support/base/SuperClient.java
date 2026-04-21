package com.uneed.common.support.base;

import com.uneed.common.mybatis.page.PageData;
import com.uneed.common.mybatis.page.PageSearch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.List;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/5/2
 */
public interface SuperClient<DTO extends Serializable> {

    /**
     * 新增数据，接收单个dto对象数据
     *
     * @param dto dto对象数据，必须是实现了{@link Serializable}接口的数据对象
     * @return int 受影响行数
     */
    @PostMapping("/insert")
    int insert(@RequestBody DTO dto);

    /**
     * 新增（批量）数据，接收一个dto对象集合
     *
     * @param list dto对象数据集合，必须是实现了{@link Serializable}接口的数据对象
     * @return int 受影响行数
     */
    @PostMapping("/insert-batch")
    int insertBatch(@RequestBody List<DTO> list);

    /**
     * 修改数据，接收单个dto对象数据
     *
     * @param dto dto对象数据，必须是实现了{@link Serializable}接口的数据对象
     * @return int 受影响行数
     */
    @PostMapping("/update")
    int update(@RequestBody DTO dto);

    /**
     * 全量修改数据，接收单个dto对象数据，将数据字段值为null的数据设置为空
     *
     * @param dto dto对象数据，必须是实现了{@link Serializable}接口的数据对象
     * @return int 受影响行数
     */
    @PostMapping("/update-full")
    int updateFull(@RequestBody DTO dto);

    /**
     * 修改（批量）数据，接收一个dto对象数据集合
     *
     * @param list dto对象数据集合，必须是实现了{@link Serializable}接口的数据对象
     * @return int 受影响行数
     */
    @PostMapping("/update-batch")
    int updateBatch(@RequestBody List<DTO> list);

    /**
     * 新增或修改数据，接收单个dto对象数据，会根据实体对象是否有主键，对数据进行新增或是修改操作
     *
     * @param dto dto对象数据，必须是实现了{@link Serializable}接口的数据对象
     * @return int 受影响行数
     */
    @PostMapping("/insert-or-update")
    int insertOrUpdate(@RequestBody DTO dto);

    /**
     * 批量新增或修改数据，接收一个dto对象集合，会根据实体对象是否有主键，对数据进行批量新增或是批量修改操作
     *
     * @param list dto对象数据集合，必须是实现了{@link Serializable}接口的数据对象
     * @return int 受影响行数
     */
    @PostMapping("/insert-or-update-batch")
    int insertOrUpdateBatch(@RequestBody List<DTO> list);

    /**
     * 删除数据，接收单个数据对象的id
     *
     * @param id 数据id
     * @return int 受影响行数
     */
    @GetMapping("/remove-by-id")
    int removeById(@RequestParam("id") String id);

    /**
     * 删除数据（批量），接收一个数据对象的id集合
     *
     * @param ids 数据id集合
     * @return int 受影响行数
     */
    @PostMapping("/remove-by-ids")
    int removeByIds(@RequestBody List<String> ids);

    /**
     * 获取总记录数
     *
     * @return 数据总记录数
     */
    @GetMapping("/count")
    long count();

    /**
     * 根据实体条件对象获取数据总记录数
     *
     * @param condition 条件对象，必须是实现了{@link Serializable}接口的数据对象
     * @param <POJO>    条件的泛型参数
     * @return long 数据总记录数
     */
    @PostMapping("/remove-by-condition")
    <POJO extends Serializable> long countByCondition(@RequestBody POJO condition);

    /**
     * 根据id获取单条数据
     *
     * @param id 数据id
     * @return DTO 单条dto对象数据
     */
    @GetMapping("/get-by-id")
    DTO getById(@RequestParam("id") String id);

    /**
     * 根据条件对象获取单条数据
     *
     * @param condition 条件对象，必须是实现了{@link Serializable}接口的数据对象
     * @param <POJO>    条件的泛型参数
     * @return DTO 单条dto数据
     */
    @PostMapping("/get-by-condition")
    <POJO extends Serializable> DTO getByCondition(@RequestBody POJO condition);

    /**
     * 获取所有数据
     *
     * @return List<DTO> 所有dto数据列表
     */
    @GetMapping("/list")
    List<DTO> list();

    /**
     * 根据数据的id集合批量获取数据
     *
     * @param ids 数据id集合
     * @return List<DTO> dto数据列表
     */
    @PostMapping("/list-by-ids")
    List<DTO> listByIds(@RequestBody List<Long> ids);

    /**
     * 根据条件对象批量获取数据
     *
     * @param condition 条件对象，必须是实现了{@link Serializable}接口的数据对象
     * @param <POJO>    条件的泛型参数
     * @return List<DTO> dto数据列表
     */
    @PostMapping("/list-by-condition")
    <POJO extends Serializable> List<DTO> listByCondition(@RequestBody POJO condition);

    /**
     * 根据分页条件对象获取分页后的数据
     *
     * @param search 分页条件
     * @param <POJO> 条件的泛型参数
     * @return PageData<DTO> 分页后的数据集
     */
    @PostMapping("/page-by-search")
    <POJO extends Serializable> PageData<DTO> pageBySearch(@RequestBody PageSearch<POJO> search);

}
