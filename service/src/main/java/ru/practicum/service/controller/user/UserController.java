package ru.practicum.service.controller.user;

import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.dto.user.NewUserRequest;
import ru.practicum.service.dto.user.UserDto;
import ru.practicum.service.service.user.UserService;

import java.util.List;

@RestController()
@RequestMapping("/admin/users")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(required = false)
                                                  List<Long> ids,

                                                  @RequestParam(defaultValue = "0")
                                                  @Min(0)
                                                  int from,

                                                  @RequestParam(defaultValue = "10")
                                                  @Min(1)
                                                  int size) {
        return ResponseEntity.ok(userService.getUsers(ids, from, size));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Validated NewUserRequest userCreate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(userCreate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
