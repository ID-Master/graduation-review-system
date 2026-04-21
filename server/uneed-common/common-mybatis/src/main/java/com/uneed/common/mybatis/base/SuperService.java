package com.uneed.common.mybatis.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.uneed.common.mybatis.model.SuperModel;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 业务逻辑接口超类，参照mybatis-plus插件的service方法，这里做了一定的约束，限制了通用数据的获取方法，便于做扩展
 *
 * @author diablo
 * @date 2020/4/1
 */
public interface SuperService<T extends SuperModel> {

    /**
     * 新增数据，接收单个实体对象数据
     *
     * @param entity 实体对象数据，必须是继承BaseModel的数据对象
     * @return int 受影响行数
     */
    int insert(T entity);

    /**
     * 新增（批量）数据，接收一个实体对象集合
     *
     * @param collection 实体对象数据集合，必须是继承BaseModel的数据对象
     * @return int 受影响行数
     */
    int insertBatch(Collection<? extends T> collection);

    /**
     * 修改数据，接收单个实体对象数据
     *
     * @param entity 实体对象数据，必须是继承BaseModel的数据对象
     * @return int 受影响行数
     */
    int update(T entity);

    /**
     * 修改（批量）数据，接收一个实体对象集合
     *
     * @param collection 实体对象集合
     * @return int 受影响行数
     */
    int updateBatch(Collection<? extends T> collection);

    /**
     * 修改数据，会做全量修改，将实体字段值为null的数据设置为空
     *
     * @param entity 实体对象，必须是继承BaseModel的数据对象
     * @return int 受影响行数
     */
    int updateForAll(T entity);

    /**
     * 修改数据（批量），会做全量修改，将实体字段值为null的数据设置为空
     *
     * @param collection 实体对象集合，必须是继承BaseModel的数据对象
     * @return int 受影响行数
     */
    int updateBatchForAll(Collection<? extends T> collection);

    /**
     * 新增或修改数据，接收单个实体对象数据，会根据实体对象是否有主键，对数据进行新增或是修改操作
     *
     * @param entity 实体对象数据
     * @return int 受影响行数
     */
    int insertOrUpdate(T entity);

    /**
     * 批量新增或修改数据，接收一个实体对象集合，会根据实体对象是否有主键，对数据进行新增或是修改操作
     *
     * @param collection 实体对象集合
     * @return int 受影响行数
     */
    int insertOrUpdateBatch(Collection<? extends T> collection);

    /**
     * 删除数据，接收单个数据对象的id
     *
     * @param id 数据id
     * @return int 受影响行数
     */
    int removeById(String id);

    /**
     * 删除数据（批量），接收一个数据对象的id集合
     *
     * @param ids id集合
     * @return int 受影响行数
     */
    int removeByIds(Collection<String> ids);

    /**
     * 获取总记录数
     *
     * @return 数据总记录数
     */
    long count();

    /**
     * 根据条件包装器对象获取数据总记录数
     *
     * @param wrapper 条件包装器 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @return long 数据总记录数
     */
    long count(Wrapper<T> wrapper);

    /**
     * 根据实体条件对象获取数据总记录数
     *
     * @param condition 条件对象
     * @return long 数据总记录数
     */
    <POJO extends Serializable> long countByCondition(POJO condition);

    /**
     * 根据条件包装器对象获取一条数据，若存在多条结果，只取结果的集的第一条
     *
     * @param wrapper 条件包装器 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @return T 单条实体数据
     */
    T get(Wrapper<T> wrapper);

    /**
     * 根据id获取单条数据
     *
     * @param id 数据id
     * @return T 单条实体数据
     */
    T getById(String id);

    /**
     * 根据条件对象获取单条数据
     *
     * @param condition 条件对象
     * @return T 单条实体数据
     */
    <POJO extends Serializable> T getByCondition(POJO condition);

    /**
     * 根据条件包装器对象获取一条数据，若存在多条结果，只取结果的集的第一条
     *
     * @param wrapper 条件包装器 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @return Optional<T> 单条实体数据
     */
    Optional<T> getOpt(Wrapper<T> wrapper);

    /**
     * 根据id获取单条数据
     *
     * @param id 数据id
     * @return Optional<T> 单条实体数据
     */
    Optional<T> getOptById(String id);

    /**
     * 根据条件对象获取单条数据
     *
     * @param condition 条件对象
     * @return Optional<T> 单条实体数据
     */
    <POJO extends Serializable> Optional<T> getOptByCondition(POJO condition);

    /**
     * 获取所有数据
     *
     * @return List<T> 实体数据列表
     */
    List<T> list();

    /**
     * 根据条件包装器对象批量获取数据
     *
     * @param wrapper 条件包装器 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @return List<T> 实体数据列表
     */
    List<T> list(Wrapper<T> wrapper);

    /**
     * 根据数据的id集合批量获取数据
     *
     * @param ids 数据id集合
     * @return List<T> 实体数据列表
     */
    List<T> listByIds(Collection<String> ids);

    /**
     * 根据条件对象批量获取数据
     *
     * @param condition 条件对象
     * @return List<T> 实体数据列表
     */
    <POJO extends Serializable> List<T> listByCondition(POJO condition);

    /**
     * 获取分页后的数据对象
     *
     * @param page 分页条件
     * @param <P>  分页对象数据泛型，继承 {@link com.baomidou.mybatisplus.core.metadata.IPage}
     * @return Page<T> 分页后的数据集
     */
    <P extends IPage<T>> P page(P page);

    /**
     * 根据条件包装器对象获取分页后的数据
     *
     * @param page    分页条件
     * @param wrapper 条件包装器 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param <P>     分页对象数据泛型，继承 {@link com.baomidou.mybatisplus.core.metadata.IPage}
     * @return Page<T> 分页后的数据集
     */
    <P extends IPage<T>> P page(P page, Wrapper<T> wrapper);

    /**
     * 根据条件包装器对象获取分页后的数据
     *
     * @param page      分页条件
     * @param condition 条件对象
     * @param <P>       分页对象数据泛型，继承 {@link com.baomidou.mybatisplus.core.metadata.IPage}
     * @return Page<T> 分页后的数据集
     */
    <P extends IPage<T>, POJO extends Serializable> P pageByCondition(P page, POJO condition);

    /*
     * 以下的方法使用介绍:
     *
     * 一. 名称介绍
     * 1. 方法名带有 query 的为对数据的查询操作, 方法名带有 update 的为对数据的修改操作
     * 2. 方法名带有 lambda 的为内部方法入参 column 支持函数式的
     *
     * 二. 支持介绍
     * 1. 方法名带有 query 的支持以 {@link ChainQuery} 内部的方法名结尾进行数据查询操作
     * 2. 方法名带有 update 的支持以 {@link ChainUpdate} 内部的方法名为结尾进行数据修改操作
     *
     * 三. 使用示例,只用不带 lambda 的方法各展示一个例子,其他类推
     * 1. 根据条件获取一条数据: `query().eq("column", value).one()`
     * 2. 根据条件删除一条数据: `update().eq("column", value).remove()`
     *
     */

    /**
     * 链式查询 普通
     *
     * @return QueryWrapper 的包装类
     */
    QueryChainWrapper<T> query();

    /**
     * 链式查询 lambda 式
     * <p>注意：不支持 Kotlin </p>
     *
     * @return LambdaQueryWrapper 的包装类
     */
    LambdaQueryChainWrapper<T> lambdaQuery();

    /**
     * 链式更改 普通
     *
     * @return UpdateWrapper 的包装类
     */
    UpdateChainWrapper<T> update();

    /**
     * 链式更改 lambda 式
     * <p>注意：不支持 Kotlin </p>
     *
     * @return LambdaUpdateWrapper 的包装类
     */
    LambdaUpdateChainWrapper<T> lambdaUpdate();
}
