package Service;

import DAO.AccountDAO;
import Model.Account;

import java.util.List;


public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public Account addAccount(Account account) {
        return accountDAO.insertAccount(account);
    }

    public Account getAccountByLogin(Account account) {
        return accountDAO.getAccountByLogin(account);
    }

}
