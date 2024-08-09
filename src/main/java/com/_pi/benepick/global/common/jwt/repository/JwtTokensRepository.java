package com._pi.benepick.global.common.jwt.repository;

import com._pi.benepick.global.common.jwt.entity.JwtTokens;
import org.springframework.data.repository.CrudRepository;

public interface JwtTokensRepository extends CrudRepository<JwtTokens, String>{

}
