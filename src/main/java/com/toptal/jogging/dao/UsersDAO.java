package com.toptal.jogging.dao;

import com.google.common.collect.Maps;
import com.toptal.jogging.model.User;

import java.util.Map;

/**
 * Created by Artem on 30.06.2017.
 */
public class UsersDAO {
    Map<String, User> users = Maps.newHashMap();

    public User createUser(User user) {
        if (users.containsKey(user.getName())) {
            return null;
        }
        return users.put(user.getName(), user);
    }
}
