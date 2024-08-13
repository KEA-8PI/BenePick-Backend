package com._pi.benepick.domain.hash.service;

import com._pi.benepick.domain.hash.entity.Hash;

public interface HashCommandService {
    Hash saveHash(double seed);
}
