/**
 *
 */
package com.coolance.security.rbac.repository.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Domain2InfoConverter
 * @Description 转换器
 * @Author Coolance
 * @Version
 * @Date 2019/9/13 14:27
 */
public class QueryResultConverter {

    private static Logger logger = LoggerFactory.getLogger(QueryResultConverter.class);

    /**
     * @param pageData
     * @param clazz
     * @param pageable
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T, I> Page<I> convert(Page<T> pageData, Class<I> clazz, Pageable pageable) {
        List<T> contents = pageData.getContent();
        List<I> infoList = convert(contents, clazz);
        return new PageImpl<>(infoList, pageable, pageData.getTotalElements());
    }

    public static <I, T> List<I> convert(List<T> contents, Class<I> clazz) {
        List<I> infoList = new ArrayList<>();
        for (T domain : contents) {
            I info;
            try {
                info = clazz.newInstance();
                BeanUtils.copyProperties(domain, info);
            } catch (Exception e) {
                logger.info("转换数据失败", e);
                throw new RuntimeException("转换数据失败");
            }
            if (info != null) {
                infoList.add(info);
            }

        }
        return infoList;
    }

    /**
     * @param pageData
     * @param pageable
     * @param converter
     * @return
     */
    public static <T, I> Page<I> convert(Page<T> pageData, Pageable pageable, Domain2InfoConverter<T, I> converter) {
        List<T> contents = pageData.getContent();
        List<I> infoList = convert(contents, converter);
        return new PageImpl<>(infoList, pageable, pageData.getTotalElements());
    }

    public static <I, T> List<I> convert(List<T> contents, Domain2InfoConverter<T, I> converter) {
        List<I> infoList = new ArrayList<>();
        for (T domain : contents) {
            infoList.add(converter.convert(domain));
        }
        return infoList;
    }


}
