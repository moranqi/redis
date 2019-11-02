
//import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;


public class JedisTest {
    public static void main(String[] args) {
        //连接redis服务器(在这里是连接本地的)
        Jedis jedis = new Jedis("localhost");
        Post post =new Post();
        post.setAuthor("jxf");
        post.setContent("blog");
        post.setTitle("my blog");
        Long postId = savePost(post, jedis);
        System.out.println("保存成功");
        Post myPost=getPost(postId,jedis);
        System.out.println(myPost);
        updatePost(1,"title","cdcdmk",jedis);
        del(jedis,1);
    }
//    public  Long savePost(Post post,Jedis jedis){
//        Long posts=jedis.incr("posts");
//        String postStr= JSON.toJSONString(post);
//        jedis.set("post:"+posts+":data",postStr);
//        return posts;
//    }
//    public static Post getPost(Jedis jedis,Long posyId){
//        String post=jedis.get("post:"+posyId+":data");
//        Post post1=JSON.parseObject(post,Post.class);
//        return post1;
//    }
//    public  Post update(Long Id,Jedis jedis){
//        Post post =Get(Id,jedis);
//        post.setTitle("update title");
//        String myPost =JSON.toJSONString(post);
//        jedis.set("post:"+Id+":data",myPost);
//        System.out.println("修改完成");
//        return post;
//    }
//    public void delete(Long Id,Jedis jedis){
//        jedis.del("post:"+Id+":data");
//        jedis.del("post:"+Id+":page.view");
//        System.out.println("删除成功");
//    }
    static Long savePost(Post post,Jedis jedis){
        Long postId=jedis.incr("posts");
        Map<String,String> blog=new HashMap<String, String>();
        blog.put("title",post.getTitle());
        blog.put("content",post.getContent());
        blog.put("author",post.getAuthor());
        jedis.hmset("post:"+postId+":data",blog);
        return postId;
    }
    static Post getPost(Long postId,Jedis jedis){
        Map<String,String> myBlog=jedis.hgetAll("post:"+postId+":data");
        Post post=new Post();
        post.setTitle(myBlog.get("title"));
        post.setContent(myBlog.get("content"));
        post.setAuthor(myBlog.get("author"));
        return post;
    }
    public static void del(Jedis jedis,Integer id){

        jedis.hdel("article"+id+"Data");
    }
    public static void updatePost(int postID,String key,String val,Jedis jedis){
        Map<String, String> properties = jedis.hgetAll("Article:" + postID);
        properties.put(key,val);
        jedis.hmset("Article:" + postID,properties);
    }
}