package com.posts.PostsMicroService.Services.ServiceImpl;

import com.posts.PostsMicroService.Entity.NestedPostComments;
import com.posts.PostsMicroService.Entity.Post;
import com.posts.PostsMicroService.Entity.PostsComments;
import com.posts.PostsMicroService.Repository.PostRepository;
import com.posts.PostsMicroService.Services.PostService;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;


    @Override
    public void addPost(Post post) {
        postRepository.insert(post);

    }

    @Override
    public void deletePost(String postId) {
postRepository.delete(postId);
    }

    @Override
    public void editPost(Post post) {
        postRepository.save(post);
    }

    @Override
    public Post getPostDetails(String postId) {
        return postRepository.findOne(postId);
    }

    @Override

    public Post findOnePost(String postId) {
        return postRepository.findOne(postId);
    }

    @Override
    public void deleteParentComments(Post post,String commentId) {
        List<PostsComments> comments=post.getPostsComments();


        for(PostsComments singleComment:comments)
        {
            if(singleComment.getCommentId().equalsIgnoreCase(commentId))
            {
                comments.remove(singleComment);
            }
        }

        post.setPostsComments(comments);

        postRepository.save(post);
    }

    @Override
    public void editParentComments(Post post, String commentId,String description) {

        List<PostsComments> comments=post.getPostsComments();

        List<PostsComments> edittedList=new ArrayList<>();

       // PostsComments updateComment=null;

        for (PostsComments editComment:comments)
        {
            System.out.println(commentId);
            if(editComment.getCommentId()!=null&&editComment.getCommentId().equalsIgnoreCase(commentId))
            {
                editComment.setDescription(description);
            }
            edittedList.add(editComment);
        }
//        updateComment.setDescription(description);
//        comments.add(updateComment);
        post.setPostsComments(edittedList);
        postRepository.save(post);


    }

    @Override
    public void deleteNestedComment(Post post, String commentId, String nestedCommentId) {
        List<PostsComments> comments=post.getPostsComments();
        PostsComments postsComments=null;

        for (PostsComments mainComment:comments)
        {
            if(mainComment.getCommentId()!=null&&mainComment.getCommentId().equalsIgnoreCase(commentId))
            {
                postsComments=mainComment;
            }
        }

        List<NestedPostComments> nestedPostComments=postsComments.getNestedPostComments();
        for (NestedPostComments nested:nestedPostComments)
        {
            if(nested.getNestedCommentId()!=null&&nested.getNestedCommentId().equalsIgnoreCase(nestedCommentId))
            {
                nestedPostComments.remove(nested);
            }
        }
        postsComments.setNestedPostComments(nestedPostComments);
        post.setPostsComments(comments);

        postRepository.save(post);


    }

    @Override
    public void addLikes(String postId, String userId) {
        Post post= postRepository.findOne(postId);
        post.getPostLikes().add(userId);
        postRepository.save(post);
    }

    @Override
    public void dislike(String postId, String userId) {
        Post post=postRepository.findOne(postId);
        post.getPostLikes().remove(userId);
        postRepository.save(post);

    }

    @Override
    public Boolean getLikeStatus(String postId, String userId) {
       return postRepository.findOne(userId).getPostLikes().contains(userId);

    }


}
