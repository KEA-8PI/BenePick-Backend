package com._pi.benepick.domain.members.service;

import com._pi.benepick.domain.members.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MembersCommandServiceImpl implements MembersCommandService{
    private final MembersRepository membersRepository;


}
