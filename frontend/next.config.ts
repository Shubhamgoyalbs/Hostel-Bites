import type { NextConfig } from "next";

const nextConfig: NextConfig = {
    async rewrites() {
        return [
            {
                source: "/api/:path*", // Any request starting with /api/...
                destination: "http://localhost:8080/:path*", // Forward to Spring Boot backend
            },
        ];
    },
};

export default nextConfig;
