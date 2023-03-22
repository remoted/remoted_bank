package net.remoted.bank.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.remoted.bank.conf.KakaoConfig;
import net.remoted.bank.entity.Blog;
import net.remoted.bank.entity.SearchQuery;
import net.remoted.bank.repository.BlogRepository;
import net.remoted.bank.repository.SearchQueryRepository;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

@RestController
@RequestMapping("/blog")
public class BlogController {
    private final BlogRepository blogRepository;
    private final SearchQueryRepository searchQueryRepository;
    private final KakaoConfig kakaoConfig;

    public BlogController(BlogRepository blogRepository, SearchQueryRepository searchQueryRepository,KakaoConfig kakaoConfig) {
        this.blogRepository = blogRepository;
        this.searchQueryRepository = searchQueryRepository;
        this.kakaoConfig = kakaoConfig;
    }

    @GetMapping("/search")
    public List<Blog> searchBlog(@RequestParam String query, @RequestParam String sort, @RequestParam Integer page) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = "https://dapi.kakao.com/v2/search/blog";
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        httpBuilder.addQueryParameter("query", query);
        httpBuilder.addQueryParameter("sort", sort);
        /**
         * recency, accuracy
         */
        httpBuilder.addQueryParameter("page", String.valueOf(page));

        httpBuilder.addQueryParameter("size", "50");


        /**
         * Save the search query to the database
         */
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setQuery(query);
        searchQuery.setTimestamp(LocalDateTime.now());
        searchQueryRepository.save(searchQuery);


        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .addHeader("Authorization", "KakaoAK " + kakaoConfig.getApiKey())
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        String json = response.body().string();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode documents = root.get("documents");

        List<Blog> blogList = new ArrayList<>();
        for (JsonNode document : documents) {
            Blog blog = new Blog();
            blog.setTitle(document.get("title").asText());
            blog.setContents(document.get("contents").asText());
            blog.setUrl(document.get("url").asText());
            blogList.add(blog);
        }

        blogRepository.saveAll(blogList);
        return blogList;
    }

    @GetMapping("/top10")
    public List<Object[]> getTop10SearchQueries() {
        return searchQueryRepository.findTop10SearchQueries();
    }
}