package com.mtp.aqa.self.api.misc.model;

import java.util.List;

/**
 * A data-transfer-object for the Pet resource.
 * DTO - <a href="https://stackoverflow.com/questions/1051182/what-is-a-data-transfer-object-dto">...</a>
 * <p>
 * JSON would look like:
 * <pre>
 *     [
 *   {
 *     "id": 0,
 *     "category": {
 *       "id": 0,
 *       "name": "string"
 *     },
 *     "name": "doggie",
 *     "photoUrls": [
 *       "string"
 *     ],
 *     "tags": [
 *       {
 *         "id": 0,
 *         "name": "string"
 *       }
 *     ],
 *     "status": "available"
 *   }
 * ]
 * </pre>
 */
public class PetDto {
    private long id;
    private IdNamePair category;
    private String name;
    private List<String> photoUrls;
    private List<IdNamePair> tags;
    private String status;

    public PetDto() {
    }

    public PetDto(long id, IdNamePair category, String name, List<String> photoUrls, List<IdNamePair> tags, String status) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.photoUrls = photoUrls;
        this.tags = tags;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public IdNamePair getCategory() {
        return category;
    }

    public void setCategory(IdNamePair category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public List<IdNamePair> getTags() {
        return tags;
    }

    public void setTags(List<IdNamePair> tags) {
        this.tags = tags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
