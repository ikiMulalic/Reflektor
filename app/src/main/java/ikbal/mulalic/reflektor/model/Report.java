package ikbal.mulalic.reflektor.model;

public class Report {

    private String id;
    private String categoryOfReport;
    private String descriptionOfReport;
    private String locationOfReport;
    private String photoPathReport;
    private String videoPathReport;
    private String realLocationOfReport;
    private String createdAt;
    private String locationCode;
    private String userId;
    private int position;
    private String string;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    private boolean isSent;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealLocationOfReport() {
        return realLocationOfReport;
    }

    public void setRealLocationOfReport(String realLocationOfReport) {
        this.realLocationOfReport = realLocationOfReport;
    }

    public Report(){};

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryOfReport() {
        return categoryOfReport;
    }

    public void setCategoryOfReport(String categoryOfReport) {
        this.categoryOfReport = categoryOfReport;
    }

    public String getDescriptionOfReport() {
        return descriptionOfReport;
    }

    public void setDescriptionOfReport(String descriptionOfReport) {
        this.descriptionOfReport = descriptionOfReport;
    }

    public String getLocationOfReport() {
        return locationOfReport;
    }

    public void setLocationOfReport(String locationOfReport) {
        this.locationOfReport = locationOfReport;
    }

    public String getPhotoPathReport()
    {
        return photoPathReport;
    }

    public void setPhotoPathReport(String photoPathReport) {

        this.photoPathReport = photoPathReport;
    }

    public String getVideoPathReport()
    {
        return videoPathReport;
    }

    public void setVideoPathReport(String videoPathReport) {

        this.videoPathReport = videoPathReport;
    }

}
