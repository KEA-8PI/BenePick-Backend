package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.entity.Draws;

import java.util.List;

public interface DrawsCommandService {
    void saveDrawsList(List<Draws> drawsList);
}
