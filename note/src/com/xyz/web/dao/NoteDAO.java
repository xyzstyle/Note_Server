package com.xyz.web.dao;

import com.xyz.web.model.Note;
import com.xyz.web.model.User;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
