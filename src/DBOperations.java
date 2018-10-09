/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cgdb;

import java.sql.*;

/**
 *
 * @author nakulkar
 */
public class DBOperations {
    
    String finalAccusation,currState;
    ResultSet rs;
    Statement st;
    Connection con;
    String sql;
    
    public DBOperations(String dburl) throws ClassNotFoundException, SQLException{

        Class.forName("com.mysql.jdbc.driver");
        con=DriverManager.getConnection(dburl);
        st=con.createStatement();
        
    }
    
    public String getFinalAccusation(String gid) throws SQLException{

        sql="Select accusation from result_tbl where gid='"+gid+"' ;";
        rs=st.executeQuery(sql);
        finalAccusation=rs.getString("accusation");
        return finalAccusation;
    }
    
    public int setFinalAccusation(String gid,String acc) throws SQLException{

        sql="Insert into result_tbl values('"+gid+"','"+acc+"');";
        int updstatus = st.executeUpdate(sql);   
        return updstatus;
    }
    
    public int setCurrentState(String gid, String p1,String p2,String p3, String p4, String p5) throws SQLException{
        
        sql="Insert into currstate_tbl values('"+gid+"','"+p1+"','"+p2+"','"+p3+"','"+p4+"','"+p5+"');";
        int updstatus = st.executeUpdate(sql);
        return updstatus;
        
    }
    
    public String getCurrentState(String gid, String pl) throws SQLException{
        
        sql="Select "+pl+" from currstate_tbl where gid='"+gid+"' ;";
        rs = st.executeQuery(sql);
        currState = rs.getString(pl);
        return currState;
        
    }
}
