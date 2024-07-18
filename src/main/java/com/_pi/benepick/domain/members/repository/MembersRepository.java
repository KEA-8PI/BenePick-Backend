package com._pi.benepick.domain.members.repository;

import com._pi.benepick.domain.members.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembersRepository extends JpaRepository<Members, Long> {
}
