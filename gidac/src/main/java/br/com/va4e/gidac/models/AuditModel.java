package br.com.va4e.gidac.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdAt", "updatedAt" , "createdBy", "modifiedBy"},
        allowGetters = true
)
public abstract class AuditModel implements Serializable {
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "created_at", nullable = true, updatable = false)
	    @CreatedDate
	    private Date createdAt;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name = "updated_at", nullable = true)
	    @LastModifiedDate
	    private Date updatedAt;
	    
	    
	    @Column(name = "created_by", updatable = false)
	    @CreatedBy
	    private String createdBy;
	 
	    @Column(name = "modified_by")
	    @LastModifiedBy
	    private String modifiedBy;
	    

	    public Date getCreatedAt() {
	        return createdAt;
	    }

	    public void setCreatedAt(Date createdAt) {
	        this.createdAt = createdAt;
	    }

	    public Date getUpdatedAt() {
	        return updatedAt;
	    }

	    public void setUpdatedAt(Date updatedAt) {
	        this.updatedAt = updatedAt;
	    }

		public String getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}

		public String getModifiedBy() {
			return modifiedBy;
		}

		public void setModifiedBy(String modifiedBy) {
			this.modifiedBy = modifiedBy;
		}

	    
}
