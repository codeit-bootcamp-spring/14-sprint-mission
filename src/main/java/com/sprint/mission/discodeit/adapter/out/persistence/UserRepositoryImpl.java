package com.sprint.mission.discodeit.adapter.out.persistence;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.discodeit.core.domain.user.QUser;
import com.sprint.mission.discodeit.core.domain.user.User;
import com.sprint.mission.discodeit.core.application.user.out.UserRepositoryCustom;
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
                .leftJoin(user.profile).fetchJoin();
    }
}
