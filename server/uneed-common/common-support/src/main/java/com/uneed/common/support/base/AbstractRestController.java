package com.uneed.common.support.base;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uneed.common.annotation.enums.Keyword;
import com.uneed.common.annotation.param.Condition;
import com.uneed.common.core.bean.BeanUtil;
import com.uneed.common.core.exception.unchecked.BusinessException;
import com.uneed.common.core.exception.unchecked.ParameterException;
import com.uneed.common.core.lang.StringUtil;
import com.uneed.common.core.session.UserSession;
import com.uneed.common.mybatis.base.SuperService;
import com.uneed.common.mybatis.info.AttributeColumn;
import com.uneed.common.mybatis.info.AttributeProperty;
import com.uneed.common.mybatis.model.SuperModel;
import com.uneed.common.mybatis.page.PageData;
import com.uneed.common.mybatis.page.PageSearch;
import com.uneed.common.mybatis.utils.AttributeUtil;
import com.uneed.common.mybatis.utils.Conditions;
import com.uneed.common.support.api.Result;
import com.uneed.common.support.cache.ParameterCache;
import com.uneed.common.support.convert.AbstractModelConverter;
import com.uneed.common.support.convert.Converters;
import com.uneed.common.support.excel.AbstractImportListener;
import com.uneed.common.support.excel.DefaultImportListener;
import io.swagger.annotations.ApiModelProperty;
import lombok.NonNull;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.uneed.common.core.lang.ObjectUtil.*;

/**
 * 控制器的超类
 * <p>
 * 1. 定义了统一的返回信息
 * <p>
 * 2. 定义了控制器的全局异常处理
 *
 * @param <S>  泛型参数，用来约定service
 * @param <T>  泛型参数，用来约定实体类类型
 * @param <C>  泛型参数，用来约定数据对象转换的转换器
 * @param <VO> 泛型参数，用来约定vo数据对象类型
 * @author diablo
 * @date 2020/4/27
 * @since 1.1.0
 */
public abstract class AbstractRestController<S extends SuperService<T>, T extends SuperModel, C extends AbstractModelConverter, VO extends Serializable>
        extends AbstractController {

    //********************************************* 常量数据 *********************************************/

    /**
     * 默认的参数缓存方法名称
     */
    private static final String DEFAULT_CACHE_METHOD_NAME = "list";

    /**
     * 默认有效性字段名称
     */
    private static final String DEFAULT_ACTIVE_FIELD = "status";

    //********************************************* 通用属性 *********************************************/

    /**
     * service逻辑处理器，由子类自动注入
     */
    @Autowired
    protected S service;

    /**
     * 实体类的class类型，用来做字段缓存，不需要每次都取
     */
    protected final Class<T> entityClass = getGenericType(1);

    /**
     * converter类的class类型，用来做字段缓存，不需要每次都取
     */
    protected final Class<C> converterClass = getGenericType(2);

    /**
     * VO类的class类型，用来做字段缓存，不需要每次都取
     */
    protected final Class<VO> voClass = getGenericType(3);

    /**
     * 数据对象转换器
     */
    protected final C converter = Converters.get(converterClass);

    //********************************************* 新增数据 *********************************************/

    /**
     * 通用的新增数据方法，接收一个VO类型的参数，将数据持久化到数据库中，执行逻辑：
     * <p>
     * 1. 将VO对象转换为实体类对象
     * <p>
     * 2. 对实体类数据去空格处理
     * <p>
     * 3. 执行消费函数接口
     * <p>
     * 4. 调用{@link SuperService#insert(SuperModel)}方法，将数据保存到数据库中
     * <p>
     * 5. 返回操作结果消息，如果insert方法返回的受影响行数小于1，响应"新增数据失败"的异常消息！否则，响应"操作成功"的消息，并填充已新增数据的id，，数据格式：
     * <p>
     * {<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"status": true,<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"code": 200,<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"message": "操作成功",<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"data": 1204963848243625985<p>
     * }
     *
     * @param vo       VO对象
     * @param consumer 消费函数接口，用来对新增方法做扩展，在操作数据库之前执行，会传递一个实体类对象
     * @return Result<Long> 响应结果
     */
    protected Result<String> insert(@NonNull VO vo, Consumer<T> consumer) {
        T entity = toEntity(vo);
        BeanUtil.trimProperty(entity);
        if (isNotNull(consumer)) {
            consumer.accept(entity);
        }
        int result = service.insert(entity);
        if (result < 1) {
            return super.failed("新增数据失败");
        }
        return super.success(entity.getId());
    }

    //********************************************* 修改数据 *********************************************/

    /**
     * 通用的修改数据方法，接收一个VO类型的参数，将数据持久化到数据库中，执行逻辑：
     * <p>
     * 1. 将VO对象转换为实体类对象
     * <p>
     * 2. 校验主键是否合规
     * <p>
     * 3. 对实体类数据去空格处理
     * <p>
     * 4. 执行消费函数接口
     * <p>
     * 5. 调用{@link SuperService#update(SuperModel)}方法来修改数据
     * <p>
     * 6. 返回操作结果消息，如果update方法返回的受影响行数小于1，响应"修改数据失败"的异常消息！否则，响应"操作成功"的消息，并填充已修改数据的id，数据格式：
     * <p>
     * {<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"status": true,<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"code": 200,<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"message": "操作成功",<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"data": 1204963848243625985<p>
     * }
     *
     * @param vo       VO对象
     * @param consumer 消费函数接口，用来对修改方法做扩展，在操作数据库之前执行，会传递一个实体类对象
     * @return Result<Long> 响应结果
     */
    protected Result<String> update(@NonNull VO vo, Consumer<T> consumer) {
        T entity = toEntity(vo);
        validatePrimary(entity);
        BeanUtil.trimProperty(entity);
        if (isNotNull(consumer)) {
            consumer.accept(entity);
        }
        int result = service.update(entity);
        if (result < 1) {
            return super.failed("修改数据失败");
        }
        return super.success(entity.getId());
    }

    //********************************************* 逻辑删除数据 *********************************************/

    /**
     * 通用的删除数据方法，接收数据的id值，对数据做逻辑删除处理，执行逻辑：
     * <p>
     * 1. 根据id从数据库中获取实体对象，如果取得的值为空，直接抛出"需要删除的数据不存在"的异常消息
     * <p>
     * 2. 执行消费函数接口
     * <p>
     * 3. 调用{@link SuperService#removeById(String)}方法，对数据做逻辑删除
     * <p>
     * 4. 返回操作结果消息，如果remove方法返回的受影响行数小于1，响应"删除数据失败"的异常消息！否则，响应"操作成功"的消息，并填充已删除数据的id，数据格式：
     * <p>
     * {<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"status": true,<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"code": 200,<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"message": "操作成功",<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"data": 1204963848243625985<p>
     * }
     *
     * @param id       数据的id
     * @param consumer 消费函数接口，用来对删除方法做扩展，在操作数据库之前执行，会传递一个实体类对象
     * @return Result<Long> 响应结果
     */
    protected Result<String> remove(@NonNull String id, Consumer<T> consumer) {
        T entity = service.getById(id);
        if (isNull(entity)) {
            throw new BusinessException("需要删除的数据不存在");
        }
        if (isNotNull(consumer)) {
            consumer.accept(entity);
        }
        int result = service.removeById(id);
        if (result < 1) {
            return super.failed("删除数据失败");
        }
        return super.success(id);
    }

    //********************************************* 设置数据有效性 *********************************************/

    /**
     * 通用的设置数据有效性方法，接收数据的id值，对数据做生效或失效处理，执行逻辑：
     * <p>
     * 1. 根据id从数据库中获取实体对象，如果取得的值为空，会抛出"需要设置有效性的数据不存在"的异常消息
     * <p>
     * 2. 如果转换函数接口不能空，通过执行转换函数接口获取到设置有效性后的实体数据
     * <p>
     * 3. 如果转换函数接口为空，调用{@link this#applyDefaultActive(SuperModel)}方法，获取默认设置有效性的实体数据
     * <p>
     * 4. 校验设置有效性后的实体数据，如果为空，会抛出"需要设置有效性的数据不存在"的异常消息
     * <p>
     * 5. 返回操作结果消息，如果update方法返回的受影响行数小于1，响应"设置数据有效性失败"的异常消息！否则，响应"操作成功"的消息，并填充已变更有效性数据的id，数据格式：
     * <p>
     * {<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"status": true,<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"code": 200,<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"message": "操作成功",<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"data": 1204963874449637378<p>
     * }
     *
     * @param id       数据的id
     * @param function 转换函数接口，用来对设置有效性方法做扩展，在操作数据库之前执行，会传递一个实体类对象，得到一个变更有效性后的实体对象
     * @return Result<String> 响应结果
     */
    protected Result<String> active(@NonNull String id, Function<T, T> function) {
        T entity = service.getById(id);
        if (isNull(entity)) {
            throw new BusinessException("需要设置有效性的数据不存在");
        }
        T active = isNotNull(function) ? function.apply(entity) : applyDefaultActive(entity);
        if (isNull(active)) {
            throw new BusinessException("构建有效性数据异常");
        }
        int result = service.update(active);
        if (result < 1) {
            return super.failed("设置数据有效性失败");
        }
        return super.success(String.valueOf(active.getId()));
    }


    /**
     * 根据实体对象数据，构建默认的有效性数据对象，子类也可以通过该方法来设置数据的有效性
     *
     * @param entity 实体类对象
     * @return T 设置完有效性后的新实体对象
     */
    protected T applyDefaultActive(T entity) {
        AttributeProperty property = AttributeUtil.getProperty(entityClass, DEFAULT_ACTIVE_FIELD);
        if (isNull(property)) {
            throw new BusinessException("不能获取到字段[" + DEFAULT_ACTIVE_FIELD + "]映射的属性信息");
        }
        T active = BeanUtil.newInstance(entityClass);
        active.setId(entity.getId());
        BeanUtil.setProperty(active, DEFAULT_ACTIVE_FIELD, BeanUtil.executeGetter(entity, property.getGetter()));
        return active;
    }

    //********************************************* 获取数据详情 *********************************************/

    /**
     * 通用的获取数据详情方法，接收数据的id值，将获取到的详情数据转VO对象后返回，执行逻辑：
     *
     * <p>
     * 1. 根据id从数据库中获取实体对象，如果取得的值为空，直接抛出"需要查询的数据不存在"的异常消息
     * <p>
     * 2. 执行消费函数接口
     * <p>
     * 3. 调用{@link AbstractModelConverter#toVO(SuperModel)}方法，将取得数据详情封装成VO对象
     * <p>
     * 4. 返回查询结果，数据格式：
     * <p>
     * {<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"status": true,<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"code": 200,<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"message": "操作成功",<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"data": {}<p>
     * }
     *
     * @param id       数据的id
     * @param consumer 消费函数接口，用来对返回详情做扩展，在封装成VO对象之前执行，会传递一个实体类对象
     * @return Result<VO> 响应结果
     */
    protected Result<VO> detail(@NonNull String id, Consumer<T> consumer) {
        T entity = service.getById(id);
        if (isNull(entity)) {
            throw new BusinessException("需要查询的数据不存在");
        }
        if (isNotNull(consumer)) {
            consumer.accept(entity);
        }
        return super.success(toVO(entity));
    }

    //********************************************* 获取数据列表 *********************************************/

    /**
     * 通用的获取分页数据方法，接收分页参数条件，获取分页后的数据集合，执行逻辑：
     * <p>
     * 1. 缓存参数条件至redis中，方便导出操作时获取导出条件，缓存时长默认为30分钟，子类可以通过调用{@link this#setParameterCache(Object, String, long)}方法自定义设置缓存名称、时长
     * <p>
     * 2. 构建分页条件，调用{@link Conditions#page(PageSearch, Class)}方法，设置当前查询页、查询页大小、排序条件
     * <p>
     * 3. 构建查询条件，获取分页参数的业务搜索条件对象，调用{@link Conditions#queryWrapper(Object, Class, String...)}方法，根据搜索条件配置的annotation{@link Condition},自动装配查询条件
     * <p>
     * 4. 执行消费函数接口
     * <p>
     * 5. 根据构建的分页条件、查询条件，从数据库中获取分页数据
     * <p>
     * 6. 封装响应结果，调用{@link AbstractModelConverter#toVOPage(IPage)}方法，将取得的分页数据封装成{@link PageData}对象
     * <p>
     * 7. 返回查询结果，数据格式：
     * <p>
     * {<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"status": true,<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"code": 200,<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"message": "操作成功",<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"data": {<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"records": [],<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"total": 0,<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"current": 1,<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"size": 10<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;}<p>
     * }
     *
     * @param search   分页参数条件
     * @param consumer 消费函数接口，用来对查询操作做扩展，在操作数据库之前执行，会传递一个查询条件对象{@link QueryWrapper}
     * @param <POJO>   搜索条件的泛型参数
     * @return Result<PageData < VO>> 响应结果
     */
    protected <POJO extends Serializable> Result<PageData<VO>> list(@NonNull PageSearch<POJO> search,
                                                                    Consumer<QueryWrapper<T>> consumer) {
        setParameterCache(search);
        Page<T> page = Conditions.page(search, entityClass);
        QueryWrapper<T> wrapper = Conditions.queryWrapper(search, entityClass);
        if (isNotNull(consumer)) {
            consumer.accept(wrapper);
        }
        service.page(page, wrapper);
        return super.success(toVOPage(page));
    }

    //********************************************* excel 导入 *********************************************/

    /**
     * 通用的excel导入方法，接收一个excel附件、excel数据解析类类型，实现对excel数据导入并入库操作，执行逻辑：
     * <p>
     * 1. 如果生产函数接口不为空，会执行生产函数接口，获取一个excel导入的监听器
     * <p>
     * 2. 否则，会构建一个默认的{@link DefaultImportListener}excel监听器，并设置监听器赋的service、converter属性值
     * <p>
     * 3. 调用excel工具{@link EasyExcel#read(InputStream, Class, ReadListener)}方法，完成导入功能
     * <p>
     * 4. 返回操作结果消息，并填充成功导入的数量，数据格式：
     * <p>
     * {<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"status": true,<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"code": 200,<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"message": "操作成功",<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;"data": 10<p>
     * }
     *
     * @param file        excel附件
     * @param importClass excel数据解析类类型
     * @param supplier    生产函数接口，用来生产导入监听器
     * @return Result<Integer> 返回导入成功数量
     */
    protected Result<Integer> importExcel(@NonNull MultipartFile file, @NonNull Class<?> importClass,
                                          Supplier<? extends AbstractImportListener> supplier) {
        AbstractImportListener listener = isNotNull(supplier) ? supplier.get() : new DefaultImportListener(service, converter);
        try {
            EasyExcel.read(file.getInputStream(), importClass, listener).sheet().doRead();
            return super.success(listener.importSize());
        } catch (IOException e) {
            String message = "导入excel附件失败！文件名[" + file.getOriginalFilename() + "]，原因：" + e.getMessage();
            log.error(message, e);
            return super.failed(message);
        }
    }

    //********************************************* excel 导出 *********************************************/

    /**
     * 通用的excel导出方法，接收一个导出的名称、excel数据解析类类型，完成对数据的导出并执行下载，执行逻辑：
     * <p>
     * 1. 设置httpResponse的导出响应信息，包含类型、编码、及导出的名称，用户也可以重写{@link this#setExportResponse(String)}方法来自定义响应信息
     * <p>
     * 2. 如果生产函数接口不为空，会执行生产函数接口，获取一个导出的数据集合
     * <p>
     * 3. 否则，会调用{@link this#defaultExportData()}方法来构建默认的导出数据，当然用户也可以重写该方法，实现默认的导出数据
     * <p>
     * 5. 调用excel工具{@link EasyExcel#write(OutputStream, Class)}方法，实现数据导出并下载
     * <p>
     * 6. 如果导出有异常，会执行{@link this#responseWriterError(String)}方法，将错误信息写到HttpResponse中，该方法也可以被重写
     *
     * @param exportName  导出的excel名称
     * @param exportClass 导出数据解析类类型
     * @param supplier    导出数据生产函数接口
     * @throws IOException IO异常
     */
    protected void exportExcel(String exportName, Class<?> exportClass, Supplier<List<?>> supplier) {
        try {
            //设置响应导出
            setExportResponse(exportName);
            //获取导出数据
            List<?> dataList = isNotNull(supplier) ? supplier.get() : defaultExportData();
            // 这里需要设置不关闭流
            EasyExcel.write(getResponse().getOutputStream(), nullToDefault(exportClass, voClass)).autoCloseStream(Boolean.FALSE)
                    .sheet("Sheet1").doWrite(dataList);
        } catch (Exception e) {
            String message = "导出excel文件失败，原因：" + e.getMessage();
            log.error(message, e);
            responseWriterError(message);
        }
    }

    /**
     * 设置response响应信息，设置的内容有：ContentType、CharacterEncoding、Header
     *
     * @param exportName 导出的excel名称
     * @throws UnsupportedEncodingException 转码失败异常
     */
    protected void setExportResponse(String exportName) throws UnsupportedEncodingException {
        getResponse().setContentType("application/vnd.ms-excel");
        getResponse().setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        getResponse().setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(exportName, "UTF-8") + ".xlsx");
    }

    /**
     * response对请求端打印异常信息
     *
     * @param message 异常消息
     */
    protected void responseWriterError(String message) {
        try {
            //重置response
            getResponse().reset();
            getResponse().setContentType("application/json");
            getResponse().setCharacterEncoding("utf-8");
            //构建失败消息，并返回
            getResponse().getWriter().println(JSON.toJSONString(failed(message)));
        } catch (IOException e) {
            log.error("HttpServletResponse写入导出异常信息失败，原因：" + e.getMessage(), e);
        }
    }

    /**
     * 获取默认的导出数据，默认导出的条件同列表页面查询条件，数据格式为VO对象集合
     * <p>
     * 条件信息会从缓存中获取，在每次执行列表查询时，会缓存查询条件，默认的条件缓存时长为30分钟。
     * <p>
     * 再导出查询中，会忽略分页的查询条件，保留排序条件
     *
     * @param <POJO> 条件泛型参数
     * @return List<VO> vo数据集合
     */
    protected <POJO extends Serializable> List<VO> defaultExportData() {
        PageSearch<POJO> search = getParameterCache();
        QueryWrapper<T> wrapper = isNotNull(search) ? Conditions.queryWrapper(search, entityClass) : Wrappers.query();
        if (isNotNull(search) && isNotEmpty(search.getSorts())) {
            Conditions.setSorting(wrapper, search.getSorts());
        }
        return toVO(service.list(wrapper));
    }

    /**
     * 获取导出的excel名称，这里会对导出的名称再做一次封装，在名称后面叠加日期信息
     *
     * @param name 导出的excel名称
     * @return String
     */
    protected String getExportName(String name) {
        return String.join("", name, DateTime.now().toString("yyyyMMddHHmmss"));
    }

    //********************************************* 获取泛型相关方法 *********************************************/

    /**
     * 根据索引获取当前类的泛型参数类型，获取不到，会抛异常
     *
     * @param index 泛型参数索引
     * @param <G>   泛型类型
     * @return Class<G> 泛型参数类型
     */
    protected <G> Class<G> getGenericType(int index) {
        Class<G> clazz = getGenericType(getClass(), index);
        if (isNull(clazz)) {
            throw new BusinessException("未能获取到当前继承类上的泛型参数！当前类类型[" + getClass() + "]，泛型参数索引[" + index + "]");
        }
        return clazz;
    }

    /**
     * 根据指定类型、索引获取执行类的泛型参数类型
     *
     * @param clazz 指定类型
     * @param index 泛型参数索引
     * @param <G>   泛型类型
     * @return Class<G> 泛型参数类型
     */
    @SuppressWarnings("unchecked")
    protected <G> Class<G> getGenericType(Class<?> clazz, int index) {
        return (Class<G>) BeanUtil.getSuperClassActualType(clazz, index);
    }

    //********************************************* 校验参数相关方法 *********************************************/

    /**
     * 验证数据的唯一性，通过查询数据库，判断校验值是否已存在，如果不唯一，会抛出参数校验异常。
     * <p>
     * 1. 如果没有传异常消息，会设置一个默认的异常消息，格式：#字段名称 + 的值[ + #字段值 + ]重复
     * <p>
     * 2. 字段名称，会首先获取字段注解{@link ApiModelProperty}的值，若注解不存在，会设置默认值：数据
     * <p>
     * 5. #字段值，会通过反射获取校验字段上设置的值
     *
     * @param bean    bean对象
     * @param field   需要校验的字段
     * @param message 数据不唯一的消息提醒
     */
    protected void validateUnique(Object bean, String field, String message) {
        //判断需要校验的字段是否存在
        if (isNull(AttributeUtil.getProperty(entityClass, field))) {
            throw new ParameterException("需要校验唯一性的字段[" + field + "]不存在！");
        }
        Object value = BeanUtil.getProperty(bean, field);
        if (queryValidateUniqueCount(bean, field, value) > 0) {
            throwValidateUniqueException(bean, field, message, value);
        }
    }

    /**
     * 校验主键值是否合规，若主键为null或是值<=0，会抛异常
     *
     * @param bean bean对象
     */
    protected void validatePrimary(Object bean) {
        String id = BeanUtil.getProperty(bean, AttributeUtil.getPrimaryFieldName(entityClass));
        if (StringUtil.isEmpty(id)) {
            throw new ParameterException("主键值[" + id + "]不合规，不能对数据进行修改");
        }
    }

    /**
     * 查询字段校验唯一条件的数量
     */
    private long queryValidateUniqueCount(Object bean, String field, Object value) {
        //构建查询对象
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        if (isNotNull(value)) {
            Conditions.setCondition(wrapper, Keyword.EQ, field, value);
        } else {
            Conditions.setCondition(wrapper, Keyword.IS_NULL, field, null);
        }
        //设置主键条件
        AttributeColumn primary = AttributeUtil.getPrimaryColumn(entityClass);
        String id = BeanUtil.getProperty(bean, primary.getFieldName());
        if (isNotNull(id)) {
            Conditions.setCondition(wrapper, Keyword.NE, primary.getName(), id);
        }
        //根据条件获取数量
        return service.count(wrapper);
    }

    /**
     * 抛出唯一性校验异常信息
     */
    private void throwValidateUniqueException(Object bean, String field, String message, Object value) {
        //如果异常消息不为空
        if (isNotEmpty(message)) {
            throw new ParameterException(message);
        }
        //默认异常消息
        ApiModelProperty property = BeanUtil.getPropertyAnnotation(bean.getClass(), field, ApiModelProperty.class);
        throw new ParameterException((isNotNull(property) ? property.value() : "数据") + "的值[" + value + "]重复");
    }

    //********************************************* 缓存参数相关方法 *********************************************/

    /**
     * 缓存参数
     */
    protected void setParameterCache(Object parameter) {
        setParameterCache(parameter, DEFAULT_CACHE_METHOD_NAME);
    }

    /**
     * 缓存参数，需要设置缓存名称
     */
    @SuppressWarnings("SameParameterValue")
    protected void setParameterCache(Object parameter, String methodName) {
        setParameterCache(parameter, methodName, ParameterCache.DEFAULT_EXPIRE);
    }

    /**
     * 缓存参数，需要设置缓存名称、过期时长
     */
    @SuppressWarnings("SameParameterValue")
    protected void setParameterCache(Object parameter, String methodName, long expire) {
        ParameterCache.instance().put(getParameterCacheKey(methodName), parameter, expire);
    }

    /**
     * 获取缓存参数
     */
    protected <P> P getParameterCache() {
        return getParameterCache(DEFAULT_CACHE_METHOD_NAME);
    }

    /**
     * 获取缓存参数，需要缓存名称参数
     */
    @SuppressWarnings("SameParameterValue")
    protected <P> P getParameterCache(String method) {
        return ParameterCache.instance().get(getParameterCacheKey(method));
    }

    /**
     * 构建redis缓存参数的规则，当前用户id+当前类名+方法名
     */
    private String getParameterCacheKey(String methodName) {
        return String.join(":", String.valueOf(UserSession.id()), getClass().getName(), methodName);
    }

    //********************************************* VO与实体类型数据转换方法 *********************************************/

    /**
     * vo对象数据转实体对象数据
     */
    @SuppressWarnings("unchecked")
    protected T toEntity(VO vo) {
        return (T) converter.toEntity(vo);
    }

    /**
     * vo对象数据集合转实体对象数据集合
     */
    @SuppressWarnings("unchecked")
    protected List<T> toEntity(List<VO> list) {
        return converter.toEntity(list);
    }

    /**
     * 实体对象数据转vo对象数据
     */
    @SuppressWarnings("unchecked")
    protected VO toVO(T entity) {
        return (VO) converter.toVO(entity);
    }

    /**
     * 实体对象数据集合转vo对象数据集合
     */
    @SuppressWarnings("unchecked")
    protected List<VO> toVO(List<T> list) {
        return converter.toVO(list);
    }

    /**
     * mybatis-plus的page对象数据转PageData对象数据
     */
    @SuppressWarnings("unchecked")
    protected PageData<VO> toVOPage(IPage<T> page) {
        return converter.toVOPage(page);
    }
}
