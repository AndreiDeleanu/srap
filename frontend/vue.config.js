module.exports = {
    // Change build paths to make them Maven compatible
    outputDir: "target/dist",
    assetsDir: "assets",
    // proxy all webpack dev-server requests starting with /api
    // to our Spring Boot backend (localhost:8080) using http-proxy-middleware
    // see https://cli.vuejs.org/config/#devserver-proxy
    devServer: {
        port: 8088,
        proxy: {
            "/srap": {
                target: "http://localhost:8080",
                ws: true,
                changeOrigin: true,
                //eslint-disable-next-line
                bypass: function(req, res, proxyOptions) {
                    // skipping proxy for browser request
                    if (req.headers.accept.indexOf("html") !== -1) {
                        return "/index.html";
                    }
                }
            }
        }
    }
};
