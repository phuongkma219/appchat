const express = require('express')
const router = express.Router()

const SubjectController = require('../controllers/subject')

router.get('/', SubjectController.getSubject)
router.get('/:id', SubjectController.getSubjectById)
router.get('/room', SubjectController.getSubjectRoom)
router.get('/room/:id', SubjectController.getSubjectRoomById)
router.all((req, res) => res.status(404).send('Unable to find the requested resource!'))

module.exports = router
