package com.sky.user.infrastructure.adapter.output.persistence;

import com.sky.user.domain.model.User;
import com.sky.user.domain.repository.UserRepository;
import com.sky.user.infrastructure.adapter.output.persistence.mapper.UserPersistenceMapper;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserPersistenceAdapter implements UserRepository {

	private final JpaUserRepository jpaUserRepository;
	private final UserPersistenceMapper userPersistenceMapper;

	public UserPersistenceAdapter(JpaUserRepository jpaUserRepository, UserPersistenceMapper userPersistenceMapper) {
		this.jpaUserRepository = jpaUserRepository;
		this.userPersistenceMapper = userPersistenceMapper;
	}

	@Override
	@Transactional
	public User save(User user) {
		return userPersistenceMapper.toDomain(jpaUserRepository.save(userPersistenceMapper.toEntity(user)));
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> findById(UUID id) {
		return jpaUserRepository.findById(id).map(userPersistenceMapper::toDomain);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> findByUsername(String username) {
		return jpaUserRepository.findByUsername(username).map(userPersistenceMapper::toDomain);
	}

	@Override
	@Transactional
	public void deleteById(UUID id) {
		jpaUserRepository.deleteById(id);
	}
}
