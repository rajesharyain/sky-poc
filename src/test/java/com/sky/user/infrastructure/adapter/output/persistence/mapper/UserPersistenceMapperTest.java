package com.sky.user.infrastructure.adapter.output.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sky.user.domain.model.User;
import com.sky.user.infrastructure.adapter.output.persistence.entity.UserEntity;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserPersistenceMapperTest {

	private final UserPersistenceMapper mapper = new UserPersistenceMapper();

	@Test
	void roundTrip_preservesUser() {
		UUID userId = UUID.randomUUID();
		User original = new User(userId, "bob", "bob@example.com", "secret");

		UserEntity entity = mapper.toEntity(original);
		assertThat(entity.getId()).isEqualTo(userId);
		assertThat(entity.getUsername()).isEqualTo("bob");

		User restored = mapper.toDomain(entity);
		assertThat(restored.getId()).isEqualTo(userId);
		assertThat(restored.getUsername()).isEqualTo("bob");
	}
}
