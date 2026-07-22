package co.com.nequi.api;

import co.com.nequi.api.handler.BranchHandler;
import co.com.nequi.api.handler.FranchiseHandler;
import co.com.nequi.api.handler.ProductHandler;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @RouterOperations({
        @RouterOperation(path = "/api/franchises", method = RequestMethod.POST, beanClass = FranchiseHandler.class, beanMethod = "createFranchise"),
        @RouterOperation(path = "/api/franchises/{franchiseId}/name", method = RequestMethod.PATCH, beanClass = FranchiseHandler.class, beanMethod = "updateFranchiseName"),
        @RouterOperation(path = "/api/branches", method = RequestMethod.POST, beanClass = BranchHandler.class, beanMethod = "createBranch"),
        @RouterOperation(path = "/api/branches/{branchId}/name", method = RequestMethod.PATCH, beanClass = BranchHandler.class, beanMethod = "updateBranchName"),
        @RouterOperation(path = "/api/products", method = RequestMethod.POST, beanClass = ProductHandler.class, beanMethod = "createProduct"),
        @RouterOperation(path = "/api/products/{productId}", method = RequestMethod.DELETE, beanClass = ProductHandler.class, beanMethod = "deleteProduct"),
        @RouterOperation(path = "/api/products/{productId}/stock", method = RequestMethod.PATCH, beanClass = ProductHandler.class, beanMethod = "updateProductStock"),
        @RouterOperation(path = "/api/products/{productId}/name", method = RequestMethod.PATCH, beanClass = ProductHandler.class, beanMethod = "updateProductName"),
        @RouterOperation(path = "/api/products/franchise/{franchiseId}/top-products", method = RequestMethod.GET, beanClass = ProductHandler.class, beanMethod = "getTopProducts")
    })
    @Bean
    public RouterFunction<ServerResponse> routerFunction(FranchiseHandler franchiseHandler, BranchHandler branchHandler, ProductHandler productHandler) {
        return RouterFunctions.nest(path("/api/franchises"),
                        route(POST(""), franchiseHandler::createFranchise)
                                .andRoute(PATCH("/{franchiseId}/name"), franchiseHandler::updateFranchiseName))
                .and(RouterFunctions.nest(path("/api/branches"),
                        route(POST(""), branchHandler::createBranch)
                                .andRoute(PATCH("/{branchId}/name"), branchHandler::updateBranchName)))
                .and(RouterFunctions.nest(path("/api/products"),
                        route(POST(""), productHandler::createProduct)
                                .andRoute(DELETE("/{productId}"), productHandler::deleteProduct)
                                .andRoute(PATCH("/{productId}/stock"), productHandler::updateProductStock)
                                .andRoute(PATCH("/{productId}/name"), productHandler::updateProductName)
                                .andRoute(GET("/franchise/{franchiseId}/top-products"), productHandler::getTopProducts)));
    }

}
