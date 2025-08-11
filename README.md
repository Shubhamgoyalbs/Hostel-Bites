# Hostel-Bites

🍕 **A comprehensive hostel food ordering system that connects students with delicious bites from local sellers within their hostel premises.**

## Overview

Hostel-Bites is a full-stack web application designed to revolutionize food ordering in hostel environments. Students can easily browse, order, and enjoy their favorite snacks and meals from various sellers operating within their hostel, making campus dining more convenient and accessible.

**Live Demo:**  
You can access the deployed website here: [https://frontend-orpin-omega-27.vercel.app/](https://frontend-orpin-omega-27.vercel.app/)

## Features

- **User Management**: Role-based authentication (Students, Sellers, Admin)
- **Product Catalog**: Browse and search through available food items
- **Order Management**: Place, track, and manage orders
- **Seller Dashboard**: Manage inventory and fulfill orders
- **Secure Authentication**: JWT-based authentication system
- **Real-time Updates**: Live order status updates

## Technology Stack

### Backend
- Spring Boot 3.5.4
- Java 21
- PostgreSQL
- Spring Security + JWT
- Maven

### Frontend
- Next.js
- TypeScript
- React
- Tailwind CSS (assumed)

## Project Structure

```
Hostel-Bites/
├── backend/          # Spring Boot REST API
│   ├── src/
│   ├── pom.xml
│   └── README.md     # Backend documentation
├── frontend/         # Next.js web application
│   ├── src/
│   ├── package.json
│   └── README.md     # Frontend documentation
└── README.md         # This file
```

## Quick Start

### Backend Setup
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### Frontend Setup
```bash
cd frontend
npm install
npm run dev
```

For detailed setup instructions, see the individual README files in the `backend/` and `frontend/` directories.

## Documentation

- [Backend API Documentation](./backend/README.md) - Comprehensive backend documentation
- [Frontend Documentation](./frontend/README.md) - Frontend setup and development guide

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

---

**Author:** Shubham Goyal  
**Last Updated:** August 2025
