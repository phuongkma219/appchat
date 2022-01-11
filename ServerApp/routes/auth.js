const express = require('express')
const router = express.Router()

const AuthController = require('../controllers/auth')
const { checkLoginCondition, checkRegisterCondition, checkAuth } = require('../middleware/auth')

router.post('/login', checkLoginCondition, AuthController.login)
router.post('/register', checkRegisterCondition, AuthController.register)
router.post('/forgot-password', AuthController.forgotPassword)
router.use(checkAuth)
router.get('/logout', AuthController.logout)
router.put('/change-password', AuthController.changePassword)
router.all((req, res) => res.status(404).send('Unable to find the requested resource!'))

module.exports = router
