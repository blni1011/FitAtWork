package eu.iums.fitwork;

public class Exercise {

    /*
    Exercise Klasse:
    Exercise-Objekt wie es in der DB gespeichert und wieder ausgelesen wird.
     */

    private String url;
    private Integer fitpoints;
    private String description;
    private String category;
    private String title;
    private String area;

    public Exercise() {

    }

    public Exercise(String title, String description, String category, Integer fitpoints, String url, String area) {
        this.url = url;
        this.fitpoints = fitpoints;
        this.description = description;
        this.category = category;
        this.title = title;
        this.area = area;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Integer getFitpoints() {
        return fitpoints;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getArea() {
        return area;
    }

}
