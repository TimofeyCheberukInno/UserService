package com.app.impl.service.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.app.impl.entity.User;

public class UserServiceSupport {
	public static List<Long> findNotCachedUsersIds(
            Collection<Long> ids,
            List<User> cachedUsers
    ) {
		Set<Long> cachedUsersIds = new HashSet<>(cachedUsers.size());
		for (User user : cachedUsers) {
			cachedUsersIds.add(user.getId());
		}

		List<Long> idsToTakeInDB = new ArrayList<>();
		if (cachedUsers.size() < ids.size()) {
			for (Long id : ids) {
				if (!cachedUsersIds.contains(id)) {
					idsToTakeInDB.add(id);
				}
			}
		}

		return idsToTakeInDB;
	}
}
