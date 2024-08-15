package com._pi.benepick.domain.members.repository;
import com._pi.benepick.domain.members.entity.Members;
import java.util.Optional;

import com._pi.benepick.domain.members.entity.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
public interface MembersRepository extends JpaRepository<Members, String> {

    @Query(value = "select * from members where id = ?", nativeQuery = true)
    Optional<Members> findByIdWithNativeQuery(String id);

    @Query("select m FROM  Members m where LOWER(m.name) LIKE  LOWER(CONCAT('%', :name, '%') ) and  m.role =:role ")
    Page<Members> findByNameContainingIgnoreCase(String name, Pageable pageable, Role role);

    Page<Members> findAllByRole(Role role,Pageable pageable);

}
