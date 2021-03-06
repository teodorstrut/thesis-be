package com.bachelor.thesisbe.service;

import com.bachelor.thesisbe.model.*;
import com.bachelor.thesisbe.repo.PostFileRepo;
import com.bachelor.thesisbe.repo.PostRepo;
import com.bachelor.thesisbe.repo.UserPostRatingRepo;
import com.bachelor.thesisbe.views.FileViewModel;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PostService {
    private final PostRepo postRepo;
    private final UserPostRatingRepo ratingsRepo;
    private final PostFileRepo fileRepo;

    public PostService(PostRepo postRepo, UserPostRatingRepo ratingsRepo, PostFileRepo fileRepo) {
        this.postRepo = postRepo;
        this.ratingsRepo = ratingsRepo;
        this.fileRepo = fileRepo;
    }

    public Long addPost(String title, String description, UserEntity owner, Forum parentForum, FileViewModel file) throws Exception {
        PostFile fileModel = null;
        if (file != null) {
            fileModel = createFile(file);
            fileRepo.save(fileModel);
        }
        Post newPost = postRepo.save(new Post(
                title,
                description,
                owner,
                parentForum,
                new HashSet<>(),
                fileModel,
                new HashSet<>())
        );
        return newPost.getId();
    }

    public void likePost(UserEntity user, Post post, boolean isliked) {
        Optional<UserPostRating> rating = ratingsRepo.findById(new UserPostKey(user.getId(), post.getId()));
        if (rating.isPresent()) {
            UserPostRating ratingEntity = rating.get();
            if (ratingEntity.isLiked() != isliked) {
                ratingEntity.setLiked(isliked);
                ratingsRepo.save(ratingEntity);
            }
        } else {
            ratingsRepo.save(new UserPostRating(user, post, isliked));
        }
    }

    public List<Post> getPostsForForum(Long forumId, int pageSize, int pageIndex) {
        List<Post> posts = postRepo.findByForum_IdOrderByCreatedDateDesc(forumId);
        return getPostPageByPageIndexAndPageSize(pageIndex, pageSize, posts);
    }

    public Post getPostById(long postId) {
        return postRepo.findById(postId).orElse(null);
    }

    public void updatePostDescription(Long postId, String newDescription) {
        Optional<Post> postOptional = postRepo.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.setDescription(newDescription);
            postRepo.save(post);
        }
    }

    public List<Post> getNewestPosts(int pageIndex, int pageSize) {
        List<Post> posts = new ArrayList<>(postRepo.findAllByOrderByCreatedDateDesc());

        return getPostPageByPageIndexAndPageSize(pageIndex, pageSize, posts);
    }

    public List<Post> getPopularPosts(int pageIndex, int pageSize) {
        List<Post> posts = new ArrayList<>(postRepo.findAllByOrderByCreatedDateDesc());
        posts.sort(Comparator.comparingInt(post -> post.getUserLikes().size()));
        Collections.reverse(posts);
        return getPostPageByPageIndexAndPageSize(pageIndex, pageSize, posts);
    }

    private List<Post> getPostPageByPageIndexAndPageSize(int pageIndex, int pageSize, List<Post> posts) {
        int start = pageIndex * pageSize;
        int end = pageIndex * pageSize + pageSize;
        if (start > posts.size()) {
            start = posts.size();
        }
        if (end > posts.size()) {
            end = posts.size();
        }
        return posts.subList(start, end);
    }

    private PostFile createFile(FileViewModel fileViewModel) throws Exception {
        PostFile newFile = new PostFile();
        newFile.setFileName(fileViewModel.getName());
        newFile.setFileType(fileViewModel.getType());

        String fileLocation = writeFileToDisk(fileViewModel);
        newFile.setFilePath(fileLocation);
        return newFile;
    }

    /**
     * This method writes the data given from the server to the disk
     *
     * @param fileViewModel the data from the client
     * @return the file path, if the writing was successful
     * @throws IOException in case the directories or file could not be written
     */
    private String writeFileToDisk(FileViewModel fileViewModel) throws IOException {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        SimpleDateFormat fileTimestamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

        String fileLocation = "/PostFilesLocation/";
        fileLocation += dateFormat.format(currentDate);
        fileLocation += "/";

        File directory = new File(fileLocation);
        Path directoryPath = Paths.get(fileLocation);
        if (Files.exists(directoryPath)) {
            fileLocation += fileTimestamp.format(new Timestamp(currentDate.getTime())) + fileViewModel.getName();
            Path filePath = Paths.get(fileLocation);
            Files.write(filePath, fileViewModel.getData());
        } else {
            boolean directoriesCreated = directory.mkdirs();
            if (directoriesCreated) {
                fileLocation += fileTimestamp.format(new Timestamp(currentDate.getTime())) + fileViewModel.getName();
                Path filePath = Paths.get(fileLocation);
                Files.write(filePath, fileViewModel.getData());
            }
        }
        return fileLocation;
    }
}
