package org.example.service;

import org.example.exceptions.SeatUnavailableException;
import org.example.interfaces.SeatLockProvider;
import org.example.models.Seat;
import org.example.models.SeatLock;
import org.example.models.Show;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemorySeatLockService implements SeatLockProvider {

    private final Map<Show, Map<Seat, SeatLock>> seatLocks;

    private final Integer lockTimeoutInMinutes;

    public InMemorySeatLockService(int lockTimeoutInMinutes) {
        seatLocks =  new ConcurrentHashMap<>();
        this.lockTimeoutInMinutes = lockTimeoutInMinutes;
    }

    @Override
    public synchronized void lockSeats(List<Seat> seats, Show show, String user) {
        Map<Seat, SeatLock> showSeatLocks =
                seatLocks.computeIfAbsent(show, s -> new ConcurrentHashMap<>());
        synchronized (showSeatLocks) {
            for (Seat seat : seats) {
                if (isSeatLockedByAnotherUser(seat, show, user)) {
                    throw new SeatUnavailableException("Seat not available");
                }
            }
            seats.forEach(seat -> {
                lockSeat(seat, show, user);
            });
        }
    }

    @Override
    public boolean validateLock(List<Seat> seats, Show show, String user) {
        Map<Seat, SeatLock> showSeatLocks =
                seatLocks.computeIfAbsent(show, s -> new ConcurrentHashMap<>());
        synchronized (showSeatLocks) {
            for (Seat seat : seats) {
                Optional<SeatLock> optionalSeatLock = findBySeatIdAndShowIdAndLockedBy(seat, show, user);
                if (optionalSeatLock.isEmpty() || isLockExpired(optionalSeatLock.get())) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public void releaseLock(List<Seat> seats, Show show, String userId) {
        Map<Seat, SeatLock> showSeatLocks =
                seatLocks.computeIfAbsent(show, s -> new ConcurrentHashMap<>());
        synchronized (showSeatLocks) {
            for (Seat seat : seats) {
                Optional<SeatLock> optionalSeatLock = findBySeatIdAndShowIdAndLockedBy(seat, show, userId);
                if (optionalSeatLock.isPresent()) {
                    seatLocks.get(show).remove(seat);
                }
            }
        }
    }

    @Override
    public List<Seat> getLockedSeats(Show show) {
        if (seatLocks.get(show) == null) {
            return Collections.emptyList();
        }
        return seatLocks.get(show).entrySet().stream()
                .filter(entry -> !isLockExpired(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private void lockSeat(Seat seat, Show show, String user) {
        Optional<SeatLock> optionalSeatLock = findBySeatIdAndShowId(seat, show);
        if(optionalSeatLock.isPresent()) {
            SeatLock seatLock = optionalSeatLock.get();
            if(isLockExpired(seatLock) || seatLock.getLockedBy().equals(user)) {
                seatLock.setLockedBy(user);
                seatLock.setLockedUntil(LocalDateTime.now().plusMinutes(lockTimeoutInMinutes));
            } else {
                throw new SeatUnavailableException("Seat already locked by " + seatLock.getLockedBy());
            }
        } else {
            SeatLock seatLock = new SeatLock(
                    UUID.randomUUID().toString(),
                    seat,
                    show,
                    user,
                    LocalDateTime.now().plusMinutes(lockTimeoutInMinutes)
            );
            seatLocks.computeIfAbsent(show, s -> new ConcurrentHashMap<>());
            seatLocks.get(show).put(seat, seatLock);
        }
    }

    private Optional<SeatLock> findBySeatIdAndShowId(Seat seat, Show show) {
        if (seatLocks.get(show) == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(
                seatLocks.get(show).get(seat)
        );
    }

    private Optional<SeatLock> findBySeatIdAndShowIdAndLockedBy(Seat seat, Show show, String lockedBy) {
        return findBySeatIdAndShowId(seat, show)
        .filter(seatLock -> seatLock.getLockedBy().equals(lockedBy));
    }

    private boolean isLockExpired(SeatLock seatLock) {
        return seatLock.getLockedUntil().isBefore(LocalDateTime.now());
    }


    private boolean isSeatLockedByAnotherUser(Seat seat, Show show, String user){
        Optional<SeatLock> seatLock = findBySeatIdAndShowId(seat, show);
        return seatLock.map(lock -> !isLockExpired(lock) && !lock.getLockedBy().equals(user))
                .orElse(false);
    }
}
