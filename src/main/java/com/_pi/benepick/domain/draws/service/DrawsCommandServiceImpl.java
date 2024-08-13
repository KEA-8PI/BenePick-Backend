package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.repository.DrawsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DrawsCommandServiceImpl implements DrawsCommandService {

    private final DrawsRepository drawsRepository;

    public void saveDrawsList(List<Draws> drawsList) {
        drawsRepository.saveAll(drawsList);
    }

}
