package eu.iums.fitwork;

public class Exercise {

    private String url;
    private Integer fitpoints;
    private String description;
    private String category;
    private String title;

    public Exercise() {

    }

    public Exercise(String title, String description, String category, Integer fitpoints, String url) {
        this.url = url;
        this.fitpoints = fitpoints;
        this.description = description;
        this.category = category;
        this.title = title;
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

}
