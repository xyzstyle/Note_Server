package com.xyz.web.dao;

import com.xyz.web.model.Note;
import com.xyz.web.model.User;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by xyz in Note_Server on 2018/11/14 .
 */
public class NoteDAO {

    private SqlManager manager;
    private String sql = "";
    private ResultSet rs;

    public NoteDAO() throws IOException, ClassNotFoundException, SQLException {
        manager = SqlManager.createInstance();
    }

    public boolean insert(Note note) throws SQLException {

        boolean result;
        sql = "insert into note(userId,remoteId,name,content,createdTime) values(? ,?,?,?,?)";
        Object[] params = new Object[] {note.getUserId(),note.getRemoteId(),note.getName(),note.getContent(),note.getCreatedTime()};
        manager.connectDB();
        result = manager.executeUpdate(sql, params);
        manager.closeDB();
        return result;
    }

    public ArrayList<Note> getNotes(long userId) throws SQLException{
        ArrayList<Note> notes = new ArrayList<>();
        sql="select * from note where userId=?" ;
        manager.connectDB();
        Object[] params = new Object[] {userId};
        rs = manager.executeQuery(sql, params);
        while (rs.next()) {

            Note note = new Note();
            note.setId(rs.getLong("id"));
            note.setUserId(rs.getLong("userId"));
            note.setRemoteId(rs.getInt("remoteId"));
            note.setName(rs.getString("name"));
            note.setContent(rs.getString("content"));
            note.setCreatedTime(rs.getLong("createdTime"));
            notes.add(note);

        }
        manager.closeDB();
        return notes;

    }
}
