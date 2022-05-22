package hr.fer.pi.planinarskidnevnik.dtos.MountainPath;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;
import java.sql.Date;
import java.sql.Time;

public class MountainPathCreateRequest {

    private Long hillId;

    @Size(max = 128, message = "Ime smije sadržavati najviše 128 znakova.")
    @NotBlank(message = "Ime je obavezno")
    private String name;

    @Size(max = 128, message = "Početna točka smije sadržavati najviše 128 znakova.")
    @NotBlank(message = "Početna točka je obavezna")
    private String startPoint;

    @Size(max = 128, message = "Završna točka smije sadržavati najviše 128 znakova.")
    @NotBlank(message = "Završna točka je obavezna")
    private String endPoint;

    @NotNull
    @JsonFormat(pattern = "HH:mm:ss")
    private Time avgWalkTime;

    @NotNull
    private Long length;

    private Long seaLevelDiff;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateCreated;

    @NotNull
    @JsonProperty("isPrivate")
    private Boolean isPrivate;

    @Min(1)
    @Max(10)
    private Short difficulty;

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Long getHillId() {
        return hillId;
    }

    public void setHillId(Long hillId) {
        this.hillId = hillId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public Time getAvgWalkTime() {
        return avgWalkTime;
    }

    public void setAvgWalkTime(Time avgWalkTime) {
        this.avgWalkTime = avgWalkTime;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public Long getSeaLevelDiff() {
        return seaLevelDiff;
    }

    public void setSeaLevelDiff(Long seaLevelDiff) {
        this.seaLevelDiff = seaLevelDiff;
    }

    public Boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Short getDifficulty() {return difficulty;}

    public void setDifficulty(Short difficulty) {this.difficulty = difficulty;}

}
