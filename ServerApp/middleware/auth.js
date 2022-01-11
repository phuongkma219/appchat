const UserService = require('../services/user')

const apiResponse = require('../utils/apiResponse')
const { compare } = require('../utils/bcrypt')
const { verifyToken } = require('../utils/token')

const Status = require('../constants/status')
const Message = require('../constants/message')

const checkLoginCondition = async (req, res, next) => {
  try {
    const { account, password } = req.body
    const user = await UserService.getByAccount(account)
    if (!user) return res.status(Status.NOT_FOUND).json(apiResponse(false, Status.NOT_FOUND, Message.ACCOUNT_NOT_EXIST))
    else {
      const isPasswordValid = compare(password, user.password)
      if (!isPasswordValid)
        return res.status(Status.UNAUTHORIZED).json(apiResponse(Status.NOT_FOUND, Message.WRONG_PASSWORD))
      else {
        req.user = user
        next()
      }
    }
  } catch (error) {
    console.log(error)
    return res.status(Status.INTERNAL_SERVER_ERROR).send(Message.INTERNAL_SERVER_ERROR)
  }
}

const checkRegisterCondition = async (req, res, next) => {
  const isAccountValid = await UserService.getByAccount(req.body.account)
  if (isAccountValid) return res.status(Status.CONFLICT).json(apiResponse(Status.CONFLICT, Message.ACCOUNT_EXIST))
  else next()
}

const checkAuth = (req, res, next) => {
  var token  = req.headers["authorization"]
  
  if (token == null) {
    next()
    return
  }
  if (token.startsWith("Bearer")) {
    token = token.substring(7, token.length )
  }
  console.log("token "+token)
 let verify = verifyToken(token)
  console.log(verify)
  next()

}

module.exports = {
  checkLoginCondition,
  checkRegisterCondition,
  checkAuth,
}
