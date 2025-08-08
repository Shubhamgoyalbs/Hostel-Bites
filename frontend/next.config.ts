import type { NextConfig } from "next";

const nextConfig: NextConfig = {
    async rewrites() {
        return [
            {
                source: "/api/auth/:path*", // Auth endpoints (without /api/ prefix in backend)
                destination: "http://localhost:8080/auth/:path*", 
            },
            {
                source: "/api/:path*", // Other API endpoints (with /api/ prefix in backend)
                destination: "http://localhost:8080/api/:path*", 
            },
        ];
    },
};

export default nextConfig;
