# Student Management System

A full-stack **Student Management System** built with **Node.js**, **Express.js**, and **MongoDB Atlas**. Features a professional admin dashboard UI with complete CRUD operations, aggregation reports, and REST API.

[![Node.js](https://img.shields.io/badge/Node.js-18%2B-339933?style=flat&logo=nodedotjs&logoColor=white)](https://nodejs.org)
[![Express](https://img.shields.io/badge/Express-5.x-000000?style=flat&logo=express&logoColor=white)](https://expressjs.com)
[![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-47A248?style=flat&logo=mongodb&logoColor=white)](https://www.mongodb.com/atlas)
[![Vercel](https://img.shields.io/badge/Deployed-Vercel-000000?style=flat&logo=vercel&logoColor=white)](https://vercel.com)

---

## Live Demo

**Production URL:** https://student-management-system-gray-beta.vercel.app

---

## Features

- **Dashboard** — Stats overview (total students, courses, average marks, top score)
- **Students Management** — Add, view, edit, delete students with modal forms
- **Courses Management** — Full CRUD for courses
- **Performance Reports** — Aggregation pipeline showing per-course averages and grades
- **Responsive UI** — Works on desktop and mobile
- **REST API** — Full JSON API for all operations

---

## Tech Stack

| Layer      | Technology              |
|------------|-------------------------|
| Runtime    | Node.js 18+             |
| Framework  | Express.js 5            |
| Database   | MongoDB Atlas (Mongoose)|
| Frontend   | Vanilla HTML/CSS/JS     |
| Deployment | Vercel                  |
| Dev Tool   | Nodemon                 |

---

## Project Structure

```
Student-Management-System/
├── config/
│   └── database.js          # MongoDB connection (cached for serverless)
├── controllers/
│   ├── studentController.js # Student CRUD + aggregation
│   └── courseController.js  # Course CRUD
├── models/
│   ├── Student.js           # Mongoose student schema
│   └── Course.js            # Mongoose course schema
├── public/
│   └── index.html           # Frontend dashboard SPA
├── routes/
│   ├── studentRoutes.js     # /api/students routes
│   └── courseRoutes.js      # /api/courses routes
├── .env                     # Environment variables (not committed)
├── .gitignore
├── package.json
├── server.js                # App entry point
└── vercel.json              # Vercel deployment config
```

---

## Getting Started

### Prerequisites

- Node.js 18+
- MongoDB Atlas account
- npm or yarn

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/deepmaha2006/StudentManagementSystem.git
   cd StudentManagementSystem
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Set up environment variables**

   Create a `.env` file in the root:
   ```env
   PORT=5000
   MONGODB_URI=mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/StudentDB?retryWrites=true&w=majority
   ```

4. **Start the development server**
   ```bash
   npm run dev
   ```

5. **Open your browser**
   ```
   http://localhost:5000
   ```

---

## API Endpoints

### Students

| Method | Endpoint                                  | Description              |
|--------|-------------------------------------------|--------------------------|
| GET    | `/api/students`                           | Get all students         |
| GET    | `/api/students/:id`                       | Get student by ID        |
| POST   | `/api/students`                           | Create a new student     |
| PUT    | `/api/students/:id`                       | Update student by ID     |
| DELETE | `/api/students/:id`                       | Delete student by ID     |
| GET    | `/api/students/reports/course-performance`| Aggregation report       |

### Courses

| Method | Endpoint              | Description           |
|--------|-----------------------|-----------------------|
| GET    | `/api/courses`        | Get all courses       |
| GET    | `/api/courses/:id`    | Get course by ID      |
| POST   | `/api/courses`        | Create a new course   |
| PUT    | `/api/courses/:id`    | Update course by ID   |
| DELETE | `/api/courses/:id`    | Delete course by ID   |

---

## Sample Request Bodies

**Create Student** — `POST /api/students`
```json
{
    "studentId": 108,
    "name": "Kiran Kumar",
    "age": 21,
    "courseId": "CS101",
    "marks": 86
}
```

**Create Course** — `POST /api/courses`
```json
{
    "_id": "AI101",
    "courseName": "Artificial Intelligence",
    "department": "Computer Science",
    "credits": 4
}
```

**Course Performance Report** — `GET /api/students/reports/course-performance`
```json
[
    {
        "courseId": "CS101",
        "courseName": "Computer Science",
        "totalStudents": 3,
        "averageMarks": 85
    }
]
```

---

## Database Schema

### Students Collection

| Field      | Type   | Required | Constraints        |
|------------|--------|----------|--------------------|
| studentId  | Number | Yes      | Unique             |
| name       | String | Yes      | Trimmed            |
| age        | Number | Yes      | Min: 1             |
| courseId   | String | Yes      | Trimmed            |
| marks      | Number | Yes      | Min: 0, Max: 100   |

### Courses Collection

| Field      | Type   | Required | Constraints        |
|------------|--------|----------|--------------------|
| _id        | String | Yes      | e.g. "CS101"       |
| courseName | String | Yes      | Unique, Trimmed    |
| department | String | Yes      |                    |
| credits    | Number | Yes      |                    |

---

## Deployment

### Deploy to Vercel

1. Push code to GitHub
2. Connect repository on [Vercel](https://vercel.com)
3. Set environment variable `MONGODB_URI` in Vercel project settings
4. Deploy

Or deploy via CLI:
```bash
npx vercel --prod
```

---

## Scripts

```bash
npm start      # Start production server
npm run dev    # Start with nodemon (auto-reload)
```

---

## Environment Variables

| Variable     | Description                        | Required |
|--------------|------------------------------------|----------|
| `PORT`       | Server port (default: 5000)        | No       |
| `MONGODB_URI`| MongoDB Atlas connection string    | Yes      |

---

## License

MIT License — feel free to use and modify.

---

## Author

**Deepesh** — [GitHub](https://github.com/deepmaha2006)
