package com._pi.benepick.domain.hash.service;

import com._pi.benepick.domain.draws.service.algorithm.DrawAlgorithm;
import com._pi.benepick.domain.hash.entity.Hash;
import com._pi.benepick.domain.hash.repository.HashsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HashQueryServiceImpl implements HashQueryService {

    private final HashsRepository hashsRepository;
    public Hash findByCryptoHash(String hash) {
        return hashsRepository.findByCryptoHash(hash).orElse(Hash.builder()
            .seed(DrawAlgorithm.generateSeed())
            .cryptoHash(hash)
            .build());
    }
}
