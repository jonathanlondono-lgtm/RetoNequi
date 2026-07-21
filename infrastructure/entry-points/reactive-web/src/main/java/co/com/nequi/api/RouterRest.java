package co.com.nequi.api;

import co.com.nequi.api.handler.BranchHandler;
import co.com.nequi.api.handler.FranchiseHandler;
import co.com.nequi.api.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;



@Configuration
public class RouterRest {
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
