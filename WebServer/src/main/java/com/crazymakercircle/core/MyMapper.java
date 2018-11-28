/**
 * Created by 尼恩 at 疯狂创客圈
 */

package com.crazymakercircle.core;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;


/**
 * The interface My mapper.
 *
 * @param <T> the type parameter @author paascloud.net@gmail.com
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}