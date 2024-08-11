package com._pi.benepick.domain.members.repository;
import com._pi.benepick.domain.members.entity.Members;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
public interface MembersRepository extends JpaRepository<Members, String> {

    @Query(value = "select * from members where id = ?", nativeQuery = true)
    Optional<Members> findByIdWithNativeQuery(String id);

    @Query("select m from Members m where m.name= :name ")
    Optional<Members> findAllByName(String name);

    @Query("select m FROM  Members m where LOWER(m.name) LIKE  LOWER(CONCAT('%', :name, '%') )")
    Page<Members> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
