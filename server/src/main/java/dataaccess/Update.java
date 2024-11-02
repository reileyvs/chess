package dataaccess;

import exceptions.DataAccessException;
import exceptions.RecordException;
import chess.ChessGame;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.Types.NULL;

public interface Update {
    static int executeUpdate(String statement, Connection conn, Object... params) throws DataAccessException {
        try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
            for (var i=0; i < params.length; i++) {
                var param = params[i];
                if (param instanceof String p) {
                    ps.setString(i + 1, p);
                }
                else if (param instanceof Integer p) {
                    ps.setInt(i + 1, p);
                }
                else if (param instanceof ChessGame p) {
                    ps.setString(i + 1, p.toString());
                }
                else if (param == null) {
                    ps.setNull(i + 1, NULL);
                }
            }
            ps.executeUpdate();

            var rs = ps.getGeneratedKeys();
            if(rs.next()) {
                return rs.getInt(1);
            }

            return 0;
        } catch (SQLException ex) {
            throw new RecordException(ex.getMessage());
        }
    }
}
