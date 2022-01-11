const express = require('express')
const router = express.Router()

const AttendanceController = require('../controllers/attendance')

router.get('/', AttendanceController.getAttendance)
router.get('/:id', AttendanceController.getAttendanceById)
router.all((req, res) => res.status(404).send('Unable to find the requested resource!'))

module.exports = router
