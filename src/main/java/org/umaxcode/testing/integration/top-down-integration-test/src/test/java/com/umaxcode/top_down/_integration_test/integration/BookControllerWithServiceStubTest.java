package com.umaxcode.top_down._integration_test.integration;

import com.umaxcode.top_down._integration_test.controller.BookingController;
import com.umaxcode.top_down._integration_test.domain.entity.Book;
import com.umaxcode.top_down._integration_test.domain.entity.User;
import com.umaxcode.top_down._integration_test.service.BookService;
import com.umaxcode.top_down._integration_test.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookControllerWithServiceStubTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private BookService bookService;


    @Test
    void searchBooks_ShouldReturnBooksWithASpecificTitle() throws Exception {

        String title = "Effective Java";

        List<Book> books = List.of(Book.builder()
                .title(title)
                .author("Maxwell")
                .build());

        when(bookService.searchBooksByTitle(title))
                .thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/library/books-spec")
                        .param("title", "Effective Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Effective Java"))
                .andExpect(jsonPath("$[0].author").value("Maxwell"));
    }

    @Test
    void getAllBooks_ShouldReturnAllBooks() throws Exception {

        List<Book> books = List.of(
                Book.builder()
                        .title("Title 1")
                        .author("Author 1")
                        .build(),
                Book.builder()
                        .title("Title 2")
                        .author("Author 2")
                        .build()

        );

        when(bookService.getAllBooks())
                .thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/library/books-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Title 1"))
                .andExpect(jsonPath("$[0].author").value("Author 1"))
                .andExpect(jsonPath("$[1].title").value("Title 2"))
                .andExpect(jsonPath("$[1].author").value("Author 2"));
    }

    @Test
    void getUsers_ShouldReturnUsers() throws Exception {

        List<User> users = List.of(
                User.builder()
                        .email("alice@gmail.com")
                        .name("Alice")
                        .build(),
                User.builder()
                        .email("boo@gmail.com")
                        .name("Bob")
                        .build()
        );
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/library/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].email").value("alice@gmail.com"))
                .andExpect(jsonPath("$[1].name").value("Bob"))
                .andExpect(jsonPath("$[1].email").value("boo@gmail.com"));
    }

    @Test
    void findAUser_ShouldFindAUserById() throws Exception {

        User user = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .name("test")
                .build();

        when(userService.getUserById(user.getId())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/library/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.email").value("test@gmail.com"));
    }
}