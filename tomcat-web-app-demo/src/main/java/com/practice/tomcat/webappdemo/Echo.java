package com.practice.tomcat.webappdemo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Echo extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (null == req.getQueryString()) {
            resp.getOutputStream().write("please input param in url ! ".getBytes());
            return;
        }
        List<byte[]> bytes = new ArrayList<byte[]>();
        bytes.add(new byte[1024]);
        System.out.println("query : " + req.getQueryString());

        resp.getOutputStream().write(req.getQueryString().getBytes());

    }
}
