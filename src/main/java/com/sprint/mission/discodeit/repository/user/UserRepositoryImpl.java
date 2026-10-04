package com.sprint.mission.discodeit.repository.user;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.discodeit.domain.user.QUser;
import com.sprint.mission.discodeit.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private final QUser user = QUser.user;

    @Override
    public List<User> findByIdIn(List<UUID> userIds) {
        return getUserJPAQuery()
                .where(user.id.in(userIds))
                .fetch();
    }

    @Override
    public List<User> findAll() {
        return getUserJPAQuery()
                .fetch();
    }

    private JPAQuery<User> getUserJPAQuery() {
        return queryFactory
                .selectFrom(user)
                .join(user.status).fetchJoin()
                .leftJoin(user.profile).fetchJoin();
    }
}
