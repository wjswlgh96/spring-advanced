package org.example.expert.domain.comment.repository;

import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findByTodoIdWithUser_쿼리_동작_테스트() throws Exception {
        //given
        User user = new User("email", "Dndldka19@", UserRole.USER);
        entityManager.persist(user);

        Todo todo = new Todo("title", "contents", "weather", user);
        entityManager.persist(todo);

        Comment comment = new Comment("contents", user, todo);
        entityManager.persist(comment);
        entityManager.flush();

        //when
        List<Comment> result = commentRepository.findByTodoIdWithUser(todo.getId());

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(comment.getId());
        assertThat(result.get(0).getContents()).isEqualTo("contents");
        assertThat(result.get(0).getTodo().getId()).isEqualTo(todo.getId());
        assertThat(result.get(0).getUser().getId()).isEqualTo(user.getId());
    }
}