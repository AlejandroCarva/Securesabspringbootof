package com.tallerjava.taller.service;

import com.tallerjava.taller.model.Notification;
import java.util.List;

public interface NotificationService {
    long countUnread(Integer usuarioId);
    List<Notification> listLatest(Integer usuarioId, int limit);
    Notification markRead(Long id);
    void markAllRead(Integer usuarioId);
    Notification save(Notification n);
}
