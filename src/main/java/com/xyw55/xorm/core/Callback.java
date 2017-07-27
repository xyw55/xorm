package com.xyw55.xorm.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by xiayiwei on 7/24/17.
 */
public interface Callback {
    public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs);
}
