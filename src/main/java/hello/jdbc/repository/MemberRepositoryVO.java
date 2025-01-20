package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;

/**
 * JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryVO {

    public Member save(Member member) throws SQLException{
        String sql = "insert into member(member_id, money) values (?, ?)";

        Connection con = null;
        // Statement 와 차이점: 파라메터 바인딩 기능이 추가된게 PreparedStatement(Statement 의 자식)
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            // SQL injection 공격 예방을 위해 파라메터 바인딩을 사용 -> 값을 직접 sql에 입력하지 않는다
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        }catch (SQLException e) {
            log.error("DB ERROR ", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    // 리소스 정리
    private void close(Connection con, Statement stmt, ResultSet rs) {

        if(rs != null) {
            try {
                rs.close();
            }catch (SQLException e) {
                log.error("ERROR ", e);
            }
        }

        if(stmt != null) {
            try {
                stmt.close();
            }catch (SQLException e) {
                log.error("ERROR ", e);
            }
        }

        if(con != null) {
            try {
                con.close();
            }catch (SQLException e) {
                log.error("ERROR ", e);
            }
        }
    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
