package com.uneed.common.mybatis.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.lang.ObjectUtil;
import com.uneed.common.core.lang.Validate;
import com.uneed.common.mybatis.model.SuperModel;
import com.uneed.common.mybatis.utils.Conditions;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.MyBatisExceptionTranslator;
import org.mybatis.spring.SqlSessionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import static com.uneed.common.core.lang.ObjectUtil.*;
import static com.uneed.common.mybatis.utils.Conditions.updateWrapper;

/**
 * service的默认实现，该类实现了最常用的增、删、改、查方法，子类可以通过继承，获取SQL常用功能的赋能.
 * 泛型：
 * M，定义mapper接口的泛型参数
 * T，定义实体的泛型参数
 *
 * @author diablo
 * @date 2020/4/2
 */
public class SuperServiceImpl<M extends SuperMapper<T>, T extends SuperModel> implements SuperService<T> {

    /**
     * 日志记录器
     */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * mapper接口，泛型参数赋值后，会自动注入
     */
    @SuppressWarnings("ALL")
    @Autowired
    protected M mapper;

    //////////////////////////////////////////////////////// 公共方法 ///////////////////////////////////////////////////////////////////////////////

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insert(T entity) {
        return mapper.insert(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertBatch(Collection<? extends T> collection) {
        String sqlStatement = getSqlStatement(SqlMethod.INSERT_ONE);
        return executeBatch(collection, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int update(T entity) {
        return mapper.updateById(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateBatch(Collection<? extends T> collection) {
        String statement = getSqlStatement(SqlMethod.UPDATE_BY_ID);
        return executeBatch(collection, (sqlSession, entity) -> {
            MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
            param.put(Constants.ENTITY, entity);
            sqlSession.update(statement, param);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateForAll(T entity) {
        Wrapper<T> wrapper = updateWrapper(entity);
        return mapper.update(wrapper.getEntity(), wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateBatchForAll(Collection<? extends T> collection) {
        final int[] count = {0};
        if(isNotEmpty(collection)){
            collection.forEach(t -> count[0] +=updateForAll(t));
        }
        return count[0];
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertOrUpdate(T entity) {
        Validate.notNull(entity, "bean can't be null!");
        return isNull(entity.getId()) || isNull(getById(entity.getId())) ? insert(entity) : update(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertOrUpdateBatch(Collection<? extends T> collection) {
        String statement = getSqlStatement(SqlMethod.UPDATE_BY_ID);
        return insertOrUpdateBatch(collection, (session, entity) -> {
            if (isNull(entity.getId())) {
                return true;
            }
            return isNull(session.selectOne(getSqlStatement(SqlMethod.SELECT_BY_ID), entity));
        }, (session, entity) -> {
            MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
            param.put(Constants.ENTITY, entity);
            session.update(statement, param);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int removeById(String id) {
        return mapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int removeByIds(Collection<String> ids) {
        return isEmpty(ids) ? 0 : mapper.deleteBatchIds(ids);
    }

    @Override
    public long count() {
        return count(Wrappers.emptyWrapper());
    }

    @Override
    public long count(Wrapper<T> wrapper) {
        return nullToDefault(mapper.selectCount(wrapper), 0);
    }

    @Override
    public <POJO extends Serializable> long countByCondition(POJO condition) {
        return count(Conditions.queryWrapper(condition, getEntityClass()));
    }

    @Override
    public T get(Wrapper<T> wrapper) {
        IPage<T> page = page(new Page<>(0, 1), wrapper);
        return isEmpty(page.getRecords()) ? null : page.getRecords().get(0);
    }

    @Override
    public T getById(String id) {
        return mapper.selectById(id);
    }

    @Override
    public <POJO extends Serializable> T getByCondition(POJO condition) {
        return get(Conditions.queryWrapper(condition, getEntityClass()));
    }

    @Override
    public Optional<T> getOpt(Wrapper<T> wrapper) {
        return Optional.ofNullable(get(wrapper));
    }

    @Override
    public Optional<T> getOptById(String id) {
        return Optional.ofNullable(getById(id));
    }

    @Override
    public <POJO extends Serializable> Optional<T> getOptByCondition(POJO condition) {
        return Optional.ofNullable(getByCondition(condition));
    }

    @Override
    public List<T> list() {
        return list(Wrappers.emptyWrapper());
    }

    @Override
    public List<T> list(Wrapper<T> wrapper) {
        return mapper.selectList(wrapper);
    }

    @Override
    public List<T> listByIds(Collection<String> ids) {
        return isEmpty(ids) ? Lists.newArrayList() : mapper.selectBatchIds(ids);
    }

    @Override
    public <POJO extends Serializable> List<T> listByCondition(POJO condition) {
        return list(Conditions.queryWrapper(condition, getEntityClass()));
    }

    @Override
    public <P extends IPage<T>> P page(P page) {
        return page(page, Wrappers.emptyWrapper());
    }

    @Override
    public <P extends IPage<T>> P page(P page, Wrapper<T> wrapper) {
        return mapper.selectPage(page, wrapper);
    }

    @Override
    public <P extends IPage<T>, POJO extends Serializable> P pageByCondition(P page, POJO condition) {
        return page(page, Conditions.queryWrapper(condition, getEntityClass()));
    }

    @Override
    public QueryChainWrapper<T> query() {
        return ChainWrappers.queryChain(mapper);
    }

    @Override
    public LambdaQueryChainWrapper<T> lambdaQuery() {
        return ChainWrappers.lambdaQueryChain(mapper);
    }

    @Override
    public UpdateChainWrapper<T> update() {
        return ChainWrappers.updateChain(mapper);
    }

    @Override
    public LambdaUpdateChainWrapper<T> lambdaUpdate() {
        return ChainWrappers.lambdaUpdateChain(mapper);
    }

    //////////////////////////////////////////////////////// 功能方法 ///////////////////////////////////////////////////////////////////////////////

    /**
     * 执行批量操作（默认批次提交数量{@link SuperServiceImpl#getBatchSize}）
     *
     * @param collection 数据集合
     * @param consumer   执行方法
     * @param <E>        泛型
     * @return int 受影响行数
     */
    protected <E> int executeBatch(Collection<E> collection, BiConsumer<SqlSession, E> consumer) {
        return executeBatch(collection, consumer, getBatchSize());
    }

    /**
     * 执行批量新增或修改
     *
     * @param collection 数据集合
     * @param predicate  判断是否信息的函数
     * @param consumer   执行修改函数
     * @param <E>        泛型
     * @return int 受影响行数
     */
    public <E> int insertOrUpdateBatch(Collection<E> collection, BiPredicate<SqlSession, E> predicate, BiConsumer<SqlSession, E> consumer) {
        String statement = getSqlStatement(SqlMethod.INSERT_ONE);
        return executeBatch(collection, (session, entity) -> {
            if (predicate.test(session, entity)) {
                session.insert(statement, entity);
            } else {
                consumer.accept(session, entity);
            }
        }, getBatchSize());
    }

    /**
     * 执行批量操作
     *
     * @param collection 数据集合
     * @param consumer   执行方法
     * @param batchSize  批量大小
     * @param <E>        泛型
     * @return int 受影响行数
     */
    protected <E> int executeBatch(Collection<E> collection, BiConsumer<SqlSession, E> consumer, int batchSize) {
        Validate.isTrue(batchSize > 0, "batchSize must not be less than one");
        boolean result = ObjectUtil.isNotEmpty(collection) && executeBatch(session -> {
            int size = collection.size();
            int i = 1;
            for (E element : collection) {
                consumer.accept(session, element);
                if ((i % batchSize == 0) || i == size) {
                    session.flushStatements();
                }
                i++;
            }
        });
        return result ? collection.size() : 0;
    }

    /**
     * 执行批量操作
     *
     * @param consumer 执行方法
     */
    protected boolean executeBatch(Consumer<SqlSession> consumer) {
        SqlSessionFactory factory = SqlHelper.sqlSessionFactory(getEntityClass());
        SqlSessionHolder holder = (SqlSessionHolder) TransactionSynchronizationManager.getResource(factory);
        boolean transaction = TransactionSynchronizationManager.isSynchronizationActive();
        if (holder != null) {
            SqlSession session = holder.getSqlSession();
            //原生无法支持执行器切换，当存在批量操作时，会嵌套两个session的，优先commit上一个session
            //按道理来说，这里的值应该一直为false。
            session.commit(!transaction);
        }
        SqlSession session = factory.openSession(ExecutorType.BATCH);
        if (!transaction) {
            log.warn("SqlSession [{}] was not registered for synchronization because DataSource is not transactional", session);
        }
        try {
            consumer.accept(session);
            //非事物情况下，强制commit。
            session.commit(!transaction);
            return true;
        } catch (Throwable t) {
            session.rollback();
            Throwable unwrapped = ExceptionUtil.unwrapThrowable(t);
            if (unwrapped instanceof RuntimeException) {
                MyBatisExceptionTranslator translator = new MyBatisExceptionTranslator(
                        factory.getConfiguration().getEnvironment().getDataSource(),
                        true);
                throw Objects.requireNonNull(translator.translateExceptionIfPossible((RuntimeException) unwrapped));
            }
            throw ExceptionUtils.mpe(unwrapped);
        } finally {
            session.close();
        }
    }

    /**
     * 获取 SqlStatement
     *
     * @param sqlMethod mybatis plus 执行的方法枚举参数
     * @return String
     */
    protected String getSqlStatement(SqlMethod sqlMethod) {
        return SqlHelper.getSqlStatement(getMapperClass(), sqlMethod);
    }

    /**
     * 批次操作的数量，默认值为1000，子类可以重写当前方法，为批次操作设置一个新的数值
     *
     * @return Integer
     */
    protected Integer getBatchSize() {
        if (ObjectUtil.isNull(batchSize)) {
            batchSize = 1000;
        }
        return batchSize;
    }

    /**
     * 返回当前实体类的类型，默认会从泛型参数中获取，子类也可以重写该方法
     *
     * @return Class<T> 实体类的类型
     */
    @SuppressWarnings("unchecked")
    protected Class<M> getMapperClass() {
        if (ObjectUtil.isNull(mapperClass)) {
            mapperClass = (Class<M>) ReflectionKit.getSuperClassGenericType(getClass(), 0);
        }
        return mapperClass;
    }

    /**
     * 返回当前Mapper类的类型，默认会从泛型参数中获取，子类也可以重写该方法
     *
     * @return Class<M> Mapper类的类型
     */
    @SuppressWarnings("unchecked")
    protected Class<T> getEntityClass() {
        if (ObjectUtil.isNull(entityClass)) {
            entityClass = (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 1);
        }
        return entityClass;
    }

    //////////////////////////////////////////////////////// 私有属性 ///////////////////////////////////////////////////////////////////////////////

    /**
     * 批次操作值
     */
    private Integer batchSize;

    /**
     * Mapper类的class类型
     */
    private Class<M> mapperClass;

    /**
     * 实体类的class类型
     */
    private Class<T> entityClass;

}
