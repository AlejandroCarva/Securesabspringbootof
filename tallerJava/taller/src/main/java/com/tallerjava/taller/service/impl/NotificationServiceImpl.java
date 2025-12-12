package com.tallerjava.taller.service.impl;

import com.tallerjava.taller.model.Notification;
import com.tallerjava.taller.repository.NotificationRepository;
import com.tallerjava.taller.service.NotificationService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    public NotificationServiceImpl(NotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public long countUnread(Integer usuarioId) {
        return repository.countUnreadByUsuarioId(usuarioId);
    }

    @Override
    public List<Notification> listLatest(Integer usuarioId, int limit) {
        int size = Math.max(1, limit);
        return repository.findLatestByUsuario(usuarioId, PageRequest.of(0, size));
    }

    @Override
    public Notification markRead(Long id) {
        return repository.findById(id)
                .map(n -> {
                    n.setReadFlag(true);
                    return repository.save(n);
                })
                .orElse(null);
    }

    @Override
    public void markAllRead(Integer usuarioId) {
        List<Notification> list = repository.findUnreadByUsuario(usuarioId);
        list.forEach(n -> n.setReadFlag(true));
        repository.saveAll(list);
    }

    @Override
    public Notification save(Notification n) {
        if (n.getCreatedAt() == null) {
            n.setCreatedAt(LocalDateTime.now());
        }
        if (n.getReadFlag() == null) {
            n.setReadFlag(false);
        }
        return repository.save(n);
    }
}
