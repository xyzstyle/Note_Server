package com.xyz.web.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.xyz.web.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.xyz.web.dao.UserDAO;
import com.xyz.web.model.User;

/**
 * @author xyz
 * Feb 19, 2012 http://localhost:8080/JSONDemo2ServerV2/user
 */
public class UserServlet extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int method = Integer.parseInt(request.getParameter("method"));
        response.setContentType("text/json;charset=UTF-8");
        switch (method) {
            case 1:
                getUsers(response);
                break;
            case 2:
                insertUsers(request, response);
                break;
            case 3:
                getUserById(request, response);
                break;

            default:
                break;
        }
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private void getUsers(HttpServletResponse response) throws IOException {

        JSONObject resultJson = new JSONObject();
        UserDAO userDAO;
        try {
            userDAO = new UserDAO();
            ArrayList<User> userList = userDAO.getUsers();
            JSONObject retObject = new JSONObject();
            retObject.put("totalNum", userList.size());
            JSONArray userJsonArray = new JSONArray();
            for (User user : userList) {
                JSONObject userObject = new JSONObject();
                userObject.put("_id", user.getId());
                userObject.put("name", user.getName());
                userObject.put("password", user.getPassword());
                userJsonArray.put(userObject);
            }
            retObject.put("info", userJsonArray);
            resultJson.put("retCode", 0);
            resultJson.put("msg", "ok");
            resultJson.put("data", retObject);
        } catch (SQLException | ClassNotFoundException | JSONException e) {
            e.printStackTrace();
            resultJson = Utils.getErrJson(e);
        }
        PrintWriter out = response.getWriter();
        out.println(resultJson);
        out.flush();
        out.close();
    }

    private void insertUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String inputString = Utils.streamToString(request.getInputStream());
        List<User> users = parseJson(inputString);
        JSONObject resultJson = new JSONObject();
        boolean result = true;
        if (users.size() != 0) {
            try {
                UserDAO dao;
                dao = new UserDAO();
                for (User user : users) {

                    if (!dao.insert(user)) {
                        result = false;
                        break;
                    }
                }
                if (result) {
                    resultJson.put("ret", 0);
                    resultJson.put("msg", "ok");
                } else {
                    resultJson.put("ret", 1);
                    resultJson.put("msg", "fail");
                }
            } catch (SQLException | ClassNotFoundException | JSONException e) {
                e.printStackTrace();
                resultJson = Utils.getErrJson(e);
            }
        }
        PrintWriter out = response.getWriter();
        out.println(resultJson);
        out.flush();
        out.close();
    }

   

    private List<User> parseJson(String jsonString) {
        List<User> users = new ArrayList<>();
        JSONObject reqObject;
        try {
            reqObject = new JSONObject(jsonString);
            if (reqObject.getInt("reqCode") == 1) {

                JSONArray userArray = reqObject.getJSONArray("info");
                //JSONArray userArray = new JSONArray(jsonString);
                if (userArray.length() > 0) {
                    for (int i = 0; i < userArray.length(); i++) {
                        JSONObject userObject = userArray.getJSONObject(i);
                        String username = userObject.getString("username");
                        String password = userObject.getString("password");
                        User user = new User();
                        user.setPassword(password);
                        user.setName(username);
                        users.add(user);
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return users;
    }

    private void getUserById(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String inputString = Utils.streamToString(request.getInputStream());

        System.out.println("inputString:" + inputString);

        JSONObject retJSON = new JSONObject();
        JSONObject reqObject;
        try {
            reqObject = new JSONObject(inputString);
            if (reqObject.getInt("reqCode") == 3) {
                JSONObject infoObject = reqObject.getJSONObject("info");
                int id = infoObject.getInt("id");

                UserDAO userDAO = new UserDAO();
                User user = userDAO.getUserById(String.valueOf(id));
                JSONObject retObject = new JSONObject();
                retObject.put("totalNum", 1);
                JSONArray userJsonArray = new JSONArray();

                JSONObject userObject = new JSONObject();
                userObject.put("_id", user.getId());
                userObject.put("name", user.getName());
                userObject.put("password", user.getPassword());
                userJsonArray.put(userObject);

                retObject.put("info", userJsonArray);
                retJSON.put("retCode", 0);
                retJSON.put("msg", "ok");
                retJSON.put("data", retObject);


            }
        } catch (SQLException | ClassNotFoundException | JSONException e) {
            e.printStackTrace();
            retJSON = Utils.getErrJson(e);
        }
        PrintWriter out = response.getWriter();
        out.println(retJSON);
        out.flush();
        out.close();
    }

    
}
