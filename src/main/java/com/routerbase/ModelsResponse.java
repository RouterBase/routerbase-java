package com.routerbase;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public final class ModelsResponse {
    private List<ModelInfo> data = new ArrayList<>();

    public List<ModelInfo> getData() {
        return data;
    }

    public void setData(List<ModelInfo> data) {
        this.data = data;
    }

    public static final class ModelInfo {
        private String id;
        private String object;
        private Long created;

        @JsonProperty("owned_by")
        private String ownedBy;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public Long getCreated() {
            return created;
        }

        public void setCreated(Long created) {
            this.created = created;
        }

        public String getOwnedBy() {
            return ownedBy;
        }

        public void setOwnedBy(String ownedBy) {
            this.ownedBy = ownedBy;
        }
    }
}
