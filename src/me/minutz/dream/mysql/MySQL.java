package me.minutz.dream.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySQL
{
  private String HOST = null;
  private String DB = null;
  private String USER = null;
  private String PASS = null;
  private boolean connected = false;

  private Statement st = null;
  public Connection con = null;
  private MySQLFunc MySQL;
  public String conName;


  public MySQL(String name)
  {
    this.conName = name;
    this.connected = false;
  }

  public Boolean Connect(String host, String db, String user, String pass) {
    this.HOST = host;
    this.DB = db;
    this.USER = user;
    this.PASS = pass;
    this.MySQL = new MySQLFunc(host, db, user, pass);
    this.con = this.MySQL.open();
    try {
      this.st = this.con.createStatement();
      this.connected = true;
  	System.out.println("[" + this.conName + "] Connected to the database.");
    } catch (SQLException e) {
      this.connected = false;
  	System.out.println("[" + this.conName + "] Could not connect to the database.");
    }
    this.MySQL.close(this.con);
    return Boolean.valueOf(this.connected);
  }
  
  public void execute(String query) {
    this.MySQL = new MySQLFunc(this.HOST, this.DB, this.USER, this.PASS);
    this.con = this.MySQL.open();
    try {
      this.st = this.con.createStatement();
      this.st.execute(query);
    } catch (SQLException e) {
    	System.out.println("[" + this.conName + "] Error executing statement: " + e.getMessage());
    }
    this.MySQL.close(this.con);
  }

  public ResultSet query(String query) {
    this.MySQL = new MySQLFunc(this.HOST, this.DB, this.USER, this.PASS);
    this.con = this.MySQL.open();
    ResultSet rs = null;
    try {
      this.st = this.con.createStatement();
      rs = this.st.executeQuery(query);
    } catch (SQLException e) {
    	System.out.println("[" + this.conName + "] Error executing query: " + e.getMessage());
    }
    return rs;
  }
}