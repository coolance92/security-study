package com.coolance.security.rbac.repository.support;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.Date;

/**
 * @ClassName AbstractConditionBuilder
 * @Description 查询条件建造器
 * @Author Coolance
 * @Version
 * @Date 2019/9/13 14:27
 */
public abstract class AbstractConditionBuilder<T> {

    /**
     * 添加in条件
     *
     * @param queryWrapper
     * @param values
     */
    protected void addInConditionToColumn(QueryWrapper<T> queryWrapper, String column, Object values) {
        if (needAddCondition(values)) {
            Path<?> fieldPath = getPath(queryWrapper.getRoot(), column);
            if (values.getClass().isArray()) {
                queryWrapper.addPredicate(fieldPath.in((Object[]) values));
            } else if (values instanceof Collection) {
                queryWrapper.addPredicate(fieldPath.in((Collection<?>) values));
            }
        }
    }

    /**
     * 添加between条件查询
     *
     * @param queryWrapper
     * @param minValue     范围下限
     * @param maxValue     范围上限
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void addBetweenConditionToColumn(QueryWrapper<T> queryWrapper, String column, Comparable minValue, Comparable maxValue) {
        if (minValue != null || maxValue != null) {
            Path<? extends Comparable> fieldPath = getPath(queryWrapper.getRoot(), column);
            if (minValue != null && maxValue != null) {
                queryWrapper.addPredicate(queryWrapper.getCb().between(fieldPath, minValue, processMaxValueOnDate(maxValue)));
            } else if (minValue != null) {
                queryWrapper.addPredicate(queryWrapper.getCb().greaterThanOrEqualTo(fieldPath, minValue));
            } else if (maxValue != null) {
                queryWrapper.addPredicate(queryWrapper.getCb().lessThanOrEqualTo(fieldPath, processMaxValueOnDate(maxValue)));
            }
        }
    }

    /**
     * 当范围查询的条件是小于，并且值的类型是Date时，将传入的Date值变为当天的夜里12点的值。
     *
     * @param maxValue
     * @return
     */
    private Comparable processMaxValueOnDate(Comparable maxValue) {
        if (maxValue instanceof Date) {
            maxValue = new DateTime(maxValue).withTimeAtStartOfDay().plusDays(1).plusSeconds(-1).toDate();
        }
        return maxValue;
    }

    /**
     * 添加大于条件查询
     *
     * @param queryWrapper
     * @param minValue
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void addGreaterThanConditionToColumn(QueryWrapper<T> queryWrapper, String column, Comparable minValue) {
        if (minValue != null) {
            Path<? extends Comparable> fieldPath = getPath(queryWrapper.getRoot(), column);
            queryWrapper.addPredicate(queryWrapper.getCb().greaterThan(fieldPath, minValue));
        }
    }

    /**
     * 添加大于等于条件查询
     *
     * @param queryWrapper
     * @param minValue
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void addGreaterThanOrEqualConditionToColumn(QueryWrapper<T> queryWrapper, String column, Comparable minValue) {
        if (minValue != null) {
            Path<? extends Comparable> fieldPath = getPath(queryWrapper.getRoot(), column);
            queryWrapper.addPredicate(queryWrapper.getCb().greaterThanOrEqualTo(fieldPath, minValue));
        }
    }

    /**
     * 添加小于条件查询
     *
     * @param queryWrapper
     * @param maxValue
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void addLessThanConditionToColumn(QueryWrapper<T> queryWrapper, String column, Comparable maxValue) {
        if (maxValue != null) {
            Path<? extends Comparable> fieldPath = getPath(queryWrapper.getRoot(), column);
            queryWrapper.addPredicate(queryWrapper.getCb().lessThan(fieldPath, processMaxValueOnDate(maxValue)));
        }
    }

    /**
     * 添加小于等于条件查询
     *
     * @param queryWrapper
     * @param maxValue
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void addLessThanOrEqualConditionToColumn(QueryWrapper<T> queryWrapper, String column, Comparable maxValue) {
        if (maxValue != null) {
            Path<? extends Comparable> fieldPath = getPath(queryWrapper.getRoot(), column);
            queryWrapper.addPredicate(queryWrapper.getCb().lessThanOrEqualTo(fieldPath, processMaxValueOnDate(maxValue)));
        }
    }

    /**
     * <pre>
     * 添加like条件
     * <pre>
     * @param queryWrapper
     * @param column
     * @param value
     */
    protected void addLikeConditionToColumn(QueryWrapper<T> queryWrapper, String column, String value) {
        if (StringUtils.isNotBlank(value)) {
            queryWrapper.addPredicate(createLikeCondition(queryWrapper, column, value));
        }
    }

    /**
     * @param queryWrapper
     * @param column
     * @param value
     * @return
     */
    protected Predicate createLikeCondition(QueryWrapper<T> queryWrapper, String column, String value) {
        Path<String> fieldPath = getPath(queryWrapper.getRoot(), column);
        Predicate condition = queryWrapper.getCb().like(fieldPath, "%" + value + "%");
        return condition;
    }

    /**
     * 添加like条件
     *
     * @param queryWrapper
     * @param column
     * @param value
     */
    @SuppressWarnings("unchecked")
    protected void addStartsWidthConditionToColumn(QueryWrapper<T> queryWrapper, String column, String value) {
        if (StringUtils.isNotBlank(value)) {
            Path<String> fieldPath = getPath(queryWrapper.getRoot(), column);
            queryWrapper.addPredicate(queryWrapper.getCb().like(fieldPath, value + "%"));
        }
    }

    /**
     * 添加等于条件
     *
     * @param queryWrapper
     * @param column       指出要向哪个字段添加条件
     * @param value        指定字段的值
     */
    protected void addEqualsConditionToColumn(QueryWrapper<T> queryWrapper, String column, Object value) {
        if (needAddCondition(value)) {
            Path<?> fieldPath = getPath(queryWrapper.getRoot(), column);
            queryWrapper.addPredicate(queryWrapper.getCb().equal(fieldPath, value));
        }
    }

    /**
     * 添加不等于条件
     *
     * @param queryWrapper
     * @param column       指出要向哪个字段添加条件
     * @param value        指定字段的值
     */
    protected void addNotEqualsConditionToColumn(QueryWrapper<T> queryWrapper, String column, Object value) {
        if (needAddCondition(value)) {
            Path<?> fieldPath = getPath(queryWrapper.getRoot(), column);
            queryWrapper.addPredicate(queryWrapper.getCb().notEqual(fieldPath, value));
        }
    }

    /**
     * @param root
     * @param property
     * @return
     */
    @SuppressWarnings("rawtypes")
    protected Path getPath(Root root, String property) {
        String[] names = StringUtils.split(property, ".");
        Path path = root.get(names[0]);
        for (int i = 1; i < names.length; i++) {
            path = path.get(names[i]);
        }
        return path;
    }

    /**
     * 判断是否需要添加where条件
     *
     * @param value
     * @return
     */
    protected boolean needAddCondition(Object value) {
        boolean addCondition = false;
        if (value != null) {
            if (value instanceof String) {
                if (StringUtils.isNotBlank(value.toString())) {
                    addCondition = true;
                }
            } else if (value.getClass().isArray()) {
                if (ArrayUtils.isNotEmpty((Object[]) value)) {
                    addCondition = true;
                }
            } else if (value instanceof Collection) {
                if (CollectionUtils.isNotEmpty((Collection) value)) {
                    addCondition = true;
                }
            } else {
                addCondition = true;
            }
        }
        return addCondition;
    }

}
