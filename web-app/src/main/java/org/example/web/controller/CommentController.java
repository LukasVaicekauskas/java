package org.example.web.controller;

import org.example.web.model.Comment;
import org.example.web.model.User;
import org.example.web.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // Create a new top-level comment
    @PostMapping("/books/{bookId}")
    public ResponseEntity<Comment> createComment(@PathVariable String bookId, @RequestBody CreateCommentRequest request) {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            Comment comment = commentService.createComment(request.getContent(), currentUser.getId(), bookId);
            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Create a reply to an existing comment
    @PostMapping("/{commentId}/replies")
    public ResponseEntity<Comment> createReply(@PathVariable String commentId, @RequestBody CreateCommentRequest request) {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            Comment reply = commentService.createReply(request.getContent(), currentUser.getId(), commentId);
            return ResponseEntity.ok(reply);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get all top-level comments for a book
    @GetMapping("/books/{bookId}")
    public ResponseEntity<List<Comment>> getTopLevelCommentsByBookId(@PathVariable String bookId) {
        try {
            List<Comment> comments = commentService.getTopLevelCommentsByBookId(bookId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get all comments for a book (including replies)


    // Get top-level comments with their replies (nested structure)
    @GetMapping("/books/{bookId}/nested")
    public ResponseEntity<List<Comment>> getTopLevelCommentsWithRepliesByBookId(@PathVariable String bookId) {
        try {
            List<Comment> comments = commentService.getTopLevelCommentsWithRepliesByBookId(bookId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get replies to a specific comment
    @GetMapping("/{commentId}/replies")
    public ResponseEntity<List<Comment>> getRepliesByCommentId(@PathVariable String commentId) {
        try {
            List<Comment> replies = commentService.getRepliesByCommentId(commentId);
            return ResponseEntity.ok(replies);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }







    // Update a comment
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable String commentId, @RequestBody UpdateCommentRequest request) {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            Comment updatedComment = commentService.updateComment(commentId, request.getContent(), currentUser.getId());
            return ResponseEntity.ok(updatedComment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Delete a comment (soft delete)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId) {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            boolean deleted = commentService.deleteComment(commentId, currentUser.getId());
            return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get comment count for a book
    @GetMapping("/books/{bookId}/count")
    public ResponseEntity<Long> getCommentCountByBookId(@PathVariable String bookId) {
        try {
            long count = commentService.getCommentCountByBookId(bookId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }









    // Inner classes for request/response
    public static class CreateCommentRequest {
        private String content;

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    public static class UpdateCommentRequest {
        private String content;

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}
