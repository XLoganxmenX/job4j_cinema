package ru.job4j.dto;

import java.util.Objects;

public class FilmSessionPageDto {
    int id;
    private FilmSessionDto filmSessionDto;
    private int hallRowCount;
    private int hallPlaceCount;

    public FilmSessionPageDto() {
    }

    public FilmSessionPageDto(int id, FilmSessionDto filmSessionDto, int hallRowCount, int hallPlaceCount) {
        this.id = id;
        this.filmSessionDto = filmSessionDto;
        this.hallRowCount = hallRowCount;
        this.hallPlaceCount = hallPlaceCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FilmSessionDto getFilmSessionDto() {
        return filmSessionDto;
    }

    public void setFilmSessionDto(FilmSessionDto filmSessionDto) {
        this.filmSessionDto = filmSessionDto;
    }

    public int getHallRowCount() {
        return hallRowCount;
    }

    public void setHallRowCount(int hallRowCount) {
        this.hallRowCount = hallRowCount;
    }

    public int getHallPlaceCount() {
        return hallPlaceCount;
    }

    public void setHallPlaceCount(int hallPlaceCount) {
        this.hallPlaceCount = hallPlaceCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FilmSessionPageDto that = (FilmSessionPageDto) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
