package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {

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

    public Member findById(String memberId) throws SQLException{
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();
            if(rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }
        } catch (SQLException e) {
            log.error("DB ERROR", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public void update(String memberId, int money) throws SQLException{
        String sql = "update member set money=? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);

        } catch (SQLException e) {
            log.error("DB ERROR", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }

    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("DB ERROR", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
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
