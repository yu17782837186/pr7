package com.ning.servlet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@WebServlet("/pool")
public class TestServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        try {
            Context ctx = new InitialContext();
            //获取数据库连接池对象
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/test");
            Connection coon = ds.getConnection();
            PreparedStatement ps = coon.prepareStatement("select * from flower");
            ResultSet rs = ps.executeQuery();//ctrl+alt+v
            PrintWriter out = response.getWriter();
            while(rs.next()){
                out.print(rs.getInt(1)+"&nbsp;&nbsp;&nbsp;&nbsp;"+rs.getString("name")+"<br />");
            }
            out.flush();
            out.close();
            rs.close();//并不是真正的关掉 而是把它转换成idle状态(空闲状态)等待应用程序使用
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
