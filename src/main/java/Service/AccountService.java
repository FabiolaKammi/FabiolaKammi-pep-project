package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO = new AccountDAO();

    public Account registerAccount(String userName, String passWord) {
        if (userName.isBlank() || passWord.isBlank() || passWord.length() < 4) {
            return null;
        }
    Account createdAccount = accountDAO.registerAccount(userName, passWord);
        return createdAccount;
    }

    public Account login(String username, String password) {
        //input validation
        if(username == null || username.isBlank() || password == null || password.isBlank()){
            return null;
        }
        Account account = accountDAO.getAccountByUsername(username);

    // Check if account exists and the password matches
    if (account != null && account.getPassword().equals(password)) {
        return accountDAO.login(username, password);
    }
       
        //perform login
        return null;
    }
    public Account getAccountByUsername(String username){
        return accountDAO.getAccountByUsername(username);
    }
}
