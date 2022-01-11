const express = require('express')
const router = express.Router()

const CourseController = require('../controllers/course')

router.get('/', CourseController.getCourse)
router.get('/:id', CourseController.getCourseById)
router.all((req, res) => res.status(404).send('Unable to find the requested resource!'))

module.exports = router
