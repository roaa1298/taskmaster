package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Task type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Tasks")
public final class Task implements Model {
  public static final QueryField ID = field("Task", "id");
  public static final QueryField TITLE = field("Task", "title");
  public static final QueryField DESCRIPTION = field("Task", "description");
  public static final QueryField STATUS = field("Task", "status");
  public static final QueryField IMAGE = field("Task", "image");
  public static final QueryField LOCATION_LATITUDE = field("Task", "location_latitude");
  public static final QueryField LOCATION_LONGITUDE = field("Task", "location_longitude");
  public static final QueryField TEAM_TASKS_ID = field("Task", "teamTasksId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String title;
  private final @ModelField(targetType="String") String description;
  private final @ModelField(targetType="Status", isRequired = true) Status status;
  private final @ModelField(targetType="String") String image;
  private final @ModelField(targetType="String") String location_latitude;
  private final @ModelField(targetType="String") String location_longitude;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  private final @ModelField(targetType="ID") String teamTasksId;
  public String getId() {
      return id;
  }
  
  public String getTitle() {
      return title;
  }
  
  public String getDescription() {
      return description;
  }
  
  public Status getStatus() {
      return status;
  }
  
  public String getImage() {
      return image;
  }
  
  public String getLocationLatitude() {
      return location_latitude;
  }
  
  public String getLocationLongitude() {
      return location_longitude;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  public String getTeamTasksId() {
      return teamTasksId;
  }
  
  private Task(String id, String title, String description, Status status, String image, String location_latitude, String location_longitude, String teamTasksId) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.status = status;
    this.image = image;
    this.location_latitude = location_latitude;
    this.location_longitude = location_longitude;
    this.teamTasksId = teamTasksId;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Task task = (Task) obj;
      return ObjectsCompat.equals(getId(), task.getId()) &&
              ObjectsCompat.equals(getTitle(), task.getTitle()) &&
              ObjectsCompat.equals(getDescription(), task.getDescription()) &&
              ObjectsCompat.equals(getStatus(), task.getStatus()) &&
              ObjectsCompat.equals(getImage(), task.getImage()) &&
              ObjectsCompat.equals(getLocationLatitude(), task.getLocationLatitude()) &&
              ObjectsCompat.equals(getLocationLongitude(), task.getLocationLongitude()) &&
              ObjectsCompat.equals(getCreatedAt(), task.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), task.getUpdatedAt()) &&
              ObjectsCompat.equals(getTeamTasksId(), task.getTeamTasksId());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getTitle())
      .append(getDescription())
      .append(getStatus())
      .append(getImage())
      .append(getLocationLatitude())
      .append(getLocationLongitude())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .append(getTeamTasksId())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Task {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("title=" + String.valueOf(getTitle()) + ", ")
      .append("description=" + String.valueOf(getDescription()) + ", ")
      .append("status=" + String.valueOf(getStatus()) + ", ")
      .append("image=" + String.valueOf(getImage()) + ", ")
      .append("location_latitude=" + String.valueOf(getLocationLatitude()) + ", ")
      .append("location_longitude=" + String.valueOf(getLocationLongitude()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()) + ", ")
      .append("teamTasksId=" + String.valueOf(getTeamTasksId()))
      .append("}")
      .toString();
  }
  
  public static TitleStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Task justId(String id) {
    return new Task(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      title,
      description,
      status,
      image,
      location_latitude,
      location_longitude,
      teamTasksId);
  }
  public interface TitleStep {
    StatusStep title(String title);
  }
  

  public interface StatusStep {
    BuildStep status(Status status);
  }
  

  public interface BuildStep {
    Task build();
    BuildStep id(String id);
    BuildStep description(String description);
    BuildStep image(String image);
    BuildStep locationLatitude(String locationLatitude);
    BuildStep locationLongitude(String locationLongitude);
    BuildStep teamTasksId(String teamTasksId);
  }
  

  public static class Builder implements TitleStep, StatusStep, BuildStep {
    private String id;
    private String title;
    private Status status;
    private String description;
    private String image;
    private String location_latitude;
    private String location_longitude;
    private String teamTasksId;
    @Override
     public Task build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Task(
          id,
          title,
          description,
          status,
          image,
          location_latitude,
          location_longitude,
          teamTasksId);
    }
    
    @Override
     public StatusStep title(String title) {
        Objects.requireNonNull(title);
        this.title = title;
        return this;
    }
    
    @Override
     public BuildStep status(Status status) {
        Objects.requireNonNull(status);
        this.status = status;
        return this;
    }
    
    @Override
     public BuildStep description(String description) {
        this.description = description;
        return this;
    }
    
    @Override
     public BuildStep image(String image) {
        this.image = image;
        return this;
    }
    
    @Override
     public BuildStep locationLatitude(String locationLatitude) {
        this.location_latitude = locationLatitude;
        return this;
    }
    
    @Override
     public BuildStep locationLongitude(String locationLongitude) {
        this.location_longitude = locationLongitude;
        return this;
    }
    
    @Override
     public BuildStep teamTasksId(String teamTasksId) {
        this.teamTasksId = teamTasksId;
        return this;
    }
    
    /** 
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String title, String description, Status status, String image, String locationLatitude, String locationLongitude, String teamTasksId) {
      super.id(id);
      super.title(title)
        .status(status)
        .description(description)
        .image(image)
        .locationLatitude(locationLatitude)
        .locationLongitude(locationLongitude)
        .teamTasksId(teamTasksId);
    }
    
    @Override
     public CopyOfBuilder title(String title) {
      return (CopyOfBuilder) super.title(title);
    }
    
    @Override
     public CopyOfBuilder status(Status status) {
      return (CopyOfBuilder) super.status(status);
    }
    
    @Override
     public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
    }
    
    @Override
     public CopyOfBuilder image(String image) {
      return (CopyOfBuilder) super.image(image);
    }
    
    @Override
     public CopyOfBuilder locationLatitude(String locationLatitude) {
      return (CopyOfBuilder) super.locationLatitude(locationLatitude);
    }
    
    @Override
     public CopyOfBuilder locationLongitude(String locationLongitude) {
      return (CopyOfBuilder) super.locationLongitude(locationLongitude);
    }
    
    @Override
     public CopyOfBuilder teamTasksId(String teamTasksId) {
      return (CopyOfBuilder) super.teamTasksId(teamTasksId);
    }
  }
  
}
