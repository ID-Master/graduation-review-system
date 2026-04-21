package com.uneed.common.mybatis.utils;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.uneed.common.core.collection.ArrayUtil;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.lang.Validate;
import com.uneed.common.mybatis.model.IdModel;
import com.uneed.common.mybatis.model.SuperModel;
import com.uneed.common.mybatis.model.TenantModel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.uneed.common.core.lang.ObjectUtil.*;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/15
 */
@Slf4j
public class TableUtil {

    /**
     * 公共类常量
     */
    public static final Class<?>[] SUPER_CLASS = {IdModel.class, SuperModel.class, TenantModel.class};

    /**
     * 公共字段常量
     */
    public static final String[] SUPER_FIELDS = {"id", "createdBy", "updatedBy", "createdDate", "updatedDate", "removeFlag",
            "tenantId"};

    /**
     * 公共数据库列字段常量
     */
    public static final String[] SUPER_COLUMNS = {"id", "created_by", "updated_by", "created_date", "updated_date", "remove_flag",
            "tenant_id"};

    /**
     * 私有化构造函数
     */
    private TableUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 获取实体映射的表信息 {@link TableInfo}
     *
     * @param entityClass 实体类类型
     * @return TableInfo
     */
    public static TableInfo getTableInfo(Class<?> entityClass) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        Validate.notNull(tableInfo, "execute error! because can not find cache of TableInfo for entity[{}]!", entityClass);
        return tableInfo;
    }

    /**
     * 根据实体映射表信息获取映射的字段信息集合，不含主键
     *
     * @param tableInfo 实体映射表继续
     * @return List<TableFieldInfo>
     */
    public static List<TableFieldInfo> getTableFieldInfos(TableInfo tableInfo) {
        return isNotNull(tableInfo) ? nullToDefault(tableInfo.getFieldList(), Lists.newArrayList()) : Lists.newArrayList();
    }

    /**
     * 根据字段名称从TableInfo中获取表字段TableFieldInfo信息
     *
     * @param tableInfo 表信息
     * @param field     字段名称，也可以是映射的数据库列名称
     * @return TableFieldInfo 表字段信息
     */
    public static TableFieldInfo getTableFieldInfo(TableInfo tableInfo, String field) {
        return getTableFieldInfos(tableInfo).stream()
                                            .filter(f -> equal(f.getProperty(), field) || equal(f.getColumn(), field))
                                            .findFirst().orElse(null);
    }

    /**
     * 判断是否公共字段
     *
     * @param field 字段名称，也可以是映射的数据库列名称
     * @return boolean
     */
    public static Boolean isSuper(String field) {
        return ArrayUtil.contains(SUPER_FIELDS, field) || ArrayUtil.contains(SUPER_COLUMNS, field);
    }

    /**
     * 判断是否公共字段的超类
     *
     * @param clazz 类型
     * @return boolean
     */
    public static Boolean isSuperClass(Class<?> clazz) {
        return ArrayUtil.contains(SUPER_CLASS, clazz);
    }

    /**
     * 判断是否是主键
     *
     * @param field 字段名称，也可以是映射的数据库列名称
     * @param info  表信息
     * @return boolean
     */
    public static boolean isPrimary(String field, TableInfo info) {
        return isNotNull(info) && (equal(field, info.getKeyProperty()) || equal(field, info.getKeyColumn()));
    }
}
