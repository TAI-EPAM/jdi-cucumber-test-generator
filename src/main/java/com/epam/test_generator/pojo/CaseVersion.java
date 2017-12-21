package com.epam.test_generator.pojo;

import com.epam.test_generator.entities.Case;

public class CaseVersion {

    private Case caze;

    private Long versionId;

    public CaseVersion(Case caze, Long versionId) {
        this.caze = caze;
        this.versionId = versionId;
    }

    public Case getCaze() {
        return caze;
    }

    public void setCaze(Case caze) {
        this.caze = caze;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CaseVersion that = (CaseVersion) o;

        return (caze != null ? caze.equals(that.caze) : that.caze == null)
            && (versionId != null ? versionId.equals(that.versionId) : that.versionId == null);
    }

    @Override
    public int hashCode() {
        int result = caze != null ? caze.hashCode() : 0;
        result = 31 * result + (versionId != null ? versionId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CaseVersion{" +
            "caze=" + caze +
            ", versionId=" + versionId +
            '}';
    }
}
