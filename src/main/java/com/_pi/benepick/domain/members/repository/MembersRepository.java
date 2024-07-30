package com._pi.benepick.domain.members.repository;

import com._pi.benepick.domain.members.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MembersRepository extends JpaRepository<Members, String> {


    @Query("select m from Members m where m.name= :name ")
    Optional<Members> findAllByName(String name);

    @Modifying
    @Query("update  Members m SET  m.name =:name,m.deptName =:deptName,m.point =:point, m.penaltyCnt =:penaltyCnt where m.id =:id")
    void updateMembers(String id,String name,String deptName,Long point,int penaltyCnt);
}
