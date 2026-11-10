package com.unsent.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity @Table(name="journal_items",indexes={@Index(name="idx_kind_date",columnList="kind,event_date"),@Index(name="idx_journey",columnList="journey_id")})
public class JournalItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="kind", nullable=false,length=40) private String kind;
    @Column(name="journey_id") private Long journeyId;
    @Column(name="event_date") private LocalDate eventDate;
    @Column(nullable=false,length=160) private String title;
    @Column(columnDefinition="CLOB") private String body;
    @Column(columnDefinition="CLOB") private String details;
    @Column(nullable=false) private Instant createdAt=Instant.now();
    private Instant updatedAt=Instant.now();
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getKind(){return kind;} public void setKind(String v){kind=v;}
    public Long getJourneyId(){return journeyId;} public void setJourneyId(Long v){journeyId=v;}
    public LocalDate getEventDate(){return eventDate;} public void setEventDate(LocalDate v){eventDate=v;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getBody(){return body;} public void setBody(String v){body=v;}
    public String getDetails(){return details;} public void setDetails(String v){details=v;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant v){createdAt=v;}
    public Instant getUpdatedAt(){return updatedAt;} public void setUpdatedAt(Instant v){updatedAt=v;}
}
