package com.uneed.common.core.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class description goes here.
 *
 * @author diablo
 * @date 17/12/31
 */
public interface ExceptionHandle {

    Object handle(HttpServletRequest request, HttpServletResponse response, Exception exception);

}
