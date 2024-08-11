package com._pi.benepick.domain.members.repository;

import com._pi.benepick.domain.members.entity.Members;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface MembersRepository extends JpaRepository<Members, String> {

    @Query(value = "select * from members where id = ?", nativeQuery = true)
    Optional<Members> findByIdWithNativeQuery(String id);

    @Query("select m from Members m where m.name= :name ")
    Optional<Members> findAllByName(String name);

    @Query("select m FROM  Members m where LOWER(m.name) LIKE  LOWER(CONCAT('%', :name, '%') )")
    Page<Members> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("select m FROM  Members m where LOWER(m.name) LIKE  LOWER(CONCAT('%', :name, '%') )")
    List<Members> countAllByName(String name);


}
