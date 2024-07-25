package ru.job4j.dto;

import ru.job4j.model.FilmSession;

import java.time.LocalDateTime;
import java.util.Objects;

public class FilmSessionDto {
    int id;
    private String hallName;
    private int filmId;
    private String filmName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int price;

    public FilmSessionDto() {
    }

    public FilmSessionDto(int id, String hallName, int filmId, String filmName,
                          LocalDateTime startTime, LocalDateTime endTime, int price) {
        this.id = id;
        this.hallName = hallName;
        this.filmId = filmId;
        this.filmName = filmName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }

    public FilmSessionDto(FilmSession filmSession, String hallName, int filmId, String filmName) {
        this.id = filmSession.getId();
        this.hallName = hallName;
        this.filmId = filmId;
        this.filmName = filmName;
        this.startTime = filmSession.getStartTime();
        this.endTime = filmSession.getEndTime();
        this.price = filmSession.getPrice();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FilmSessionDto that = (FilmSessionDto) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
