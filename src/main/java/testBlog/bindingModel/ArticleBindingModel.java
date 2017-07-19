package testBlog.bindingModel;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class ArticleBindingModel {
    @NotNull
    private String title;
    @NotNull
    private String content;
    private Integer categoryId;
    private String tagString;
    private MultipartFile[] imagestoBeInBase64;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String context) {
        this.content = context;
    }

    public String getTagString() {
        return tagString;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }

    public MultipartFile[] getImagestoBeInBase64() {
        return imagestoBeInBase64;
    }

    public void setImagestoBeInBase64(MultipartFile[] imagestoBeInBase64) {
        this.imagestoBeInBase64 = imagestoBeInBase64;
    }
}
