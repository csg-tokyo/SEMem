/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package org.apache.hadoop.mapreduce.jobhistory;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class JobUnsuccessfulCompletion extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"JobUnsuccessfulCompletion\",\"namespace\":\"org.apache.hadoop.mapreduce.jobhistory\",\"fields\":[{\"name\":\"jobid\",\"type\":\"string\"},{\"name\":\"finishTime\",\"type\":\"long\"},{\"name\":\"finishedMaps\",\"type\":\"int\"},{\"name\":\"finishedReduces\",\"type\":\"int\"},{\"name\":\"jobStatus\",\"type\":\"string\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.lang.CharSequence jobid;
  @Deprecated public long finishTime;
  @Deprecated public int finishedMaps;
  @Deprecated public int finishedReduces;
  @Deprecated public java.lang.CharSequence jobStatus;

  /**
   * Default constructor.
   */
  public JobUnsuccessfulCompletion() {}

  /**
   * All-args constructor.
   */
  public JobUnsuccessfulCompletion(java.lang.CharSequence jobid, java.lang.Long finishTime, java.lang.Integer finishedMaps, java.lang.Integer finishedReduces, java.lang.CharSequence jobStatus) {
    this.jobid = jobid;
    this.finishTime = finishTime;
    this.finishedMaps = finishedMaps;
    this.finishedReduces = finishedReduces;
    this.jobStatus = jobStatus;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return jobid;
    case 1: return finishTime;
    case 2: return finishedMaps;
    case 3: return finishedReduces;
    case 4: return jobStatus;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: jobid = (java.lang.CharSequence)value$; break;
    case 1: finishTime = (java.lang.Long)value$; break;
    case 2: finishedMaps = (java.lang.Integer)value$; break;
    case 3: finishedReduces = (java.lang.Integer)value$; break;
    case 4: jobStatus = (java.lang.CharSequence)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'jobid' field.
   */
  public java.lang.CharSequence getJobid() {
    return jobid;
  }

  /**
   * Sets the value of the 'jobid' field.
   * @param value the value to set.
   */
  public void setJobid(java.lang.CharSequence value) {
    this.jobid = value;
  }

  /**
   * Gets the value of the 'finishTime' field.
   */
  public java.lang.Long getFinishTime() {
    return finishTime;
  }

  /**
   * Sets the value of the 'finishTime' field.
   * @param value the value to set.
   */
  public void setFinishTime(java.lang.Long value) {
    this.finishTime = value;
  }

  /**
   * Gets the value of the 'finishedMaps' field.
   */
  public java.lang.Integer getFinishedMaps() {
    return finishedMaps;
  }

  /**
   * Sets the value of the 'finishedMaps' field.
   * @param value the value to set.
   */
  public void setFinishedMaps(java.lang.Integer value) {
    this.finishedMaps = value;
  }

  /**
   * Gets the value of the 'finishedReduces' field.
   */
  public java.lang.Integer getFinishedReduces() {
    return finishedReduces;
  }

  /**
   * Sets the value of the 'finishedReduces' field.
   * @param value the value to set.
   */
  public void setFinishedReduces(java.lang.Integer value) {
    this.finishedReduces = value;
  }

  /**
   * Gets the value of the 'jobStatus' field.
   */
  public java.lang.CharSequence getJobStatus() {
    return jobStatus;
  }

  /**
   * Sets the value of the 'jobStatus' field.
   * @param value the value to set.
   */
  public void setJobStatus(java.lang.CharSequence value) {
    this.jobStatus = value;
  }

  /** Creates a new JobUnsuccessfulCompletion RecordBuilder */
  public static org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion.Builder newBuilder() {
    return new org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion.Builder();
  }
  
  /** Creates a new JobUnsuccessfulCompletion RecordBuilder by copying an existing Builder */
  public static org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion.Builder newBuilder(org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion.Builder other) {
    return new org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion.Builder(other);
  }
  
  /** Creates a new JobUnsuccessfulCompletion RecordBuilder by copying an existing JobUnsuccessfulCompletion instance */
  public static org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion.Builder newBuilder(org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion other) {
    return new org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion.Builder(other);
  }
  
  /**
   * RecordBuilder for JobUnsuccessfulCompletion instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<JobUnsuccessfulCompletion>
    implements org.apache.avro.data.RecordBuilder<JobUnsuccessfulCompletion> {

    private java.lang.CharSequence jobid;
    private long finishTime;
    private int finishedMaps;
    private int finishedReduces;
    private java.lang.CharSequence jobStatus;

    /** Creates a new Builder */
    private Builder() {
      super(org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion.Builder other) {
      super(other);
    }
    
    /** Creates a Builder by copying an existing JobUnsuccessfulCompletion instance */
    private Builder(org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion other) {
            super(org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion.SCHEMA$);
      if (isValidValue(fields()[0], other.jobid)) {
        this.jobid = data().deepCopy(fields()[0].schema(), other.jobid);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.finishTime)) {
        this.finishTime = data().deepCopy(fields()[1].schema(), other.finishTime);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.finishedMaps)) {
        this.finishedMaps = data().deepCopy(fields()[2].schema(), other.finishedMaps);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.finishedReduces)) {
        this.finishedReduces = data().deepCopy(fields()[3].schema(), other.finishedReduces);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.jobStatus)) {
        this.jobStatus = data().deepCopy(fields()[4].schema(), other.jobStatus);
        fieldSetFlags()[4] = true;
      }
    }

    /** Gets the value of the 'jobid' field */
    public java.lang.CharSequence getJobid() {
      return jobid;
    }
    
    /** Sets the value of the 'jobid' field */
    public org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion.Builder setJobid(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.jobid = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'jobid' field has been set */
    public boolean hasJobid() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'jobid' field */
    public org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion.Builder clearJobid() {
      jobid = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'finishTime' field */
    public java.lang.Long getFinishTime() {
      return finishTime;
    }
    
    /** Sets the value of the 'finishTime' field */
    public org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion.Builder setFinishTime(long value) {
      validate(fields()[1], value);
      this.finishTime = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'finishTime' field has been set */
    public boolean hasFinishTime() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'finishTime' field */
    public org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion.Builder clearFinishTime() {
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'finishedMaps' field */
    public java.lang.Integer getFinishedMaps() {
      return finishedMaps;
    }
    
    /** Sets the value of the 'finishedMaps' field */
    public org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion.Builder setFinishedMaps(int value) {
      validate(fields()[2], value);
      this.finishedMaps = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'finishedMaps' field has been set */
    public boolean hasFinishedMaps() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'finishedMaps' field */
    public org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion.Builder clearFinishedMaps() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'finishedReduces' field */
    public java.lang.Integer getFinishedReduces() {
      return finishedReduces;
    }
    
    /** Sets the value of the 'finishedReduces' field */
    public org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion.Builder setFinishedReduces(int value) {
      validate(fields()[3], value);
      this.finishedReduces = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'finishedReduces' field has been set */
    public boolean hasFinishedReduces() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'finishedReduces' field */
    public org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion.Builder clearFinishedReduces() {
      fieldSetFlags()[3] = false;
      return this;
    }

    /** Gets the value of the 'jobStatus' field */
    public java.lang.CharSequence getJobStatus() {
      return jobStatus;
    }
    
    /** Sets the value of the 'jobStatus' field */
    public org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion.Builder setJobStatus(java.lang.CharSequence value) {
      validate(fields()[4], value);
      this.jobStatus = value;
      fieldSetFlags()[4] = true;
      return this; 
    }
    
    /** Checks whether the 'jobStatus' field has been set */
    public boolean hasJobStatus() {
      return fieldSetFlags()[4];
    }
    
    /** Clears the value of the 'jobStatus' field */
    public org.apache.hadoop.mapreduce.jobhistory.JobUnsuccessfulCompletion.Builder clearJobStatus() {
      jobStatus = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    public JobUnsuccessfulCompletion build() {
      try {
        JobUnsuccessfulCompletion record = new JobUnsuccessfulCompletion();
        record.jobid = fieldSetFlags()[0] ? this.jobid : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.finishTime = fieldSetFlags()[1] ? this.finishTime : (java.lang.Long) defaultValue(fields()[1]);
        record.finishedMaps = fieldSetFlags()[2] ? this.finishedMaps : (java.lang.Integer) defaultValue(fields()[2]);
        record.finishedReduces = fieldSetFlags()[3] ? this.finishedReduces : (java.lang.Integer) defaultValue(fields()[3]);
        record.jobStatus = fieldSetFlags()[4] ? this.jobStatus : (java.lang.CharSequence) defaultValue(fields()[4]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
