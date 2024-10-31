package server;

import Exceptions.DataAccessException;
import dataaccess.MySqlAuthDAO;

public class GlobalVariables {
    private MySqlAuthDAO authDAO;

    public GlobalVariables() throws DataAccessException {
        try {
            authDAO=new MySqlAuthDAO();
        } catch(DataAccessException ex) {
            throw new DataAccessException("DAO inits failed");
        }
    }
}
