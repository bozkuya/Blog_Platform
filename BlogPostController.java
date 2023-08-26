import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blog")
public class BlogPostController {
    
    @Autowired
    private BlogPostRepository blogPostRepository;
    
    @Autowired
    private UserRepository userRepository;

    // Create a new blog post
    @PostMapping("/create")
    public ResponseEntity<?> createBlogPost(@RequestBody BlogPost newBlogPost, @RequestParam Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            newBlogPost.setAuthor(user);
            blogPostRepository.save(newBlogPost);
            return ResponseEntity.ok(newBlogPost);
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

    // Get all blog posts
    @GetMapping("/all")
    public List<BlogPost> getAllBlogPosts() {
        return blogPostRepository.findAll();
    }

    // Get a single blog post by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBlogPostById(@PathVariable Long id) {
        Optional<BlogPost> blogPost = blogPostRepository.findById(id);
        
        return blogPost.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a blog post
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBlogPost(@PathVariable Long id, @RequestBody BlogPost updatedBlogPost) {
        Optional<BlogPost> blogPostOptional = blogPostRepository.findById(id);
        
        if (blogPostOptional.isPresent()) {
            BlogPost existingBlogPost = blogPostOptional.get();
            existingBlogPost.setTitle(updatedBlogPost.getTitle());
            existingBlogPost.setContent(updatedBlogPost.getContent());
            blogPostRepository.save(existingBlogPost);
            return ResponseEntity.ok(existingBlogPost);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a blog post
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlogPost(@PathVariable Long id) {
        Optional<BlogPost> blogPost = blogPostRepository.findById(id);
        
        if (blogPost.isPresent()) {
            blogPostRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
