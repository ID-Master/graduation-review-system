package com.uneed.common.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.uneed.common.core.lang.UUIDUtil;
import lombok.Data;
import org.apache.ibatis.reflection.MetaObject;

import static com.uneed.common.core.lang.ObjectUtil.nullToDefault;

/**
 * AuditModel子类的公共填充处理对象.
 *
 * @author diablo
 * @date 2018/9/30
 */
@Data
public class SuperModelMetaObjectHandler implements MetaObjectHandler {

    /**
     * 新增数据时的ID
     */
    private String createIdField = "id";

    /**
     * 创建用户字段名称
     */
    private String createUserField = "createdBy";

    /**
     * 创建时间字段名称
     */
    private String createTimeField = "createdDate";

    /**
     * 最后修改用户字段名称
     */
    private String updateUserField = "updatedBy";

    /**
     * 最后修改时间字段名称
     */
    private String updateTimeField = "updatedDate";

    /**
     * 逻辑删除标记字段名称
     */
    private String removeFlagField = "removeFlag";

    /**
     * 逻辑删除默认值
     */
    private Object removeFlagValue = 0;

    /**
     * 当前用户填充器
     */
    private CurrentUserFiller userFiller;

    /**
     * 当前时间填充器
     */
    private CurrentDateFiller dateFiller;

    ///////////////////////构造函数/////////////////////////////////////////////////////

    public SuperModelMetaObjectHandler() {
        this(null);
    }

    public SuperModelMetaObjectHandler(CurrentUserFiller userFiller) {
        this(userFiller, null);
    }

    public SuperModelMetaObjectHandler(CurrentUserFiller userFiller, CurrentDateFiller dateFiller) {
        this.userFiller = nullToDefault(userFiller, new DefaultCurrentUserFiller());
        this.dateFiller = nullToDefault(dateFiller, new DefaultCurrentDateFiller());
    }

    /**
     * 新增时需填充数据
     *
     * @param metaObject 元数据对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //当前用户
        Object currentUser = getUserFiller().currentId();
        //填充创建人
        fillMetaObject(metaObject, getCreateUserField(), currentUser, false);
        //填充ID
        fillMetaObject(metaObject, getCreateIdField(), UUIDUtil.gen32UUID(), true);
        //填充最后更新人
        fillMetaObject(metaObject, getUpdateUserField(), currentUser, false);
        //当前时间
        Object currentDate = getDateFiller().currentDate();
        //填充创建时间
        fillMetaObject(metaObject, getCreateTimeField(), currentDate, false);
        //填充最后更新时间
        fillMetaObject(metaObject, getUpdateTimeField(), currentDate, false);
        //填充逻辑删除标记
        fillMetaObject(metaObject, getRemoveFlagField(), getRemoveFlagValue(), false);
    }

    /**
     * 修改时需填充数据
     *
     * @param metaObject 元数据对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        //填充最后更新人
        fillMetaObject(metaObject, getUpdateUserField(), getUserFiller().currentId(), true);
        //填充最后更新时间
        fillMetaObject(metaObject, getUpdateTimeField(), getDateFiller().currentDate(), true);
    }

    /**
     * 填充字段的默认值方法
     *
     * @param metaObject 元数据对象
     * @param fieldName  字段名称
     * @param value      字段值
     * @param isForcible 表示强制更新
     */
    protected void fillMetaObject(MetaObject metaObject, String fieldName, Object value, Boolean isForcible) {
        if (isForcible) {
            this.setFieldValByName(fieldName, value, metaObject);
            return;
        }
        Object obj = this.getFieldValByName(fieldName, metaObject);
        if (obj == null) {
            this.setFieldValByName(fieldName, value, metaObject);
        }
    }
}

