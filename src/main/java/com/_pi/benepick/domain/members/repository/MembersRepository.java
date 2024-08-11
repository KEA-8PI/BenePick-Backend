package com._pi.benepick.domain.members.repository;

import com._pi.benepick.domain.members.entity.Members;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;
public interface MembersRepository extends JpaRepository<Members, String> {

    @Query("select m FROM  Members m where LOWER(m.name) LIKE  LOWER(CONCAT('%', :name, '%') )")
    Page<Members> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
