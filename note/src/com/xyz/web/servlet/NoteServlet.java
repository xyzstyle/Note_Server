package com.xyz.web.servlet;

import com.xyz.web.dao.NoteDAO;
import com.xyz.web.dao.UserDAO;
import com.xyz.web.model.Note;
import com.xyz.web.model.User;
import com.xyz.web.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by xyz in Note_Server on 2018/11/14 .
 */
public class NoteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int method = Integer.parseInt(req.getParameter("method"));
        resp.setContentType("text/json;charset=UTF-8");
        switch (method) {
            case 1:
                bakNotes(req, resp);
                break;
            case 2:

                break;
            case 3:

                break;

            default:

        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void bakNotes(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String inputString = Utils.streamToString(req.getInputStream());
        JSONObject resultJson = parseJson(inputString);
        PrintWriter out = resp.getWriter();
        out.println(resultJson);
        out.flush();
        out.close();
    }

    private JSONObject parseJson(String json) throws IOException {
        System.out.println("json:" + json);
        JSONObject resultJson = new JSONObject();
        ArrayList<Note> notes = new ArrayList<>();
        try {
            JSONObject reqObject = new JSONObject(json);
            int reqCode = reqObject.getInt("reqCode");
            if (reqCode == 3) {
                JSONObject userJson = reqObject.getJSONObject("user");
                User user = new User();
                user.setId(userJson.getLong("id"));
                user.setPassword(userJson.getString("password"));
                UserDAO userDAO = new UserDAO();
                if (!userDAO.checkUser(user)) {
                    resultJson.put("ret", 1);
                    resultJson.put("msg", "User Check Fail");
                    return resultJson;
                }
                JSONArray noteArray = reqObject.getJSONArray("notes");
                int count = reqObject.getInt("count");
                Note note;
                JSONObject noteJsonObj;
                for (int i = 0; i < count; i++) {
                    noteJsonObj = noteArray.getJSONObject(i);
                    note = new Note();
                    note.setUserId(user.getId());
                    note.setName(noteJsonObj.getString("name"));
                    note.setContent(noteJsonObj.getString("content"));
                    note.setCreatedTime(noteJsonObj.getLong("created_time"));
                    note.setRemoteId(noteJsonObj.getInt("id"));
                    notes.add(note);

                }
                boolean result = true;
                if (notes.size() == 0) {
                    resultJson.put("ret", 1);
                    resultJson.put("msg", "note number is zero");
                }else{

                    NoteDAO dao = new NoteDAO();
                    for (Note myNote : notes) {
                        if (!dao.insert(myNote)) {
                            result = false;
                            break;
                        }
                    }
                    if (result) {
                        resultJson.put("ret", 0);
                        resultJson.put("msg", "ok");
                    } else {
                        resultJson.put("ret", 1);
                        resultJson.put("msg", "DB fail");
                    }

                }

            }
        } catch (JSONException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            resultJson = Utils.getErrJson(e);
        }
        return resultJson;
    }
}
