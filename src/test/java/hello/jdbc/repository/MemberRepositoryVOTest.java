package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryVOTest {
     MemberRepositoryVO repository = new MemberRepositoryVO();
    @Test
    void save() throws SQLException {
        Member member = new Member("memberV0", 10000);
        repository.save(member);
    }
}