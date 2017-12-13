package servlet;

import beans.UserInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static servlet.StaticFile.userInfoList;

/**
 * Created by Ting on 2017/7/17.
 */
@WebServlet(urlPatterns = {"/register"})
public class register extends HttpServlet {
    boolean userMatch(String name, String pass){
        for(int i=0;i<userInfoList.size();i++){
            if(userInfoList.get(i).getUserName().equals(name)&&userInfoList.get(i).getPassword().equals(pass)){
                return true;
            }
        }
        // System.out.println("Key = " + key + ", Value = " + value);
        return false;
    }
    boolean userHave(String name){
        for(int i=0;i<userInfoList.size();i++){
            if(userInfoList.get(i).getUserName().equals(name)){
                return true;
            }
        }
        // System.out.println("Key = " + key + ", Value = " + value);
        return false;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session=req.getSession();

        String name=req.getParameter("name2");
        String pass=req.getParameter("pass2");
        if(name!=null&&pass!=null){
            if(!userHave(name)){
                UserInfo user=new UserInfo(name,pass);
                userInfoList.add(user);
                session.setAttribute("name",name);
                resp.sendRedirect("/console");
            }else{
                if(userMatch(name,pass)){
                    session.setAttribute("name",name);
                    resp.sendRedirect("/console");
                }else{
                    req.setAttribute("registerError","user exist and pass not match");
                    req.getRequestDispatcher("/index.jsp").forward(req,resp);
                }
            }

        }else{
            req.setAttribute("registerError","register false");
            req.getRequestDispatcher("/index.jsp").forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
