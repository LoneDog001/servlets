package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.JavaConfig;
import ru.netology.controller.PostController;
import ru.netology.exception.NotFoundException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;
    private final static String GET = "GET";
    private final static String POST = "POST";
    private final static String DELETE = "DELETE";
    private final static String REQUEST_WITH_ADDENDUM = "/api/posts/\\d+";
    private final static String REQUEST = "/api/posts/";

    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext(JavaConfig.class);
        controller = (PostController) context.getBean("postController");
        final var service = context.getBean("postService");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            long id = 0;
            if (path.matches(REQUEST_WITH_ADDENDUM)) {
                id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
            }
            if (method.equals(GET) && path.equals(REQUEST)) {
                controller.all(resp);
                return;
            }
            if (method.equals(GET) && path.matches(REQUEST_WITH_ADDENDUM)) {
                controller.getById(id, resp);
                return;
            }
            if (method.equals(POST) && path.equals(REQUEST)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(DELETE) && path.matches(REQUEST_WITH_ADDENDUM)) {
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (NotFoundException notFoundException) {
            notFoundException.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
