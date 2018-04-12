package com.blospace.ipfs.base;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/** 
 * 
 * controller luohui
 *  
 */
public abstract class BaseController implements ServletContextAware {
	protected transient final Logger logger = LoggerFactory.getLogger(getClass());

    private String servletRealPath;
    private String servletContextPath;
	private ServletContext servletContext;

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
        this.servletRealPath = servletContext.getRealPath("/");
        this.servletContextPath = servletContext.getContextPath();
	}
	
	/**
	 *
	 * @param request
	 * @return
	 */
	protected HttpSession getSession(HttpServletRequest request){
		return request.getSession();
	}

	/**
	 *
	 * @return
	 */
	protected ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * 获取上下文路径
	 * 
	 * @return
	 */
	protected String getServletContextPath() {
		return servletContextPath;
	}

    public String getServletRealPath() {
        return servletRealPath;
    }

    public void setServletRealPath(String servletRealPath) {
        this.servletRealPath = servletRealPath;
    }

    public ModelAndView go2errorPage(String errorMsg, ModelAndView result){

        result = result != null ? result : new ModelAndView();
        result.addObject("serverPath", getServletContextPath());
        result.addObject("returnMsg",errorMsg);
        result.setViewName("/resource/html/error_404");

        return result;
    }

	/**
	 * 返回封装好的ModelAndView
	 * 主要是在model中放入了应用的工程路径
	 *
	 * @param model
	 * @param viewName
	 * @return
	 */
	protected ModelAndView packagingMAV(Map<String,Object> model, String viewName){

		if(model == null){
			model = Maps.newHashMap();
		}
		model.put("serverPath", getServletContextPath());
		return new ModelAndView(viewName, model);
	}

}