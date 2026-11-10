package com.unsent.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Instant;

@Entity @Table(name="daily_checkins",uniqueConstraints=@UniqueConstraint(columnNames={"journey_id","check_date"}))
public class DailyCheckin {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="journey_id",nullable=false) private Long journeyId;
 @Column(name="check_date",nullable=false) private LocalDate date;
 private boolean honored;
 private String note;
 private Instant createdAt=Instant.now();
 public Long getId(){return id;} public Long getJourneyId(){return journeyId;} public void setJourneyId(Long v){journeyId=v;}
 public LocalDate getDate(){return date;} public void setDate(LocalDate v){date=v;}
 public boolean isHonored(){return honored;} public void setHonored(boolean v){honored=v;}
 public String getNote(){return note;} public void setNote(String v){note=v;}
 public Instant getCreatedAt(){return createdAt;}
}
