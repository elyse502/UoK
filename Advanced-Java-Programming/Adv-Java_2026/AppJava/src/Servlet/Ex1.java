package Servlet;

import javax.io.*;

public class Ex1 extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    public Ex1 () {
        
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletOutputStream out = response.getOutputStream();
        
        out.println("<html>");
        out.println("<head><title>Hello Servlet</title></head>");
        out.println("<body>");
        out.println("<h3>Hello World</h3>");
        out.println("This is my first Servlet");
        out.println("</body>");
        out.println("</html>");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }    
}
