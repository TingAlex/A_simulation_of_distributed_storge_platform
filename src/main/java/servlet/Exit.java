package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Ting on 2017/7/17.
 */
@WebServlet(urlPatterns = {"/exit"})
public class Exit extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取session
        HttpSession session = request.getSession();
        // 获取用户对象
        String user = (String)session.getAttribute("name");
        // 判断用户是否有效
        if(user != null){
            // 将用户对象逐出session
            session.removeAttribute("user");
            request.setAttribute("exitMessage","exit success");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
        // 转发到message.jsp页面
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
