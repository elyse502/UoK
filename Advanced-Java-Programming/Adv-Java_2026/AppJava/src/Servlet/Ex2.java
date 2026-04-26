package Servlet;

import java.io.*;
import javax.servlet.*;

public class Ex2 extends HttpServlet {
    
    public static final long serialVersionUID = 1L;
    
    public Ex2() {
        
    }
    
    @Override
    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletOutputStream out = response.getOutputStream();
        
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Hello Servlet</title>");
        out.println("<body>");
        out.println("<h3>Hello World</h3>");
        out.println("This is the first servlet");
        out.println("</body>");
        out.println("<html>");
    }
    
    @Override
    protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
