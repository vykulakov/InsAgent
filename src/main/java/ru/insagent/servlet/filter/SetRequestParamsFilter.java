package ru.insagent.servlet.filter;


import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.insagent.model.Menu;

public class SetRequestParamsFilter implements Filter {
	private final Logger logger = LoggerFactory.getLogger(SetRequestParamsFilter.class);

	@Override
	public void init(FilterConfig paramFilterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		HttpServletResponse response = (HttpServletResponse)servletResponse;

		logger.info(request.getRequestURI());

		String uri = request.getRequestURI();
		String[] uris = uri.split("/");
		String action = uris[uris.length - 1];

		@SuppressWarnings("unchecked")
		List<Menu> menu = (List<Menu>) SecurityUtils.getSubject().getSession().getAttribute("menu");
		if(menu != null) {
			for(Menu item : menu) {
				if(item.getAction() != null && item.getAction().equals(action)) {
					item.setActive(true);
				} else {
					item.setActive(false);
				}
			}
			request.setAttribute("menu", menu);
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}
