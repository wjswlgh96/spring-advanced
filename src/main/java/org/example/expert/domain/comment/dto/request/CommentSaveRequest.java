package org.example.expert.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentSaveRequest {

    @NotBlank
    private String contents;

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        CommentSaveRequest that = (CommentSaveRequest) object;
        return Objects.equals(getContents(), that.getContents());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getContents());
    }
}
