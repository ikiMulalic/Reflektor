package ikbal.mulalic.reflektor.model;

public class Report {

    private String id;
    private String categoryOfReport;
    private String descriptionOfReport;
    private String locationOfReport;
    private String photoPathReport;
    private String videoPathReport;
    private String realLocationOfReport;

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
        if(photoPathReport == null)
        {
            this.photoPathReport = null;
        }
        this.photoPathReport = photoPathReport;
    }

    public String getVideoPathReport()
    {
        return videoPathReport;
    }

    public void setVideoPathReport(String videoPathReport) {
        if(videoPathReport == null)
        {
            this.videoPathReport = null;
        }
        this.videoPathReport = videoPathReport;
    }

}
