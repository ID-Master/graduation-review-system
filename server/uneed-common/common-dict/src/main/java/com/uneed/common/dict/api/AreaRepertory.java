package com.uneed.common.dict.api;

import com.uneed.common.dict.constant.AreaConstant;
import com.uneed.common.dict.entity.Area;
import org.springframework.data.redis.core.BoundHashOperations;

import java.util.Collection;
import java.util.List;

/**
 * 行政区域缓存操作接口，包含了在缓存中对字典数据进行增、删、改、查等操作
 * <p>
 * 1. 行政区域一共4级，省/直辖市、市、区/县、街道/乡镇，编码取至国家行政区域的规范编码
 * <p>
 * 2. 前3级的编码为6位，第4级编码为9位
 * <p>
 * 3. 默认排序会按照编码排序，可以通过后台修改排序
 *
 * @author anding.huang@u-need.cn
 * @date 2019/12/18
 */
public interface AreaRepertory {

    /**
     * 根据行政区域编号获取行政区域数据对象
     *
     * @param code 行政区域编号
     * @return Area 行政区域对象
     */
    Area get(String code);

    /**
     * 根据行政区域编号获取其父级行政区域数据对象
     *
     * @param code 行政区域编号
     * @return Area 行政区域对象
     * @since 1.1.0
     */
    Area parent(String code);

    /**
     * 根据行政区域编号获取行政区域名称
     *
     * @param code 行政区域编号
     * @return String 行政区域名称
     */
    String getName(String code);

    /**
     * 根据行政区域编号集合获取行政区域对象集合
     *
     * @param codes 行政区域编号集合
     * @return Area 行政区域对象集合
     * @since 1.1.0
     */
    List<Area> list(Collection<String> codes);

    /**
     * 根据行政区域编号，获取其子集，获取一级行政区域，code参数传{@link AreaConstant#ROOT_KEY}
     *
     * @param code 行政区域编号
     * @return List<Area> 子级行政区域
     * @since 1.1.0
     */
    default List<Area> subList(String code) {
        return subList(code, false);
    }

    /**
     * 根据行政区域编号，获取其子集，获取一级行政区域，code参数传{@link AreaConstant#ROOT_KEY}
     *
     * @param code   行政区域编号
     * @param isFull 是否全量子集，指返回的集合中包含了子集的子集
     * @return List<Area> 子级行政区域
     * @since 1.1.0
     */
    List<Area> subList(String code, boolean isFull);

    /**
     * 新增行政区域数据
     * <p>
     * 该方法接收一个封装后的行政区域对象，并将该行政区域加入到缓存中
     *
     * @param area 行政区域对象
     */
    void add(Area area);

    /**
     * 新增行政区域数据
     * <p>
     * 该方法接收一个封装后的行政区域对象集合，并将集合中的行政区域加入到缓存中
     *
     * @param areas 行政区域集合
     */
    void add(Collection<Area> areas);

    /**
     * 根据行政区域编号，从缓存中删除行政区域数据，会删除其对应的所有子集
     *
     * @param code 行政区域编号
     */
    void del(String code);

    /**
     * 根据行政区域编号集合，从缓存中删除行政区域信息，会删除其对应的所有子集
     *
     * @param codes 行政区域编号集合
     */
    void del(Collection<String> codes);

    /**
     * 清空所有行政区域数据
     *
     * @since 1.1.0
     */
    void clear();

    /**
     * 初始化缓存中的行政区域数据
     * <p>
     * 该方法会做俩件事：
     * 1.将areas集合中的行政区域数据加入到缓存中
     * 2.删除缓存中那些不存在于areas集合中的行政区域数据
     *
     * @param areas 行政区域集合
     */
    void initialization(Collection<Area> areas);

    /**
     * 获取RedisTemplate的BoundHashOperations对象，系统会为行政区域数据集合构建一个hash对象
     *
     * @return BoundHashOperations<String, String, String>
     * @since 1.1.0
     */
    BoundHashOperations<String, String, Area> getHash();
}
