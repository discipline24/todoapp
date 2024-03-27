package SJToyProject.ToDoApp.Member.repository;

import SJToyProject.ToDoApp.Member.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findByEmail(String email);
}
