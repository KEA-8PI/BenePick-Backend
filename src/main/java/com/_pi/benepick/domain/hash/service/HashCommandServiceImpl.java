package com._pi.benepick.domain.hash.service;

import com._pi.benepick.domain.draws.service.algorithm.DoubleToSHA256;
import com._pi.benepick.domain.hash.entity.Hash;
import com._pi.benepick.domain.hash.repository.HashsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HashCommandServiceImpl implements HashCommandService {

    private final HashsRepository hashsRepository;

    public Hash saveHash(double seed) {
        Hash hash = Hash.builder()
                .cryptoHash(DoubleToSHA256.getSHA256Hash(seed))
                .seed(seed)
                .build();
        return hashsRepository.save(hash);
    }
}
