package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
    private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) throws NotFoundException {
        if (post.getId() == 0) {
            post.setId(idCounter.incrementAndGet());
            posts.put(post.getId(), post);
        } else {
            if (posts.containsKey(post.getId())) {
                posts.replace(post.getId(), post);
            } else {
                throw new NotFoundException("файл не найден");
            }
        }
        return post;
    }

    public void removeById(long id) {
        if (posts.contains(id)) {
            posts.remove(id);
        } else {
            throw new NotFoundException();
        }
    }
}
