package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    public List<Account> getAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "Select * from account;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                accounts.add(account);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public List<Account> getAccount(Account acct){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();

        try {
            //Write SQL logic here
            String sql = "Select * from account where username = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, acct.getUsername());
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                accounts.add(account);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public Account getAccountByLogin(Account acct){
        Connection connection = ConnectionUtil.getConnection();

        try {
            //Write SQL logic here
            String sql = "Select * from account where username = ? and password = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, acct.getUsername());
            preparedStatement.setString(2, acct.getPassword());
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return account;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            if(getAccount(account).isEmpty() && account.getUsername()!=null && !account.getUsername().isEmpty() && account.getPassword().length() >= 4) {
                String sql = "Insert into account (username, password) values (?, ?);" ;
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                //write preparedStatement's setString method here.
                    preparedStatement.setString(1, account.getUsername());
                    preparedStatement.setString(2, account.getPassword());

                    preparedStatement.executeUpdate();
                    ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
                    if(pkeyResultSet.next()){
                        int generated_account_id = (int) pkeyResultSet.getLong(1);
                        return new Account(generated_account_id, account.getUsername(), account.getPassword());
                    }
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        return null;
    }

}
