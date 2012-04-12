/**
 * 
 */
package com.altavia.props;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hkachetel
 * 
 */
public class PropsServlet extends HttpServlet {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private static final long serialVersionUID = 1L;
    private PropsAcces propsAcces;

    public PropsServlet() {
	super();
    }

    @Override
    public void init() throws ServletException {
	super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	log.info("appel de la servlet PropsServlet");
	String propsFileName = req.getParameter("propsFileName");

	if (propsFileName == null) {
	    resp.sendError(404, "Pas de paramamètre propsFileName");
	    return;
	}
	try {
	    String propName = req.getParameter("propName");
	    String propVal = req.getParameter("propVal");

	    initPropsAcces(propsFileName);
	    
	    if (propName != null && propVal != null) {
		propsAcces.setProperty(propName, propVal, true);
	    }
	    
	    PrintWriter out = resp.getWriter();
	    if (propName != null && propVal == null) {
		out.write((String) propsAcces.getProperty(propName));
	    } else {
		propsAcces.getProperties().store(out, "No comment");
	    }
	} catch (Exception e) {
	    resp.sendError(500, "Erreur lors du traitement de la Servlet " + e.getMessage());
	    return;
	}
    }

    private void initPropsAcces(String propsFileName) {
	propsAcces = PropsAcces.getPropsAcces(propsFileName);
    }
}
