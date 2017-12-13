package servlet;

import beans.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedList;

import static beans.Constants.*;
import static servlet.StaticFile.*;

/**
 * Created by Ting on 2017/7/16.
 */
@WebServlet(urlPatterns = {"/login"})
public class login extends HttpServlet{
        boolean userMatch(String name, String pass){
            for(int i=0;i<userInfoList.size();i++){
                if(userInfoList.get(i).getUserName().equals(name)&&userInfoList.get(i).getPassword().equals(pass)){
                    return true;
                }
            }
            // System.out.println("Key = " + key + ", Value = " + value);
        return false;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session=req.getSession();

        String name=req.getParameter("name1");
        String pass=req.getParameter("pass1");
        if(userMatch(name,pass)){
            session.setAttribute("name",name);
            resp.sendRedirect("/console");
        }else{
            req.setAttribute("loginError","login false");
            req.getRequestDispatcher("/index.jsp").forward(req,resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
